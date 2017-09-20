package com.fandango.test.repository;

import com.fandango.test.model.CarItem;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ArticleRepository extends PagingAndSortingRepository<CarItem, Integer> {

}
