package org.practice.observer.observer1;

public class Main {
    public static void main(String[] args) {
        EventManager eventManager = new EventManager();
        User u = new User("1", "name");
        User u2 = new User("3", "name");
        User u3 = new User("2", "name");
        User u4 = new User("4", "name");

        eventManager.addAlert(u, new Alert(u, "INFY", 1600, new EmailObserver()));
        eventManager.addAlert(u2, new Alert(u2, "INFY", 1600, new SmsObserver()));
        eventManager.addAlert(u3, new Alert(u3, "INFY", 1600, new EmailObserver()));
        eventManager.addAlert(u4, new Alert(u4, "INFY", 1600, new EmailObserver()));

        long endTime = System.currentTimeMillis() + 10;
        while (System.currentTimeMillis() < endTime) {
            eventManager.onTick("INFY", Math.random() * 3300);
        }

        eventManager.removeAlert(u4);

        endTime = System.currentTimeMillis() + 10;
        while (System.currentTimeMillis() < endTime) {
            eventManager.onTick("INFY", Math.random() * 3300);
        }
    }
}
