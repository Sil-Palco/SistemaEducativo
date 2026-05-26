package com.school.service;

import java.util.List;
import java.util.Optional;

import com.school.model.Nota;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.school.dao.EstudianteDao;
import com.school.model.Estudiante;

@Service
public class EstudianteServiceImpl implements EstudianteService{

	@Autowired
	private EstudianteDao estudianteDao;
	
	@Override
	@Transactional
	public Estudiante save(Estudiante estudiante) {
		return estudianteDao.save(estudiante);
	}

	@Override
	@Transactional(readOnly = false)
	public List<Estudiante> saveAll(List<Estudiante> estudiantes) {

		estudiantes.stream()
				.forEach(e -> e.getAsistencias()
						.stream()
						.forEach(a-> { if(a.getEstudiante() == null) a.setEstudiante(e); }));

		return estudianteDao.saveAll(estudiantes);
	}

	@Override
	public Estudiante loginUsuario(String username, String password) {
		return estudianteDao.loginUsuario(username, password);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Estudiante> getEstudianteById(Long id) {
		return estudianteDao.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Estudiante> findAll() {
		return estudianteDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Estudiante> findAll(Pageable pageable) {
		return estudianteDao.findAll(pageable);
	}

	@Override
	@Transactional
	public boolean delete(Long id) {
		return getEstudianteById(id).map(estudiante -> {
			estudianteDao.deleteById(id);
			return true;
		}).orElse(false);
	}


	@Override
	public Estudiante findByDniAndDni(String username, String password) {
		return estudianteDao.findByDniAndDni(username,password);
	}

	@Override
	public Estudiante findByDni(String dni) {
		return estudianteDao.findByDni(dni);
	}


}
