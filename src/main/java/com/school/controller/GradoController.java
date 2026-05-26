package com.school.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.school.model.Grado;
import com.school.service.GradoService;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/grados")
public class GradoController {

	@Autowired
	private GradoService gradoService;

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<List<Grado>> getAllGrados(){
		return new ResponseEntity<List<Grado>>(gradoService.findAll(), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<?> getGrado(@PathVariable Long id){
		
		Optional<Grado> grado = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			grado = gradoService.getGradoById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al consultar el grado en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(grado.isEmpty()) {
			response.put("mensaje", "El grado con el ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Grado>(grado.get() ,HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/crear")
	public ResponseEntity<?> saveGrado(@Valid @RequestBody Grado grado, BindingResult results){
		
		Grado gradoNuevo = null;
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
			gradoNuevo = gradoService.save(grado);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al insertar el grado en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("grado", gradoNuevo);
		response.put("mensaje", "La grado ha sido creado con éxito");
		
		return new ResponseEntity<Map<String, Object>>(response ,HttpStatus.CREATED);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<?> updateGrado(@Valid @RequestBody Grado grado, BindingResult results, @PathVariable Long id){
		
		Grado gradoActual = gradoService.getGradoById(id).orElse(null);
		Grado gradoActualizado = null;
		Map<String, Object> response = new HashMap<>();
		
		if(results.hasErrors()) {
			List<String> errors = results.getFieldErrors()
					.stream()
					.map(er -> "El campo '" + er.getField() +"' " + er.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(gradoActual == null) {
			response.put("mensaje", "Error: No se pudo editar, el grado con el ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			
			gradoActual.setNombre(grado.getNombre());
			
			
			gradoActualizado = gradoService.save(gradoActual);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el grado en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("grado", gradoActualizado);
		response.put("mensaje", "La grado ha sido actualizado con éxito");
		
		return new ResponseEntity<Map<String, Object>>(response ,HttpStatus.CREATED);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteGrado(@PathVariable Long id){
	
		Grado grado = gradoService.getGradoById(id).orElse(null);
		Map<String, Object> response = new HashMap<>();
		
		if(grado == null) {
			response.put("mensaje", "Error: No se pudo eliminar el grado con el ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			gradoService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el grado en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El grado ha sido eliminado con éxito");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
}
