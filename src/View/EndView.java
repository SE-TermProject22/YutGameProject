package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import Controller.GameController;


public class EndView extends JPanel {
    private JButton restartButton;
    private JButton exitButton;
    private StartView startView;

    private Image endBackground;
    private Image[] winnerImages = new Image[4];
    private int winnerId = 1; // 기본값: 플레이어 1 우승 -> controller finish처리에서 값받아와서 후에 reset

    public EndView() {
        setLayout(null);
        loadImages();
        initUI();
    }

    private void loadImages() {
        endBackground = new ImageIcon("image/종료 화면.png").getImage();  // ✅ 배경 이미지 추가
        for (int i = 0; i < 4; i++) {
            winnerImages[i] = new ImageIcon("image/Winner" + (i + 1) + ".png").getImage();
        }
    }

    private void initUI() {
        restartButton = createImageButton("image/재시작버튼.png", 573, 84);
        exitButton = createImageButton("image/종료버튼.png", 353, 84);

        add(restartButton);
        add(exitButton);

    }

    public void addRestartButtonListener(ActionListener listener) {
        restartButton.addActionListener(listener);
    }

    public void addExitButtonListener(ActionListener listener) {
        exitButton.addActionListener(listener);
    }

    private JButton createImageButton(String path, int x, int y) {
        ImageIcon icon = new ImageIcon(path);
        JButton button = new JButton(icon);
        button.setBounds(x, y, icon.getIconWidth(), icon.getIconHeight());
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        return button;
    }

    public void setWinner(int playerId) {
        this.winnerId = playerId;
        repaint();
    }
/*
    // 보드 초기화
    public void clearBoard() {
        this.removeAll();
        this.revalidate();
        this.repaint();
    }

    // 말 초기화
    public void clearHorses() {
        this.removeAll();
        this.revalidate();
        this.repaint();
    }
*/
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (endBackground != null) {
            g.drawImage(endBackground, 0, 0, getWidth(), getHeight(), null);
        }
        if (winnerId >= 1 && winnerId <= 4) {
            g.drawImage(winnerImages[winnerId - 1], 516, 583, null);
        }
    }
}

