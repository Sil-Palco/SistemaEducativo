package com.school.controller;

import com.school.model.Clase;
import com.school.model.Empleado;
import com.school.model.Especialidad;
import com.school.service.EmpleadoService;
import com.school.service.EspecialidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {
    
    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private EspecialidadService especialidadService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Empleado>> getAllEmpleados(){
        return new ResponseEntity<>(empleadoService.findAll(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR')")
    @GetMapping("/clases")
    public ResponseEntity<List<Clase>> getClasesEmpleado(@RequestParam("id") String id){
        return new ResponseEntity<List<Clase>>(empleadoService.findClasesProfesor(Long.parseLong(id)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/crear")
    public ResponseEntity<?> saveEmpleado(@Valid @RequestBody Empleado empleado, BindingResult results){
        Empleado empleadoNuevo = null;
        Map<String, Object> response = new HashMap<>();

        if(results.hasErrors()) {
            List<String> errors = results.getFieldErrors()
                    .stream()
                    .map(er -> "El campo '" + er.getField() +"' " + er.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("errors", errors);

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            String[] nombres = empleado.getNombres().split(" ");
            empleado.setCorreo(nombres[0] + "." + empleado.getApellidoPaterno());
            empleado.setCorreo(empleado.getCorreo().concat("@elamericano.edu.pe").toLowerCase());
            empleadoNuevo = empleadoService.save(empleado);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al insertar el empleado en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El empleado ha sido creado con éxito!");
        response.put("empleado", empleadoNuevo);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getEmpleado(@PathVariable Long id){

        Optional<Empleado> empleado = null;
        Map<String, Object> response = new HashMap<>();

        try {
            empleado = empleadoService.getEmpleadoById(id);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al consultar el empleado en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(empleado.isEmpty()) {
            response.put("mensaje", "El Empleado con el ID: ".concat(id.toString().concat(" no existe en la base de datos")));

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Empleado>(empleado.get(), HttpStatus.OK);

    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR')")
    @GetMapping("/buscarDni")
    public ResponseEntity<?> getEmpleadoByDni(@RequestParam String dni){

        Empleado empleado = null;
        Map<String, Object> response = new HashMap<>();

        try {
            empleado = empleadoService.findByDni(dni);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al consultar el empleado en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(empleado == null) {
            response.put("mensaje", "El Empleado con el DNI: ".concat(dni.concat(" no existe en la base de datos")));

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Empleado>(empleado, HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmpleado(@Valid @RequestBody Empleado empleado ,BindingResult results , @PathVariable Long id){
        Empleado empleadoActual = empleadoService.getEmpleadoById(id).get();
        Empleado empleadoActualizado = null;
        Map<String, Object> response = new HashMap<>();

        if(results.hasErrors()) {
            List<String> errors = results.getFieldErrors()
                    .stream()
                    .map(er -> "El campo '" + er.getField() +"' " + er.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("errors", errors);

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if(empleadoActual == null) {
            response.put("mensaje", "Error: No se pudo editar, el empleado con el ID: ".concat(id.toString().concat(" no existe en la base de datos")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            empleadoActual.setNombres(empleado.getNombres());
            empleadoActual.setApellidoPaterno(empleado.getApellidoPaterno());
            empleadoActual.setApellidoMaterno(empleado.getApellidoMaterno());
            empleadoActual.setDni(empleado.getDni());
            empleadoActual.setFechaNacimiento(empleado.getFechaNacimiento());
            empleadoActual.setCorreo(empleado.getCorreo());
            empleadoActual.setSexo(empleado.getSexo());
            empleadoActual.setDomicilio(empleado.getDomicilio());
            empleadoActual.setEspecialidades(empleado.getEspecialidades());

            empleadoActualizado = empleadoService.save(empleadoActual);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar el Empleado en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El empleado ha sido actualizado con éxito!");
        response.put("empleado", empleadoActualizado);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmpleado(@PathVariable Long id){

        Map<String, Object> response = new HashMap<>();
        Empleado empleado = empleadoService.getEmpleadoById(id).orElse(null);

        if(empleado == null) {
            response.put("mensaje", "Error: No se pudo eliminar, el empleado con el ID: ".concat(id.toString().concat(" no existe en la base de datos")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            for(Especialidad e: empleado.getEspecialidades()){
                empleado.getEspecialidades().remove(e);
            }
            empleadoService.delete(id);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al eliminar el empleado en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El empleado ha sido eliminado con éxito");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/especialidades")
    public ResponseEntity<List<Especialidad>> listarEspecialidades(){
        return new ResponseEntity<List<Especialidad>>(especialidadService.findAll(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/especialidades/crear")
    public ResponseEntity<?> crearEspecialidad(@RequestBody Especialidad especialidad, BindingResult results){

        Especialidad especialidadNueva = null;
        Map<String, Object> response = new HashMap<>();

        if(results.hasErrors()) {
            List<String> errors = results.getFieldErrors()
                    .stream()
                    .map(er -> "El campo '" + er.getField() +"' " + er.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("errors", errors);

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            especialidadNueva = especialidadService.save(especialidad);
        }catch (DataAccessException e) {
            response.put("mensaje", "Error al insertar el empleado en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "La especialidad se creo con éxito!");
        response.put("especialidad", especialidadNueva);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }
    
}
