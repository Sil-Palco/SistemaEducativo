package com.school.security.dao;

import java.util.Optional;

import com.school.security.enums.RolNombre;
import com.school.security.models.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRolDao extends JpaRepository<Rol, Long>{

	Optional<Rol> findByRolNombre(RolNombre rolNombre);
}
