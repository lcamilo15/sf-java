package com.lcamilo.store_front.rule.actions;

import com.lcamilo.store_front.model.CalculableItem;
import com.lcamilo.store_front.rule.ItemRule.Then;

/**
 * Created by luiscamilo on 1/26/18
 */
public class IncrementShelfLifeAction implements Then {
  int incrementBy;
  IncrementShelfLifeAction(int incrementBy) {
    this.incrementBy = incrementBy;
  }
  @Override
  public void accept(CalculableItem calculableItem) {
    calculableItem.incrementWorthBy(incrementBy);
  }
  public static IncrementShelfLifeAction incrementShelfLifeAction(int incrementBy) {
    return new IncrementShelfLifeAction(incrementBy);
  }
}
