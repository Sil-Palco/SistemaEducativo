package com.school.service;

import com.school.dao.RespuestAsignacionDao;
import com.school.model.Asignacion;
import com.school.model.RespuestaAsignacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RespuestaAsignacionServiceImpl implements RespuestaAsignacionService{

    @Autowired
    private RespuestAsignacionDao respuestAsignacionDao;

    @Autowired
    private AsignacionService asignacionService;


    @Override
    public RespuestaAsignacion save(RespuestaAsignacion respuestaAsignacion, Long idAsignacion) {
        Asignacion asignacionEncontrada = asignacionService.getAsignacionoById(idAsignacion).orElseThrow();
        respuestaAsignacion.setAsignacion(asignacionEncontrada);
        return respuestAsignacionDao.save(respuestaAsignacion);
    }

    @Override
    public RespuestaAsignacion update(RespuestaAsignacion respuestaAsignacion) {
        return respuestAsignacionDao.save(respuestaAsignacion);
    }

    @Override
    public Optional<RespuestaAsignacion> findById(Long id) {
        return respuestAsignacionDao.findById(id);
    }

    @Override
    public List<RespuestaAsignacion> findAllByAsignacion(Long idAsignacion) {
        return respuestAsignacionDao.findAllByAsignacion(idAsignacion);
    }

    @Override
    public boolean delete(Long id) {
        return findById(id).map(c -> {
            respuestAsignacionDao.deleteById(id);
            return true;
        }).orElse(false);
    }

    @Override
    public Optional<RespuestaAsignacion> findPorDniEstudianteAndAsignacion(String dniEstudiante, Long idAsignacion) {
        return respuestAsignacionDao.findPorDniEstudianteAndAsignacion(dniEstudiante, idAsignacion);
    }
}
