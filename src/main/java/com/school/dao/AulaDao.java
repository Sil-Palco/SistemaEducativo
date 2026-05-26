package com.school.dao;

import com.school.model.Clase;
import com.school.model.Curso;
import com.school.model.Estudiante;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.school.model.Aula;

import java.util.List;

public interface AulaDao extends CrudRepository<Aula, Long>{

    @Query("FROM Estudiante e WHERE e.aulaEstudiante.id = ?1")
    public List<Estudiante> findEstudiantesAula(Long id);

    @Query("FROM Clase c WHERE c.aula.id = ?1")
    public List<Clase> findClasesAula(Long id);


    @Query("FROM Aula a where a.gradoAula.id = ?1")
    public List<Aula> findAulaPorGrado(Long id);
}
