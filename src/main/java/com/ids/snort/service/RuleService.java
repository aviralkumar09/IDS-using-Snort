package com.ids.snort.service;

import com.ids.snort.dto.RuleResponse;
import com.ids.snort.model.Rule;
import com.ids.snort.repository.RuleRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RuleService {
  private final RuleRepository ruleRepository;

  public RuleService(RuleRepository ruleRepository) {
    this.ruleRepository = ruleRepository;
  }

  public List<RuleResponse> listRules() {
    List<RuleResponse> responses = new ArrayList<>();
    for (Rule rule : ruleRepository.findAll()) {
      responses.add(new RuleResponse(
          rule.getSid(),
          rule.getName(),
          rule.getDescription(),
          rule.getSeverity().name(),
          rule.isEnabled()));
    }
    return responses;
  }
}
