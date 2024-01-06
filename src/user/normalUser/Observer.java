package user.normalUser;

public interface Observer {
    /**
     * Receive the current notification
     * @param notification - the notification that a subject wants to send to the observer
     */
    void update(Notification notification);
}
