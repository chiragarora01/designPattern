package org.practice.observer1;

public class SmsObserver implements Observer {
    @Override
    public void update(User u, String event) {
        System.out.println(u.toString() + " " + event);
    }
}
