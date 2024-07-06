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

public class DeleteMenuApp {
    public static void showDeleteMenu() {
        JFrame frame = new JFrame("Delete Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

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

        JLabel idLabel = createStyledLabel("Menu ID:");
        idLabel.setBounds(10, 20, 130, 25);
        panel.add(idLabel);

        JTextField idText = createStyledTextField();
        idText.setBounds(150, 20, 200, 25);
        panel.add(idText);

        JButton deleteButton = createStyledButton("Delete");
        deleteButton.setBounds(10, 60, 80, 25);
        panel.add(deleteButton);

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String id = idText.getText();

                try {
                    String url = "http://localhost:3000/menu/" + id;
                    HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                    conn.setRequestMethod("DELETE");
                    conn.setRequestProperty("Content-Type", "application/json");

                    int responseCode = conn.getResponseCode();
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    if (responseCode == 200) {
                        JOptionPane.showMessageDialog(frame, "Menu deleted successfully!");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to delete menu!");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                }
            }
        });

        JButton backButton = createStyledButton("Back");
        backButton.setBounds(100, 60, 80, 25);
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
