package com.fandango.test.controller;

import com.fandango.test.model.CarItem;
import com.fandango.test.repository.ArticleRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ArticlesController {

  private Logger log = LoggerFactory.getLogger(ArticlesController.class);


  private ArticleRepository carItemRepository;

  public ArticlesController(ArticleRepository carItemRepository){
    this.carItemRepository = carItemRepository;
  }

  public List<CarItem> getAllItems(Pageable pageable){
    log.debug("Getting all the items ");
    return carItemRepository.findAll(pageable).getContent();
  }

  public Optional<CarItem> findCarItemById(Integer id){
    log.debug(String.format("Getting the item with id %d",id));
    return Optional.ofNullable(carItemRepository.findOne(id));
  }

}
