package com.school.dao;

import com.school.model.DiaSemana;
import com.school.model.Nivel;
import com.school.model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;

import com.school.model.Matricula;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MatriculaDao extends JpaRepository<Matricula, Long>{

    @Query("FROM Turno")
    public List<Turno> getTurnos();

    @Query("FROM Nivel")
    public List<Nivel> getNiveles();

    @Query("FROM DiaSemana")
    public List<DiaSemana> getDias();

    @Query("FROM Matricula m where m.estudiante.id = ?1")
    public List<Matricula> getMatriculasPorEstudiante(Long id);
}
