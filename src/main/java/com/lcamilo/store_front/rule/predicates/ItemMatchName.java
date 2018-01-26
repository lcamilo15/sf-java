package com.lcamilo.store_front.rule.predicates;

import com.lcamilo.store_front.model.CalculableItem;
import com.lcamilo.store_front.rule.ItemRule.When;

/**
 * Created by luiscamilo on 1/26/18
 */
public class ItemMatchName implements When {
  String name;
  ItemMatchName(String name) {
    this.name = name;
  }
  @Override
  public boolean test(CalculableItem calculableItem) {
    return calculableItem.getName().equalsIgnoreCase(name);
  }
  public static ItemMatchName itemNameMatches(String name) {
    return new ItemMatchName(name);
  }
}
