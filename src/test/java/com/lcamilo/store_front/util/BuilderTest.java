package com.lcamilo.store_front.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.lcamilo.store_front.model.CalculableItem;
import com.lcamilo.store_front.model.ItemRuleAdapter;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Created by luiscamilo on 1/21/18
 */

class BuilderTest {
  CalculableItem expedtedBuiltItem;

  @BeforeEach
  public void setup() {
    expedtedBuiltItem = new ItemRuleAdapter<>();
    expedtedBuiltItem.setName("NAME");
    expedtedBuiltItem.setWorth(10);
    expedtedBuiltItem.setShelfLife(10);
  }

  @Test
  public void item_build_by_builder_should_build_item_with_built_parameters() {

    CalculableItem builtItem = ItemBuilder
        .anItem()
        .withName(expedtedBuiltItem.getName())
        .withShelfLife(expedtedBuiltItem.getShelfLife())
        .withWorth(expedtedBuiltItem.getWorth()).build();

    assertThat(builtItem).isEqualTo(expedtedBuiltItem)
        .as("Built item %s should be equal to item with same properties %s",
            builtItem,
            expedtedBuiltItem
        );
  }

  @Test
  public void builder_from_should_create_copy_of_item() {
    CalculableItem builtItem = ItemBuilder.from(expedtedBuiltItem).build();
    assertThat(builtItem).isEqualTo(expedtedBuiltItem)
        .as("Built item %s should be equal to item with same properties %s",
            builtItem,
            expedtedBuiltItem
        );
  }

  @Test
  public void simple_inventory_builder_check() {
    List<CalculableItem> inventoryItems = InventoryBuilder.anInventoryBuilder()
        .addItem()
        .withName(expedtedBuiltItem.getName())
        .withShelfLife(expedtedBuiltItem.getShelfLife())
        .withWorth(expedtedBuiltItem.getWorth())
        .done().build();

    inventoryItems.forEach(inventoryItem -> {
      assertThat(inventoryItem).isEqualTo(expedtedBuiltItem)
          .as("Built item %s should be equal to item with same properties %s",
              inventoryItem,
              expedtedBuiltItem
          );
    });
  }

  @Test
  public void simple_invalid_inventory_builder_check() {
    List<CalculableItem> inventoryItems = InventoryBuilder.anInventoryBuilder()
        .addItem()
        .withName(expedtedBuiltItem.getName())
        .withShelfLife(expedtedBuiltItem.getShelfLife())
        .withWorth(expedtedBuiltItem.getWorth())
        .done().build();

    InventoryBuilder.from(inventoryItems).build().forEach(inventoryItem -> {
      inventoryItem.setShelfLife(inventoryItem.getShelfLife() - 1);
      assertThat(inventoryItem).isNotEqualTo(expedtedBuiltItem)
          .as("Built item %s should not be equal to item with same properties %s", inventoryItem, expedtedBuiltItem);
    });

    InventoryBuilder.from(inventoryItems).build().forEach(inventoryItem -> {
      inventoryItem.setWorth(inventoryItem.getWorth() - 1);
      assertThat(inventoryItem).isNotEqualTo(expedtedBuiltItem)
          .as("Built item %s should not be equal to item with same properties %s", inventoryItem, expedtedBuiltItem);
    });

    InventoryBuilder.from(inventoryItems).build().forEach(inventoryItem -> {
      inventoryItem.setName(inventoryItem.getName().concat("D"));
      assertThat(inventoryItem).isNotEqualTo(expedtedBuiltItem)
          .as("Built item %s should not be equal to item with same properties %s", inventoryItem, expedtedBuiltItem);
    });

    InventoryBuilder.from(inventoryItems).build().forEach(inventoryItem -> {
      assertThat(inventoryItem).isNotEqualTo(new Object()).as("Built item %s should not be equal to any object", inventoryItem);
    });
  }
}