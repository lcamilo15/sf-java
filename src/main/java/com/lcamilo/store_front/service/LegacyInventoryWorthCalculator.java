package com.lcamilo.store_front.service;

import com.lcamilo.store_front.model.CalculableItem;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by luiscamilo on 1/21/18
 */
public class LegacyInventoryWorthCalculator implements InventoryWorthCalculator {

  public CalculableItem updateWorth(CalculableItem calculableItem) {
    if (! calculableItem.getName().equalsIgnoreCase("Gold") && ! calculableItem.getName().equalsIgnoreCase("Helium")) {
      if (calculableItem.getWorth()> 0) {
        if (! calculableItem.getName().equalsIgnoreCase("Cadmium")) {
          calculableItem.setWorth(calculableItem.getWorth() - 1);
        }
      }
    } else {
      if (calculableItem.getWorth() < 50) {
        calculableItem.setWorth(calculableItem.getWorth()+1);;
        if (calculableItem.getName().equalsIgnoreCase("Helium")) {
          if (calculableItem.getShelfLife() < 11) {
            if (calculableItem.getWorth() < 50) {
              calculableItem.setWorth(calculableItem.getWorth() + 1);
            }
          }
          if (calculableItem.getShelfLife() < 6) {
            if (calculableItem.getWorth() < 50) {
              calculableItem.setWorth(calculableItem.getWorth()+1);
            }
          }
        }
      }
    }

    if (! calculableItem.getName().equalsIgnoreCase("Cadmium")) {
      calculableItem.setShelfLife(calculableItem.getShelfLife()-1);
    }

    if (calculableItem.getShelfLife() < 0) {
      if (! calculableItem.getName().equalsIgnoreCase("Gold")) {
        if (! calculableItem.getName().equalsIgnoreCase("Helium")) {
          if (calculableItem.getWorth() > 0) {
            if (! calculableItem.getName().equalsIgnoreCase("Cadmium")) {
              calculableItem.setWorth(calculableItem.getWorth()-1);;
            }
          }
        } else {
          calculableItem.setWorth(0);
        }
      } else {
        if (calculableItem.getWorth() < 50) {
          calculableItem.setWorth(calculableItem.getWorth()+1);
        }
      }
    }

    return calculableItem;
  }

  @Override
  public List<CalculableItem> updateInventoryWorth(List<CalculableItem> items) {
    return items.stream().map(this::updateWorth).collect(Collectors.toList());
  }
}