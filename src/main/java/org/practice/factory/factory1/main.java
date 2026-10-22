package org.practice.factory.factory1;

public class main {
    public static void main(String[] args) throws IllegalAccessException {
        NotificationFactory notificationFactory = new NotificationFactory();

        admin(notificationFactory);
        client(notificationFactory);
    }

    private static void client(NotificationFactory notificationFactory) throws IllegalAccessException {
        Notification whatsapp = notificationFactory.getNotificationType("Whatsapp");
    }

    private static void admin( NotificationFactory notificationFactory) {
        Notification whatsapp = new WhatsApp();

        notificationFactory.registerNotificationType("Whatsapp",whatsapp);
    }
}
