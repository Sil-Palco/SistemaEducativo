package com.school.security.dto;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotBlank;

public class NuevoUsuario {

	@NotBlank
	private String username;

	@NotBlank
	private String password;

	
	private Boolean enabled;
	private Set<String> roles = new HashSet<>();

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
	public Set<String> getRoles() {
		return roles;
	}
	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
	
	
}
