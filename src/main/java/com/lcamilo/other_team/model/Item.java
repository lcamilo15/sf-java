package com.lcamilo.other_team.model;

/**
 * Created by luiscamilo on 1/21/18
 * NOTE: You may feel tempted to change the casing of the property on this class,
 * but please don't change it as this code is was created and maintained by X
 * team.
 */
public class Item {
  /**
   * Name of course
   */
  public String Name;
  /**
   * Value which denotes the number of days we have to sell the item
   */
  public int ShelfLife;
  /**
   * How valuable the item is
   */
  public int Worth;

  public String getName() {
    return Name;
  }

  public void setName(String name) {
    Name = name;
  }

  public int getShelfLife() {
    return ShelfLife;
  }

  public void setShelfLife(int shelfLife) {
    ShelfLife = shelfLife;
  }

  public int getWorth() {
    return Worth;
  }

  public void setWorth(int worth) {
    Worth = worth;
  }

}
