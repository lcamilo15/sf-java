package com.lcamilo.store_front.service;

import com.lcamilo.store_front.model.ItemRuleAdapter;

/**
 * Created by luiscamilo on 1/21/18
 * Rule should return a new Item,
 * it should not update the item itself.
 */
public interface InventoryItemWorthRule {
  public boolean matches(ItemRuleAdapter item);
  public ItemRuleAdapter execute(ItemRuleAdapter item);
}
