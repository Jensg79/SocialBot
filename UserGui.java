import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.io.PrintStream;

public class UserGui extends JFrame {
    private JTextField usernameField, postField, pinnwandUsernameField, nachrichtField;
    private JPasswordField passwordField;
    private JTextArea jsonResponseArea, outputArea;
    private user userObj;

    public UserGui() {
        super("SocialBot – Nutzerverwaltung");
        userObj = new user();

        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(createInputPanel(), BorderLayout.NORTH);
        add(createButtonPanel(), BorderLayout.CENTER);
        add(createOutputPanel(), BorderLayout.SOUTH);

        redirectSystemStreams();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Eingaben"));

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        pinnwandUsernameField = new JTextField();
        nachrichtField = new JTextField();

        panel.add(new JLabel("Benutzername:"));
        panel.add(usernameField);
        panel.add(new JLabel("Passwort:"));
        panel.add(passwordField);
        panel.add(new JLabel("Nachricht:"));
        panel.add(nachrichtField);
        panel.add(new JLabel("Pinnwand von:"));
        panel.add(pinnwandUsernameField);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        panel.add(createButton("Registrieren", e -> handleRegistration()));
        panel.add(createButton("Posten", e -> handlePostCreation()));
        panel.add(createButton("Pinnwand anzeigen", e -> handlePinnwandAnzeigen()));
        panel.add(createButton("Alle Nutzer anzeigen", e -> handleAlleNutzerAnzeigen()));

        return panel;
    }

    private JPanel createOutputPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setBorder(BorderFactory.createTitledBorder("Ausgabe"));

        outputArea = new JTextArea(5, 40);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        outputArea.setEditable(false);

        jsonResponseArea = new JTextArea(5, 40);
        jsonResponseArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        jsonResponseArea.setEditable(false);

        panel.add(new JScrollPane(outputArea));
        panel.add(new JScrollPane(jsonResponseArea));

        return panel;
    }

    private JButton createButton(String text, ActionListener action) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        //btn.setBackground(new Color(224, 224, 224));
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.addActionListener(action);
        return btn;
    }

    private void handleRegistration() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        userObj.nutzerAnmelden(username, password);
        showOutput("Nutzer " + username + " wurde erfolgreich registriert.");
    }

    private void handlePostCreation() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String nachricht = nachrichtField.getText();
        userObj.posten(username, password, nachricht);
        showOutput("Nachricht wurde gepostet: " + nachricht);
    }

    private void handlePinnwandAnzeigen() {
        String username = pinnwandUsernameField.getText();
        String jsonResponse = userObj.pinnwandEinesNutzers(username);
        showJsonResponse(jsonResponse);
        showOutput("Pinnwand von Nutzer " + username + " wurde angezeigt.");
    }

    private void handleAlleNutzerAnzeigen() {
        userObj.alleNutzerAnzeigen();
        showOutput("Alle registrierten Nutzer wurden angezeigt.");
    }

    private void showOutput(String message) {
        outputArea.setText(message);
    }

    private void showJsonResponse(String response) {
        jsonResponseArea.setText(response);
    }

    private void redirectSystemStreams() {
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) {
                jsonResponseArea.append(String.valueOf((char) b));
                jsonResponseArea.setCaretPosition(jsonResponseArea.getDocument().getLength());
            }
        };
        System.setOut(new PrintStream(out));
        System.setErr(new PrintStream(out));
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new UserGui();
    }
}