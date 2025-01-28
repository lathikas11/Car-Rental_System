import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class RegistrationForm {
    private JFrame frame;
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JComboBox<String> carComboBox;
    private JButton rentButton;
    private JTextArea carDetailsArea;
    private List<Car> cars = new ArrayList<>();  // A list to hold available cars

    public static void main(String[] args) {
        try {
            // Ensure tables are created before the app starts
            DBConnection.createTablesIfNotExists();

            EventQueue.invokeLater(() -> {
                try {
                    RegistrationForm window = new RegistrationForm();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public RegistrationForm() throws ClassNotFoundException {
        frame = new JFrame("Car Rental System");
        frame.setBounds(100, 100, 600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.getContentPane().setBackground(new Color(240, 240, 240));  // Light gray background

        // Add header label
        JLabel headerLabel = new JLabel("Car Rental System", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(new Color(70, 130, 180));  // Steel blue color
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        frame.getContentPane().add(headerLabel);
        frame.getContentPane().add(Box.createVerticalStrut(20)); // Vertical space

        // Name field
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        namePanel.setBackground(new Color(240, 240, 240));
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        nameLabel.setForeground(new Color(50, 50, 50));
        nameField = new JTextField(20);
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        nameField.setBackground(new Color(255, 255, 255));
        nameField.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180)));
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        frame.getContentPane().add(namePanel);

        // Email field
        JPanel emailPanel = new JPanel();
        emailPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        emailPanel.setBackground(new Color(240, 240, 240));
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emailLabel.setForeground(new Color(50, 50, 50));
        emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setBackground(new Color(255, 255, 255));
        emailField.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180)));
        emailPanel.add(emailLabel);
        emailPanel.add(emailField);
        frame.getContentPane().add(emailPanel);

        // Password field
        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        passwordPanel.setBackground(new Color(240, 240, 240));
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setForeground(new Color(50, 50, 50));
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBackground(new Color(255, 255, 255));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180)));
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        frame.getContentPane().add(passwordPanel);

        // Register button
        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(70, 130, 180));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.addActionListener(e -> {
            try {
                registerUser();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });
        frame.getContentPane().add(registerButton);
        frame.getContentPane().add(Box.createVerticalStrut(20)); // Vertical space

        // Car selection label
        JLabel carLabel = new JLabel("Select Car to Rent:");
        carLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        carLabel.setForeground(new Color(50, 50, 50));
        carLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        frame.getContentPane().add(carLabel);

        // Car selection combo box
        carComboBox = new JComboBox<>();
        carComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        carComboBox.setBackground(Color.WHITE);
        carComboBox.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180)));
        loadCars();  // Populate combo box with available cars
        carComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        carComboBox.addActionListener(e -> displayCarDetails()); // Display car details when selection changes
        frame.getContentPane().add(carComboBox);
        frame.getContentPane().add(Box.createVerticalStrut(10)); // Vertical space

        // Car details area
        carDetailsArea = new JTextArea(5, 30);
        carDetailsArea.setFont(new Font("Arial", Font.PLAIN, 14));
        carDetailsArea.setEditable(false);
        carDetailsArea.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180)));
        frame.getContentPane().add(new JScrollPane(carDetailsArea));

        frame.getContentPane().add(Box.createVerticalStrut(20)); // Vertical space

        // Rent button
        rentButton = new JButton("Rent Car");
        rentButton.setFont(new Font("Arial", Font.BOLD, 14));
        rentButton.setBackground(new Color(34, 139, 34));  // Forest green color
        rentButton.setForeground(Color.WHITE);
        rentButton.setFocusPainted(false);
        rentButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        rentButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        rentButton.addActionListener(e -> {
            try {
                rentCar();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });
        frame.getContentPane().add(rentButton);
    }

    private void registerUser() throws ClassNotFoundException {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "All fields are required!");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String query = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, name);
            pst.setString(2, email);
            pst.setString(3, password);

            int result = pst.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(frame, "Registration successful!");
            } else {
                JOptionPane.showMessageDialog(frame, "Error: Registration failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database error: " + e.getMessage());
        }
    }

    private void loadCars() throws ClassNotFoundException {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT id, model, year, rental_price FROM cars";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("id");
                String model = rs.getString("model");
                int year = rs.getInt("year");
                double rentalPrice = rs.getDouble("rental_price");
                cars.add(new Car(id, model, year, rentalPrice));
                carComboBox.addItem(model);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayCarDetails() {
        int selectedCarIndex = carComboBox.getSelectedIndex();
        if (selectedCarIndex >= 0) {
            Car selectedCar = cars.get(selectedCarIndex);
            String carDetails = "Model: " + selectedCar.getModel() + "\n" +
                                 "Year: " + selectedCar.getYear() + "\n" +
                                 "Rental Price: $" + selectedCar.getRentalPrice() + " per day";
            carDetailsArea.setText(carDetails);
        }
    }

    private void rentCar() throws ClassNotFoundException {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please register before renting a car!");
            return;
        }

        int selectedCarIndex = carComboBox.getSelectedIndex();
        if (selectedCarIndex == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a car to rent!");
            return;
        }

        Car selectedCar = cars.get(selectedCarIndex);

        try (Connection conn = DBConnection.getConnection()) {
            // First, check if the user exists or register them
            String userQuery = "SELECT id FROM users WHERE email = ?";
            PreparedStatement pst = conn.prepareStatement(userQuery);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();

            int userId = -1;
            if (rs.next()) {
                userId = rs.getInt("id");
            } else {
                // Register the user if not found
                String insertUserQuery = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
                pst = conn.prepareStatement(insertUserQuery, Statement.RETURN_GENERATED_KEYS);
                pst.setString(1, name);
                pst.setString(2, email);
                pst.setString(3, password);
                pst.executeUpdate();
                rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    userId = rs.getInt(1);
                }
            }

            // Rent the car
            String rentQuery = "INSERT INTO rentals (user_id, car_id, rental_date) VALUES (?, ?, NOW())";
            pst = conn.prepareStatement(rentQuery);
            pst.setInt(1, userId);
            pst.setInt(2, selectedCar.getId());
            pst.executeUpdate();

            JOptionPane.showMessageDialog(frame, "Car rental successful!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database error: " + e.getMessage());
        }
    }

    // Inner class for Car
    class Car {
        private int id;
        private String model;
        private int year;
        private double rentalPrice;

        public Car(int id, String model, int year, double rentalPrice) {
            this.id = id;
            this.model = model;
            this.year = year;
            this.rentalPrice = rentalPrice;
        }

        public int getId() {
            return id;
        }

        public String getModel() {
            return model;
        }

        public int getYear() {
            return year;
        }

        public double getRentalPrice() {
            return rentalPrice;
        }
    }
}
