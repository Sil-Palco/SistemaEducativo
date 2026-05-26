package com.school.security.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.school.security.dto.JwtDto;
import com.school.security.dto.LoginUsuario;
import com.school.security.dto.NuevoUsuario;
import com.school.security.enums.RolNombre;
import com.school.security.jwt.JwtProvider;
import com.school.security.models.Rol;
import com.school.security.models.Usuario;
import com.school.security.service.IRolService;
import com.school.security.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://localhost:4200"})
public class AuthController {

	@Autowired
	private PasswordEncoder passwordEnconder;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private IRolService rolService;
	
	@Autowired
	private JwtProvider jwtProvider;
	
	@PutMapping("/usuarios/{id}")
	public ResponseEntity<?> actualizarUsuario(@Valid @RequestBody Usuario usuario, BindingResult result, @PathVariable Long id){
		Usuario usuarioActual = usuarioService.findById(id);
		Usuario usuarioActualizado = null;
		
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(usuarioActual == null) {
			response.put("mensaje", "Error: No se pudo editar el usuario con el ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			usuarioActual.setEnabled(usuario.getEnabled());
			usuarioActual.setPassword(usuario.getPassword());
			
			usuarioActualizado = usuarioService.save(usuarioActual);
		}catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el usuario en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "Usuario actualizado con éxito!");
		response.put("usuario", usuarioActualizado);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PostMapping("/nuevo")
	public ResponseEntity<?> nuevo(@Valid @RequestBody NuevoUsuario nuevoUsuario, BindingResult result){
		
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(usuarioService.existsByUsername(nuevoUsuario.getUsername())) {
			response.put("mensaje", "El usuario ya existe");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);

		}

		Usuario usuario = new Usuario(nuevoUsuario.getUsername(), passwordEnconder.encode(nuevoUsuario.getPassword()), nuevoUsuario.getEnabled());

		Set<Rol> roles = new HashSet<>();
		if(nuevoUsuario.getRoles().contains("ROLE_ESTUDIANTE")){
			roles.add(rolService.findByRolNombre(RolNombre.ROLE_ESTUDIANTE).get());
		}

		if(nuevoUsuario.getRoles().contains("ROLE_PROFESOR")){
			roles.add(rolService.findByRolNombre(RolNombre.ROLE_PROFESOR).get());
		}

		if(nuevoUsuario.getRoles().contains("ROLE_ADMIN")){
			roles.add(rolService.findByRolNombre(RolNombre.ROLE_ADMIN).get());
		}
		
		if(nuevoUsuario.getRoles().contains("admin"))
			roles.add(rolService.findByRolNombre(RolNombre.ROLE_ADMIN).get());
		
		usuario.setRoles(roles);
		
		Usuario usuarioCreado = usuarioService.save(usuario);
		
		response.put("mensaje", "Usuario guardado");
		response.put("usuario", usuarioCreado);
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
		
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginUsuario loginUsuario, BindingResult result){
		
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		
		Authentication auth = authManager
				.authenticate(
						new UsernamePasswordAuthenticationToken(loginUsuario.getUsername(), loginUsuario.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		String jwt = jwtProvider.generatToken(auth);
		
		JwtDto jwtDto = new JwtDto(jwt);
		
		response.put("mensaje", "Login exitoso");
		response.put("jwtDto", jwtDto);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@GetMapping("/usuarios/{username}")
	public ResponseEntity<?> findUsuario(@PathVariable String username){
		
		Usuario usuario = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			usuario = usuarioService.findByUsername(username).get();
		}catch(DataAccessException e) {//Por si hay un error en la BBDD
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(usuario == null) {
			response.put("mensaje", "El usuario: ".concat(usuario.toString().concat(" no existe en la base de datos")));
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		
		return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<JwtDto> refresh(@RequestBody JwtDto jwtDto) throws ParseException{
		String token = jwtProvider.refreshToken(jwtDto);
		JwtDto jwt = new JwtDto(token);
		
		return new ResponseEntity<JwtDto>(jwt, HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
}
