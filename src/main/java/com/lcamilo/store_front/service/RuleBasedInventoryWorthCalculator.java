package com.lcamilo.store_front.service;

import com.lcamilo.store_front.model.CalculableItem;
import com.lcamilo.store_front.rule.ItemRule;
import com.lcamilo.store_front.rule.ItemRule.When;
import com.lcamilo.store_front.util.InventoryBuilder;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by luiscamilo on 1/21/18
 */
public class RuleBasedInventoryWorthCalculator implements InventoryWorthCalculator   {

  Set<ItemRule> itemRules = new HashSet<>();

  public RuleBasedInventoryWorthCalculator() {
    When IS_GOLD = (conditionalItem)->conditionalItem.getName().equalsIgnoreCase("GOLD");
    When IS_CADMIUM = (conditionalItem)->conditionalItem.getName().equalsIgnoreCase("CADMIUM");
    When IS_HELIUM = (conditionalItem)->conditionalItem.getName().equalsIgnoreCase("HELIUM");
    When IS_ALCHEMY = (conditionalItem)->conditionalItem.getName().equalsIgnoreCase("ALCHEMY");

    When IS_SPECIAL_ITEM = IS_ALCHEMY.or(IS_HELIUM).or(IS_CADMIUM).or(IS_GOLD);
    When NON_SPECIAL_ITEM = IS_SPECIAL_ITEM.negate();

    //EACH day our system lowers both values for every item
    ItemRule WORTH_DECREASE_RULE = ItemRule.create(
        NON_SPECIAL_ITEM.and(item -> item.getWorth() > 0),
        (item)-> {
          //Once the shelf life date has passed, Worth degrades twice as fast
          int decreaseBy = item.getShelfLife() > 0 ? 1 : 2;
          int worth = Math.max(item.getWorth() - decreaseBy, 0);
          item.setWorth(worth);
        });

    ItemRule SHELF_LIFE_DECREASE = ItemRule.create(
        IS_CADMIUM.negate(),
        (item)-> item.setShelfLife(item.getShelfLife()-1));

    //Increases in Worth the older it gets
    ItemRule WORTH_INCREASE_RULE = ItemRule.create(
        //"Gold" actually increases in Worth the older it gets
        //The Worth of an item is never more than 50
        IS_GOLD.and(item -> item.getWorth() < 50),
        (item)-> {
          //Once the shelf life date has passed, Worth increases twice as fast
          int increaseBy = item.getShelfLife() > 0 ? 1 : 2;
          //The Worth of an item is never more than 50
          int worth = Math.min(item.getWorth() + increaseBy, 50);
          item.setWorth(worth);
        });

    //"Helium", like gold, increases in Worth as it's ShelfLife value changes;
    ItemRule HELIUM_RULE = ItemRule.create(
        //The Worth of an item is never more than 50
        IS_HELIUM.and(item -> item.getWorth() < 50),
        (item)-> {
          int shelfLife = item.getShelfLife();
          int worth = 0;
          int increaseBy = 1;
          //Worth drops to 0 once the ShelfLife is passed
          //By just letting worth be zero
          if (shelfLife > 0) {
            //Increases By 2 when there are 10 days or less
            if (shelfLife > 5 && shelfLife < 10) {
              increaseBy = 2;
            }
            //Increases By 3 when there are 5  days or less
            else if (shelfLife > 3 && shelfLife < 5) {
              increaseBy = 3;
            }
            worth = Math.min(item.getWorth() + increaseBy, 50);
          }
          item.setWorth(worth);
        });

    itemRules.add(SHELF_LIFE_DECREASE);
    itemRules.add(WORTH_DECREASE_RULE);
    itemRules.add(WORTH_INCREASE_RULE);
    itemRules.add(HELIUM_RULE);
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
