package com.school.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.school.model.Estudiante;
import com.school.service.EstudianteService;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

	@Autowired
	private EstudianteService estudianteService;

	@PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR')")
	@GetMapping
	public ResponseEntity<List<Estudiante>> getAllEstudiantes(){
		return new ResponseEntity<>(estudianteService.findAll(), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/page/{page}")
	public ResponseEntity<Page<Estudiante>> getAllEstudiantesPage(@PathVariable Integer page){

		Pageable pageable = PageRequest.of(page, 8);

		return new ResponseEntity<>(estudianteService.findAll(pageable), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/crear")
	public ResponseEntity<?> saveEstudiante(@Valid @RequestBody Estudiante estudiante, BindingResult results){
		Estudiante estudianteNuevo = null;
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
			estudianteNuevo = estudianteService.save(estudiante);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al insertar el estudiante en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El estudiante ha sido creado con éxito!");
		response.put("estudiante", estudianteNuevo);
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
		
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR')")
	@PutMapping("/actualizarEstudiantes")
	public ResponseEntity<?> saveAllEstudiante(@RequestBody List<Estudiante> estudiantes){
		List<Estudiante> estudiantesActualizar = estudiantes;
		Map<String, Object> response = new HashMap<>();


		try {
			estudiantesActualizar = estudianteService.saveAll(estudiantes);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al insertar el estudiante en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El estudiante ha sido creado con éxito!");
		response.put("estudiantes", estudiantesActualizar);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

	}

	@PreAuthorize("hasAnyRole('ADMIN', 'ESTUDIANTE', 'PROFESOR')")
	@GetMapping("/{id}")
	public ResponseEntity<?> getEstudiante(@PathVariable Long id){
		
		Optional<Estudiante> estudiante = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			estudiante = estudianteService.getEstudianteById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al consultar el estudiante en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(estudiante.isEmpty()) {
			response.put("mensaje", "El estudiante con el ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Estudiante>(estudiante.get(), HttpStatus.OK);
		
	}

//	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/buscarDni")
	public ResponseEntity<?> getEstudiantePorUserAndPassword(@RequestParam String username, @RequestParam String password){

		Estudiante estudiante = null;
		Map<String, Object> response = new HashMap<>();

		try {
			estudiante = estudianteService.findByDniAndDni(username, password);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al consultar el estudiante en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if(estudiante == null) {
			response.put("mensaje", "El estudiante con el DNI: ".concat(username.toString().concat(" no existe en la base de datos")));

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Estudiante>(estudiante, HttpStatus.OK);

	}

	@PreAuthorize("hasAnyRole('ADMIN', 'ESTUDIANTE')")
	@GetMapping("/porDni")
	public ResponseEntity<?> getEstudiantePorDni(@RequestParam("dni") String dni){

		Estudiante estudiante = null;
		Map<String, Object> response = new HashMap<>();

		try {
			estudiante = estudianteService.findByDni(dni);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al consultar el estudiante en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if(estudiante == null) {
			response.put("mensaje", "El estudiante con el DNI: ".concat(dni.concat(" no existe en la base de datos")));

		}

		response.put("estudiante", estudiante);

		return new ResponseEntity<>(response, HttpStatus.OK);

	}


	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<?> updateEstudiante(@Valid @RequestBody Estudiante estudiante ,BindingResult results , @PathVariable Long id){
		Estudiante estudianteActual = estudianteService.getEstudianteById(id).get();
		Estudiante estudianteActualizado = null;
		Map<String, Object> response = new HashMap<>();
		
		if(results.hasErrors()) {
			List<String> errors = results.getFieldErrors()
					.stream()
					.map(er -> "El campo '" + er.getField() +"' " + er.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(estudianteActual == null) {
			response.put("mensaje", "Error: No se pudo editar, el estudiante con el ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			estudianteActual.setNombres(estudiante.getNombres());
			estudianteActual.setApellidoPaterno(estudiante.getApellidoPaterno());
			estudianteActual.setApellidoMaterno(estudiante.getApellidoMaterno());
			estudianteActual.setDni(estudiante.getDni());
			estudianteActual.setFechaNacimiento(estudiante.getFechaNacimiento());
			estudianteActual.setCorreo(estudiante.getCorreo());
			estudianteActual.setSexo(estudiante.getSexo());
			estudianteActual.setAulaEstudiante(estudiante.getAulaEstudiante());
			estudianteActual.setDomicilio(estudiante.getDomicilio());
			estudianteActual.setApoderado(estudiante.getApoderado());
			estudianteActual.setGrado(estudiante.getGrado());
			estudianteActual.setAsistencias(estudiante.getAsistencias());
			estudianteActual.setNivel(estudiante.getNivel());
			estudianteActual.setTurno(estudiante.getTurno());
			estudianteActual.setNotas(estudiante.getNotas());
//			estudianteActual.setUsuario(estudiante.getUsuario());
			
			estudianteActualizado = estudianteService.save(estudianteActual);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el estudiante en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El estudiante ha sido actualizado con éxito!");
		response.put("estudiante", estudianteActualizado);
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/actualizarSinAsistencia/{id}")
	public ResponseEntity<?> updateEstudianteAula(@Valid @RequestBody Estudiante estudiante ,BindingResult results , @PathVariable Long id){
		Estudiante estudianteActual = estudianteService.getEstudianteById(id).get();
		Estudiante estudianteActualizado = null;
		Map<String, Object> response = new HashMap<>();

		if(results.hasErrors()) {
			List<String> errors = results.getFieldErrors()
					.stream()
					.map(er -> "El campo '" + er.getField() +"' " + er.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errors", errors);

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		if(estudianteActual == null) {
			response.put("mensaje", "Error: No se pudo editar, el estudiante con el ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			estudianteActual.setNombres(estudiante.getNombres());
			estudianteActual.setApellidoPaterno(estudiante.getApellidoPaterno());
			estudianteActual.setApellidoMaterno(estudiante.getApellidoMaterno());
			estudianteActual.setDni(estudiante.getDni());
			estudianteActual.setFechaNacimiento(estudiante.getFechaNacimiento());
			estudianteActual.setCorreo(estudiante.getCorreo());
			estudianteActual.setSexo(estudiante.getSexo());
			estudianteActual.setAulaEstudiante(estudiante.getAulaEstudiante());
			estudianteActual.setDomicilio(estudiante.getDomicilio());
			estudianteActual.setApoderado(estudiante.getApoderado());
			estudianteActual.setGrado(estudiante.getGrado());
			estudianteActual.setNivel(estudiante.getNivel());
			estudianteActual.setTurno(estudiante.getTurno());
			estudianteActual.setNotas(estudiante.getNotas());

			estudianteActualizado = estudianteService.save(estudianteActual);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el estudiante en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El estudiante ha sido actualizado con éxito!");
		response.put("estudiante", estudianteActualizado);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteEstudiante(@PathVariable Long id){
		
		Map<String, Object> response = new HashMap<>();
		Estudiante estudiante = estudianteService.getEstudianteById(id).orElse(null);
		
		if(estudiante == null) {
			response.put("mensaje", "Error: No se pudo eliminar, el estudiante con el ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			estudianteService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el estudiante en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El estudiante ha sido eliminado con éxito");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
