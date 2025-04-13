import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.io.PrintStream;

public class BotsGui extends JFrame {
    private JTextField intField, stringField, postField;
    private JTextArea jsonResponseArea;
    private NetzwerkZugriff socialbotnet;
    private SocialBot bot1;

    public BotsGui() {
        super("Bot-Armee für Social Media");
        this.socialbotnet = new NetzwerkZugriff("https://socialbotnet-hg69.onrender.com");
        bot1 = new SocialBot();

        initComponents();
        setWindowProperties();
        redirectSystemStreams();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        intField = new JTextField(10);
        stringField = new JTextField(10);
        postField = new JTextField(10);
        jsonResponseArea = new JTextArea(10, 30);
        jsonResponseArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(jsonResponseArea);

        JButton botCreateButton = createButton("Bots erzeugen", e -> handleBotCreation());
        JButton likeButton = createButton("Like Posts", e -> handleHashTagLike());
        JButton postButton = createButton("Posten", e -> handlePostCreation());

        addComponent(new JLabel("Anzahl der Bots:"), 0, 0, gbc);
        addComponent(intField, 1, 0, gbc);
        addComponent(botCreateButton, 2, 0, gbc);

        addComponent(new JLabel("Hashtag:"), 0, 1, gbc);
        addComponent(stringField, 1, 1, gbc);
        addComponent(likeButton, 2, 1, gbc);

        addComponent(new JLabel("Post:"), 0, 2, gbc);
        addComponent(postField, 1, 2, gbc);
        addComponent(postButton, 2, 2, gbc);

        gbc.gridwidth = 3;
        addComponent(scrollPane, 0, 3, gbc);
    }

    private JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(new Color(41, 128, 185));
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setBorderPainted(false);
        return button;
    }

    private void addComponent(Component comp, int x, int y, GridBagConstraints gbc) {
        gbc.gridx = x;
        gbc.gridy = y;
        add(comp, gbc);
    }

    private void setWindowProperties() {
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
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

    private void handleBotCreation() {
        try {
            int botsAnzahl = Integer.parseInt(intField.getText());
            bot1.armeeErzeugen(botsAnzahl);
            showJsonResponse(socialbotnet.GETAnfrageSenden("/api/users"));
        } catch (NumberFormatException e) {
            showJsonResponse("Bitte eine gültige Zahl eingeben.");
        }
    }

    private void handleHashTagLike() {
        String eingabe = stringField.getText();
        bot1.hashTagLiken(eingabe);
        showJsonResponse("Alle Posts mit Hashtag " + eingabe + " geliked!");
    }

    private void handlePostCreation() {
        String post = postField.getText();
        bot1.botsPosten(post);
        showJsonResponse("Post erstellt: " + post);
    }

    private void showJsonResponse(String response) {
        jsonResponseArea.setText(response);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BotsGui::new);
    }
}
