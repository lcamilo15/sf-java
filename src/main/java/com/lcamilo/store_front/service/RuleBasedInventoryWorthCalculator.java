package com.lcamilo.store_front.service;

import static com.lcamilo.store_front.rule.ItemRuleBuilder.then;
import static com.lcamilo.store_front.rule.ItemRuleBuilder.when;
import static com.lcamilo.store_front.rule.actions.IncrementShelfLifeAction.incrementShelfLifeBy;
import static com.lcamilo.store_front.rule.actions.IncrementWorthByAction.incrementBy;
import static com.lcamilo.store_front.rule.predicates.ItemMatchName.itemNameMatches;
import static com.lcamilo.store_front.rule.predicates.ItemShelfConstraint.shelfDateBetween;
import static com.lcamilo.store_front.rule.predicates.ItemShelfConstraint.shelfDateLessThanOrEqualsTo;

import com.lcamilo.store_front.model.CalculableItem;
import com.lcamilo.store_front.rule.ItemRule;
import com.lcamilo.store_front.rule.ItemRule.When;
import com.lcamilo.store_front.rule.ItemRuleBuilder;
import com.lcamilo.store_front.util.InventoryBuilder;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by luiscamilo on 1/21/18
 */
public class RuleBasedInventoryWorthCalculator implements InventoryWorthCalculator {
  Set<ItemRule> itemRules = new LinkedHashSet<>();
  When IS_GOLD = itemNameMatches("GOLD");
  When IS_CADMIUM = itemNameMatches("CADMIUM");
  When IS_HELIUM = itemNameMatches("HELIUM");
  When IS_ALCHEMY = itemNameMatches("ALCHEMY IRON");
  When IS_SPECIAL_ITEM = IS_ALCHEMY.or(IS_HELIUM).or(IS_CADMIUM).or(IS_GOLD);
  When IS_REGULAR_ITEM = IS_SPECIAL_ITEM.negate();

  public RuleBasedInventoryWorthCalculator() {

    When SHELF_DATE_HAS_PASSED = shelfDateLessThanOrEqualsTo(0);
    When SHELF_DATE_HAS_NOT_PASSED = SHELF_DATE_HAS_PASSED.negate();

    int incrementValueBy = -1;
    int defaultMaxWorthValue = 50;

    itemRules = ItemRuleBuilder.newBuilder()
        .addRule(
            when(IS_REGULAR_ITEM.and(SHELF_DATE_HAS_NOT_PASSED)),
            then(incrementBy(incrementValueBy, defaultMaxWorthValue))
        )
        .addRule(
            when(IS_REGULAR_ITEM.and(SHELF_DATE_HAS_PASSED)),
            then(incrementBy(incrementValueBy * 2, defaultMaxWorthValue))
        )
        .addRule(
            when(IS_GOLD.and(SHELF_DATE_HAS_NOT_PASSED)),
            then(incrementBy(Math.abs(incrementValueBy), defaultMaxWorthValue))
        )
        .addRule(
            when(IS_GOLD.and(SHELF_DATE_HAS_PASSED)),
            then(incrementBy(Math.abs(incrementValueBy) * 2, defaultMaxWorthValue))
        )
        .addRule(
            when(IS_CADMIUM),
            then(calculableItem -> calculableItem.setWorth(80))
        )
        .addRule(
            when(IS_ALCHEMY.and(SHELF_DATE_HAS_NOT_PASSED)),
            then(incrementBy(incrementValueBy * 2, defaultMaxWorthValue))
        )
        .addRule(
            when(IS_ALCHEMY.and(SHELF_DATE_HAS_PASSED)),
            then(incrementBy(incrementValueBy * 4, defaultMaxWorthValue))
        )
        .addRule(
            when(IS_HELIUM.and(SHELF_DATE_HAS_NOT_PASSED)),
            then(incrementBy(Math.abs(incrementValueBy), defaultMaxWorthValue))
        )
        .addRule(
            when(IS_HELIUM.and(shelfDateBetween(5, 10))),
            then(incrementBy(Math.abs(incrementValueBy) * 2, defaultMaxWorthValue))
        )
        .addRule(
            when(IS_HELIUM.and(shelfDateLessThanOrEqualsTo(4))),
            then(incrementBy(Math.abs(incrementValueBy) * 3, defaultMaxWorthValue))
        )
        .addRule(
            when(IS_HELIUM.and(SHELF_DATE_HAS_PASSED)),
            then(item -> item.setWorth(0))
        ).addRule(
            when(item->true),
            then(incrementShelfLifeBy(-1))
        ).build()
    ;
  }

  @Override
  public List<CalculableItem> updateInventoryWorth(List<CalculableItem> items) {
    return InventoryBuilder.from(items).build().stream()
        .peek(this::updateWorth).collect(Collectors.toList());
  }

  @Override
  public CalculableItem updateWorth(CalculableItem item) {
    itemRules.forEach(itemRule-> {
      if (itemRule.test(item)) {
        itemRule.execute(item);
      }
    });
    return item;
  }
}
