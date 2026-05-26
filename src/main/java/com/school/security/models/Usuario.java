package com.school.security.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "usuarios")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty
	@Column(unique = true)
	private String username;
	
	@NotEmpty
	private String password;
	
	private Boolean enabled;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "usuario_rol", joinColumns = @JoinColumn(name="usuario_id"),
			   inverseJoinColumns = @JoinColumn(name="rol_id"))
	private Set<Rol> roles = new HashSet<>();

	public Usuario() {
	}
//
//	public Usuario(@NotNull String nombre, @NotNull String username, @NotNull String password, Boolean enabled) {
//		this.nombre = nombre;
//		this.username = username;
//		this.password = password;
//		this.enabled = enabled;
//	}
//
	

	public Usuario(@NotEmpty String username,
			@NotEmpty String password, Boolean enabled) {

		this.username = username;
		this.password = password;
		this.enabled = enabled;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Set<Rol> getRoles() {
		return roles;
	}

	public void setRoles(Set<Rol> roles) {
		this.roles = roles;
	}
	
}
