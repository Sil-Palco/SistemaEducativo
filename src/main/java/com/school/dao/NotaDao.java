package com.school.dao;

import com.school.model.Nota;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NotaDao extends CrudRepository<Nota, Long> {

    @Query("select n from Nota n inner join Estudiante e on n.estudiante.id = e.id where n.curso.id = ?1 and e.aulaEstudiante.id = ?2")
    public List<Nota> notasPorAulaYCurso(Long idCurso, Long idAula);

}
