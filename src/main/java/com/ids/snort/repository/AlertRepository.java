package com.ids.snort.repository;

import com.ids.snort.model.Alert;
import com.ids.snort.model.Severity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlertRepository extends JpaRepository<Alert, Long>, JpaSpecificationExecutor<Alert> {
  List<Alert> findByEventTimeAfterOrderByEventTimeDesc(LocalDateTime since, Pageable pageable);

  @Query("select a.severity, count(a) from Alert a group by a.severity")
  List<Object[]> countBySeverity();

  @Query("select a.protocol, count(a) from Alert a where a.protocol is not null group by a.protocol")
  List<Object[]> countByProtocol();

  @Query("select a.srcIp, count(a) from Alert a where a.srcIp is not null group by a.srcIp order by count(a) desc")
  List<Object[]> topSources(Pageable pageable);

  @Query("select a.dstIp, count(a) from Alert a where a.dstIp is not null group by a.dstIp order by count(a) desc")
  List<Object[]> topDestinations(Pageable pageable);

  @Query("select a from Alert a where a.eventTime >= :since order by a.eventTime desc")
  List<Alert> findRecentAlerts(@Param("since") LocalDateTime since, Pageable pageable);

  long countBySeverity(Severity severity);
}
