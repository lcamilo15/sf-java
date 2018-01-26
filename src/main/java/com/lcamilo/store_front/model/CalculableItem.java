package com.lcamilo.store_front.model;

/**
 * Created by luiscamilo on 1/21/18
 */
public interface CalculableItem {

  String getName();

  void setName(String name);

  int getShelfLife();

  void setShelfLife(int shelfLife);

  int getWorth();

  void setWorth(int worth);

  default void incrementShelfLifeBy(int shellfLife) {
    this.setShelfLife(this.getShelfLife() + shellfLife);
  }

  default void incrementWorthBy(int worth) {
    this.setWorth(this.getWorth() + worth);
  }

  default boolean shelfLifeHasPassed() {
    return this.getShelfLife() <= 0;
  }

  default boolean shelfLifeBetween(int from, int to) {
    return this.getShelfLife() >= from && this.getShelfLife() <= to;
  }
}
