package com.school.service;

import com.school.model.Asistencia;
import com.school.reportDto.AsistenciaReporte;

import java.util.List;

public interface AsistenciaService {

    byte[] generarReporteAsistencia(String tipo, String fecha);
    public List<Asistencia> findByFecha(String fecha);
    public List<Asistencia> findAsistenciaByFechaAula(String fecha, Long idAula);
    public AsistenciaReporte obtenerDatosAsistenciaPorDia(String fecha);
    public List<Asistencia> updateAsistencias(List<Asistencia> asistencias);
}
