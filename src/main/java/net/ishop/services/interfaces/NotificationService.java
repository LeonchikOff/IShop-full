package net.ishop.services.interfaces;

import net.ishop.entities.Order;

public interface NotificationService {
    void sendNotification(String notificationAddress, String notificationMessage);
}
