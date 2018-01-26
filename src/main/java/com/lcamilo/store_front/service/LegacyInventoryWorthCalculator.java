package com.lcamilo.store_front.service;

import com.lcamilo.store_front.model.CalculableItem;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by luiscamilo on 1/21/18
 */
public class LegacyInventoryWorthCalculator implements InventoryWorthCalculator {
  final int increaseWorthValueBy = -1;
  final int defaultMaxWorthValue = 50;

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

  private void applyRegularItemValueRule(CalculableItem calculableItem) {
    if (isRegularItem(calculableItem)) {
      int regularItemIncreaseBy = increaseWorthValueBy;
      if (calculableItem.shelfLifeHasPassed()) {
        regularItemIncreaseBy *= 2;
      }
      calculableItem.incrementWorthBy(regularItemIncreaseBy);
    }
  }

  private void applyGoldRule(CalculableItem calculableItem) {
    if (isGold(calculableItem)) {
      int goldWorthValueBy = Math.abs(increaseWorthValueBy);
      if (calculableItem.shelfLifeHasPassed()) {
        goldWorthValueBy *= 2;
      }
      calculableItem.incrementWorthBy(goldWorthValueBy);
    }
  }

  private void applyHeliumRule(CalculableItem calculableItem) {
    if (isHelium(calculableItem)) {
      int heliumWorthValueBy = Math.abs(increaseWorthValueBy);
      if (! calculableItem.shelfLifeHasPassed()) {
        if (calculableItem.shelfLifeBetween(5, 10)) {
          heliumWorthValueBy *= 2;
        } else if (calculableItem.shelfLifeBetween(1, 5)) {
          heliumWorthValueBy *= 3;
        }
        calculableItem.incrementWorthBy(heliumWorthValueBy);
      } else {
        calculableItem.setWorth(0);
      }
    }
  }

  private void applyCadmiumRule(CalculableItem calculableItem) {
    if (isCadmium(calculableItem)) {
      if (calculableItem.getWorth() != 80) {
        throw new RuntimeException("Cadmium value must be 80");
      }
    }
  }

  private void applyAlchemyRule(CalculableItem calculableItem) {
    if (isAlchemy(calculableItem)) {
      int heliumWorthValueBy = increaseWorthValueBy * 2;
      if (calculableItem.shelfLifeHasPassed()) {
        heliumWorthValueBy = heliumWorthValueBy * 2;
      }
      calculableItem.incrementWorthBy(heliumWorthValueBy);
    }
  }

  private void applyMaxWorthValueRule(CalculableItem calculableItem) {
    if (isAlchemy(calculableItem)) {
      calculableItem.setWorth(Math.max(calculableItem.getWorth(), 100));
    } else if (!isCadmium(calculableItem)){
      calculableItem.setWorth(Math.min(calculableItem.getWorth(), defaultMaxWorthValue));
    }
  }

  private void applyMinWorthValueRule(CalculableItem calculableItem) {
    calculableItem.setWorth(Math.max(calculableItem.getWorth(), 0));
  }

  public boolean isGold(CalculableItem calculableItem) {
    return calculableItem.getName().equalsIgnoreCase("GOLD");
  }

  public boolean isHelium(CalculableItem calculableItem) {
    return calculableItem.getName().equalsIgnoreCase("HELIUM");
  }

  public boolean isCadmium(CalculableItem calculableItem) {
    return calculableItem.getName().equalsIgnoreCase("CADMIUM");
  }

  public boolean isAlchemy(CalculableItem calculableItem) {
    return calculableItem.getName().matches("ALCHEMY IRON");
  }

  public boolean isSpecialItem(CalculableItem calculableItem) {
    return isGold(calculableItem) || isHelium(calculableItem) || isCadmium(calculableItem) || isAlchemy(calculableItem);
  }

  public boolean isRegularItem(CalculableItem calculableItem) {
    return ! isSpecialItem(calculableItem);
  }

  @Override
  public List<CalculableItem> updateInventoryWorth(List<CalculableItem> items) {
    return items.stream().map(this::updateWorth).collect(Collectors.toList());
  }
}