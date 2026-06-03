package com.ids.snort.service;

import com.ids.snort.dto.AlertResponse;
import com.ids.snort.model.Alert;
import com.ids.snort.model.Severity;
import com.ids.snort.repository.AlertRepository;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AlertService {
  private final AlertRepository alertRepository;

  public AlertService(AlertRepository alertRepository) {
    this.alertRepository = alertRepository;
  }

  public List<AlertResponse> searchAlerts(String severity, String query, int limit) {
    Specification<Alert> spec = (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (StringUtils.hasText(severity)) {
        predicates.add(criteriaBuilder.equal(root.get("severity"), Severity.valueOf(severity)));
      }
      if (StringUtils.hasText(query)) {
        String like = "%" + query.toLowerCase() + "%";
        predicates.add(criteriaBuilder.or(
            criteriaBuilder.like(criteriaBuilder.lower(root.get("message")), like),
            criteriaBuilder.like(criteriaBuilder.lower(root.get("srcIp")), like),
            criteriaBuilder.like(criteriaBuilder.lower(root.get("dstIp")), like)));
      }
      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };

    List<Alert> alerts = alertRepository.findAll(spec, PageRequest.of(0, limit)).getContent();
    List<AlertResponse> responses = new ArrayList<>();
    for (Alert alert : alerts) {
      responses.add(new AlertResponse(
          alert.getId(),
          alert.getEventTime(),
          alert.getMessage(),
          alert.getPriority(),
          alert.getSeverity().name(),
          alert.getProtocol(),
          alert.getSrcIp(),
          alert.getSrcPort(),
          alert.getDstIp(),
          alert.getDstPort()));
    }
    return responses;
  }
}
