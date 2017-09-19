package com.fandango.test.controller;


import com.fandango.test.model.CarItem;
import com.fandango.test.repository.CarItemRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticlesController {

  private Logger log = LoggerFactory.getLogger(ArticlesController.class);

  @Autowired
  private CarItemRepository carItemRepository;

  @RequestMapping(value="/articles", method = RequestMethod.GET, produces = "application/json")
  public ResponseEntity<List<CarItem>> getCarArticles(){
    log.debug("getting all the articles in a cart");
    List<CarItem> results = (List<CarItem>) carItemRepository.findAll();
    log.debug(String.format("found %d cartItems",results.size()));
    return new ResponseEntity(null, HttpStatus.NOT_FOUND);
  }


  @RequestMapping( value = "/articles/{id}",method = RequestMethod.GET, produces = "application/json")
  public ResponseEntity<CarItem> getArticleById(@PathVariable String articleId){
    log.debug("getting all the articles in a cart");
    return new ResponseEntity(null, HttpStatus.NO_CONTENT);
  }

}
