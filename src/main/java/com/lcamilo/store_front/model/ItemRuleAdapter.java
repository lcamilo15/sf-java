package com.lcamilo.store_front.model;

import com.lcamilo.other_team.model.Item;


public class ItemRuleAdapter<T extends Item> implements CalculableItem  {

  final T item;

  public ItemRuleAdapter(T t) {
    this.item = t;
  }

  public ItemRuleAdapter() {
    this((T) new Item());
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
  final public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ItemRuleAdapter)) {
      return false;
    }

    ItemRuleAdapter itemWrapper = (ItemRuleAdapter) o;

    if (itemWrapper.item == null && item == null) {
      return true;
    }

    if (itemWrapper.item != null && item == null || itemWrapper.item == null) {
      return false;
    }

    if (getShelfLife() != itemWrapper.getShelfLife()) {
      return false;
    }

    if (itemWrapper.item != item) {
      return false;
    }

    if (getWorth() != itemWrapper.getWorth()) {
      return false;
    }

    return getName() != null ? getName().equals(itemWrapper.getName()) : itemWrapper.getName() == null;
  }

  @Override
  final public int hashCode() {
    int result = 0;
    if (item != null) {
      result = getName() != null ? getName().hashCode() : 0;
      result = 31 * result + getShelfLife();
      result = 31 * result + getWorth();
    }
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
