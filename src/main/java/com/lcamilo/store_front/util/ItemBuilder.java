package com.lcamilo.store_front.util;

import com.lcamilo.store_front.model.CalculableItem;
import com.lcamilo.store_front.model.ItemWrapper;
import com.lcamilo.store_front.util.Builder.ParentAwareBuilder;

/**
 * Created by luiscamilo on 1/21/18
 */
public class ItemBuilder extends ParentAwareBuilder<CalculableItem, InventoryBuilder> {
  public String Name;
  public int ShelfLife;
  public int Worth;

  public ItemBuilder(InventoryBuilder parentBuilder) {
    super(parentBuilder);
  }

  public ItemBuilder() {
    super();
  }

  public static ItemBuilder anItem() {
    return new ItemBuilder();
  }

  public ItemBuilder withName(String Name) {
    this.Name = Name;
    return this;
  }

  public ItemBuilder withShelfLife(int ShelfLife) {
    this.ShelfLife = ShelfLife;
    return this;
  }

  public ItemBuilder withWorth(int Worth) {
    this.Worth = Worth;
    return this;
  }

  public CalculableItem build() {
    ItemWrapper item = new ItemWrapper();
    item.setName(Name);
    item.setShelfLife(ShelfLife);
    item.setWorth(Worth);
    return item;
  }

  public static ItemBuilder from(CalculableItem item) {
    ItemBuilder itemBuilder = new ItemBuilder();
    itemBuilder.withName(item.getName());
    itemBuilder.withShelfLife(item.getShelfLife());
    itemBuilder.withWorth(item.getWorth());
    return itemBuilder;
  }
}
