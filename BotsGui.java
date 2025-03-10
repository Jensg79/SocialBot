import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.io.PrintStream;

public class BotsGui extends JFrame {
    private JTextField intField;
    private JTextField stringField;
    private JTextField postField;
    private JTextArea jsonResponseArea;
    private NetzwerkZugriff socialbotnet;
    private SocialBot bot1;

    // Konstruktor für die GUI
    public BotsGui() {
        super("Die BotArmee für Social Media");
        this.socialbotnet = new NetzwerkZugriff("https://socialbotnet-ajvb.onrender.com");
        bot1 = new SocialBot();

        // Modernes Design und Layout
        initComponents();
        setWindowProperties();

        // Umleiten der Standardausgabe in die TextArea
        redirectSystemStreams();
    }

    // Initialisierung der GUI-Komponenten
    private void initComponents() {
        getContentPane().setBackground(new Color(245, 245, 245)); // Heller Hintergrund
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font standardFont = new Font("SansSerif", Font.PLAIN, 16);
        Color buttonColor = new Color(51, 153, 255);  // Blau für Buttons
        Color textFieldColor = new Color(224, 224, 224);
        Color borderColor = new Color(180, 180, 180);

        // Eingabe Anzahl der Bots
        JLabel label1 = new JLabel("Anzahl der Bots:");
        label1.setFont(standardFont);
        intField = createTextField(standardFont, textFieldColor, borderColor);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(label1, gbc);
        gbc.gridx = 1;
        add(intField, gbc);

        // Button zum Erzeugen der Bots
        JButton button1 = createButton("Erzeuge", standardFont, buttonColor);
        button1.addActionListener(e -> handleBotCreation());
        gbc.gridx = 2;
        add(button1, gbc);

        // Eingabe für HashTag
        JLabel label2 = new JLabel("HashTag:");
        label2.setFont(standardFont);
        stringField = createTextField(standardFont, textFieldColor, borderColor);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(label2, gbc);
        gbc.gridx = 1;
        add(stringField, gbc);

        // Button zum Liken von Posts mit dem Hashtag
        JButton button2 = createButton("Like", standardFont, buttonColor);
        button2.addActionListener(e -> handleHashTagLike());
        gbc.gridx = 2;
        add(button2, gbc);

        // Eingabe für Post
        JLabel labelpost = new JLabel("Posten:");
        labelpost.setFont(standardFont);
        postField = createTextField(standardFont, textFieldColor, borderColor);
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(labelpost, gbc);
        gbc.gridx = 1;
        add(postField, gbc);

        // Button zum Posten von Nachrichten
        JButton button3 = createButton("Speichern", standardFont, buttonColor);
        button3.addActionListener(e -> handlePostCreation());
        gbc.gridx = 2;
        add(button3, gbc);

        // JSON-Antwortbereich
        jsonResponseArea = new JTextArea(10, 30);
        jsonResponseArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        jsonResponseArea.setEditable(false);
        jsonResponseArea.setBorder(BorderFactory.createLineBorder(borderColor));
        JScrollPane scrollPane = new JScrollPane(jsonResponseArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        add(scrollPane, gbc);
    }

    // Methode zum Erstellen eines Textfeldes
    private JTextField createTextField(Font font, Color bgColor, Color borderColor) {
        JTextField textField = new JTextField(10);
        textField.setFont(font);
        textField.setBackground(bgColor);
        textField.setBorder(BorderFactory.createLineBorder(borderColor));
        return textField;
    }

    // Methode zum Erstellen eines Buttons
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
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
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

    // Methode zur Verarbeitung der Bot-Erstellung
    private void handleBotCreation() {
        try {
            int botsAnzahl = Integer.parseInt(intField.getText());
            bot1.armeeErzeugen(botsAnzahl);
            String antwort = socialbotnet.GETAnfrageSenden("/api/users");
            showJsonResponse(antwort);
        } catch (NumberFormatException e) {
            showJsonResponse("Bitte eine gültige Zahl für die Anzahl der Bots eingeben.");
        }
    }

    // Methode zur Verarbeitung von Like-Aktionen für einen HashTag
    private void handleHashTagLike() {
        String eingabe = stringField.getText();
        bot1.hashTagLiken(eingabe);
        showJsonResponse("Alle Posts mit dem Hashtag " + eingabe + " wurden geliked!");
    }

    // Methode zur Verarbeitung des Postens von Nachrichten
    private void handlePostCreation() {
        String post = postField.getText();
        bot1.botsPosten(post);
        showJsonResponse("Der Post '" + post + "' wurde erfolgreich von allen Bots erstellt!");
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

        new BotsGui();
    }
}
