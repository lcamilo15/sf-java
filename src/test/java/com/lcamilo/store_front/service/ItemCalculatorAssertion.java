package com.lcamilo.store_front.service;

import com.lcamilo.store_front.model.CalculableItem;
import org.assertj.core.api.AbstractAssert;

/**
 * Created by luiscamilo on 1/21/18
 */
public class ItemCalculatorAssertion extends AbstractAssert<ItemCalculatorAssertion, CalculableItem> {
  public ItemCalculatorAssertion(CalculableItem item) {
    super(item, ItemCalculatorAssertion.class);
  }

  public ItemCalculatorAssertion worthHasDegraded(int degradedBy, CalculableItem previousItem) {
    isNotNull();
    Integer actualDegradedBy = previousItem.getWorth() - actual.getWorth();
    org.assertj.core.api.Assertions.assertThat(actualDegradedBy).as(
        "Item: %s's worth should be degraded by %s after but it was degraded by %s. Previous: %s, Actual: %s",
        actual,
        degradedBy,
        actualDegradedBy,
        previousItem.getWorth(),
        actual.getWorth())
        .isEqualTo(degradedBy);
    return this;
  }

  public ItemCalculatorAssertion shelfLiveHasDegraded(int degradedBy, CalculableItem previousItem) {
    isNotNull();
    Integer actualDegradedBy = previousItem.getShelfLife() - actual.getShelfLife();
    org.assertj.core.api.Assertions.assertThat(actualDegradedBy).as(
        "Item: %s's shelfLife should be degraded by %s after update but was degraded by %s.",
        actual,
        degradedBy,
        actualDegradedBy)
        .isEqualTo(degradedBy);
    return this;
  }

  public ItemCalculatorAssertion worthHasNotDegraded(CalculableItem previousItem) {
    return worthHasDegraded(0, previousItem);
  }

  public static ItemCalculatorAssertion assertThat(CalculableItem actual) {
    return new ItemCalculatorAssertion(actual);
  }

  public ItemCalculatorAssertion worthIsNotGreaterThan(int expected) {
    org.assertj.core.api.Assertions.assertThat(actual.getWorth()).as(
        "Item: %s's worth should not be greater than %s,",
        actual,
        expected
        ).isLessThanOrEqualTo(expected);
    return this;
  }

  public ItemCalculatorAssertion worthIsNotNegative() {
    isNotNull();
    org.assertj.core.api.Assertions.assertThat(actual.getWorth()).as(
        "Item: %s's worth should not be negative but actual worth is %s",
        actual,
        actual.getWorth())
        .isGreaterThanOrEqualTo(0);
    return this;
  }

  public ItemCalculatorAssertion worthHasIncreased(int expectedIncreasedBy, CalculableItem previousItem) {
    isNotNull();
    Integer increasedBy = actual.getWorth() - previousItem.getWorth();
    org.assertj.core.api.Assertions.assertThat(expectedIncreasedBy).as(
        "Item: %s's worth should increase by %s after update. Expected %s but %s given.",
        actual,
        expectedIncreasedBy,
        previousItem.getWorth() + expectedIncreasedBy,
        actual.getWorth())
        .isEqualTo(increasedBy);
    return this;
  }

  public ItemCalculatorAssertion worthIsEqualsTo(int expected) {
    isNotNull();
    org.assertj.core.api.Assertions.assertThat(actual.getWorth()).as(
        "Item: %s's worth should be equals to %s.",
        actual,
        actual.getWorth(),
        expected)
        .isEqualTo(expected);
    return this;
  }
}
