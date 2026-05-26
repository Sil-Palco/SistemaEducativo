package com.school.service;

import com.school.dao.AsignacionDao;
import com.school.dao.ClaseDao;
import com.school.model.Asignacion;
import com.school.model.Clase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AsignacionServiceImpl implements AsignacionService{

    @Autowired
    private AsignacionDao asignacionDao;

    @Autowired
    private ClaseService claseService;

    @Override
    public Asignacion save(Asignacion asignacion, Long idClase) {

        Clase claseEncontrada = claseService.getClaseById(idClase).orElseThrow();
        asignacion.setClase(claseEncontrada);

        return asignacionDao.save(asignacion);
    }

    @Override
    public Asignacion update(Asignacion asignacion) {
        return asignacionDao.save(asignacion);
    }

    @Override
    public Optional<Asignacion> getAsignacionoById(Long id) {
        Asignacion asignacionEncontrada = asignacionDao.findById(id).orElseThrow();
        LocalDateTime fechaAhora = LocalDateTime.now();

        asignacionEncontrada.setActivo(fechaAhora.isBefore(asignacionEncontrada.getFechaFin()) && fechaAhora.isAfter(asignacionEncontrada.getFechaInicio()));
        asignacionEncontrada = asignacionDao.save(asignacionEncontrada);

        return Optional.of(asignacionEncontrada);
    }

    @Override
    public boolean deleteById(Long id) {
        return getAsignacionoById(id).map(c -> {
            asignacionDao.deleteById(id);
            return true;
        }).orElse(false);
    }
}
