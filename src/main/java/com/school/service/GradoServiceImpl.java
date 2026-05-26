package com.school.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.school.dao.GradoDao;
import com.school.model.Grado;

@Service
public class GradoServiceImpl implements GradoService {

	@Autowired
	private GradoDao gradoDao;

	@Override
	public Grado save(Grado grado) {
		return gradoDao.save(grado);
	}

	@Override
	public Optional<Grado> getGradoById(Long id) {
		return gradoDao.findById(id);
	}

	@Override
	public List<Grado> findAll() {
		return gradoDao.findAll();
	}

	@Override
	public boolean delete(Long id) {
		return getGradoById(id).map(g -> {
			gradoDao.deleteById(id);
			return true;
		}).orElse(false);
	}
	
	
	
}
