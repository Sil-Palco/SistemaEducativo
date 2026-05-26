package com.school.service;

import com.school.model.Especialidad;

import java.util.List;
import java.util.Optional;

public interface EspecialidadService {

    public Especialidad save(Especialidad especialidad);

    public Optional<Especialidad> findById(Long id);

    public List<Especialidad> findAll();

    public Boolean deleteById(Long id);
}
