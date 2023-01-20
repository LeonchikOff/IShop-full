package net.ishop.services.interfaces;

import net.ishop.entities.Order;

public interface NotificationService {
    void sendNotificationAboutCreatedNewOrder(String notificationAddress, Order order);
}
