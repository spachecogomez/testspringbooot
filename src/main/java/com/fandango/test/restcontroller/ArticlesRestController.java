package com.fandango.test.restcontroller;


import com.fandango.test.controller.ArticlesController;
import com.fandango.test.model.CarItem;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticlesRestController {

  private Logger log = LoggerFactory.getLogger(ArticlesRestController.class);

  @Autowired
  private ArticlesController articlesController;

  @RequestMapping(value="/articles", method = RequestMethod.GET, produces = "application/json")
  public ResponseEntity<List<CarItem>> getCarArticles(@RequestParam("start") int start, @RequestParam("size") int size){
    log.debug("getting all the articles in a cart");
    List<CarItem> results = articlesController.getAllItems(new PageRequest(start,size));
    log.debug(String.format("found %d cartItems",results.size()));
    if(results.isEmpty()){
      return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    }else{
      return new ResponseEntity(results, HttpStatus.OK);
    }
  }


  @RequestMapping( value = "/articles/{id}",method = RequestMethod.GET, produces = "application/json")
  public ResponseEntity<CarItem> getArticleById(@PathVariable Integer id){
    log.debug("getting all the articles in a cart");
    Optional<CarItem> carItemOptional = articlesController.findCarItemById(id);
    if(carItemOptional.isPresent()){
      return new ResponseEntity<CarItem>(carItemOptional.get(), HttpStatus.OK);
    }else{
      return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }
  }

}
