package View.Swing;

import View.Interface.IEndView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class EndView extends JPanel implements IEndView {
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
        endBackground = new ImageIcon(getClass().getResource("/image/종료 화면.png")).getImage();
        for (int i = 0; i < 4; i++) {
            winnerImages[i] = new ImageIcon(getClass().getResource("/image/Winner" + (i + 1) + ".png")).getImage();
        }
    }

    private JButton createButton(String imagePath, int x, int y) {
        ImageIcon icon = new ImageIcon(getClass().getResource("/" + imagePath));
        JButton button = new JButton(icon);
        int width = icon.getIconWidth();
        int height = icon.getIconHeight();

        button.setBounds(x, y, width, height); //버튼 위치, 크기 지정
        button.setBorderPainted(false); //버튼 테두리 제거
        button.setContentAreaFilled(false); //버튼 내부 배경 색상 채우기 비활성화 (기본 회색 배경으로 채워질 수도 있음)
        button.setFocusPainted(false); //포커스 표시 그리지 않게 하기 (버튼 클릭 후 생기는 이상한 외곽선 없애기)
        button.setOpaque(false); //버튼을 투명하게 만들기 (배경과 잘 어울리게 하기 위해)

        return button;
    }

    private void initUI() {
        restartButton = createButton("/image/재시작버튼.png", 573, 84);
        exitButton = createButton("/image/종료버튼.png", 353, 84);

        add(restartButton);
        add(exitButton);
    }

    @Override
    public void addRestartButtonListener(Object listener) {
        restartButton.addActionListener((ActionListener) listener);
    }

    @Override
    public void addExitButtonListener(Object listener) {
        exitButton.addActionListener((ActionListener) listener);
    }

//    private JButton createImageButton(String path, int x, int y) {
//        ImageIcon icon = new ImageIcon(getClass().getResource(path));
//        JButton button = new JButton(icon);
//        button.setBounds(x, y, icon.getIconWidth(), icon.getIconHeight());
//        button.setBorderPainted(false);
//        button.setContentAreaFilled(false);
//        button.setFocusPainted(false);
//        return button;
//    }

    @Override
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
            g.drawImage(winnerImages[winnerId - 1], 440, 455, null);
        }
    }
}

