package com.school.security.service;

import java.util.Optional;

import com.school.security.dao.IRolDao;
import com.school.security.enums.RolNombre;
import com.school.security.models.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class IRoleServiceImpl implements IRolService{

	@Autowired
	private IRolDao rolDao;
	
	@Override
	public Optional<Rol> findByRolNombre(RolNombre rolNombre) {
		return rolDao.findByRolNombre(rolNombre);
	}

	@Override
	public void save(Rol rol) {
		rolDao.save(rol);
	}

}
