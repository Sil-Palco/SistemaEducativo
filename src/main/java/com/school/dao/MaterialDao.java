package com.school.dao;

import com.school.model.Material;
import org.springframework.data.repository.CrudRepository;

public interface MaterialDao extends CrudRepository<Material, Long> {
}
