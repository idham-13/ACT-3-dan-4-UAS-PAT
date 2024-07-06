import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class CreateMenuApp {
    public static void showCreateMenu() {
        JFrame frame = new JFrame("Create Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // Center the frame on the screen
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel, frame);

        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel, JFrame frame) {
        panel.setLayout(null);
        panel.setBackground(Color.BLACK);

        JLabel accountNameLabel = createStyledLabel("Account Name:");
        accountNameLabel.setBounds(10, 20, 130, 25);
        panel.add(accountNameLabel);

        JTextField accountNameText = createStyledTextField();
        accountNameText.setBounds(150, 20, 200, 25);
        panel.add(accountNameText);

        JLabel priceLabel = createStyledLabel("Price:");
        priceLabel.setBounds(10, 60, 130, 25);
        panel.add(priceLabel);

        JTextField priceText = createStyledTextField();
        priceText.setBounds(150, 60, 200, 25);
        panel.add(priceText);

        JLabel deskripsiLabel = createStyledLabel("Deskripsi:");
        deskripsiLabel.setBounds(10, 100, 130, 25);
        panel.add(deskripsiLabel);

        JTextField deskripsiText = createStyledTextField();
        deskripsiText.setBounds(150, 100, 200, 25);
        panel.add(deskripsiText);

        JLabel gameNameLabel = createStyledLabel("Game Name:");
        gameNameLabel.setBounds(10, 140, 130, 25);
        panel.add(gameNameLabel);

        JTextField gameNameText = createStyledTextField();
        gameNameText.setBounds(150, 140, 200, 25);
        panel.add(gameNameText);

        JButton createButton = createStyledButton("Create");
        createButton.setBounds(10, 180, 80, 25);
        panel.add(createButton);

        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String accountName = accountNameText.getText();
                String price = priceText.getText();
                String deskripsi = deskripsiText.getText();
                String gameName = gameNameText.getText();

                try {
                    String url = "http://localhost:3000/create_menu";
                    HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");

                    String newMenu = "{\"AccountName\": \"" + accountName + "\", \"Price\": \"" + price + "\", \"Deskripsi\": \"" + deskripsi + "\", \"GameName\": \"" + gameName + "\"}";
                    System.out.println("JSON Input: " + newMenu);

                    conn.setDoOutput(true);
                    OutputStream os = conn.getOutputStream();
                    os.write(newMenu.getBytes(StandardCharsets.UTF_8));
                    os.flush();
                    os.close();

                    int responseCode = conn.getResponseCode();
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    if (responseCode == 201) {
                        JOptionPane.showMessageDialog(frame, "Menu created successfully!");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to create menu!");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                }
            }
        });

        JButton backButton = createStyledButton("Back");
        backButton.setBounds(100, 180, 80, 25);
        panel.add(backButton);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                Main.showMainMenu();
            }
        });
    }

    private static JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.GREEN);
        label.setFont(new Font("Monospaced", Font.PLAIN, 14));  // Use a monospaced font for a retro look
        return label;
    }

    private static JTextField createStyledTextField() {
        JTextField textField = new JTextField(20);
        textField.setBackground(Color.BLACK);
        textField.setForeground(Color.GREEN);
        textField.setCaretColor(Color.GREEN);
        textField.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
        textField.setFont(new Font("Monospaced", Font.PLAIN, 14));  // Use a monospaced font for a retro look
        return textField;
    }

    private static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("Monospaced", Font.PLAIN, 14));  // Use a monospaced font for a retro look
        button.setBackground(Color.BLACK);
        button.setForeground(Color.GREEN);
        button.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}
