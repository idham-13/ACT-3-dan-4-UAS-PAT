import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        showMainMenu();
    }

    public static void showMainMenu() {
        JFrame frame = new JFrame("Menu Management");
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

        JButton createButton = createStyledButton("Create Menu");
        createButton.setBounds(50, 50, 300, 40);
        panel.add(createButton);

        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                CreateMenuApp.showCreateMenu();
            }
        });

        JButton updateButton = createStyledButton("Update Menu");
        updateButton.setBounds(50, 100, 300, 40);
        panel.add(updateButton);

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                UpdateMenuApp.showUpdateMenu();
            }
        });

        JButton deleteButton = createStyledButton("Delete Menu");
        deleteButton.setBounds(50, 150, 300, 40);
        panel.add(deleteButton);

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                DeleteMenuApp.showDeleteMenu();
            }
        });

        JButton updateStatusButton = createStyledButton("Update Status");
        updateStatusButton.setBounds(50, 200, 300, 40);
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
        button.setFont(loadPixelFont());
        button.setBackground(Color.BLACK);
        button.setForeground(Color.GREEN);
        button.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private static Font loadPixelFont() {
        try {
            Font pixelFont = Font.createFont(Font.TRUETYPE_FONT, Main.class.getResourceAsStream("/pixel_font.ttf")).deriveFont(18f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(pixelFont);
            return pixelFont;
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("Monospaced", Font.PLAIN, 18);
        }
    }
}
