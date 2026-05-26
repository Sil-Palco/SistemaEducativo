package com.school.service;

import java.util.List;
import java.util.Optional;

import com.school.model.Aula;
import com.school.model.Clase;
import com.school.model.Estudiante;

public interface AulaService {

	public Aula save(Aula aula);
	
	public Optional<Aula> getAulaById(Long id);

	public List<Aula> findAll();
	
	public boolean delete(Long id);

	public List<Estudiante> findEstudiantesAula(Long id);
	public List<Clase> findClasesAula(Long id);

}
