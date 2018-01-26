package com.lcamilo.store_front.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.lcamilo.store_front.model.CalculableItem;
import com.lcamilo.store_front.util.InventoryBuilder;
import com.lcamilo.store_front.util.ItemBuilder;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Created by luiscamilo on 1/21/18
 */
@RunWith(Parameterized.class)
public class InventoryWorthCalculatorTest {
  InventoryWorthCalculator inventoryWorthCalculator;

  public InventoryWorthCalculatorTest(InventoryWorthCalculator inventoryWorthCalculator) {
    this.inventoryWorthCalculator = inventoryWorthCalculator;
  }

  @Parameters
  public static Object[] data() {
    return new Object[] {
        new LegacyInventoryWorthCalculator(),
        new RuleBasedInventoryWorthCalculator()
    };
  }

  @Test
  public void regular_item_worth_should_degrade_twice_as_fast_once_when_shelf_life_has_passed() {
    CalculableItem previousItem = ItemBuilder.anItem().withName("Aluminum Shackles")
        .withShelfLife(0)
        .withWorth(20)
        .build();

    for (int currentDay = 0; currentDay < 10; currentDay++) {
      CalculableItem updatedItem = inventoryWorthCalculator.updateWorth(ItemBuilder.from(previousItem).build());

      //Worth of an Item is never negative
      ItemCalculatorAssertion.assertThat(updatedItem).worthIsNotNegative();

      //Assert Worth Degraded by 1
      ItemCalculatorAssertion.assertThat(updatedItem).worthHasDegraded(2, previousItem);

      //Assert ShelfLife Degraded by 1
      ItemCalculatorAssertion.assertThat(updatedItem).shelfLiveHasDegraded(1, previousItem);

      previousItem = ItemBuilder.from(updatedItem).build();
    }
  }

  @Test
  public void regular_itemWorth_should_degrade_daily() {
    int numberOfDays = 10;

    CalculableItem previousItem = ItemBuilder.anItem().withName("Aluminum Shackles")
        .withShelfLife(numberOfDays)
        .withWorth(20)
        .build();

    for (int currentDay = 0; currentDay < numberOfDays; currentDay++) {
      CalculableItem updatedItem = inventoryWorthCalculator.updateWorth(ItemBuilder.from(previousItem).build());

      //Worth of an Item is never negative
      ItemCalculatorAssertion.assertThat(updatedItem).worthIsNotNegative();

      //Assert Worth Degraded by 1
      ItemCalculatorAssertion.assertThat(updatedItem).worthHasDegraded(1, previousItem);

      //Assert ShelfLife Degraded by 1
      ItemCalculatorAssertion.assertThat(updatedItem).shelfLiveHasDegraded(1, previousItem);

      previousItem = ItemBuilder.from(updatedItem).build();
    }
  }

  @Test
  public void gold_should_increase_worth_the_older_it_gets() {
    int numberOfDays = 10;

    CalculableItem previousItem = ItemBuilder.anItem().withName("Gold")
        .withShelfLife(numberOfDays)
        .withWorth(20)
        .build();

    for (int currentDay = 0; currentDay < numberOfDays; currentDay++) {
      CalculableItem updatedItem = inventoryWorthCalculator.updateWorth(ItemBuilder.from(previousItem).build());

      //Worth of an Item is never negative
      ItemCalculatorAssertion.assertThat(updatedItem).worthIsNotNegative();

      //Assert Worth Increased by 1
      ItemCalculatorAssertion.assertThat(updatedItem).worthHasIncreased(1, previousItem);

      //Assert ShelfLife Degraded by 1
      ItemCalculatorAssertion.assertThat(updatedItem).shelfLiveHasDegraded(1, previousItem);

      previousItem = ItemBuilder.from(updatedItem).build();
    }
  }

  @Test
  public void gold_should_double_increase_worth_once_shelf_life_has_passed_the_older_it_gets() {
    CalculableItem previousItem = ItemBuilder.anItem().withName("Gold")
        .withShelfLife(0)
        .withWorth(20)
        .build();

    for (int currentDay = 0; currentDay < 10; currentDay++) {
      CalculableItem updatedItem = inventoryWorthCalculator.updateWorth(ItemBuilder.from(previousItem).build());

      //Worth of an Item is never negative
      ItemCalculatorAssertion.assertThat(updatedItem).worthIsNotNegative();

      //Assert Worth Increased by 2
      ItemCalculatorAssertion.assertThat(updatedItem).worthHasIncreased(2, previousItem);

      //Assert ShelfLife Degraded by 1
      ItemCalculatorAssertion.assertThat(updatedItem).shelfLiveHasDegraded(1, previousItem);

      previousItem = ItemBuilder.from(updatedItem).build();
    }
  }

  @Test
  public void worth_of_item_is_never_greater_than_50_except_cadium_or_alchemy() {
    List<CalculableItem> inventoryItems = InventoryBuilder.anInventoryBuilder()
        .addItem()
          .withName("Aluminum Shackles")
          .withShelfLife(10)
          .withWorth(20)
          .done()
        .addItem()
          .withName("Gold")
          .withShelfLife(2)
          .withWorth(50)
          .done()
        .addItem()
          .withName("Plutonium Pinball Parts")
          .withShelfLife(5)
          .withWorth(7)
          .done()
        .addItem()
          .withName("Cadmium")
          .withShelfLife(0)
          .withWorth(80)
          .done()
        .addItem()
          .withName("Helium")
          .withShelfLife(15)
          .withWorth(38)
          .done()
        .addItem()
          .withName("Alchemy Iron")
          .withShelfLife(3)
          .withWorth(75)
          .done()
        .build();

    inventoryWorthCalculator.updateInventoryWorth(inventoryItems).stream().filter(it->! it.getName().matches("(?ui)^(Alchemy Iron|Cadmium)$")).forEach(item->{
      int numberOfDays = item.getShelfLife() + 10;
      while(numberOfDays-- > 0) {
        ItemCalculatorAssertion.assertThat(item).worthIsNotGreaterThan(50);
        item = inventoryWorthCalculator.updateWorth(item);
      }
    });
  }

  @Test
  public void camdium_worth_never_increases() {
    int numberOfDays = 20;

    CalculableItem previousItem = ItemBuilder.anItem().withName("Cadmium")
        .withShelfLife(10)
        .withWorth(80)
        .build();

    for (int currentDay = 0; currentDay < numberOfDays; currentDay++) {
      CalculableItem updatedItem = inventoryWorthCalculator.updateWorth(ItemBuilder.from(previousItem).build());

      //Worth of an Item is never negative
      ItemCalculatorAssertion.assertThat(updatedItem).worthIsNotNegative();

      //Assert Worth Has not increased
      ItemCalculatorAssertion.assertThat(updatedItem).worthHasIncreased(0, previousItem);

      //Assert ShelfLife Degraded by 1
      ItemCalculatorAssertion.assertThat(updatedItem).shelfLiveHasDegraded(1, previousItem);

      previousItem = ItemBuilder.from(updatedItem).build();
    }
  }

  @Test
  public void helium_increase_by_two_in_worth_as_shelfLife_value_changes_below_10() {
    CalculableItem previousItem = ItemBuilder.anItem().withName("Helium")
        .withShelfLife(20)
        .withWorth(20)
        .build();

    while (previousItem.getShelfLife() > 0){
      CalculableItem updatedItem = inventoryWorthCalculator.updateWorth(ItemBuilder.from(previousItem).build());
      if (previousItem.getShelfLife() >= 10 && previousItem.getShelfLife() <= 5) {
        ItemCalculatorAssertion.assertThat(updatedItem).worthHasIncreased(1, previousItem);

        //Worth of an Item is never negative
        ItemCalculatorAssertion.assertThat(updatedItem).worthIsNotNegative();

        //Assert Worth Has Increased
        ItemCalculatorAssertion.assertThat(updatedItem).worthHasIncreased(2, previousItem);
      }

      //Assert ShelfLife Degraded by 1
      ItemCalculatorAssertion.assertThat(updatedItem).shelfLiveHasDegraded(1, previousItem);

      previousItem = ItemBuilder.from(updatedItem).build();
    }

    assertThat(previousItem.getShelfLife()).isEqualTo(0);
  }

  @Test
  public void helium_increase_by_two_in_worth_as_shelfLife_value_changes_below_5() {
    CalculableItem previousItem = ItemBuilder.anItem().withName("Helium")
        .withShelfLife(20)
        .withWorth(20)
        .build();

    while (previousItem.getShelfLife() > 0){
      CalculableItem updatedItem = inventoryWorthCalculator.updateWorth(ItemBuilder.from(previousItem).build());

      if (previousItem.getShelfLife() >= 5 && previousItem.getShelfLife() <= 3) {
        ItemCalculatorAssertion.assertThat(updatedItem).worthHasIncreased(1, previousItem);

        //Worth of an Item is never negative
        ItemCalculatorAssertion.assertThat(updatedItem).worthIsNotNegative();

        //Assert Worth Has Increased
        ItemCalculatorAssertion.assertThat(updatedItem).worthHasIncreased(1, previousItem);
      }

      //Assert ShelfLife Degraded by 1
      ItemCalculatorAssertion.assertThat(updatedItem).shelfLiveHasDegraded(1, previousItem);

      previousItem = ItemBuilder.from(updatedItem).build();
    }

    assertThat(previousItem.getShelfLife()).isEqualTo(0);
  }

  @Test
  public void helium_drops_to_0_in_worth_as_shelfLife_has_passed() {
    CalculableItem previousItem = ItemBuilder.anItem().withName("Helium")
        .withShelfLife(20)
        .withWorth(20)
        .build();

    while (previousItem.getShelfLife() > -10){
      CalculableItem updatedItem = inventoryWorthCalculator.updateWorth(ItemBuilder.from(previousItem).build());

      if (previousItem.getShelfLife() < 0) {

        //Worth of an Item is never negative
        ItemCalculatorAssertion.assertThat(updatedItem).worthIsNotNegative();

        //Assert Worth Has Increased
        ItemCalculatorAssertion.assertThat(updatedItem).worthIsEqualsTo(0);
      }

      //Assert ShelfLife Degraded by 1
      ItemCalculatorAssertion.assertThat(updatedItem).shelfLiveHasDegraded(1, previousItem);

      previousItem = ItemBuilder.from(updatedItem).build();
    }

    assertThat(previousItem.getShelfLife()).isEqualTo(-10);
  }
}