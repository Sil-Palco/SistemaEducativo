package com.school.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "respuestas")
public class RespuestaAsignacion implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String archivo;

    private String nota;

    private String nombresEstudiante;

    private String dniEstudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "asignacion_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Asignacion asignacion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public Asignacion getAsignacion() {
        return asignacion;
    }

    public void setAsignacion(Asignacion asignacion) {
        this.asignacion = asignacion;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getDniEstudiante() {
        return dniEstudiante;
    }

    public void setDniEstudiante(String dniEstudiante) {
        this.dniEstudiante = dniEstudiante;
    }

    public String getNombresEstudiante() {
        return nombresEstudiante;
    }

    public void setNombresEstudiante(String nombresEstudiante) {
        this.nombresEstudiante = nombresEstudiante;
    }

    private static final long serialVersionUID = -3781853874082300649L;

}
