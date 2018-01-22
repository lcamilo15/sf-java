package com.lcamilo.store_front.util;

import com.lcamilo.store_front.model.CalculableItem;
import com.lcamilo.store_front.util.Builder.ChildAwareBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by luiscamilo on 1/21/18
 */
public class InventoryBuilder implements ChildAwareBuilder<List<CalculableItem>, ItemBuilder> {
  List<ItemBuilder> itemList = new ArrayList<>();

  @Override
  public void childDone(ItemBuilder itemBuilder) {
    itemList.add(itemBuilder);
  }

  @Override
  public List<CalculableItem> build() {
    return itemList.stream().map(ItemBuilder::build).collect(Collectors.toList());
  }

  public ItemBuilder addItem() {
    return new ItemBuilder(this);
  }

  public static InventoryBuilder anInventoryBuilder() {
    return new InventoryBuilder();
  }

  public static InventoryBuilder from(List<CalculableItem> inventoryItems) {
    InventoryBuilder inventoryBuilder = anInventoryBuilder();
    inventoryBuilder.itemList = inventoryItems.stream().map(ItemBuilder::from).collect(Collectors.toList());
    return inventoryBuilder;
  }
}