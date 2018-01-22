package com.lcamilo.store_front.util;

/**
 * Created by luiscamilo on 1/21/18
 */
public interface Builder<I> {
  interface ChildAwareBuilder<I, CB> extends Builder<I> {
    void childDone(CB childBuilder);
  }
  abstract class ParentAwareBuilder<I, PB extends ChildAwareBuilder> implements Builder<I> {
    PB parentBuilder;
    ParentAwareBuilder() {
      this.parentBuilder = null;
    }
    ParentAwareBuilder(PB parentBuilder) {
      this.parentBuilder= parentBuilder;
    }
    public PB done() {
      if (parentBuilder != null) {
        parentBuilder.childDone(this);
      }
      return parentBuilder;
    }
  }
  I build();
}