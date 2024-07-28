import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class HomePage implements ActionListener {

    JFrame window = new JFrame("Login");
    JTextField usernameField;
    JPasswordField passwordField;

    String userTable = "user_table"; 

    Color lightBlue = new Color(173, 216, 230); 
    Color lightGreen = new Color(144, 238, 144); 
    Color lightRed = new Color(255, 182, 193); 

    public HomePage() {
        window.setIconImage(Toolkit.getDefaultToolkit().getImage("C:/Users/suren/OneDrive/Pictures/Screenshots/icon_java2.png"));
        window.getContentPane().setBackground(Color.white);
        window.setSize(400, 300);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(null);
        window.setResizable(false);
        window.setLocationRelativeTo(null);

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(50, 50, 80, 25);
        window.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(140, 50, 200, 25);
        window.add(usernameField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(50, 100, 80, 25);
        window.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(140, 100, 200, 25);
        window.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(50, 150, 100, 30);
        loginButton.setActionCommand("Login");
        loginButton.addActionListener(this);
        loginButton.setBackground(lightGreen); 
        window.add(loginButton);

        JButton signupButton = new JButton("Signup");
        signupButton.setBounds(160, 150, 100, 30);
        signupButton.setActionCommand("Signup");
        signupButton.addActionListener(this);
        signupButton.setBackground(lightBlue); 
        window.add(signupButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(270, 150, 100, 30);
        cancelButton.setActionCommand("Cancel");
        cancelButton.addActionListener(this);
        cancelButton.setBackground(lightRed); 
        window.add(cancelButton);

        window.setVisible(true);
        
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        try {
            String url = "jdbc:mysql://localhost:3306/electricity_bills";
            String user = "root";
            String pass = "Satya2356";
            Connection con = DriverManager.getConnection(url, user, pass);
            if (con != null) {
                System.out.println("Connection Successful");

                Statement st = con.createStatement();
                String createTableQuery = "CREATE TABLE IF NOT EXISTS " + userTable + " (Username VARCHAR(50) PRIMARY KEY, Password VARCHAR(50))";
                st.executeUpdate(createTableQuery);
                System.out.println("Table created or already exists.");
            }
        } catch (SQLException ex) {
            System.out.println("Error creating table: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void insertUser(String username, String password) {
        try {
            String url = "jdbc:mysql://localhost:3306/electricity_bills";
            String user = "root";
            String pass = "Satya2356";
            Connection con = DriverManager.getConnection(url, user, pass);
            if (con != null) {
                System.out.println("Connection Successful");

                String sql = "INSERT INTO " + userTable + " (Username, Password) VALUES (?, ?)";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, username);
                ps.setString(2, password);

                int rowsInserted = ps.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("A new user was inserted successfully!");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Login")) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            try {
                String url = "jdbc:mysql://localhost:3306/electricity_bills";
                String user = "root";
                String pass = "Satya2356";
                Connection con = DriverManager.getConnection(url, user, pass);
                if (con != null) {
                    System.out.println("Connection Successful");

                    String sql = "SELECT * FROM " + userTable + " WHERE Username = ? AND Password = ?";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setString(1, username);
                    ps.setString(2, password);

                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(window, "Login Successful");
                        new NewCustomerPage(); // Switch to NewCustomerPage
                        window.dispose(); // Close current window
                    } else {
                        JOptionPane.showMessageDialog(window, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException ex) {
                System.out.println("Error: " + ex.getMessage());
                ex.printStackTrace();
                JOptionPane.showMessageDialog(window, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getActionCommand().equals("Signup")) {
            SignupDialog signupDialog = new SignupDialog(window);
            signupDialog.setVisible(true);
        } else if (e.getActionCommand().equals("Cancel")) {
            window.dispose();
        }
    }

    class SignupDialog extends JDialog implements ActionListener {
        JTextField newUsernameField;
        JPasswordField newPasswordField;

        SignupDialog(Frame parent) {
            super(parent, "Signup", true);
            JPanel panel = new JPanel();
            panel.setBackground(lightBlue); 
            JLabel newUsernameLabel = new JLabel("New Username:");
            newUsernameLabel.setForeground(Color.white); 
            newUsernameField = new JTextField(20);
            JLabel newPasswordLabel = new JLabel("New Password:");
            newPasswordLabel.setForeground(Color.white); 
            newPasswordField = new JPasswordField(20);
            JButton signupButton = new JButton("Signup");
            JButton cancelButton = new JButton("Cancel");

            signupButton.addActionListener(this);
            cancelButton.addActionListener(this);

            panel.add(newUsernameLabel);
            panel.add(newUsernameField);
            panel.add(newPasswordLabel);
            panel.add(newPasswordField);
            panel.add(signupButton);
            panel.add(cancelButton);
            getContentPane().add(panel);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setSize(300, 200);
            setLocationRelativeTo(parent);
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Signup")) {
                String newUsername = newUsernameField.getText().trim();
                String newPassword = new String(newPasswordField.getPassword());

                insertUser(newUsername, newPassword);

                JOptionPane.showMessageDialog(this, "Signup Successful. You can now login.");
                dispose();
            } else if (e.getActionCommand().equals("Cancel")) {
                dispose();
            }
        }
    }

    public static void main(String[] args) {
        new HomePage();
    }
}
