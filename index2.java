import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginPage {
    JFrame frame;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                LoginPage window = new LoginPage();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public LoginPage() {
        // Frame setup
        frame = new JFrame("Car Rental System - Login");
        frame.setBounds(100, 100, 400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new GridLayout(4, 2, 10, 10));  // Adjusted padding between components
        frame.getContentPane().setBackground(new Color(255, 182, 193));  // Light pink background

        // Email label and input
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        emailLabel.setForeground(Color.BLACK);  // Black text color
        emailField = new JTextField();
        emailField.setBackground(new Color(255, 255, 255));  // White background for input
        emailField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        frame.getContentPane().add(emailLabel);
        frame.getContentPane().add(emailField);

        // Password label and input
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setForeground(Color.BLACK);  // Black text color
        passwordField = new JPasswordField();
        passwordField.setBackground(new Color(255, 255, 255));  // White background for input
        passwordField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        frame.getContentPane().add(passwordLabel);
        frame.getContentPane().add(passwordField);

        // Login button
        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 0, 0));  // Black background for login button
        loginButton.setForeground(Color.WHITE);  // White text on button
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(e -> login());
        frame.getContentPane().add(loginButton);

        // Register button
        registerButton = new JButton("Register");
        registerButton.setBackground(new Color(0, 0, 0));  // Light pink color for register button
        registerButton.setForeground(Color.WHITE);  // White text on button
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        registerButton.setFocusPainted(false);
        registerButton.addActionListener(e -> openRegistrationPage());
        frame.getContentPane().add(registerButton);
    }

    private void login() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter both email and password.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT role FROM users WHERE email = ? AND password = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, email);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                if (role.equals("user")) {
                    // Open HTML booking page (use desktop browser)
                    openBookingPage();
                } else {
                    // Admin page (if you need to implement one)
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid email or password.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database error: " + e.getMessage());
        }
    }

    private void openBookingPage() {
        try {
            // Open booking page in browser (can use desktop's default browser)
            String url = "file:///D:/carRental/users.html";  // Modify path to point to your HTML file
            Desktop.getDesktop().browse(new java.net.URI(url));
            frame.dispose();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error opening booking page.");
        }
    }

    private void openRegistrationPage() {
        new RegistrationPage().frame.setVisible(true);
        frame.dispose();
    }
}
