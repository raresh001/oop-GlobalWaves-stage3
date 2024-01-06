package user.normalUser;

import java.util.ArrayList;

public final class Subject {
    private final ArrayList<Observer> observers = new ArrayList<>();

    /**
     * subscribe/unsubscribe to the current subject
     * @param observer - the subscriber
     * @return - if the observer is added to the list
     */
    public boolean subscribe(final Observer observer) {
        for (Observer observer1 : observers) {
            if (observer1.equals(observer)) {
                observers.remove(observer);
                return false;
            }
        }

        observers.add(observer);
        return true;
    }

    /**
     * @param notification - the notification that is sent to all the observers
     */
    public void notify(final Notification notification) {
        for (Observer observer : observers) {
            observer.update(notification);
        }
    }
}
