package com.school.service;

import java.util.List;
import java.util.Optional;

import com.school.model.Matricula;
import com.school.model.Nota;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.school.model.Estudiante;


public interface EstudianteService {

	public Estudiante save(Estudiante estudiante);

	public List<Estudiante> saveAll(List<Estudiante> estudiantes);

	public Estudiante loginUsuario(String username, String password);
	
	public Optional<Estudiante> getEstudianteById(Long id);

	public List<Estudiante> findAll();
	
	public Page<Estudiante> findAll(Pageable pageable);
	
	public boolean delete(Long id);

	public Estudiante findByDniAndDni(String username, String password);

	public Estudiante findByDni(String dni);


}
