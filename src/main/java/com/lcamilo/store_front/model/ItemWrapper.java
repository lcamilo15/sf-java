package com.lcamilo.store_front.model;

import com.lcamilo.other_team.model.Item;


public class ItemWrapper<T extends Item> implements CalculableItem  {

  T item;

  public ItemWrapper(T t) {
    this.item = t;
  }

  public ItemWrapper() {
    this.item = (T) new Item();
  }

  public String getName() {
    return this.item.getName();
  }

  public void setName(String name) {
    this.item.setName(name);
  }

  public int getShelfLife() {
    return this.item.getShelfLife();
  }

  public void setShelfLife(int shelfLife) {
    this.item.setShelfLife(shelfLife);
  }

  public int getWorth() {
    return this.item.getWorth();
  }

  public void setWorth(int worth) {
    this.item.setWorth(worth);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ItemWrapper)) {
      return false;
    }

    ItemWrapper itemWrapper = (ItemWrapper) o;

    if (getShelfLife() != itemWrapper.getShelfLife()) {
      return false;
    }

    if (getWorth() != itemWrapper.getWorth()) {
      return false;
    }

    return getName() != null ? getName().equals(itemWrapper.getName()) : itemWrapper.getName() == null;
  }

  @Override
  public int hashCode() {
    int result = getName() != null ? getName().hashCode() : 0;
    result = 31 * result + getShelfLife();
    result = 31 * result + getWorth();
    return result;
  }

  @Override
  public String toString() {
    return "Item{" +
        "Name='" + item.getName() + '\'' +
        ", ShelfLife=" + item.getShelfLife() +
        ", Worth=" + item.getWorth() +
        '}';
  }

}
