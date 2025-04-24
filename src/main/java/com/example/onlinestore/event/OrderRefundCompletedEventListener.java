package com.example.onlinestore.event;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class OrderRefundCompletedEventListener implements ApplicationListener<OrderRefundCompletedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(OrderRefundCompletedEventListener.class);
    @Override
    public void onApplicationEvent(OrderRefundCompletedEvent event) {
        logger.info("收到退款完成事件，payload:{}", JSON.toJSONString(event.getEventData()));

    }
}
