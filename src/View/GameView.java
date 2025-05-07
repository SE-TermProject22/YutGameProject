package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class GameView  extends JPanel {
    private Image board;
    private JButton throwButton;
    private Map<String, Image> horseImages;
    private Map<String, Point> horsePositions;

    public GameView() {
        setLayout(null);
        horsePositions = new HashMap<>();
        horseImages = new HashMap<>();
        loadImages();
        initUI();
    }

    private void loadImages() {
        horseImages.put("red", new ImageIcon("image/red.png").getImage());
        horseImages.put("blue", new ImageIcon("image/blue.png").getImage());
        horseImages.put("yellow", new ImageIcon("image/yellow.png").getImage());
        horseImages.put("green", new ImageIcon("image/green.png").getImage());
    }

    //버튼 생성 메서드
    private JButton createButton(String imagePath, int x, int y) {
        ImageIcon icon = new ImageIcon(imagePath);
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

    //기본 세팅
    private void initUI() {
        throwButton = createButton("image/윷 던지기.png", 785, 410);
        add(throwButton);
    }

    public void setBoardType(String boardType) {
        board = new ImageIcon("image/" + boardType + " board.png").getImage();
        repaint();
    }

    //멀 위치 초기화 메서드
    public void placeHorses(List<String> colors) {
        for (String color : colors) {
            //setHorsePosition(color, , );
        }
        repaint();
    }

//    public void placeHorses(List<String> colors) {
//        int x = 50;  // x 좌표를 50부터 시작
//        int y = 50;  // y 좌표를 50으로 고정 (필요에 따라 조정 가능)
//
//        for (String color : colors) {
//            setHorsePosition(color, x, y);
//            x += 100;  // 각 말의 x 좌표를 100씩 증가시켜서 수평으로 배치
//        }
//        repaint();
//    }

    //말 위치를 업데이트하는 메서드
    public void setHorsePosition(String color, int x, int y) {
        horsePositions.put(color, new Point(x,y));
        repaint();
    }

    public void addThrowButtonListener(ActionListener listener) {
        throwButton.addActionListener(listener);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (board != null) {
            g.drawImage(board, 0, 0, getWidth(), getHeight(), null);
        }

        for (String color : horsePositions.keySet()) {
            Image horseImage = horseImages.get(color);
            Point position = horsePositions.get(color);

            if (horseImage != null && position != null) {
                g.drawImage(horseImages.get(color), position.x, position.y, 40, 40, null);
            }
        }
    }
}