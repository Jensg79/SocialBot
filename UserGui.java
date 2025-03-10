import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.io.PrintStream;

public class UserGui extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField postField;
    private JTextArea jsonResponseArea;
    private JTextField pinnwandUsernameField;
    private JTextArea outputArea;
    private user userObj;

    // Konstruktor zur Initialisierung der GUI
    public UserGui() {
        super("Social Media Bot - Nutzerverwaltung");
        userObj = new user();

        // Festlegen des Layouts und grundlegendes Design
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        Font standardFont = new Font("SansSerif", Font.PLAIN, 16);
        Color buttonColor = new Color(51, 153, 255);
        Color textFieldColor = new Color(224, 224, 224);
        Color borderColor = new Color(180, 180, 180);

        // Label und Eingabe für Benutzername
        JLabel usernameLabel = new JLabel("Benutzername:");
        usernameLabel.setFont(standardFont);
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(200, 30));
        usernameField.setFont(standardFont);
        usernameField.setBackground(textFieldColor);
        usernameField.setBorder(BorderFactory.createLineBorder(borderColor));
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(usernameLabel, gbc);
        gbc.gridx = 1;
        add(usernameField, gbc);

        // Label und Eingabe für Passwort
        JLabel passwordLabel = new JLabel("Passwort:");
        passwordLabel.setFont(standardFont);
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 30));
        passwordField.setFont(standardFont);
        passwordField.setBackground(textFieldColor);
        passwordField.setBorder(BorderFactory.createLineBorder(borderColor));
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(passwordLabel, gbc);
        gbc.gridx = 1;
        add(passwordField, gbc);

        // Button zum Registrieren des Nutzers
        JButton registerButton = createButton("Registrieren", standardFont, buttonColor);
        registerButton.addActionListener(e -> handleRegistration());
        gbc.gridx = 2;
        gbc.gridy = 1;
        add(registerButton, gbc);

        // Label und Eingabe für Pinnwand
        JLabel pinnwandLabel = new JLabel("Pinnwand anzeigen:");
        pinnwandLabel.setFont(standardFont);
        pinnwandUsernameField = new JTextField();
        pinnwandUsernameField.setPreferredSize(new Dimension(200, 30));
        pinnwandUsernameField.setFont(standardFont);
        pinnwandUsernameField.setBackground(textFieldColor);
        pinnwandUsernameField.setBorder(BorderFactory.createLineBorder(borderColor));
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(pinnwandLabel, gbc);
        gbc.gridx = 1;
        add(pinnwandUsernameField, gbc);

        // Button zum Anzeigen der Pinnwand eines Nutzers
        JButton pinnwandButton = createButton("Pinnwand anzeigen", standardFont, buttonColor);
        pinnwandButton.addActionListener(e -> handlePinnwandAnzeigen());
        gbc.gridx = 2;
        add(pinnwandButton, gbc);

        // Label und Eingabe für Posts
        JLabel postLabel = new JLabel("Post Nachricht:");
        postLabel.setFont(standardFont);
        postField = new JTextField();
        postField.setPreferredSize(new Dimension(200, 30));
        postField.setFont(standardFont);
        postField.setBackground(textFieldColor);
        postField.setBorder(BorderFactory.createLineBorder(borderColor));
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(postLabel, gbc);
        gbc.gridx = 1;
        add(postField, gbc);

        // Button zum Posten einer Nachricht
        JButton postButton = createButton("Posten", standardFont, buttonColor);
        postButton.addActionListener(e -> handlePostCreation());
        gbc.gridx = 2;
        add(postButton, gbc);

        // Button zum Anzeigen aller Nutzer
        JButton alleNutzerButton = createButton("Alle Nutzer anzeigen", standardFont, buttonColor);
        alleNutzerButton.addActionListener(e -> handleAlleNutzerAnzeigen());
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        add(alleNutzerButton, gbc);

        // Ausgabe-Bereich
        outputArea = new JTextArea(10, 30);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        outputArea.setEditable(false);
        outputArea.setBorder(BorderFactory.createLineBorder(borderColor));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(scrollPane, gbc);

        // TextArea für die Anzeige von JSON-Antworten
        jsonResponseArea = new JTextArea(10, 30);
        jsonResponseArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        jsonResponseArea.setEditable(false);
        jsonResponseArea.setBorder(BorderFactory.createLineBorder(borderColor));
        JScrollPane jsonScrollPane = new JScrollPane(jsonResponseArea);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(jsonScrollPane, gbc);

        // System.out und System.err umleiten
        redirectSystemStreams();

        setWindowProperties();
    }

    // Erstellen eines Buttons mit einheitlichem Design
    private JButton createButton(String text, Font font, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    // Grundeinstellungen für das Fenster
    private void setWindowProperties() {
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Methode zur Verarbeitung der Nutzerregistrierung
    private void handleRegistration() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        userObj.nutzerAnmelden(username, password);
        showOutput("Nutzer " + username + " wurde erfolgreich registriert.");
    }

    // Methode zur Verarbeitung des Postens von Nachrichten
    private void handlePostCreation() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String nachricht = postField.getText();
        userObj.posten(username, password, nachricht);
        showOutput("Nachricht wurde gepostet: " + nachricht);
    }

    // Methode zum Anzeigen der Pinnwand eines Nutzers
    private void handlePinnwandAnzeigen() {
        String username = pinnwandUsernameField.getText();
        String jsonResponse = userObj.pinnwandEinesNutzers(username);
        showJsonResponse(jsonResponse);  // JSON in der jsonResponseArea anzeigen
        showOutput("Pinnwand von Nutzer " + username + " wurde angezeigt.");
    }

    // Methode zum Anzeigen aller Nutzer
    private void handleAlleNutzerAnzeigen() {
        userObj.alleNutzerAnzeigen();
        showOutput("Alle registrierten Nutzer wurden angezeigt.");
    }

    // Methode zum Anzeigen von Ausgaben in der TextArea
    private void showOutput(String message) {
        outputArea.setText(message);
    }

    // Methode zum Umleiten der Standardausgabe in die JTextArea
    private void redirectSystemStreams() {
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) {
                jsonResponseArea.append(String.valueOf((char) b));
                jsonResponseArea.setCaretPosition(jsonResponseArea.getDocument().getLength()); // Automatisches Scrollen
            }
        };
        System.setOut(new PrintStream(out));
        System.setErr(new PrintStream(out));
    }

    // Methode zum Anzeigen von JSON-Antworten
    private void showJsonResponse(String response) {
        jsonResponseArea.setText(response);
    }

    // Main-Methode zum Starten der GUI
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        new UserGui();
    }
}