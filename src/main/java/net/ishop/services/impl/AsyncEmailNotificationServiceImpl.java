package net.ishop.services.impl;

import net.framework.annotations.dependency_injection.Autowired;
import net.framework.annotations.dependency_injection.Component;
import net.framework.annotations.dependency_injection.Value;
import net.ishop.entities.Order;
import net.ishop.services.interfaces.NotificationContentBuilderService;
import net.ishop.services.interfaces.NotificationService;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class AsyncEmailNotificationServiceImpl implements NotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncEmailNotificationServiceImpl.class);
    private final ExecutorService executorService;

    @Value(value = "email.smtp.server")
    private String smtpHost;
    @Value(value = "email.smtp.port")
    private String smtpPort;
    @Value(value = "email.smtp.username")
    private String smtpUserName;
    @Value(value = "email.smtp.password")
    private String smtpPassword;
    @Value(value = "email.smtp.fromAddress")
    private String fromAddress;
    @Value(value = "email.smtp.numberOfAttempts")
    private String numberOfAttempts;

    public AsyncEmailNotificationServiceImpl() {
        this.executorService = Executors.newCachedThreadPool();
    }

    @Override
    public void sendNotification(String notificationAddress, String notificationMessage) {
        executorService.submit(new EmailItem(
                notificationAddress,
                "New Order",
                notificationMessage,
                Integer.parseInt(numberOfAttempts)));
    }

    public void close() {
        executorService.shutdown();
    }

    private class EmailItem implements Runnable {
        String notificationAddress;
        String theme;
        String notificationMessage;
        int numberOfAttempts;

        public EmailItem(String notificationAddress, String theme, String notificationMessage, int numberOfAttempts) {
            this.notificationAddress = notificationAddress;
            this.theme = theme;
            this.notificationMessage = notificationMessage;
            this.numberOfAttempts = numberOfAttempts;
        }

        @Override
        public void run() {
            try {
                SimpleEmail email = new SimpleEmail();
                email.setCharset("UTF-8");
                email.setHostName(smtpHost);
                email.setSSLOnConnect(true);
                email.setSslSmtpPort(smtpPort);
                email.setFrom(fromAddress);
                email.setAuthentication(smtpUserName, smtpPassword);
                email.setSubject(theme);
                email.setMsg(notificationMessage);
                email.addTo(notificationAddress);
                email.send();
            } catch (EmailException e) {
                LOGGER.error("Can't send email: " + e.getMessage(), e);
                numberOfAttempts--;
                if (numberOfAttempts > 0) {
                    LOGGER.info("Resend email: {}", this.toString());
                    executorService.submit(this);
                } else {
                    LOGGER.error("Email was not sent: limit of try count");
                }
            } catch (Exception e) {
                LOGGER.error("Error during send email: " + e.getMessage(), e);
            }
        }
    }
}
