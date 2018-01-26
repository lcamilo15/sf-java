package com.lcamilo.store_front.service;

import com.lcamilo.store_front.model.CalculableItem;
import java.util.List;

/**
 * Created by luiscamilo on 1/21/18
 */
public interface InventoryWorthCalculator {

  List<CalculableItem> updateInventoryWorth(List<CalculableItem> items);

  CalculableItem updateWorth(CalculableItem item);

}