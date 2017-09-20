package com.fandango.test.restcontroller;

import com.fandango.test.controller.ArticlesController;
import com.fandango.test.controller.CartController;
import com.fandango.test.model.Car;
import com.fandango.test.model.CarItem;
import com.fandango.test.repository.ArticleRepository;
import com.fandango.test.repository.CarRepository;
import java.util.List;
import java.util.Optional;
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
public class CartRestController {

  private Logger log = LoggerFactory.getLogger(CartRestController.class);

  @Autowired
  private CarRepository carRepository;

  @Autowired
  private ArticleRepository carItemRepository;

  @Autowired
  private CartController cartController;

  @Autowired
  private ArticlesController articlesController;

  @RequestMapping(value = "/cart/{id}", method = RequestMethod.GET, produces = "application/json")
  public ResponseEntity getCartById(@PathVariable Integer id) {
    log.debug(String.format("Getting the car with ID %s", id));
    Optional<Car> car = cartController.getCarById(id);
    log.debug("Found the car "+car);
    if(car.isPresent()){
      log.debug("1.Cart items : "+car.get().getCarItemList());
      return new ResponseEntity(car.get(), HttpStatus.OK);
    }else{
      return new ResponseEntity( null, HttpStatus.NOT_FOUND);
    }
  }

  @RequestMapping(value = "/cart", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
  public ResponseEntity createCart(@RequestBody Car car) {
    log.debug(String.format("Creating the car with id %d", car.getId()));
    try{
      cartController.createCart(car);
      return ResponseEntity.ok(car);
    }catch(Exception e){
      log.error("Exception on cart creation", e);
      return new ResponseEntity("Could not create the cart", HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping(value = "/cart/{id}", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
  public ResponseEntity addItemToCart(@RequestBody CarItem carItem, @PathVariable Integer id) {
    log.debug(String.format("Adding the article %d to the car %d",carItem.getId(),id));
    Optional<Car> car = cartController.getCarById(id);

    if(car.isPresent()){
      log.debug("Found car "+car);
      CarItem relatedObject = carItemRepository.findOne(carItem.getId());
      log.debug(String.format("Found the item with id: %d",relatedObject.getId()));

      car.get().getCarItemList().add(relatedObject);
      relatedObject.setCar(car.get());

      carRepository.save(car.get());

      return ResponseEntity.ok(car.get());
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
    Optional<Car> car = cartController.getCarById(id);
    if(car.isPresent()){
      List<CarItem> items = car.get().getCarItemList();
      items.stream().forEach( i -> i.setCar(null));
      car.get().setCarItemList(null);
      carRepository.save(car.get());
      return ResponseEntity.ok(car.get());
    }else{
      return new ResponseEntity("Cart not found", HttpStatus.NOT_FOUND);
    }

  }

  @RequestMapping(value = "/cart/payment/{id}" , method = RequestMethod.GET, produces = "application/json")
  public ResponseEntity getPayment(@PathVariable Integer id){
    log.debug("Removing the article %d from cart %d",id);
    return ResponseEntity.ok(cartController.getCartPayment(id));
  }

}
