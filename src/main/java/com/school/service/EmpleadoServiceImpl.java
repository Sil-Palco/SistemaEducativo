package com.school.service;

import com.school.dao.EmpleadoDao;
import com.school.model.Clase;
import com.school.model.Empleado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoServiceImpl implements EmpleadoService{

    @Autowired
    private EmpleadoDao empleadoDao;

    @Override
    public Empleado save(Empleado empleado) {
        return empleadoDao.save(empleado);
    }

    @Override
    public Optional<Empleado> getEmpleadoById(Long id) {
        return empleadoDao.findById(id);
    }

    @Override
    public List<Empleado> findAll() {
        return empleadoDao.findAll();
    }

    @Override
    public Page<Empleado> findAll(Pageable pageable) {
        return empleadoDao.findAll(pageable);
    }

    @Override
    public boolean delete(Long id) {
        return getEmpleadoById(id).map(e -> {
            empleadoDao.deleteById(id);
            return true;
        }).orElse(false);
    }

    @Override
    public List<Clase> findClasesProfesor(Long id) {
        return empleadoDao.findClasesProfesor(id);
    }

    @Override
    public Empleado findByDni(String dni) {
        return empleadoDao.findByDni(dni);
    }
}
