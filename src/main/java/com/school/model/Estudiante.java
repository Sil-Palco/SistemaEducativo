package com.school.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.school.security.models.Usuario;

@Entity
@Table(name = "estudiantes")
public class Estudiante implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty(message = "no puede estar vacío.")
	@Size(min = 2, max = 40, message = "tiene que ser entre 2 y 40 caracteres.")
	private String nombres;

	@Column(name = "apellido_paterno")
	@NotEmpty(message = "no puede estar vacío.")
	@Size(min = 2, max = 20, message = "tiene que ser entre 2 y 15 caracteres.")
	private String apellidoPaterno;

	@Column(name = "apellido_materno")
	@NotEmpty(message = "no puede estar vacío")
	@Size(min = 2, max = 20, message = "tiene que ser entre 2 y 15 caracteres.")
	private String apellidoMaterno;

	@NotNull(message = "no puede estar vacío.")
	@Column(name = "fecha_nacimiento")
	private String fechaNacimiento;

	@NotEmpty(message = "no puede estar vacío.")
	@Size(min = 8, max = 8, message = " tiene que tener {min} caracteres.")
	private String dni;

	@NotEmpty(message = "no puede estar vacío")
	@Size(min = 10, max = 80, message = " tiene que ser entre 10 y 80 caracteres.")
	private String domicilio;

	@Size(min = 9, max = 9, message = " tiene que tener 8 caracteres.")
	@NotEmpty(message = "no puede estar vacío")
	private String celular;

	@NotEmpty(message = "no puede estar vacío")
	private String sexo;

	private String correo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Aula aulaEstudiante;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	@JoinColumn(name = "apoderado_id", nullable = false)
//	@NotNull(message = "no puede estar vacío")
	@JoinColumn(name = "apoderado_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Apoderado apoderado;

	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull(message = "no puede estar vacío")
	@JoinColumn(name = "grado_id", nullable = false)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Grado grado;

	@OneToOne(fetch = FetchType.LAZY)
//	@NotNull(message = "no puede estar vacío")
	@JoinColumn(name = "usuario_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Usuario usuario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "turno_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Turno turno;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "nivel_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Nivel nivel;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "estudiante")
	@JsonIgnoreProperties({"estudiante","hibernateLazyInitializer", "handler"})
	private List<Asistencia> asistencias = new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "estudiante")
	private List<Nota> notas = new ArrayList<>();

	public Estudiante() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidoPaterno() {
		return apellidoPaterno;
	}

	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}

	public String getApellidoMaterno() {
		return apellidoMaterno;
	}

	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}

	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public Aula getAulaEstudiante() {
		return aulaEstudiante;
	}

	public void setAulaEstudiante(Aula aula) {
		this.aulaEstudiante = aula;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public Apoderado getApoderado() {
		return apoderado;
	}

	public void setApoderado(Apoderado apoderado) {
		this.apoderado = apoderado;
	}

	public Grado getGrado() {
		return grado;
	}

	public void setGrado(Grado grado) {
		this.grado = grado;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Turno getTurno() {
		return turno;
	}

	public void setTurno(Turno turno) {
		this.turno = turno;
	}

	public Nivel getNivel() {
		return nivel;
	}

	public void setNivel(Nivel nivel) {
		this.nivel = nivel;
	}

	public List<Asistencia> getAsistencias() {
		return asistencias;
	}

	public void setAsistencias(List<Asistencia> asistencias) {
		this.asistencias = asistencias;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public List<Nota> getNotas() {
		return notas;
	}

	public void setNotas(List<Nota> notas) {
		this.notas = notas;
	}

	private static final long serialVersionUID = 1L;
}
