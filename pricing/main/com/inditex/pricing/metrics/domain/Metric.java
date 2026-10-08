package com.inditex.pricing.metrics.domain;

import java.io.Serializable;
import java.util.Map;

import lombok.Value;

@Value
public class Metric {
  String message;

  Map<String, Serializable> json;
}
