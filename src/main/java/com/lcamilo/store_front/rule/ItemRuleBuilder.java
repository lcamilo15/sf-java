package com.lcamilo.store_front.rule;

import com.lcamilo.store_front.rule.ItemRule.Then;
import com.lcamilo.store_front.rule.ItemRule.When;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by luiscamilo on 1/26/18
 */
public class ItemRuleBuilder  {
  private Set<ItemRule> itemRules = new LinkedHashSet<>();
  public static ItemRuleBuilder newBuilder() {
    return new ItemRuleBuilder();
  }
  public ItemRuleBuilder addRule(
      When when,
      Then then
  ) {
    itemRules.add(ItemRule.create(when, then));
    return this;
  }
}
