package com.fandango.test.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import com.fandango.test.model.CarItem;
import com.fandango.test.repository.ArticleRepository;
import java.util.Arrays;
import java.util.Optional;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RunWith(JUnit4.class)
public class ArticleControllerTest {

  private static final Integer ARTICLE_ID = Integer.MAX_VALUE;
  private Logger log = LoggerFactory.getLogger(ArticleControllerTest.class);
  @Mock
  private CarItem carItem;
  @Mock
  private ArticleRepository articleRepository;
  private ArticlesController articlesController;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    articlesController = new ArticlesController(articleRepository);
  }

  @Test
  public void testFindExistingArticle() {
    log.info("article repository:" + articleRepository);
    when(articleRepository.findOne(ARTICLE_ID)).then(result -> carItem);
    Optional<CarItem> carItemOptional = articlesController.findCarItemById(ARTICLE_ID);
    assertThat(carItemOptional.isPresent(), equalTo(true));
    assertThat(carItemOptional.get(), equalTo(carItem));
  }

  @Test
  public void testFindAllArticles() {
    Pageable pageable = new PageRequest(0, 4);
    when(articleRepository.findAll(pageable))
        .then(result -> new PageImpl(Arrays.asList(carItem, carItem, carItem, carItem)));
    List<CarItem> queryResult = articlesController.getAllItems(pageable);
    assertThat(queryResult.size(), equalTo(4));
    assertThat(queryResult.get(0) , equalTo(carItem));
  }

  @Test(expected = Exception.class)
  public void testFindArticleException(){
    when(articleRepository.findOne(ARTICLE_ID)).thenThrow( new Exception());
    articlesController.findCarItemById(ARTICLE_ID);
  }


}
