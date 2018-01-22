package com.lcamilo.store_front.rule;

import com.lcamilo.store_front.model.CalculableItem;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by luiscamilo on 1/21/18
 */
public abstract class ItemRule implements Predicate<CalculableItem>, Function<CalculableItem, CalculableItem> {
  Predicate<CalculableItem> itemRulePredicate;

  public ItemRule() {
    this.itemRulePredicate = this::matches;
  }

  public abstract boolean matches(CalculableItem calculableItem);

  @Override
  public boolean test(CalculableItem item) {
    return itemRulePredicate.test(item);
  }

  @Override
  public CalculableItem apply(CalculableItem calculableItem) {
    return this.execute(calculableItem);
  }

  public abstract CalculableItem execute(CalculableItem item);

  @FunctionalInterface
  public interface When extends Predicate<CalculableItem>{
    default When and(When other) {
      Objects.requireNonNull(other);
      return (t) -> test(t) && other.test(t);
    }
    default When or(When other) {
      Objects.requireNonNull(other);
      return (t) -> test(t) || other.test(t);
    }
    default When negate() {
      return (t) -> !test(t);
    }

  }

  @FunctionalInterface
  public interface Then extends Consumer<CalculableItem>{

  }

  public static ItemRule create(When when, Then then) {
    return new ItemRule() {
      @Override
      public boolean matches(CalculableItem calculableItem) {
        return when.test(calculableItem);
      }

      @Override
      public CalculableItem execute(CalculableItem item) {
        then.accept(item);
        return item;
      }
    };
  }
}
