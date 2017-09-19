package com.fandango.test.model;


import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity
public class Car {
  
  @Id
  private Integer id;

  @Column
  private String name;

  @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
  private List<CarItem> carItemList;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public List<CarItem> getCarItemList() {
    return carItemList;
  }

  public void setCarItemList(List<CarItem> carItemList) {
    this.carItemList = carItemList;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
