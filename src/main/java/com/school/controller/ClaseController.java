package com.school.controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.school.dao.MaterialDao;
import com.school.model.Asignacion;
import com.school.model.Material;
import com.school.model.Nota;
import com.school.service.FrecuenciaService;
import com.school.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.school.model.Clase;
import com.school.service.ClaseService;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/clases")
public class ClaseController {

	@Autowired
	private ClaseService claseService;

	@Autowired
	private MaterialDao materialDao;

	@Autowired
	private FrecuenciaService frecuenciaService;

	@Autowired
	private UploadFileService uploadFileService;

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<List<Clase>> getAllClases(){
		return new ResponseEntity<List<Clase>>(claseService.findAll(), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR')")
	@PostMapping("/crearNota")
	public ResponseEntity<Nota> crearNota(@RequestBody Nota nota){
		return new ResponseEntity<Nota>(nota, HttpStatus.CREATED);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR', 'ESTUDIANTE')")
	@GetMapping("/{id}")
	public ResponseEntity<?> getClase(@PathVariable Long id){
		
		Optional<Clase> clase = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			clase = claseService.getClaseById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al consultar la clase en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(clase.isEmpty()) {
			response.put("mensaje", "La clase con el ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Clase>(clase.get() ,HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/crear")
	public ResponseEntity<?> saveClase(@Valid @RequestBody Clase clase, BindingResult result){
		
		Clase claseNueva = null;
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(er -> "El campo '" + er.getField() + "' " + er.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		}
		
		try {
			claseNueva = claseService.save(clase);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al insertar la clase en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("clase", claseNueva);
		response.put("mensaje", "La clase ha sido creada con éxito");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}


	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<?> updateClase(@Valid @RequestBody Clase clase, BindingResult result, @PathVariable Long id){
		
		Clase claseActual = claseService.getClaseById(id).orElse(null);
		Clase claseActualizada = null;
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(er -> "El campo '" + er.getField() + "' " + er.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		}
		
		
		if(claseActual == null) {
			response.put("mensaje", "Error: No se pudo editar la clase con el ID: ".concat(id.toString().concat(" no existe en la base de datos.")));
		}
		
		try {
			//seteamos las clases a null para luego guardarla y obtener luego solo las nuevas frecuencias
			claseActual.getFrecuencias().forEach(f -> f.setClase(null));
			claseActual = claseService.saveNoFrecuenciaUpdate(claseActual);

			claseActual.setAula(clase.getAula());
			claseActual.setEmpleado(clase.getEmpleado());
			claseActual.setCurso(clase.getCurso());
			claseActual.setFrecuencias(clase.getFrecuencias());
			claseActual.setMateriales(clase.getMateriales());
			claseActualizada = claseService.update(claseActual);

			//eliminamos las clases que tienen la clase en NULL
			frecuenciaService.deleFrecuenciasNulas();
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al insertar la clase en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("clase", claseActualizada);
		response.put("mensaje", "La clase ha sido actualizada con éxito");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteClase(@PathVariable Long id){
		
		Clase clase = claseService.getClaseById(id).orElse(null);
		Map<String, Object> response = new HashMap<>();
		
		if(clase == null) {
			response.put("mensaje", "Error: No se pudo eliminar la clase con el ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			clase.getMateriales().forEach(c -> uploadFileService.deleteFile(c.getArchivo()));
			claseService.delete(id);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar la clase en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "La clase ha sido eliminada con éxito");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR')")
	@PostMapping("/uploads")
	public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("idClase") String idClase, @RequestParam("nombreFile") String nombreFile){
		Map<String, Object> response = new HashMap<>();

		Clase clase = claseService.getClaseById(Long.parseLong(idClase)).orElse(null);

		if(!archivo.isEmpty()){
			String nombreArchivo = null;

			try {
				nombreArchivo = uploadFileService.uploadFile(archivo);

				Material material = new Material();
				material.setNombre(nombreFile);
				material.setArchivo(nombreArchivo);
				clase.getMateriales().add(material);

				claseService.update(clase);
			} catch (IOException e) {
				response.put("mensaje", "Error al subir archivo.");
				response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));

				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

			response.put("clase", clase);
			response.put("mensaje", "Has subido correctamente el archivo " + nombreArchivo);


		}

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR')")
	@DeleteMapping("/eliminarMaterial")
	public ResponseEntity<?> deleteArchivo(@RequestParam("idClase") String idClase, @RequestParam("idMaterial") String idMaterial){

		Map<String, Object> response = new HashMap<>();
		Clase clase = claseService.getClaseById(Long.parseLong(idClase)).orElse(null);

		if(clase == null){
			response.put("mensaje", "La clase con el id "+idClase+" no existe en la base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		Material materialEncontrado = clase.getMateriales()
				.stream()
				.filter(material -> Long.parseLong(idMaterial) == material.getId())
				.findFirst()
				.orElse(null);

		if(materialEncontrado == null){
			response.put("mensaje", "El material con el id "+idMaterial+" no existe en la base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		String archivoParaBorrar = materialEncontrado.getArchivo();

		if(archivoParaBorrar == null || archivoParaBorrar.length() == 0 || archivoParaBorrar.length() < 0){
			response.put("mensaje", "Hubo un error al eliminar el material "+archivoParaBorrar);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		try{

			clase.getMateriales().remove(materialEncontrado);
			uploadFileService.deleteFile(archivoParaBorrar);
			claseService.update(clase);
			materialDao.deleteById(materialEncontrado.getId());
		}catch (DataAccessException e){
			response.put("mensaje", "Error al eliminar el archivo en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}



		response.put("mensaje", "El archivo se eliminó con éxito.");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@GetMapping("/uploads/{nombre:.+}")
	public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String nombre) throws MalformedURLException {

		byte[] data = uploadFileService.cargarImagen(nombre);
		ByteArrayResource resource = new ByteArrayResource(data);

		return ResponseEntity.ok().contentLength(data.length).header("Content.type", "application/octet-stream")
				.header("Content-disposition", "attachment; filename=\"" + nombre + "\"").body(resource);
	}

	@GetMapping("/asignacionesPorClase/{idClase}")
	public ResponseEntity<List<Asignacion>> listaAsignacionesPorClase(@PathVariable Long idClase) {
		return new ResponseEntity<>(claseService.asignacionesPorClase(idClase), HttpStatus.OK);
	}
	
}
