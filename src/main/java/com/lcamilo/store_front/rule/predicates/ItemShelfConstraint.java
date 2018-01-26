package com.lcamilo.store_front.rule.predicates;

import com.lcamilo.store_front.model.CalculableItem;
import com.lcamilo.store_front.rule.ItemRule.When;

/**
 * Created by luiscamilo on 1/26/18
 */
public class ItemShelfConstraint implements When {
  When shelfConstraint;
  ItemShelfConstraint(When shelfConstraint) {
    this.shelfConstraint = shelfConstraint;
  }
  @Override
  public boolean test(CalculableItem calculableItem) {
    return shelfConstraint.test(calculableItem);
  }
  public static ItemShelfConstraint shelfDateBetween(int from, int to) {
    return new ItemShelfConstraint((calculableItem)->{
      int shelfLife = calculableItem.getShelfLife();
      return shelfLife >= from && shelfLife <= to;
    });
  }
  public static ItemShelfConstraint shelfDateGreaterThanOrEqualsTo(int gt) {
    return new ItemShelfConstraint((calculableItem)->{
      int shelfLife = calculableItem.getShelfLife();
      return shelfLife >= gt;
    });
  }
  public static ItemShelfConstraint shelfDateLessThanOrEqualsTo(int lte) {
    return new ItemShelfConstraint((calculableItem)->{
      int shelfLife = calculableItem.getShelfLife();
      return shelfLife <= lte;
    });
  }
}
