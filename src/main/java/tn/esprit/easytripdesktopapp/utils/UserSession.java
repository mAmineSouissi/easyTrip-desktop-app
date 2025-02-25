package tn.esprit.easytripdesktopapp.utils;

import tn.esprit.easytripdesktopapp.models.User;
import tn.esprit.easytripdesktopapp.services.ServiceUser;

public class UserSession {
    private static UserSession instance;
    private User user;

    private UserSession(User user) {
        this.user = user;
    }

    public static void createSession(User user) {
        if (instance == null) {
            instance = new UserSession(user);
        } else {
            instance.setUser(user);  // Use setUser to update the session
        }
    }

    public static UserSession getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Session has not been created yet.");
        }
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void clearSession() {
        this.user = null;
        instance = null;
    }
}

