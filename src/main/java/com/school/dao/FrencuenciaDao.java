package com.school.dao;

import com.school.model.Frecuencia;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FrencuenciaDao extends CrudRepository<Frecuencia, Long> {

    @Query("FROM Frecuencia f WHERE f.clase = null")
    public List<Frecuencia> findAllFrecuenciaNulos();

}
