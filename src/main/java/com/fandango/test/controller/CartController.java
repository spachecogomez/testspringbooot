package com.fandango.test.controller;


import com.fandango.test.model.Car;
import com.fandango.test.model.CarItem;
import com.fandango.test.repository.ArticleRepository;
import com.fandango.test.repository.CarRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CartController {

  private Logger log = LoggerFactory.getLogger(CartController.class);

  @Autowired
  private CarRepository carRepository;

  @Autowired
  private ArticleRepository carItemRepository;

  public Optional<Car> getCarById(Integer id ){
    return Optional.ofNullable(carRepository.findOne(id));
  }

  public List<Car> getAllCartsByPage(Pageable pageable){
    return carRepository.findAll(pageable).getContent();
  }

  public void addItemToCart(CarItem carItem, Integer id) throws Exception{
    Car car = carRepository.findOne(id);
    CarItem relatedObject = carItemRepository.findOne(carItem.getId());
    log.debug(String.format("Found the item with id: %d",relatedObject.getId()));

    car.getCarItemList().add(relatedObject);
    relatedObject.setCar(car);

    carRepository.save(car);
  }

  public void createCart(Car car) throws Exception{
    carRepository.save(car);
  }

  public void clearCart(Integer id){
    Car car = carRepository.findOne(id);
    List<CarItem> items = car.getCarItemList();
    items.stream().forEach( i -> i.setCar(null));
    car.setCarItemList(null);
    carRepository.save(car);
  }

  public Integer getCartPayment(Integer id){
    Car car = carRepository.findOne(id);
    return car.getCarItemList().stream().mapToInt( it -> it.getPrice()).sum();
  }
}
