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
        // Fenstername setzen
        super("Die BotArmee für Social Media");
        this.socialbotnet = new NetzwerkZugriff("https://socialbotnet-ajvb.onrender.com");

        // SocialBot-Objekt
        bot1 = new SocialBot();

        // Modernes Design: Hellgrauer Hintergrund, dunklere Schrift
        getContentPane().setBackground(new Color(245, 245, 245)); // Heller, neutraler Hintergrund
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Mehr Platz zwischen den Komponenten
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Standardfont für alle Komponenten
        Font standardFont = new Font("SansSerif", Font.PLAIN, 16);

        // Farben für Buttons und Textfelder
        Color buttonColor = new Color(51, 153, 255);  // Leuchtendes Blau für Buttons
        Color textFieldColor = new Color(224, 224, 224);  // Leichtes Grau für Textfelder
        Color borderColor = new Color(180, 180, 180);  // Dezentes Grau für Ränder

        // Label und erstes Textfeld (Anzahl der Bots)
        JLabel label1 = new JLabel("Anzahl der Bots:");
        label1.setFont(standardFont);
        intField = new JTextField(10);
        intField.setFont(standardFont);
        intField.setBackground(textFieldColor);
        intField.setBorder(BorderFactory.createLineBorder(borderColor));

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(label1, gbc);

        gbc.gridx = 1;
        add(intField, gbc);

        // Erster Button (Erzeuge)
        JButton button1 = new JButton("Erzeuge");
        button1.setFont(standardFont);
        button1.setBackground(buttonColor);
        button1.setForeground(Color.WHITE);  // Weißer Text
        button1.setFocusPainted(false);  // Entferne den Fokusrahmen
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int botsAnzahl = Integer.parseInt(intField.getText());
                bot1.armeeErzeugen(botsAnzahl);
                String antwort = socialbotnet.GETAnfrageSenden("/api/users");
                showJsonResponse(antwort);
            }
        });

        gbc.gridx = 2;
        add(button1, gbc);

        // Label und zweites Textfeld (HashTag)
        JLabel label2 = new JLabel("HashTag:");
        label2.setFont(standardFont);
        stringField = new JTextField(10);
        stringField.setFont(standardFont);
        stringField.setBackground(textFieldColor);
        stringField.setBorder(BorderFactory.createLineBorder(borderColor));

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(label2, gbc);

        gbc.gridx = 1;
        add(stringField, gbc);

        // Zweiter Button (Like)
        JButton button2 = new JButton("Like");
        button2.setFont(standardFont);
        button2.setBackground(buttonColor);
        button2.setForeground(Color.WHITE);
        button2.setFocusPainted(false);
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String eingabe = stringField.getText();
                bot1.hashTagLiken(eingabe);
                String antwort = "Alle Posts mit dem Hashtag " + eingabe + " wurden geliked!";
                displayJsonResponseLikes(antwort);
            }
        });

        gbc.gridx = 2;
        add(button2, gbc);

        // Posten-Bereich (Label, Textfeld, Button)
        JLabel labelpost = new JLabel("Posten:");
        labelpost.setFont(standardFont);
        postField = new JTextField(10);
        postField.setFont(standardFont);
        postField.setBackground(textFieldColor);
        postField.setBorder(BorderFactory.createLineBorder(borderColor));

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(labelpost, gbc);

        gbc.gridx = 1;
        add(postField, gbc);

        // Dritter Button (Speichern)
        JButton button3 = new JButton("Speichern");
        button3.setFont(standardFont);
        button3.setBackground(buttonColor);
        button3.setForeground(Color.WHITE);
        button3.setFocusPainted(false);
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String post = postField.getText();
                bot1.botsPosten(post);
                String antwort = "Der Post '" + post + "' wurde erfolgreich von allen Bots erstellt!";
                displayJsonResponseLikes(antwort);
            }
        });

        gbc.gridx = 2;
        add(button3, gbc);

        // JSON-Antwortbereich (TextArea mit Scrollbar)
        jsonResponseArea = new JTextArea(10, 30);
        jsonResponseArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); // Monospace für JSON-Text
        jsonResponseArea.setEditable(false);
        jsonResponseArea.setBorder(BorderFactory.createLineBorder(borderColor));

        JScrollPane scrollPane = new JScrollPane(jsonResponseArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        add(scrollPane, gbc);
        
        // Umleiten der Standardausgabe in die TextArea
        redirectSystemStreams();

        // Grundlegende Fenstereinstellungen
        setSize(600, 400); // Größerer Raum für die moderne Darstellung
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

        // Methode zum Umleiten der Standardausgabe (System.out und System.err)
    private void redirectSystemStreams() {
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) {
                // Einzelne Bytes in die TextArea schreiben
                jsonResponseArea.append(String.valueOf((char) b));
                jsonResponseArea.setCaretPosition(jsonResponseArea.getDocument().getLength()); // Scrollt automatisch nach unten
            }
        };

        PrintStream ps = new PrintStream(out);
        System.setOut(ps);
        System.setErr(ps);
    }
    
    // Beispielmethoden zum Anzeigen von JSON-Antworten
    private void showJsonResponse(String response) {
        jsonResponseArea.setText(response);
    }

    private void displayJsonResponseLikes(String response) {
        jsonResponseArea.setText(response);
    }

    // Main-Methode zum Starten der GUI
    public static void main(String[] args) {
        // Nutzen von Nimbus Look-and-Feel für modernes Design (optional)
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

        new BotsGui();  // Erstellen des GUI-Fensters
    }
}
