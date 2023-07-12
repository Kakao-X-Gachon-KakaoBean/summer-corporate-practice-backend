package com.kakaobean.core.notification.infrastructure;

public enum QueueNameConfig {

    USER_PREFIX("user-"), PROJECT_PREFIX("project-");

    private String prefix;

    QueueNameConfig(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
