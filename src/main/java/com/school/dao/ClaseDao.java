package com.school.dao;

import com.school.model.Asignacion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.school.model.Clase;

import java.util.List;

public interface ClaseDao extends CrudRepository<Clase, Long> {

    @Query("FROM Clase c where c.curso.id = ?1 and c.aula.id = ?2")
    public Clase findClasePorAulaYCurso(Long idCurso, Long idAula);

    @Query("From Asignacion a where a.clase.id = ?1")
    public List<Asignacion> asignacionesPorClase(Long idClase);
}
