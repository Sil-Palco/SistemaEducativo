package com.school.security.service;

import java.util.Optional;

import com.school.security.dao.IUsuarioDao;
import com.school.security.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IUsuarioServiceImpl implements IUsuarioService{

	@Autowired
	private IUsuarioDao usuarioDao;
	
	@Override
	public Optional<Usuario> findByUsername(String username) {
		return usuarioDao.findByUsername(username);
	}

	@Override
	public Boolean existsByUsername(String username) {
		return usuarioDao.existsByUsername(username);
	}

	@Override
	public Usuario save(Usuario usuario) {
		return usuarioDao.save(usuario);
	}

	@Override
	public Usuario findById(Long id) {
		return usuarioDao.findById(id).orElse(null);
	}

	@Override
	public void delete(Long id) {
		usuarioDao.deleteById(id);
	}


}
