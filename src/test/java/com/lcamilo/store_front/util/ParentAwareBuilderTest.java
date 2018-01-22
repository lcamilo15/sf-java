package com.lcamilo.store_front.util;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.times;

import com.lcamilo.store_front.model.CalculableItem;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Created by luiscamilo on 1/21/18
 */
@RunWith(MockitoJUnitRunner.class)
public class ParentAwareBuilderTest {
  @Spy
  InventoryBuilder inventoryBuilder;
  int itemsToAdd = 10;
  @Before
  public void setupBuilder() {
    int itemsToAdd = 10;
    for (int itemsAdded = 0; itemsAdded < itemsToAdd; itemsAdded++) {
      inventoryBuilder.addItem().withWorth(10).done();
    }
  }
  @Test
  public void child_must_call_childDone_on_parent_builder() {
    Mockito.verify(inventoryBuilder, times(itemsToAdd)).childDone(Mockito.any());
  }
  @Test
  public void parent_list_must_have_size_equals_to_added_items() {
    List<CalculableItem> items = inventoryBuilder.build();
    assertThat(items).hasSize(itemsToAdd);
  }
}