package com.school.controller;

import com.school.model.*;
import com.school.service.AsignacionService;
import com.school.service.EstudianteService;
import com.school.service.RespuestaAsignacionService;
import com.school.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = {"*"})
@RequestMapping("/api/asignaciones")
public class AsignacionController {

    @Autowired
    private AsignacionService asignacionService;

    @Autowired
    private RespuestaAsignacionService respuestaAsignacionService;

    @Autowired
    private UploadFileService uploadFileService;

    @Autowired
    private EstudianteService estudianteService;

    @PostMapping("/crear/{idClase}")
    public ResponseEntity<?> crearAsignacion(@Valid @RequestBody Asignacion asignacion, BindingResult results, @PathVariable Long idClase) {

        Asignacion asignacionNueva = null;
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
            asignacionNueva = asignacionService.save(asignacion, idClase);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al insertar la asignación en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "La asignación ha sido creada con éxito!");
        response.put("asignacion", asignacionNueva);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/actualizar/{idAsignacion}")
    public ResponseEntity<?> actualizarAsignacion(@Valid @RequestBody Asignacion asignacion, BindingResult results, @PathVariable Long idAsignacion) {

        Asignacion asignacionEncontrada = asignacionService.getAsignacionoById(idAsignacion).orElse(null);
        Asignacion asginacionActualizada = null;
        Map<String, Object> response = new HashMap<>();

        if(results.hasErrors()) {
            List<String> errors = results.getFieldErrors()
                    .stream()
                    .map(er -> "El campo '" + er.getField() +"' " + er.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("errors", errors);

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if(asignacionEncontrada == null) {
            response.put("mensaje", "Error: No se pudo editar la clase con el ID: ".concat(idAsignacion.toString().concat(" no existe en la base de datos.")));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        try {
            asignacionEncontrada.setTitulo(asignacion.getTitulo());
            asignacionEncontrada.setDescripcion(asignacion.getDescripcion());
            asignacionEncontrada.setFechaFin(asignacion.getFechaFin());
            asignacionEncontrada.setFechaInicio(asignacion.getFechaInicio());
            asginacionActualizada = asignacionService.update(asignacionEncontrada);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "La asignación ha sido actualizada con éxito!");
        response.put("asignacion", asginacionActualizada);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAsignacion(@PathVariable Long id){

        Optional<Asignacion> asignacion = null;
        Map<String, Object> response = new HashMap<>();

        try {
            asignacion = asignacionService.getAsignacionoById(id);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al consultar la asignación en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(asignacion.isEmpty()) {
            response.put("mensaje", "La asignación con el ID: ".concat(id.toString().concat(" no existe en la base de datos")));

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        response.put("asignacion", asignacion.get());

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarAsignacion(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        List<RespuestaAsignacion> respuestas = respuestaAsignacionService.findAllByAsignacion(id);

        try {
            if(!respuestas.isEmpty()){
                respuestas.forEach(r -> {
                    uploadFileService.deleteFile(r.getArchivo());
                    respuestaAsignacionService.delete(r.getId());
                });
            }

            asignacionService.deleteById(id);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al eliminar la asignación en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("eliminado", "La asignación con el id: "+id+" se eliminó con éxito.");

        return new ResponseEntity<>(response,HttpStatus.OK);
    }


    @GetMapping("/respuestas/{idAsignacion}")
    public ResponseEntity<List<RespuestaAsignacion>> listaRespuestasPorAsignacion(@PathVariable Long idAsignacion) {
        return new ResponseEntity<>(respuestaAsignacionService.findAllByAsignacion(idAsignacion), HttpStatus.OK);
    }

    @PutMapping("/respuestas/{idRespuesta}")
    public ResponseEntity<?> actualizarRespuestaAsignacion(@RequestParam("nota") String nota, @PathVariable Long idRespuesta){

        RespuestaAsignacion respuestaAsignacionEncontrada = respuestaAsignacionService.findById(idRespuesta).orElse(null);
        RespuestaAsignacion respuestaActualizada = null;
        Map<String, Object> response = new HashMap<>();

        if(respuestaAsignacionEncontrada == null){
            response.put("mensaje", "La respuesta con el id: " + idRespuesta + " no existe en la BBDD.");
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }

        try{
            respuestaAsignacionEncontrada.setNota(nota);
            respuestaActualizada = respuestaAsignacionService.update(respuestaAsignacionEncontrada);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al actualizar la respuesta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "Respuesta actializada.");
        response.put("respuesta", respuestaActualizada);

        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @PostMapping("/respuestas/crear")
    public ResponseEntity<?> crearRespuestaAsignacion(@RequestParam("idAsignacion") String idAsignacion, @RequestParam("archivo") MultipartFile archivo, @RequestParam("dniEstudiante") String dniEstudiante) {

        RespuestaAsignacion respuestaAsignacionNueva = null;

        Map<String, Object> response = new HashMap<>();


        if(!archivo.isEmpty()){

            String nombreArchivo = null;
            RespuestaAsignacion respuestaAsignacion = new RespuestaAsignacion();
            Estudiante estudiante = null;
            try {
                estudiante = estudianteService.findByDni(dniEstudiante);

                nombreArchivo = uploadFileService.uploadFile(archivo);
                respuestaAsignacion.setArchivo(nombreArchivo);
                respuestaAsignacion.setNombresEstudiante(estudiante.getNombres() + " " + estudiante.getApellidoPaterno() + " " + estudiante.getApellidoMaterno());
                respuestaAsignacion.setDniEstudiante(dniEstudiante);
                respuestaAsignacionNueva = respuestaAsignacionService.save(respuestaAsignacion, Long.parseLong(idAsignacion));
            } catch (IOException e) {
                response.put("mensaje", "Error al subir archivo.");
                response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));

                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }



        response.put("mensaje", "La respuesta ha sido creada con éxito!");
        response.put("respuestaAsignacion", respuestaAsignacionNueva);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/respuestas/buscarRespuesta")
    public ResponseEntity<?> buscarRespuestaEstudiante(@RequestParam("dniEstudiante") String dniEstudiante, @RequestParam("idAsignacion") String idAsignacion){

        Optional<RespuestaAsignacion> respuestaAsignacion = respuestaAsignacionService.findPorDniEstudianteAndAsignacion(dniEstudiante,Long.parseLong(idAsignacion));
        if(respuestaAsignacion.isEmpty()){
            return new ResponseEntity<>("No se encontró la respuesta",HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(respuestaAsignacion.get(), HttpStatus.OK);
    }

    @DeleteMapping("/respuestas/{idRespuesta}")
    public ResponseEntity<?> eliminarRespuesta(@PathVariable Long idRespuesta){
        RespuestaAsignacion respuestaEncontrada = respuestaAsignacionService.findById(idRespuesta).orElse(null);

        if(respuestaEncontrada != null){
            respuestaAsignacionService.delete(idRespuesta);
            uploadFileService.deleteFile(respuestaEncontrada.getArchivo());
            return new ResponseEntity<>(true,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);
        }
    }

}
