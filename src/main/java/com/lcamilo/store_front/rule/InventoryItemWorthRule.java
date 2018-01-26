package com.lcamilo.store_front.rule;

import com.lcamilo.store_front.model.CalculableItem;
import com.lcamilo.store_front.rule.ItemRule.Then;
import com.lcamilo.store_front.rule.ItemRule.When;

/**
 * Created by luiscamilo on 1/21/18
 * Rule should return a new Item,
 * it should not update the item itself.
 */
public interface InventoryItemWorthRule {
  When when(CalculableItem item);
  Then then(CalculableItem item);
}
