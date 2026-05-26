package com.school.service;

import com.school.model.Asignacion;

import java.util.List;
import java.util.Optional;

public interface AsignacionService {

    public Asignacion save(Asignacion asignacion, Long idClase);

    public Asignacion update(Asignacion asignacion);

    public Optional<Asignacion> getAsignacionoById(Long id);

    public boolean deleteById(Long id);
}
