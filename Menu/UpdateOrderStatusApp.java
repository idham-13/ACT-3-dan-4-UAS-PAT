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

public class UpdateOrderStatusApp {
    public static void showUpdateOrder() {
        JFrame frame = new JFrame("Update Order Status");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

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

        JLabel idLabel = createStyledLabel("Order ID:");
        idLabel.setBounds(10, 20, 130, 25);
        panel.add(idLabel);

        JTextField idText = createStyledTextField();
        idText.setBounds(150, 20, 200, 25);
        panel.add(idText);

        JLabel statusLabel = createStyledLabel("Status:");
        statusLabel.setBounds(10, 60, 130, 25);
        panel.add(statusLabel);

        JTextField statusText = createStyledTextField();
        statusText.setBounds(150, 60, 200, 25);
        panel.add(statusText);

        JButton updateButton = createStyledButton("Update");
        updateButton.setBounds(10, 100, 80, 25);
        panel.add(updateButton);

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String id = idText.getText();
                String status = statusText.getText();

                try {
                    String url = "http://localhost:3000/order/" + id;
                    HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                    conn.setRequestMethod("PUT");
                    conn.setRequestProperty("Content-Type", "application/json");

                    String updateStatus = "{\"Status\": \"" + status + "\"}";
                    System.out.println("JSON Input: " + updateStatus);

                    conn.setDoOutput(true);
                    OutputStream os = conn.getOutputStream();
                    os.write(updateStatus.getBytes(StandardCharsets.UTF_8));
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

                    if (responseCode == 200) {
                        JOptionPane.showMessageDialog(frame, "Order status updated successfully!");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to update order status!");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                }
            }
        });

        JButton getAllOrdersButton = createStyledButton("Get All Orders");
        getAllOrdersButton.setBounds(10, 140, 150, 25);
        panel.add(getAllOrdersButton);

        JTextArea orderArea = new JTextArea();
        orderArea.setBackground(Color.BLACK);
        orderArea.setForeground(Color.GREEN);
        orderArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        orderArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(orderArea);
        scrollPane.setBounds(10, 180, 560, 180);
        panel.add(scrollPane);

        getAllOrdersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String url = "http://localhost:3000/order";
                    HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-Type", "application/json");

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    orderArea.setText(formatResponse(response.toString()));

                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                }
            }
        });

        JButton backButton = createStyledButton("Back");
        backButton.setBounds(100, 100, 80, 25);
        panel.add(backButton);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                Main.showMainMenu();
            }
        });
    }

    private static String formatResponse(String response) {
        // Example formatter, you can adjust it to match your JSON structure
        return response.replaceAll("\\},\\{", "\n\n").replaceAll("\\[|\\]", "").replaceAll("\\{", "\n").replaceAll("\\}", "\n");
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

    public static void main(String[] args) {
        showUpdateOrder();
    }
}

class Main {
    public static void showMainMenu() {
        JFrame frame = new JFrame("Menu Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 250);

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

        JButton createButton = createStyledButton("Create Menu");
        createButton.setBounds(10, 20, 150, 25);
        panel.add(createButton);

        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                CreateMenuApp.showCreateMenu();
            }
        });

        JButton updateButton = createStyledButton("Update Menu");
        updateButton.setBounds(10, 60, 150, 25);
        panel.add(updateButton);

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                UpdateMenuApp.showUpdateMenu();
            }
        });

        JButton deleteButton = createStyledButton("Delete Menu");
        deleteButton.setBounds(10, 100, 150, 25);
        panel.add(deleteButton);

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                DeleteMenuApp.showDeleteMenu();
            }
        });

        JButton updateStatusButton = createStyledButton("Update Status");
        updateStatusButton.setBounds(10, 140, 150, 25);
        panel.add(updateStatusButton);

        updateStatusButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                UpdateOrderStatusApp.showUpdateOrder();
            }
        });
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
