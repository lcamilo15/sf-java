package com.lcamilo.store_front.model;

/**
 * Created by luiscamilo on 1/21/18
 */
public interface CalculableItem {
  public String getName();

  public void setName(String name);

  public int getShelfLife();

  public void setShelfLife(int shelfLife);

  public int getWorth();

  public void setWorth(int worth);

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
