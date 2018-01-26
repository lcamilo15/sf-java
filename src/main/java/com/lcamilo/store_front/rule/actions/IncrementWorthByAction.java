package com.lcamilo.store_front.rule.actions;

import com.lcamilo.store_front.model.CalculableItem;
import com.lcamilo.store_front.rule.ItemRule.Then;

/**
 * Created by luiscamilo on 1/26/18
 */
public class IncrementWorthByAction implements Then {
  int incrementWorthBy;
  int minValue;
  int maxValue;
  IncrementWorthByAction(int incrementWorthBy, int minValue, int maxValue) {
    this.incrementWorthBy = incrementWorthBy;
    this.minValue = minValue;
    this.maxValue = maxValue;
  }
  @Override
  public void accept(CalculableItem calculableItem) {
    int itemWorth = calculableItem.getWorth() + incrementWorthBy;
    itemWorth = Math.min(Math.max(itemWorth, minValue), maxValue);
    calculableItem.setWorth(itemWorth);
  }
  public static IncrementWorthByAction incrementBy(int incrementWorthBy, int maxValue) {
    return new IncrementWorthByAction(incrementWorthBy, 0, maxValue);
  }
  public static IncrementWorthByAction incrementBy(int incrementWorthBy,  int minValue,  int maxValue) {
    return new IncrementWorthByAction(incrementWorthBy, minValue, maxValue);
  }
}
