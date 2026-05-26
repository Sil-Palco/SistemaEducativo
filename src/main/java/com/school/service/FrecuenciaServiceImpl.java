package com.school.service;

import com.school.dao.FrencuenciaDao;
import com.school.model.Frecuencia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FrecuenciaServiceImpl implements FrecuenciaService{

    @Autowired
    private FrencuenciaDao frencuenciaDao;

    @Override
    public void deleFrecuenciasNulas() {
        List<Frecuencia> frecuenciasOrfans = frencuenciaDao.findAllFrecuenciaNulos();
        frecuenciasOrfans.forEach(f -> delete(f.getId()));
    }

    @Override
    public void delete(Long id) {
        frencuenciaDao.deleteById(id);
    }
}
