package com.school.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "notas")
public class Nota implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer nota_bim1;
    private Integer nota_bim2;
    private Integer nota_bim3;
    private Integer nota_bim4;
    private Double promedio_final;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"notas", "hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "estudiante_id")
    private Estudiante estudiante;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNota_bim1() {
        return nota_bim1;
    }

    public void setNota_bim1(Integer nota_bim1) {
        this.nota_bim1 = nota_bim1;
    }

    public Integer getNota_bim2() {
        return nota_bim2;
    }

    public void setNota_bim2(Integer nota_bim2) {
        this.nota_bim2 = nota_bim2;
    }

    public Integer getNota_bim3() {
        return nota_bim3;
    }

    public void setNota_bim3(Integer nota_bim3) {
        this.nota_bim3 = nota_bim3;
    }

    public Integer getNota_bim4() {
        return nota_bim4;
    }

    public void setNota_bim4(Integer nota_bim4) {
        this.nota_bim4 = nota_bim4;
    }

    public Double getPromedio_final() {
        promedio_final = Double.valueOf((nota_bim1 + nota_bim2 + nota_bim3 + nota_bim4)/4);
        return promedio_final;
    }

    public void setPromedio_final(Double promedio_final) {
        this.promedio_final = promedio_final;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    private static final long serialVersionUID = 1L;
}
