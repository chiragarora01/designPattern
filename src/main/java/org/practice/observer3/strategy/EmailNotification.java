package org.practice.observer3.strategy;

public class EmailNotification implements NotificationStrategy{

    @Override
    public void onNewVideoUploads(String msg) {
        System.out.println("Email "+ msg);
    }
}


