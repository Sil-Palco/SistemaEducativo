package com.school.service;

import java.util.List;
import java.util.Optional;

import com.school.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.school.dao.MatriculaDao;

@Service
public class MatriculaServiceImpl implements MatriculaService{

	@Autowired
	private MatriculaDao matriculaDao;

	@Override
	@Transactional(readOnly = false)
	public Matricula save(Matricula matricula) {
		return matriculaDao.save(matricula);
	}

	@Override
	@Transactional
	public Optional<Matricula> getMatriculaById(Long id) {
		// TODO Auto-generated method stub
		return matriculaDao.findById(id);
	}

	@Override
	@Transactional
	public List<Matricula> findAll() {
		// TODO Auto-generated method stub
		return matriculaDao.findAll();
	}

	@Override
	@Transactional(readOnly = false)
	public boolean delete(Long id) {
		// TODO Auto-generated method stub
		return getMatriculaById(id).map(m -> {
			matriculaDao.deleteById(id);
			return true;
		}).orElse(false);
	}

	@Override
	public List<Nivel> getNiveles() {
		return matriculaDao.getNiveles();
	}

	@Override
	public List<Turno> getTurnos() {
		return matriculaDao.getTurnos();
	}

	@Override
	public List<DiaSemana> getDias() {
		return matriculaDao.getDias();
	}

	@Override
	public List<Matricula> getMatriculasPorEstudiante(Long id) {
		return matriculaDao.getMatriculasPorEstudiante(id);
	}


}
