package user.normalUser;

import java.util.ArrayList;

public final class Subject {
    private final ArrayList<Observer> observers = new ArrayList<>();

    public boolean subscribe(Observer observer) {
        for (Observer observer1 : observers) {
            if (observer1.equals(observer)) {
                observers.remove(observer);
                return false;
            }
        }

        observers.add(observer);
        return true;
    }

    public void notify(Notification notification) {
        for (Observer observer : observers) {
            observer.update(notification);
        }
    }
}
