package com.ids.snort.service;

import com.ids.snort.dto.StatsResponse;
import com.ids.snort.repository.AlertRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class StatsService {
  private final AlertRepository alertRepository;

  public StatsService(AlertRepository alertRepository) {
    this.alertRepository = alertRepository;
  }

  public StatsResponse getStats() {
    Map<String, Long> severityCounts = new HashMap<>();
    for (Object[] row : alertRepository.countBySeverity()) {
      severityCounts.put(String.valueOf(row[0]), ((Number) row[1]).longValue());
    }

    Map<String, Long> protocolCounts = new HashMap<>();
    for (Object[] row : alertRepository.countByProtocol()) {
      protocolCounts.put(String.valueOf(row[0]), ((Number) row[1]).longValue());
    }

    List<Map<String, Object>> topSources = alertRepository.topSources(PageRequest.of(0, 5))
        .stream()
        .map(row -> Map.<String, Object>of(
            "value", String.valueOf(row[0]),
            "count", ((Number) row[1]).longValue()))
        .toList();

    List<Map<String, Object>> topDestinations = alertRepository.topDestinations(PageRequest.of(0, 5))
        .stream()
        .map(row -> Map.<String, Object>of(
            "value", String.valueOf(row[0]),
            "count", ((Number) row[1]).longValue()))
        .toList();

    return new StatsResponse(alertRepository.count(), severityCounts, protocolCounts, topSources,
        topDestinations);
  }
}
