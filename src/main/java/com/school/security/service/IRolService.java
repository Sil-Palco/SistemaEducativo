package com.school.security.service;

import java.util.Optional;

import com.school.security.enums.RolNombre;
import com.school.security.models.Rol;

public interface IRolService {

	Optional<Rol> findByRolNombre(RolNombre rolNombre);
	
	void save(Rol rol); 
}
