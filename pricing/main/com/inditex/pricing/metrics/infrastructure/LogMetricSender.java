package com.inditex.pricing.metrics.infrastructure;

import java.text.MessageFormat;

import com.inditex.pricing.metrics.domain.Metric;
import com.inditex.pricing.metrics.domain.MetricSender;
import com.inditex.pricing.shared.domain.Service;
import com.inditex.pricing.shared.domain.Utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LogMetricSender implements MetricSender {

  @Override
  public void send(Metric metric) {
    final var message =
        MessageFormat.format("{0} -> \n {1}", metric.message(), Utils.toParsedJson(metric.json()));
    log.info(message);
  }
}
