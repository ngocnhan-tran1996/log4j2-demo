package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@Log4j2
class FileLogsTests {

  @Nested
  class NotExistEnv {

    @Test
    void compare_json_log_with_default_pattern() throws IOException {

      log.error("TEST");

      var now = LocalDateTime.now();
      var fileFormat = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm"));
      var file = new File("./logs/app-%s.log".formatted(fileFormat));
      var text = Files.readAllLines(file.toPath()).getFirst();
      assertThat(text)
          .isNotEqualTo(
              """
                  {"message1":"%s","message2":"%s","message3":"%s"}
                  """
                  .formatted(
                      now.minusHours(7)
                          .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                      now.minusHours(4)
                          .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                      now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                  )
          );
    }

  }

  @Nested
  class ExistEnv {

    static {
      System.setProperty("TZ", "%d{yyyy-MM-dd HH:mm}");
    }

    @Test
    void compare_json_log_with_environment_pattern() throws IOException {

      log.error("TEST");

      var now = LocalDateTime.now();
      var fileFormat = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm"));
      var file = new File("./logs/app-%s.log".formatted(fileFormat));
      var text = Files.readAllLines(file.toPath()).getFirst();
      assertThat(text)
          .isNotEqualTo(
              """
                  {"message1":"%s","message2":"%s","message3":"%s"}
                  """
                  .formatted(
                      now.minusHours(7)
                          .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                      now.minusHours(4)
                          .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                      now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                  )
          );
    }
  }

}