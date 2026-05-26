package com.school.service;

import java.util.List;
import java.util.Optional;

import com.school.model.DiaSemana;
import com.school.model.Matricula;
import com.school.model.Nivel;
import com.school.model.Turno;

public interface MatriculaService {
	
	public Matricula save(Matricula matricula);
	
	public Optional<Matricula> getMatriculaById(Long id);

	public List<Matricula> findAll();
	
	public boolean delete(Long id);

	public List<Nivel> getNiveles();

	public List<Turno> getTurnos();

	public List<DiaSemana> getDias();

	public List<Matricula> getMatriculasPorEstudiante(Long id);
}
