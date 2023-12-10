/* Copyright (C) Red Hat 2023 */
package io.opentelemetry.examples.utils;

public final class FelinePercent extends Number {
  private double value;

  public FelinePercent(double v) {
    if (v < 0.0 || v > 1.0) {
      throw new IllegalArgumentException("FelinePercent must be between 0 and 1");
    }
    value = v;
  }

  public void setValue(double v) {
    if (v < 0.0 || v > 1.0) {
      throw new IllegalArgumentException("FelinePercent must be between 0 and 1");
    }
    value = v;
  }

  @Override
  public int intValue() {
    return (int) value;
  }

  @Override
  public long longValue() {
    return (long) value;
  }

  @Override
  public float floatValue() {
    return (float) value;
  }

  @Override
  public double doubleValue() {
    return value;
  }
}
