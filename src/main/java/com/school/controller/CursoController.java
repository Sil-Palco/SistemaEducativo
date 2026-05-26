package com.school.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.school.model.Aula;
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

import com.school.model.Curso;
import com.school.service.CursoService;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/cursos")
public class CursoController {

	@Autowired
	private CursoService cursoService;

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<List<Curso>> getAllCursos(){
		return new ResponseEntity<List<Curso>>(cursoService.findAll(), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<?> getCurso(@PathVariable Long id){

		Optional<Curso> curso = null;
		Map<String, Object> response = new HashMap<>();

		try {
			curso = cursoService.getCursoById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al consultar el curso en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if(curso.isEmpty()) {
			response.put("mensaje", "El curso con el ID: ".concat(id.toString().concat(" no existe en la base de datos")));

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Curso>(curso.get() ,HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/crear")
	public ResponseEntity<?> saveCurso(@Valid @RequestBody Curso curso, BindingResult results){
		
		Curso cursoNuevo = null;
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
			cursoNuevo = cursoService.save(curso);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al insertar el curso en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El curso ha sido creado con éxito!");
		response.put("estudiante", cursoNuevo);
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
		
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<?> updateCurso(@Valid @RequestBody Curso curso ,BindingResult results , @PathVariable Long id){
		Curso cursoActual = cursoService.getCursoById(id).get();
		Curso cursoActualizado = null;
		Map<String, Object> response = new HashMap<>();
		
		if(results.hasErrors()) {
			List<String> errors = results.getFieldErrors()
					.stream()
					.map(er -> "El campo '" + er.getField() +"' " + er.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(cursoActual == null) {
			response.put("mensaje", "Error: No se pudo editar, el curso con el ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			cursoActual.setNombre(curso.getNombre());
			
			cursoActualizado = cursoService.save(cursoActual);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el curso en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El curso ha sido actualizado con éxito!");
		response.put("curso", cursoActualizado);
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCurso(@PathVariable Long id){
		
		Map<String, Object> response = new HashMap<>();
		Curso curso = cursoService.getCursoById(id).orElse(null);
		
		if(curso == null) {
			response.put("mensaje", "Error: No se pudo eliminar, el curso con el ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			cursoService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el curso en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El curso ha sido eliminado con éxito");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
}
