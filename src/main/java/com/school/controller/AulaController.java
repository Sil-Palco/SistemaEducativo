package com.school.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.school.model.Clase;
import com.school.model.Estudiante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.school.model.Aula;
import com.school.service.AulaService;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/aulas")
public class AulaController {

	@Autowired
	private AulaService aulaService;

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<List<Aula>> getAllAulas(){
		return new ResponseEntity<List<Aula>>(aulaService.findAll(), HttpStatus.OK);
	}

//	@PreAuthorize("hasAnyRole('ADMIN', 'ESTUDIANTE', 'PROFESOR')")
	@GetMapping("/estudiantes")
	public ResponseEntity<List<Estudiante>> getAllEstudiantesAula(@RequestParam("id") String id){
		return new ResponseEntity<List<Estudiante>>(aulaService.findEstudiantesAula(Long.parseLong(id)), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'ESTUDIANTE')")
	@GetMapping("/clases")
	public ResponseEntity<List<Clase>> getAllClasesAula(@RequestParam("id") String id){
		return new ResponseEntity<List<Clase>>(aulaService.findClasesAula(Long.parseLong(id)), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR')")
	@GetMapping("/{id}")
	public ResponseEntity<?> getAula(@PathVariable Long id){
		
		Optional<Aula> aula = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			aula = aulaService.getAulaById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al consultar el aula en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(aula.isEmpty()) {
			response.put("mensaje", "El aula con el ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Aula>(aula.get() ,HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/crear")
	public ResponseEntity<?> saveAula(@Valid @RequestBody Aula aula, BindingResult results){
		
		Aula aulaNuevo = null;
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
			aulaNuevo = aulaService.save(aula);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al insertar el aula en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("aula", aulaNuevo);
		response.put("mensaje", "La aula ha sido creada con éxito");
		
		return new ResponseEntity<Map<String, Object>>(response ,HttpStatus.CREATED);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<?> updateAula(@Valid @RequestBody Aula aula, BindingResult results, @PathVariable Long id){
		
		Aula aulaActual = aulaService.getAulaById(id).orElse(null);
		Aula aulaActualizado = null;
		Map<String, Object> response = new HashMap<>();
		
		if(results.hasErrors()) {
			List<String> errors = results.getFieldErrors()
					.stream()
					.map(er -> "El campo '" + er.getField() +"' " + er.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(aulaActual == null) {
			response.put("mensaje", "Error: No se pudo editar el aula con el ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			
			aulaActual.setNombre(aula.getNombre());
			aulaActual.setSeccion(aula.getSeccion());
			aulaActual.setTurno(aula.getTurno());
			aulaActual.setNivel(aula.getNivel());
			aulaActual.setGradoAula(aula.getGradoAula());
			aulaActual.setCapacidad(aula.getCapacidad());
			
			aulaActualizado = aulaService.save(aulaActual);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el aula en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("aula", aulaActualizado);
		response.put("mensaje", "La aula ha sido actualizada con éxito");
		
		return new ResponseEntity<Map<String, Object>>(response ,HttpStatus.CREATED);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteAula(@PathVariable Long id){
	
		Aula aula = aulaService.getAulaById(id).orElse(null);
		Map<String, Object> response = new HashMap<>();
		
		if(aula == null) {
			response.put("mensaje", "Error: No se pudo eliminar el aula con el ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			aulaService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el aula en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El aula ha sido eliminado con éxito");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}	
	
}
