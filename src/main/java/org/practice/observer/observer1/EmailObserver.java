package org.practice.observer.observer1;

public class EmailObserver implements Observer {
    @Override
    public void update(User u, String event) {
        System.out.println(u.toString() + " " + event);
    }
}


