package com.school.security.service;

import java.util.List;
import java.util.Optional;

import com.school.security.models.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUsuarioService {

	Optional<Usuario> findByUsername(String username);
	Boolean existsByUsername(String username);
	Usuario save(Usuario usuario);
	Usuario findById(Long id);
	void delete(Long id);
}
