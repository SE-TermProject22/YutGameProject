package View;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.ActionListener;

public class GameView  extends JPanel {
    private Image board;
    private JButton throwButton;
    private Map<String, Image> horseImages;
    private Map<String, Point> horsePositions;

    public GameView() { //생성자
        setLayout(null); //수동으로 버튼 위치를 지정하기 위해 레이아웃 매니저를 쓰지 않겠다는 의미
        loadBoard(boardType);
        loadImages(); //이미지 로딩 메서드
        initUI();
    }

    //게임판 선택
    private void loadBoard(String boardType) {
        switch (boardType) {
            case "square":
                board = new ImageIcon("image/사각형.png").getImage();
                break;
            case "pentagon":
                board = new ImageIcon("image/오각형.png").getImage();
                break;
            case "hexagon":
                board = new ImageIcon("image/육각형.png").getImage();
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

    //기본 세팅
    private void initUI() {
        throwButton = createButton("image/.png", );
    }

    //버튼 생성 메서드
    private JButton createButton(String imagePath, int x, int y, int width, int height) {
        JButton button = new JButton(new ImageIcon(new ImageIcon(imagePath).
                getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH)));
        button.setBounds(x, y, width, height); //버튼 위치, 크기 지정
        button.setBorderPainted(false); //버튼 테두리 제거
        button.setContentAreaFilled(false); //버튼 내부 배경 색상 채우기 비활성화 (기본 회색 배경으로 채워질 수도 있음)
        button.setFocusPainted(false); //포커스 표시 그리지 않게 하기 (버튼 클릭 후 생기는 이상한 외곽선 없애기)
        button.setOpaque(false); //버튼을 투명하게 만들기 (배경과 잘 어울리게 하기 위해)
        return button;
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

    //말 위치 정하기
    public void setHorsePosition(String color, int x, int y) {
        horsePositions.put(color, new Point(x,y));
        repaint();
    }

    public void addThrowButtonListener(ActionListener listener) {
        throwButton.addActionListener(listener);
    }
}