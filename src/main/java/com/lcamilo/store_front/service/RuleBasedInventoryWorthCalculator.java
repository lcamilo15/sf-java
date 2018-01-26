package com.lcamilo.store_front.service;

import static com.lcamilo.store_front.rule.predicates.ItemMatchName.itemNameMatches;
import static com.lcamilo.store_front.rule.predicates.ItemShelfConstraint.shelfDateBetween;
import static com.lcamilo.store_front.rule.predicates.ItemShelfConstraint.shelfDateLessThanOrEqualsTo;

import com.lcamilo.store_front.model.CalculableItem;
import com.lcamilo.store_front.rule.ItemRule;
import com.lcamilo.store_front.rule.ItemRule.Then;
import com.lcamilo.store_front.rule.ItemRule.When;
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

  When ALWAYS = (item)-> true;

  When SHELF_DATE_HAS_PASSED = shelfDateLessThanOrEqualsTo(0);

  Then DECREASE_SHELF_VALUE_BY_ONE = (item)-> item.setShelfLife(item.getShelfLife() - 1);

  When SHELF_DATE_HAS_NOT_PASSED = SHELF_DATE_HAS_PASSED.negate();

  When SHELF_DATE_BETWEEN_5_AND_10 = shelfDateBetween(5, 10);

  When SHELF_DATE_BETWEEN_1_AND_5 = shelfDateBetween(1, 5);

  Then DECREASE_WORTH_VALUE_BY_TWO = (item)-> item.setWorth(Math.max(item.getWorth() - 2, 0));

  Then DECREASE_WORTH_VALUE_BY_ONE = (item)-> item.setWorth(Math.max(item.getWorth() - 1, 0));

  Then INCREASE_WORTH_VALUE_BY_ONE = (item)-> item.setWorth(Math.max(item.getWorth() + 1, 0));

  Then INCREASE_WORTH_VALUE_BY_TWO = (item)-> item.setWorth(Math.max(item.getWorth() + 2, 0));

  Then INCREASE_WORTH_VALUE_BY_THREE = (item)-> item.setWorth(Math.max(item.getWorth() + 3, 0));

  Then INCREASE_WORTH_VALUE_BY_FOUR = (item)-> item.setWorth(Math.max(item.getWorth() + 3, 0));

  Then WORTH_VALUE_IS_NEVER_MORE_THAN_50 = (item)-> item.setWorth(Math.min(item.getWorth(), 50));

  Then WORTH_IS_ALWAYS_80 = (item)-> item.setWorth(80);

  Then WORTH_DROPS_TO_0 = (item)-> item.setWorth(0);

  public RuleBasedInventoryWorthCalculator() {

    // Decrease Shelf Value
    itemRules.add(ItemRule.create(
        IS_REGULAR_ITEM.and(SHELF_DATE_HAS_NOT_PASSED),
        DECREASE_WORTH_VALUE_BY_ONE
    ));

    // Once the shelf life date has passed, Worth degrades twice as fast
    itemRules.add(ItemRule.create(
        IS_REGULAR_ITEM.and(SHELF_DATE_HAS_PASSED),
        DECREASE_WORTH_VALUE_BY_TWO
    ));

    /**
     * Alchemy
     */
    // "Alchemy" items degrade in Worth twice as fast as normal items
    //For SHELF_DATE_HAS_PASSED then increase by 4
    itemRules.add(ItemRule.create(
        IS_ALCHEMY.and(SHELF_DATE_HAS_PASSED),
        INCREASE_WORTH_VALUE_BY_FOUR
    ));

    itemRules.add(ItemRule.create(
        IS_ALCHEMY.and(SHELF_DATE_HAS_NOT_PASSED),
        INCREASE_WORTH_VALUE_BY_TWO
    ));

    /**
     * GOLD
     */
    // GOLD increases the older it gets
    itemRules.add(ItemRule.create(
        IS_GOLD.and(SHELF_DATE_HAS_NOT_PASSED),
        INCREASE_WORTH_VALUE_BY_ONE
    ));

    // Gold increases value twice as fast once the shelf life date has passed
    itemRules.add(ItemRule.create(
        IS_GOLD.and(SHELF_DATE_HAS_PASSED),
        INCREASE_WORTH_VALUE_BY_TWO
    ));

    /**
     * Helium
     */
    //"Helium", like gold, increases in Worth as it's ShelfLife value changes;
    itemRules.add(ItemRule.create(
        IS_HELIUM.and(SHELF_DATE_BETWEEN_5_AND_10.negate()).and(SHELF_DATE_BETWEEN_1_AND_5.negate()),
        INCREASE_WORTH_VALUE_BY_THREE
    ));


    //Increases By 2 when there are 10 days or less
    itemRules.add(ItemRule.create(
        IS_HELIUM.and(SHELF_DATE_BETWEEN_5_AND_10),
        INCREASE_WORTH_VALUE_BY_TWO
    ));

    //Increases By 3 when there are 5  days or less
    itemRules.add(ItemRule.create(
        IS_HELIUM.and(SHELF_DATE_BETWEEN_1_AND_5),
        INCREASE_WORTH_VALUE_BY_THREE
    ));

    //Worth drops to 0 once the ShelfLife is passed
    itemRules.add(ItemRule.create(
        IS_HELIUM.and(SHELF_DATE_HAS_PASSED),
        WORTH_DROPS_TO_0
    ));


    //The Worth of an item is never more than 50
    itemRules.add(ItemRule.create(
        IS_CADMIUM.negate(),
        WORTH_VALUE_IS_NEVER_MORE_THAN_50
    ));

    /**
     * CADIUM
     */
    //"Cadmium" is rare, has a worth of 80, and will never decrease in Worth
    itemRules.add(ItemRule.create(
        IS_CADMIUM,
        WORTH_IS_ALWAYS_80
    ));

    itemRules.add(ItemRule.create(
        ALWAYS,
        DECREASE_SHELF_VALUE_BY_ONE
    ));

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
