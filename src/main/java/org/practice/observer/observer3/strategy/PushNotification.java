package org.practice.observer.observer3.strategy;

public class PushNotification implements NotificationStrategy{

    @Override
    public void onNewVideoUploads(String msg) {
        System.out.println("push "+msg);
    }
}


