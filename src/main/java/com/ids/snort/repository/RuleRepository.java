package com.ids.snort.repository;

import com.ids.snort.model.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleRepository extends JpaRepository<Rule, Long> {
  boolean existsBySid(String sid);
}
