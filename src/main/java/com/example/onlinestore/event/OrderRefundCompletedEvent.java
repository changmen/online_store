package com.example.onlinestore.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.io.Serial;
import java.util.Map;

@Getter
public class OrderRefundCompletedEvent extends ApplicationEvent {
    @Serial
    private static final long serialVersionUID = 3824898071010039384L;

    private final Map<String, Object> eventData;

    public OrderRefundCompletedEvent(Map<String, Object> eventData) {
        super(eventData);
        this.eventData = eventData;
    }
} 