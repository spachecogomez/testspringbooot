package com.fandango.test.controller;

import com.fandango.test.model.Car;
import com.fandango.test.model.CarItem;
import com.fandango.test.repository.CarItemRepository;
import com.fandango.test.repository.CarRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {

  private Logger log = LoggerFactory.getLogger(CartController.class);

  @Autowired
  private CarRepository carRepository;

  @Autowired
  private CarItemRepository carItemRepository;

  @RequestMapping(value = "/cart/{id}", method = RequestMethod.GET, produces = "application/json")
  public ResponseEntity getCartById(@PathVariable Integer id) {
    log.debug(String.format("Getting the car with ID %s", id));
    Car car = carRepository.findOne(id);
    log.debug("Found the car "+car);
    if(car != null){
      log.debug("1.Cart items : "+car.getCarItemList());
      return new ResponseEntity(car, HttpStatus.OK);
    }else{
      return new ResponseEntity( null, HttpStatus.NO_CONTENT);
    }
  }

  @RequestMapping(value = "/cart", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
  public ResponseEntity createCart(@RequestBody Car car) {
    log.debug(String.format("Creating the car with id %d", car.getId()));
    carRepository.save(car);
    return ResponseEntity.ok(car);
  }

  @RequestMapping(value = "/cart/{id}", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
  public ResponseEntity addItemToCart(@RequestBody CarItem carItem, @PathVariable Integer id) {
    log.debug(String.format("Adding the article %d to the car %d",carItem.getId(),id));
    Car car = carRepository.findOne(id);
    log.debug("Found car "+car);
    if(car != null){
      CarItem relatedObject = carItemRepository.findOne(carItem.getId());
      log.debug(String.format("Found the item with id: %d",relatedObject.getId()));

      car.getCarItemList().add(relatedObject);
      relatedObject.setCar(car);

      carRepository.save(car);

      return ResponseEntity.ok(car);
    }
    return new ResponseEntity(null, HttpStatus.NO_CONTENT);
  }

  @RequestMapping(value = "/cart/{id}" , method = RequestMethod.DELETE, consumes = "application/json" , produces = "application/json")
  public ResponseEntity removeItemFromCart(@RequestBody CarItem carItem, @PathVariable Integer id){
    log.debug("Removing the article %d from cart %d", carItem.getId(), id);
    CarItem itemToRemove = carItemRepository.findOne(carItem.getId());
    Car car = carRepository.findOne(id);
    car.getCarItemList().remove(itemToRemove);
    itemToRemove.setCar(null);
    carRepository.save(car);
    return ResponseEntity.ok(car);
  }

  @RequestMapping(value = "/cart/clean/{id}" , method = RequestMethod.DELETE, produces = "application/json")
  public ResponseEntity removeItemsFromCart(@PathVariable Integer id){
    log.debug("Removing the article %d from cart %d",id);
    Car car = carRepository.findOne(id);
    List<CarItem> items = car.getCarItemList();
    items.stream().forEach( i -> i.setCar(null));
    car.setCarItemList(null);
    carRepository.save(car);
    return ResponseEntity.ok(car);
  }

  @RequestMapping(value = "/cart/payment/{id}" , method = RequestMethod.GET, produces = "application/json")
  public ResponseEntity getPayment(@PathVariable Integer id){
    log.debug("Removing the article %d from cart %d",id);
    Car car = carRepository.findOne(id);
    Integer amountToPay = car.getCarItemList().stream().mapToInt( it -> it.getPrice()).sum();
    return ResponseEntity.ok(amountToPay);
  }

}
