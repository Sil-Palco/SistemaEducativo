package com.school.dao;

import com.school.model.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.NamedNativeQuery;
import java.util.List;

public interface AsistenciaDao extends JpaRepository<Asistencia, Long> {

    public List<Asistencia> findByFecha(String fecha);

//    @Query(value = "SELECT * FROM asistencias a inner join estudiantes e on a.estudiante_id = e.id where a.fecha = ?1 and e.aula_estudiante_id = ?2", nativeQuery = true)
//    public List<Asistencia> findAsistenciaByFechaAula(String fecha, String idAula);

    @Query("FROM Asistencia a WHERE a.fecha = ?1 and a.estudiante.aulaEstudiante.id = ?2")
    public List<Asistencia> findAsistenciaByFechaAula(String fecha, Long idAula);


}
