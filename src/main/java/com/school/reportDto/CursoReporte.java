package com.school.reportDto;

import java.io.Serializable;

public class CursoReporte implements Serializable {

    private String nombreAula;
    private String nombreCurso;
    private Long aprobados;
    private Long desaprobados;


    public CursoReporte() {
    }

    public CursoReporte(String nombreAula, String nombreCuso, Long aprobados, Long desaprobados) {
        this.nombreAula = nombreAula;
        this.nombreCurso = nombreCuso;
        this.aprobados = aprobados;
        this.desaprobados = desaprobados;
    }

    public String getNombreAula() {
        return nombreAula;
    }

    public void setNombreAula(String nombreAula) {
        this.nombreAula = nombreAula;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public Long getAprobados() {
        return aprobados;
    }

    public void setAprobados(Long aprobados) {
        this.aprobados = aprobados;
    }

    public Long getDesaprobados() {
        return desaprobados;
    }

    public void setDesaprobados(Long desaprobados) {
        this.desaprobados = desaprobados;
    }

    private static final long serialVersionUID = 1L;

}
