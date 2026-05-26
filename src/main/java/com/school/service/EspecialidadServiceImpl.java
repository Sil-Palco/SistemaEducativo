package com.school.service;

import com.school.dao.EspecialidadDao;
import com.school.model.Especialidad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EspecialidadServiceImpl implements EspecialidadService{

    @Autowired
    private EspecialidadDao especialidadDao;

    @Override
    public Especialidad save(Especialidad especialidad) {
        return especialidadDao.save(especialidad);
    }

    @Override
    public Optional<Especialidad> findById(Long id) {
        return especialidadDao.findById(id);
    }

    @Override
    public List<Especialidad> findAll() {
        return (List<Especialidad>) especialidadDao.findAll();
    }

    @Override
    public Boolean deleteById(Long id) {
        return findById(id).map(e -> {
            especialidadDao.deleteById(id);
            return true;
        }).orElse(false);
    }
}
