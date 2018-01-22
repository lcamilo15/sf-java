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
}
