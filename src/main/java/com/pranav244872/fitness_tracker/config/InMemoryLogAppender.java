package com.pranav244872.fitness_tracker.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.classic.PatternLayout;

import java.util.LinkedList;
import java.util.List;

public class InMemoryLogAppender extends AppenderBase<ILoggingEvent> {
    private static final int MAX_LINES = 1000;
    private static final LinkedList<String> LOG_BUFFER = new LinkedList<>();
    private PatternLayout layout;

    @Override
    public void start() {
        layout = new PatternLayout();
        layout.setContext(getContext());
        layout.setPattern("[%d{HH:mm:ss.SSS}] %-5level %logger{30} - %msg%n");
        layout.start();
        super.start();
    }

    @Override
    protected void append(ILoggingEvent event) {
        String formatted = layout.doLayout(event);
        synchronized (LOG_BUFFER) {
            LOG_BUFFER.addLast(formatted);
            while (LOG_BUFFER.size() > MAX_LINES) {
                LOG_BUFFER.removeFirst();
            }
        }
    }

    public static List<String> getLogs() {
        synchronized (LOG_BUFFER) {
            return new LinkedList<>(LOG_BUFFER);
        }
    }

    public static List<String> getLastNLogs(int n) {
        synchronized (LOG_BUFFER) {
            int size = LOG_BUFFER.size();
            int start = Math.max(0, size - n);
            return new LinkedList<>(LOG_BUFFER.subList(start, size));
        }
    }
}
