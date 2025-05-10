package View;

import javax.swing.*;
import java.awt.*;
import Controller.GameController;


public class EndView extends JPanel {
    private JButton restartButton;
    private JButton exitButton;
    private StartView startView;

    private Image endBackground;
    private Image[] winnerImages = new Image[4];
    private int winnerId = 1; // 기본값: 플레이어 1 우승

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


    public void setStartView(StartView startView) {
        this.startView = startView;
    }

    private GameController controller;

    public void setController(GameController controller) {
        this.controller = controller;
    }

    public void initButtonActions() {
        restartButton.addActionListener(e -> {
            this.setVisible(false);
            if (controller != null) {
                controller.restartGame();  // 여기서 게임 초기화
            }
        });

        exitButton.addActionListener(e -> System.exit(0));
    }


    private void initUI() {
        restartButton = createImageButton("image/재시작버튼.png", 573, 84);
        exitButton = createImageButton("image/종료버튼.png", 353, 84);

        exitButton.addActionListener(e -> System.exit(0));

        add(restartButton);
        add(exitButton);
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (endBackground != null) {
            g.drawImage(endBackground, 0, 0, getWidth(), getHeight(), null);
        }
        if (winnerId >= 1 && winnerId <= 4) {
            g.drawImage(winnerImages[winnerId - 1], 433, 435, null);
        }
    }
}


