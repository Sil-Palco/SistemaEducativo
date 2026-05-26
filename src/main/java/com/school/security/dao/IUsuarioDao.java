package com.school.security.dao;

import java.util.Optional;

import com.school.security.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUsuarioDao extends JpaRepository<Usuario, Long>{

	Optional<Usuario> findByUsername(String username);
	Boolean existsByUsername(String username);
}
