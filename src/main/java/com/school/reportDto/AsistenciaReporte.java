package com.school.reportDto;

import java.io.Serializable;

public class AsistenciaReporte implements Serializable {

    private String fecha;
    private Long puntual;
    private Long tardanza;
    private Long inasistencia;

    public AsistenciaReporte(String fecha, Long puntual, Long tardanza, Long inasistencia) {
        this.fecha = fecha;
        this.puntual = puntual;
        this.tardanza = tardanza;
        this.inasistencia = inasistencia;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Long getPuntual() {
        return puntual;
    }

    public void setPuntual(Long puntual) {
        this.puntual = puntual;
    }

    public Long getTardanza() {
        return tardanza;
    }

    public void setTardanza(Long tardanza) {
        this.tardanza = tardanza;
    }

    public Long getInasistencia() {
        return inasistencia;
    }

    public void setInasistencia(Long inasistencia) {
        this.inasistencia = inasistencia;
    }

    private static final long serialVersionUID = 1L;
}
