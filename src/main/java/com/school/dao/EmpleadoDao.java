package com.school.dao;

import com.school.model.Clase;
import com.school.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmpleadoDao extends JpaRepository<Empleado, Long> {

    @Query("FROM Clase c WHERE c.empleado.id = ?1")
    public List<Clase> findClasesProfesor(Long id);

    public Empleado findByDni(String dni);
}
