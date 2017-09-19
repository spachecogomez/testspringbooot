package com.fandango.test.repository;

import com.fandango.test.model.Car;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends CrudRepository<Car,Integer>{

  @Query("SELECT car FROM Car AS car INNER JOIN car.carItemList WHERE car.id = ?1")
  public Car findByIdWithCarItems(Integer id);

}
