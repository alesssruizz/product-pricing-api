package com.inditex.pricing.metrics.domain;

public interface MetricSender {
  void send(Metric metric);
}
