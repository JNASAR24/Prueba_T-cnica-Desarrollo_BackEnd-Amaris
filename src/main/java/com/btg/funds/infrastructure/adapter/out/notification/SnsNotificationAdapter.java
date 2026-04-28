package com.btg.funds.infrastructure.adapter.out.notification;

import com.btg.funds.domain.model.NotificationChannel;
import com.btg.funds.domain.port.out.NotificationPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@Component
@ConditionalOnProperty(name = "notification.provider", havingValue = "SNS")
public class SnsNotificationAdapter implements NotificationPort {

    private static final Logger LOGGER = LoggerFactory.getLogger(SnsNotificationAdapter.class);

    private final SnsClient snsClient;
    private final String emailTopicArn;

    public SnsNotificationAdapter(
            SnsClient snsClient,
            @Value("${notification.sns.email-topic-arn:}") String emailTopicArn
    ) {
        this.snsClient = snsClient;
        this.emailTopicArn = emailTopicArn;
    }

    @Override
    public void send(String destination, NotificationChannel channel, String message) {
        if (!StringUtils.hasText(destination)) {
            LOGGER.warn("SNS notification skipped. channel={} reason=empty-destination", channel);
            return;
        }

        if (channel == NotificationChannel.EMAIL) {
            if (!StringUtils.hasText(emailTopicArn)) {
                LOGGER.error("SNS EMAIL skipped. reason=missing-topic-arn destination={}", destination);
                return;
            }
            snsClient.publish(PublishRequest.builder()
                    .topicArn(emailTopicArn)
                    .subject("BTG Funds Notification")
                    .message(message)
                    .build());
            LOGGER.info("SNS EMAIL published to topicArn={} recipientHint={}", emailTopicArn, destination);
            return;
        }

        // SMS: envio directo a numero mientras se completa setup formal de cuenta.
        snsClient.publish(PublishRequest.builder()
                .phoneNumber(destination)
                .message(message)
                .build());
        LOGGER.info("SNS SMS published to phone={}", destination);
    }
}
