package com.school.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "clases")
public class Clase implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull(message = "no puede estar vacío.")
	@JoinColumn(name = "aula_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Aula aula;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "curso_id", nullable = false)
	@JsonIgnoreProperties({"clases", "hibernateLazyInitializer", "handler"})
	private Curso curso;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = " empleado_id", nullable = true)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Empleado empleado;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "clase_id")
	private List<Material> materiales = new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "clase")
	private List<Frecuencia> frecuencias = new ArrayList<>();
	
	public Clase() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Aula getAula() {
		return aula;
	}

	public void setAula(Aula aula) {
		this.aula = aula;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public List<Material> getMateriales() {
		return materiales;
	}

	public void setMateriales(List<Material> materiales) {
		this.materiales = materiales;
	}

	public List<Frecuencia> getFrecuencias() {
		return frecuencias;
	}

	public void setFrecuencias(List<Frecuencia> frecuencias) {
		this.frecuencias = frecuencias;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
