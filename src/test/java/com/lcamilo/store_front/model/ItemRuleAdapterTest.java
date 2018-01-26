package com.lcamilo.store_front.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

/**
 * Created by luiscamilo on 1/26/18
 */
public class ItemRuleAdapterTest {
  @Test
  public void testEquals() {
    EqualsVerifier.forClass(ItemRuleAdapter.class).verify();
  }
}