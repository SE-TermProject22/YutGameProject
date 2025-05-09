package View;

import Controller.GameController;
import Model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameView  extends JPanel {
    private Image board, currentImage;
    private JButton throwButton;
    private List<JLabel> playerImages = new ArrayList<>();
    private Map<String, Image> horseImages;
    private Map<String, Point> horsePositions;

    private List<Image> yutImages;
    private List<Image> resultImages;
    private Image notifyingImage;

    private Timer animationTimer;
    private int yutIndex;

    private Player currentPlayer;

    public GameView() {
        setLayout(null);
        horsePositions = new HashMap<>();
        horseImages = new HashMap<>();

        loadImages();
        initUI();
    }

    private void loadImages() {
        horseImages.clear();

        String[] colors = {"red", "blue", "yellow", "green"};

        for (String color : colors) {
            for (int i = 1; i <= 5; i++) {
                String key = color + i;
                Image img = new ImageIcon("image/" + color + "/" + color + i + ".png").getImage();
                if (img != null) {
                    horseImages.put(key, img);
                }
            }
        }

        yutImages = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            Image img = new ImageIcon("image/yut/yut" + i + ".png").getImage();
            if (img != null) {
                yutImages.add(img);
            }
        }

        resultImages = new ArrayList<>();
        String[] resultImageNames = {"1.png", "2.png", "3.png", "4.png", "5.png", "-1.png"};
        for (String imageName : resultImageNames) {
            Image resultImg = new ImageIcon("image/" + imageName).getImage();
            if (resultImg != null) {
                resultImages.add(resultImg);
            }
        }
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
        throwButton = createButton("image/윷 던지기.png", 798, 405);
        add(throwButton);

        repaint();
    }

    public void setBoardType(String boardType) {
        board = new ImageIcon("image/" + boardType + " board.png").getImage();
        repaint();
    }

    public void displayPlayers(int playerCount) {
        Point[] playerPositions = {
                new Point(692, 502),
                new Point(898, 502),
                new Point(692, 578),
                new Point(898, 578)
        };

        for (int i = 1; i <= playerCount; i++) {
            ImageIcon playerIcon = new ImageIcon("image/player" + i + ".png");
            JLabel playerLabel = new JLabel(playerIcon);

            Point pos = playerPositions[i - 1];
            playerLabel.setBounds(pos.x, pos.y, playerIcon.getIconWidth(), playerIcon.getIconHeight());

            add(playerLabel);
        }
        repaint();
    }

    public void displayHorses(List<String> selectedColors, int playerCount, int horseCount) {
        Point[] horsePositions = {
                new Point(682, 522),
                new Point(888, 522),
                new Point(682, 598),
                new Point(888, 598),
        };

        for (int i = 0; i < playerCount; i++) {
            String color = selectedColors.get(i);
            Point playerHorsePosition = horsePositions[i];

            for (int j = 1; j <= horseCount; j++) {
                String key = color + j;
                Image horseImage = horseImages.get(key);
                if (horseImage != null) {
                    JLabel horseLabel = new JLabel(new ImageIcon(horseImage));

                    int horseX = playerHorsePosition.x + (j - 1) * 34;
                    int horseY = playerHorsePosition.y;

                    horseLabel.setBounds(horseX, horseY, 40, 40);
                    add(horseLabel);
                }
            }
        }
        repaint();
    }

    //멀 위치 초기화 메서드
//    public void placeHorses(List<String> colors) {
//        for (String color : colors) {
//            //setHorsePosition(color, , );
//        }
//        repaint();
//    }

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
//    public void setHorsePosition(String color, int x, int y) {
//        horsePositions.put(color, new Point(x, y));
//        repaint();
//    }


    public void startYutAnimation() {
        yutIndex = 0;

        if (animationTimer != null) {
            animationTimer.cancel();
        }

        animationTimer = new Timer();
        animationTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (yutIndex < yutImages.size()) {
                    setCurrentImage(yutImages.get(yutIndex));
                    yutIndex++;
                } else {
                    animationTimer.cancel();
                    showResultImage();
                }
            }
        }, 0, 300);
    }

    private void showResultImage() {
        int yutResult = currentPlayer.throwYut();
        Image resultImage = getResultImagePathForYutValue(yutResult);

        if (resultImage != null) {
            setCurrentImage(resultImage);
        }

        if (yutResult == 4) {
            scheduleNotifyingImage("image/윷 한번더.png");
        } else if (yutResult == 5) {
            scheduleNotifyingImage("image/모 한번더.png");
        } else {
            notifyingImage = null;
            repaint();
        }
    }

    //notifyingImage 출력 시간 제어
    private void scheduleNotifyingImage(String imagePath) {
        Timer notifyingTimer = new Timer();

        notifyingTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                notifyingImage = new ImageIcon(imagePath).getImage();
                repaint();

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        notifyingImage = null;
                        repaint();
                    }
                }, 1100); //1.1초 뒤에 사라지기
            }
        }, 700); //윷 결과 출력되고 0.7초 뒤에 출력
    }

    // Yut 값에 맞는 이미지 경로를 반환하는 메서드
    private Image getResultImagePathForYutValue(int yutValue) {
        switch (yutValue) {
            case 1:
                return resultImages.get(0);
            case 2:
                return resultImages.get(1);
            case 3:
                return resultImages.get(2);
            case 4:
                return resultImages.get(3);
            case 5:
                return resultImages.get(4);
            case -1:
                return resultImages.get(5);
            default:
                return null;
        }
    }

    public void addThrowButtonListener(ActionListener listener) {
        throwButton.addActionListener(listener);
    }

    public void setCurrentImage(Image image) {
        currentImage = image;
        repaint();
    }

    public void setPlayer(Player player) {
        this.currentPlayer = player;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (board != null) {
            g.drawImage(board, 0, 0, getWidth(), getHeight(), null);
        }

        if (currentImage != null) {
            g.drawImage(currentImage, 670, 40, null);
        }

        if (notifyingImage != null) {
            g.drawImage(notifyingImage, 291, 294, null);
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


/*
package View;

import Model.Horse;

import javax.swing.*;
import java.util.Map;


        // ★★★ 추가: 테스트용 이미지 출력 ★★★
//        Image testImage = new ImageIcon("image/red.png").getImage();
//        if (testImage != null) {
//            // 원하는 좌표 (x, y)를 지정해서 이미지 출력
//            g.drawImage(testImage, 555, 463, 40, 40, null);
//        }
    }
}