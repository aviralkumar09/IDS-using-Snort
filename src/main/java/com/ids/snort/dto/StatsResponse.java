package com.ids.snort.dto;

import java.util.List;
import java.util.Map;

public class StatsResponse {
  private long totalAlerts;
  private Map<String, Long> severityCounts;
  private Map<String, Long> protocolCounts;
  private List<Map<String, Object>> topSources;
  private List<Map<String, Object>> topDestinations;

  public StatsResponse(long totalAlerts, Map<String, Long> severityCounts,
      Map<String, Long> protocolCounts, List<Map<String, Object>> topSources,
      List<Map<String, Object>> topDestinations) {
    this.totalAlerts = totalAlerts;
    this.severityCounts = severityCounts;
    this.protocolCounts = protocolCounts;
    this.topSources = topSources;
    this.topDestinations = topDestinations;
  }

  public long getTotalAlerts() {
    return totalAlerts;
  }

  public Map<String, Long> getSeverityCounts() {
    return severityCounts;
  }

  public Map<String, Long> getProtocolCounts() {
    return protocolCounts;
  }

  public List<Map<String, Object>> getTopSources() {
    return topSources;
  }

  public List<Map<String, Object>> getTopDestinations() {
    return topDestinations;
  }
}
