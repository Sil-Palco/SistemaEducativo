package com.school.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.school.dao.CursoDao;
import com.school.model.Curso;

@Service
public class CursoServiceImpl implements CursoService {

	@Autowired
	private CursoDao cursoDao;
	
	@Override
	@Transactional
	public Curso save(Curso curso) {
		// TODO Auto-generated method stub
		return cursoDao.save(curso);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Curso> getCursoById(Long id) {
		// TODO Auto-generated method stub
		return cursoDao.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Curso> findAll() {
		// TODO Auto-generated method stub
		return (List<Curso>) cursoDao.findAll();
	}

	@Override
	@Transactional
	public boolean delete(Long id) {
		// TODO Auto-generated method stub
		return getCursoById(id).map(c -> {
			cursoDao.deleteById(id);
			return true;
		}).orElse(false);
	}

}
