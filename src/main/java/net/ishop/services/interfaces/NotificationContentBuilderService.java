package net.ishop.services.interfaces;

import net.ishop.entities.Order;

public interface NotificationContentBuilderService {
    String buildNotificationMessageAboutCreatedNewOrder(Order order);
}
