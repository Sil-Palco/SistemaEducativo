
package com.school.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.school.dao.ApoderadoDao;
import com.school.model.Apoderado;

@Service
public class ApoderadoServiceImpl implements ApoderadoService{

	@Autowired
	private ApoderadoDao apoderadoService;
	
	@Override
	@Transactional(readOnly = false)
	public Apoderado save(Apoderado apoderado) {
		// TODO Auto-generated method stub
		return apoderadoService.save(apoderado);
	}

	@Override
	@Transactional
	public Optional<Apoderado> getApoderadoById(Long id) {
		// TODO Auto-generated method stub
		return apoderadoService.findById(id);
	}

	@Override
	@Transactional
	public List<Apoderado> findAll() {
		// TODO Auto-generated method stub
		return apoderadoService.findAll();
	}

	@Override
	@Transactional(readOnly = false)
	public boolean delete(Long id) {
		// TODO Auto-generated method stub
		return getApoderadoById(id).map(a -> {
			apoderadoService.deleteById(id);
			return true;
		}).orElse(false);
	}

	@Override
	public Apoderado findByDni(String dni) {
		return apoderadoService.findByDni(dni);
	}

}
