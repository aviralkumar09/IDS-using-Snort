package com.ids.snort.service;

import com.ids.snort.model.Rule;
import com.ids.snort.model.Severity;
import com.ids.snort.repository.RuleRepository;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {
  private final RuleRepository ruleRepository;

  public DataSeeder(RuleRepository ruleRepository) {
    this.ruleRepository = ruleRepository;
  }

  @Override
  public void run(String... args) {
    if (ruleRepository.count() > 0) {
      return;
    }

    Rule sshBruteForce = new Rule();
    sshBruteForce.setSid("1000001");
    sshBruteForce.setName("SSH brute force detection");
    sshBruteForce.setDescription("Detect repeated SSH authentication attempts.");
    sshBruteForce.setSeverity(Severity.HIGH);

    Rule dnsTunneling = new Rule();
    dnsTunneling.setSid("1000002");
    dnsTunneling.setName("DNS tunneling anomaly");
    dnsTunneling.setDescription("Identify unusually long DNS queries indicating tunneling.");
    dnsTunneling.setSeverity(Severity.MEDIUM);

    Rule webScan = new Rule();
    webScan.setSid("1000003");
    webScan.setName("Web scanner footprint");
    webScan.setDescription("Detect rapid HTTP requests that match scanning behavior.");
    webScan.setSeverity(Severity.LOW);

    ruleRepository.saveAll(List.of(sshBruteForce, dnsTunneling, webScan));
  }
}
