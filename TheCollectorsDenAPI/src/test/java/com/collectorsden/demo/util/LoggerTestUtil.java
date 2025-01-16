package com.collectorsden.demo.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

public class LoggerTestUtil {

    public static TestLogAppender captureLogs(Class<?> clazz) {
        Logger logger = (Logger) org.slf4j.LoggerFactory.getLogger(clazz);
        TestLogAppender appender = new TestLogAppender();
        appender.setContext(logger.getLoggerContext());
        logger.addAppender(appender);
        appender.start();
        return appender;
    }

    public static void assertLog(
            List<ILoggingEvent> events,
            int index,
            Level expectedLevel,
            String expectedMessage
    ) {
        Assertions.assertTrue(index < events.size(), "Log event index out of range.");
        ILoggingEvent event = events.get(index);
        Assertions.assertEquals(expectedLevel, event.getLevel(), "Unexpected log level.");
        Assertions.assertTrue(
                event.getFormattedMessage().contains(expectedMessage),
                "Log message does not contain the expected text."
        );
    }

    public static class TestLogAppender extends AppenderBase<ILoggingEvent> {
        private final List<ILoggingEvent> logs = new ArrayList<>();

        @Override
        protected void append(ILoggingEvent event) {
            logs.add(event);
        }

        public List<ILoggingEvent> getLogs() {
            return logs;
        }
    }
}
