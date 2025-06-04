package View.Swing.pane;

import javax.swing.*;
import java.awt.event.ActionListener;

public class ThrowButtonPane {
    private JPanel parentPanel;
    private JButton throwButton;
    private JButton specialThrowButton;

    public ThrowButtonPane(JPanel parentPanel) {
        this.parentPanel = parentPanel;
        initButtons();
    }

    private void initButtons() {
        throwButton = createButton("image/윷 던지기.png", 744, 405);
        parentPanel.add(throwButton);

        specialThrowButton = createButton("image/지정던지기 버튼.png", 940, 405);
        parentPanel.add(specialThrowButton);
    }

    private JButton createButton(String imagePath, int x, int y) {
        ImageIcon icon = new ImageIcon(getClass().getResource("/" + imagePath));
        JButton button = new JButton(icon);
        int width = icon.getIconWidth();
        int height = icon.getIconHeight();

        button.setBounds(x, y, width, height);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);

        return button;
    }

    public void addThrowButtonListener(ActionListener listener) {
        throwButton.addActionListener(listener);
    }

    public void addSpecialThrowListener(ActionListener listener) {
        specialThrowButton.addActionListener(listener);
    }
}