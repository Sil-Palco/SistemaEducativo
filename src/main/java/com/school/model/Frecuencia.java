package com.school.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "frecuencias")
public class Frecuencia implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String horario_inicio;

    private String horario_fin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clase_id")
    @JsonIgnoreProperties({"frecuencias", "hibernateLazyInitializer", "handler"})
    private Clase clase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "dia_id")
    private DiaSemana dia;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHorario_inicio() {
        return horario_inicio;
    }

    public void setHorario_inicio(String horario_inicio) {
        this.horario_inicio = horario_inicio;
    }

    public String getHorario_fin() {
        return horario_fin;
    }

    public void setHorario_fin(String horario_fin) {
        this.horario_fin = horario_fin;
    }

    public DiaSemana getDia() {
        return dia;
    }

    public void setDia(DiaSemana dia) {
        this.dia = dia;
    }

    public Clase getClase() {
        return clase;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }

    private static final long serialVersionUID = 1L;
}
