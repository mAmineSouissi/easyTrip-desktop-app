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
            // Ensure password is set before creating the session
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                ServiceUser serviceUser = new ServiceUser();
                User userFromDB = serviceUser.getById(user.getId());
                if (userFromDB != null) {
                    user.setPassword(userFromDB.getPassword());
                } else {
                    System.out.println("Error: User not found in the database!");
                }
            }
            instance = new UserSession(user);
        } else {
            instance.setUser(user);  // Use setUser to update the user session
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
        instance = null;
    }
}
