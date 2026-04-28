package com.btg.funds.infrastructure.adapter.out.notification;

import com.btg.funds.domain.model.NotificationChannel;
import com.btg.funds.domain.port.out.NotificationPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class MockNotificationAdapter implements NotificationPort {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockNotificationAdapter.class);

    @Override
    public void send(String destination, NotificationChannel channel, String message) {
        if (!StringUtils.hasText(destination)) {
            LOGGER.warn("Mock notification skipped. channel={} reason=empty-destination", channel);
            return;
        }
        if (channel == NotificationChannel.EMAIL) {
            LOGGER.info("Mock EMAIL sent. to={} subject={} body={}",
                    destination,
                    "BTG Funds Notification",
                    message);
            return;
        }
        LOGGER.info("Mock SMS sent. phone={} message={}", destination, message);
    }
}
