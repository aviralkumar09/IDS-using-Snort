package com.ids.snort.service;

import com.ids.snort.model.Alert;
import com.ids.snort.repository.AlertRepository;
import com.ids.snort.util.SnortAlertParser;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SnortAlertIngestor {
  private static final Logger logger = LoggerFactory.getLogger(SnortAlertIngestor.class);

  private final AlertRepository alertRepository;
  private final SnortAlertParser parser;
  private final LiveAlertPublisher publisher;
  private final Path alertFilePath;
  private long lastPosition = 0L;

  public SnortAlertIngestor(AlertRepository alertRepository, SnortAlertParser parser,
      LiveAlertPublisher publisher, @Value("${snort.alert.file}") String alertFile) {
    this.alertRepository = alertRepository;
    this.parser = parser;
    this.publisher = publisher;
    this.alertFilePath = Paths.get(alertFile);
  }

  @Scheduled(fixedDelayString = "${snort.alert.poll-interval:2000}")
  public void ingest() {
    if (!Files.exists(alertFilePath)) {
      return;
    }

    try (RandomAccessFile raf = new RandomAccessFile(alertFilePath.toFile(), "r")) {
      if (raf.length() < lastPosition) {
        lastPosition = 0L;
      }
      raf.seek(lastPosition);
      String line;
      while ((line = raf.readLine()) != null) {
        Alert alert = parser.parse(line);
        if (alert != null) {
          Alert saved = alertRepository.save(alert);
          publisher.publish(saved);
        }
      }
      lastPosition = raf.getFilePointer();
    } catch (IOException ex) {
      logger.warn("Failed to read Snort alert file", ex);
    }
  }
}
