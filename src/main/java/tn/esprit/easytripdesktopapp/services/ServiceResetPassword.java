package tn.esprit.easytripdesktopapp.services;

import org.mindrot.jbcrypt.BCrypt;
import tn.esprit.easytripdesktopapp.utils.MyDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;


public class ServiceResetPassword {
    private final ServiceUser userService;
    private final Connection cnx;
    // Store verification codes temporarily (in a real-world app, consider storing these in the database)
    private String verificationCode;
    private String userEmail;


    // Define code expiration time (in minutes)
    private static final int CODE_EXPIRATION_MINUTES = 15;

    public ServiceResetPassword() {
        this.userService = new ServiceUser();
        this.cnx = MyDataBase.getInstance().getCnx();
    }

    /**
     * Generate a random 6-digit verification code
     * @return The generated verification code
     */
    public String generateResetCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    /**
     * Store verification code in the database
     * @param email User's email
     * @param code Verification code
     */
    private void storeVerificationCode(String email, String code) {
        // Calculate expiration time
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(CODE_EXPIRATION_MINUTES);

        // Delete any existing codes for this email first
        String deleteQuery = "DELETE FROM verification_codes WHERE email = ?";

        // Insert new code
        String insertQuery = "INSERT INTO verification_codes (email, code, expires_at) VALUES (?, ?, ?)";

        try {
            // First delete existing records
            PreparedStatement deleteStmt = cnx.prepareStatement(deleteQuery);
            deleteStmt.setString(1, email);
            deleteStmt.executeUpdate();

            // Then insert new code
            PreparedStatement insertStmt = cnx.prepareStatement(insertQuery);
            insertStmt.setString(1, email);
            insertStmt.setString(2, code);
            insertStmt.setTimestamp(3, Timestamp.valueOf(expiresAt));
            insertStmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error storing verification code: " + e.getMessage());
        }
    }

    /**
     * Verify the code entered by the user
     * @param email The user's email
     * @param enteredCode The code entered by the user
     * @return true if the code is valid, false otherwise
     */
    public boolean verifyCode(String email, String enteredCode) {
        String query = "SELECT * FROM `verification_codes` WHERE `email` = ? AND `code` = ? AND `expires_at` > NOW() AND `used` = FALSE";

        try {
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, enteredCode);

            ResultSet rs = stmt.executeQuery();
//            storeVerificationCode(email, enteredCode);

            if (rs.next()) {
                // Code is valid, mark it as used
                String updateQuery = "UPDATE `verification_codes` SET `used` = TRUE WHERE `email` = ?";
                PreparedStatement updateStmt = cnx.prepareStatement(updateQuery);
                updateStmt.setString(1, email);
                updateStmt.executeUpdate();

                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error verifying code: " + e.getMessage());
        }

        return false;
    }

    /**
     * Reset the user's password
     * @param email The user's email
     * @param newPassword The new password
     * @return true if the password was reset successfully, false otherwise
     */
    public boolean resetPassword(String email, String newPassword) {
        if (email == null) {
            return false;
        }

        // Check if there's a verified code for this email
        String checkQuery = "SELECT * FROM `verification_codes` WHERE `email` = ? AND `used` = TRUE";

        try {
            PreparedStatement checkStmt = cnx.prepareStatement(checkQuery);
            checkStmt.setString(1, email);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                // No verified code found
                return false;
            }

            // Hash the new password
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

            // Update the password
            boolean updated = userService.updatePassword(email, hashedPassword);

            if (updated) {
                // Clean up the verification code
                String deleteQuery = "DELETE FROM `verification_codes` WHERE `email` = ?";
                PreparedStatement deleteStmt = cnx.prepareStatement(deleteQuery);
                deleteStmt.setString(1, email);
                deleteStmt.executeUpdate();

                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error in password reset: " + e.getMessage());
        }

        return false;
    }

    /**
     * Check if passwords match
     * @param password The password
     * @param confirmPassword The confirmation password
     * @return true if passwords match, false otherwise
     */
    public boolean passwordsMatch(String password, String confirmPassword) {
        return password != null && password.equals(confirmPassword);
    }

    public boolean sendVerificationCode(String email) {
        if (!userService.userExistsByEmail(email)) {
            return false;
        }

        // Generate a verification code
        this.verificationCode = generateResetCode();
        this.userEmail = email;

        // Email configuration
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Email credentials (use your application's email and password/app password)
        final String senderEmail = "aminesouissi681@gmail.com"; // Replace with your email
        final String password = "cimh ylri oahd pvlz"; // Replace with your app password

        // Create a session with authentication
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, password);
            }
        });

        try {
            // Create a message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Password Reset Verification Code");

            // Create the email body
            String emailBody = "Dear User,\n\n"
                    + "We received a request to reset your password for your Easy Trip account.\n"
                    + "Please use the following verification code to complete the password reset process:\n\n"
                    + "Verification Code: " + verificationCode + "\n\n"
                    + "If you did not request a password reset, please ignore this email or contact our support team.\n\n"
                    + "Thank you,\n"
                    + "Easy Trip Team";

            message.setText(emailBody);

            // Send the message
            Transport.send(message);

            System.out.println("Verification code sent to: " + email);
            storeVerificationCode(email, verificationCode);
            System.out.println("Code Stored Successfully: " + verificationCode);
            return true;
        } catch (MessagingException e) {
            System.out.println("Error sending verification code: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}