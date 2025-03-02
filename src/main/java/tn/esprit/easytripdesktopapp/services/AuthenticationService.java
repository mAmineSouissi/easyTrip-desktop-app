package tn.esprit.easytripdesktopapp.services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.mindrot.jbcrypt.BCrypt;
import tn.esprit.easytripdesktopapp.models.AccountType;
import tn.esprit.easytripdesktopapp.models.User;
import tn.esprit.easytripdesktopapp.utils.GoogleOAuthConfig;

import java.util.Map;

public class AuthenticationService extends Service<User> {

    private final ServiceUser serviceUser = new ServiceUser();

    @Override
    protected Task<User> createTask() {
        return new Task<User>() {
            @Override
            protected User call() throws Exception {
                try {
                    Map<String, Object> userInfo = GoogleOAuthConfig.getUserInfo();

                    // Extract user information from Google response
                    String email = (String) userInfo.get("email");
                    String name = (String) userInfo.get("given_name");
                    String surname = (String) userInfo.get("family_name");
                    String picture = (String) userInfo.get("picture");
                    Boolean emailVerified = (Boolean) userInfo.get("email_verified");

                    // Check if user exists in our database
                    User existingUser = serviceUser.getByEmail(email);

                    if (existingUser != null) {
                        // User exists, return the user
                        return existingUser;
                    } else {
                        // User doesn't exist, create a new one
                        User newUser = new User();
                        newUser.setEmail(email);
                        newUser.setName(name != null ? name : "user");
                        newUser.setSurname(surname != null ? surname : "user");
                        // Set a random password or leave it blank based on your security policy
                        String password="record";
                        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                        newUser.setPassword(hashedPassword);
                        newUser.setRole(AccountType.Client.name());
                        newUser.setProfilePhoto(picture);
                        newUser.setPhone("CHANGE ME");
                        newUser.setAddress("CHANGE ME");

                        // Save the new user to database
                        serviceUser.add(newUser);

                        // Fetch the newly created user with its ID
                        return serviceUser.getByEmail(email);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new Exception("Authentication failed: " + e.getMessage());
                }
            }
        };
    }
}
