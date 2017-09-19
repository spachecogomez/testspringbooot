package com.fandango.test.repository;

import com.fandango.test.model.CarItem;
import org.springframework.data.repository.CrudRepository;

public interface CarItemRepository extends CrudRepository<CarItem, Integer>{

}
