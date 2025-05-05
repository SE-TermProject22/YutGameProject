package View;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.ActionListener;

import View.StartView;

public class GameView  extends JPanel {
    private Image background;
    private JButton throwButton;
    private Map<String, Image> horseImages;
    private Map<String, Point> horsePositions;

    public GameView() { //생성자
        setLayout(null); //수동으로 버튼 위치를 지정하기 위해 레이아웃 매니저를 쓰지 않겠다는 의미
        loadBackground(boardType);
        loadImages(); //이미지 로딩 메서드
        initUI();
    }

    private void loadBackground(String boardType) {
        switch (boardType) {
            case "square":
                background = new ImageIcon("image/사각형.png").getImage();
                break;
            case "pentagon":
                background = new ImageIcon("image/오각형.png").getImage();
                break;
            case "hexagon":
                background = new ImageIcon("image/육각형.png").getImage();
                break;
        }
    }

    private void loadImages() {
        horseImages = new HashMap<>();
        horseImages.put("red", new ImageIcon("image/red.png").getImage());
        horseImages.put("blue", new ImageIcon("image/blue.png").getImage());
        horseImages.put("yellow", new ImageIcon("image/yellow.png").getImage());
        horseImages.put("green", new ImageIcon("image/green.png").getImage());
    }

    private void initUI() {
        throwButton =
    }

    //JPanel이 화면에 그려질 때 호출되는 메서드
    //이 메서드 안에서 배경 이미지를 그림
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), null);

        for (String color : horsePositions.keySet()) {
            Point p = horsePositions.get(color);
            g.drawImage(horseImages.get(color), p.x, p.y, 40, 40, null);
        }
    }

    public void setHorsePosition(String color, int x, int y) {
        horsePositions.put(color, new Point(x,y));
        repaint();
    }

    public void addThrowButtonListener(ActionListener listener) {
        throwButton.addActionListener(listener);
    }
}