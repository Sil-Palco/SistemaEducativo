package com.school.service;

import java.util.List;
import java.util.Optional;

import com.school.model.Curso;

public interface CursoService {

	public Curso save(Curso curso);
	
	public Optional<Curso> getCursoById(Long id);

	public List<Curso> findAll();
	
	public boolean delete(Long id);
}
