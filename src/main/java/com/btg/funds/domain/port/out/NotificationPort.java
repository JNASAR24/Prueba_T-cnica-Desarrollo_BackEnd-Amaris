package com.btg.funds.domain.port.out;

import com.btg.funds.domain.model.NotificationChannel;

public interface NotificationPort {
    void send(String destination, NotificationChannel channel, String message);
}
