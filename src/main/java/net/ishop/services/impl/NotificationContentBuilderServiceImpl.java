package net.ishop.services.impl;

import net.framework.annotations.dependency_injection.Component;
import net.framework.annotations.dependency_injection.Value;
import net.ishop.entities.Order;
import net.ishop.services.interfaces.NotificationContentBuilderService;

@Component
public class NotificationContentBuilderServiceImpl implements NotificationContentBuilderService {
    @Value(value = "app.host")
    private String host;

    @Override
    public String buildNotificationMessageAboutCreatedNewOrder(Order order) {
        return host + "/order?id=" + order.getId();
    }
}
