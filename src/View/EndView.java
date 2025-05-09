package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class EndView extends JPanel {

    private JButton restartButton;
    private JButton exitButton;
    private JLabel messageLabel;

    public EndView() {
        setLayout(null);
        initUI();
    }

    private void initUI() {
        messageLabel = new JLabel("게임 종료!");
        messageLabel.setFont(new Font("Arial", Font.BOLD, 24));
        messageLabel.setBounds(400, 150, 200, 50);
        add(messageLabel);

        restartButton = createButton("다시 시작", 400, 250);
        exitButton = createButton("종료하기", 400, 350);

        add(restartButton);
        add(exitButton);
    }

    private JButton createButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 150, 50);
        return button;
    }

    public void addRestartListener(ActionListener listener) {
        restartButton.addActionListener(listener);
    }

    public void addExitListener(ActionListener listener) {
        exitButton.addActionListener(listener);
    }
}