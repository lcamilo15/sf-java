StoreFront
==========
[![Build Status](https://api.travis-ci.org/lcamilo15/sf-java.svg?branch=master)](https://travis-ci.org/lcamilo15/sf-java)
[![Coverage Status](https://coveralls.io/repos/github/lcamilo15/sf-java/badge.svg?branch=master)](https://coveralls.io/github/lcamilo15/sf-java?branch=master)


## Introduction

Example of using Simple Rules to determine item worth and ShelfLife of a project.

- [com.lcamilo.other_team.model.Item](https://github.com/lcamilo15/sf-java/blob/master/src/main/java/com/lcamilo/other_team/model/Item.java) Cannot be altered as it is maintained by another team that doesn’t believe in shared code ownership. To solve this, an Item adapter was created [com.lcamilo.store_front.model.ItemRuleAdapter](https://github.com/lcamilo15/sf-java/blob/master/src/main/java/com/lcamilo/store_front/model/ItemRuleAdapter.java) which wraps an Item and add functionality without having to alter the other teams code.

- Two solutions were implemented which both implement an InventoryWorthCalculator:
 
  A modified implementation of the original code, [com.lcamilo.store_front.service.LegacyInventoryWorthCalculator](https://github.com/lcamilo15/sf-java/blob/master/src/main/java/com/lcamilo/store_front/service/LegacyInventoryWorthCalculator.java) which simplifies the logic by making the code a little more readable.
  
  ```
     public CalculableItem updateWorth(CalculableItem calculableItem) {
      applyGoldRule(calculableItem);
      applyHeliumRule(calculableItem);
      applyCadmiumRule(calculableItem);
      applyAlchemyRule(calculableItem);
      applyMaxWorthValueRule(calculableItem);
      applyMinWorthValueRule(calculableItem);
      applyRegularItemValueRule(calculableItem);
      calculableItem.incrementShelfLifeBy(-1);
      return calculableItem;
    }
  ```
  

  A dynamic rule worth based calculator, [com.lcamilo.store_front.service.RuleBasedInventoryWorthCalculator] (https://github.com/lcamilo15/sf-java/blob/master/src/main/java/com/lcamilo/store_front/service/RuleBasedInventoryWorthCalculator.java). A more flexible approach using a simple Rules engine by creating a modified version of a command pattern. 

    ```java
     itemRules = ItemRuleBuilder.newBuilder()
        .addRule(
            when(IS_REGULAR_ITEM.and(SHELF_DATE_HAS_NOT_PASSED)),
            then(incrementBy(incrementValueBy, defaultMaxWorthValue))
        )
    ```

