package View;

import Controller.GameController;
import Controller.YutResult;
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

    private int selectedResult;
    private List<JButton> resultButtons = new ArrayList<>();
    private List<JButton> horseButtons = new ArrayList<>();

    private Timer animationTimer;
    private int yutIndex;

    // private Player currentPlayer;
    private GameController gameController;

    public GameView() {
        this.gameController = gameController;
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

    public void startYutAnimation(YutResult result) {
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
                    showResultImage(result);
                }
            }

        }, 0, 300);
    }

    private void showResultImage(YutResult result) {
        Image resultImage = getResultImagePathForYutValue(result);

        if (resultImage != null) {
            setCurrentImage(resultImage);
        }

        if (result == YutResult.YUT) {
            scheduleNotifyingImage("image/윷 한번더.png");
        } else if (result == YutResult.MO) {
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
    private Image getResultImagePathForYutValue(YutResult result) {
        switch (result) {
            case YutResult.DO:
                return resultImages.get(0);
            case YutResult.GAE:
                return resultImages.get(1);
            case YutResult.GEOL:
                return resultImages.get(2);
            case YutResult.YUT:
                return resultImages.get(3);
            case YutResult.MO:
                return resultImages.get(4);
            case YutResult.BackDo:
                return resultImages.get(5);
            default:
                return null;
        }
    }

    // 결과 버튼 생성 및 표시 메서드
    public void displayResultButtons(List<Integer> yutResults) {
        clearButtons(); // 기존 버튼들 삭제

        int startX = 287;
        int startY = 353;
        int gap = 93;

        for (int i = 0; i < yutResults.size(); i++) {
            int result = yutResults.get(i);
            String imagePath = "image/윷 결과 버튼" + result + ".png";  // 결과에 맞는 이미지 경로 (예: 1.png, 2.png 등)
            JButton resultButton = createButton(imagePath, startX + (gap * i), startY);

            resultButton.addActionListener(e -> {
                selectedResult = result;
                displayHorseButtons();
            });

            add(resultButton);
            resultButtons.add(resultButton);
        }

        repaint();
    }

    // 말 선택 버튼 생성 및 표시 메서드
    public void displayHorseButtons() {
        clearButtons(); // 기존 버튼들 삭제
        Player currentPlayer = gameController.getCurrentPlayer();

        if (currentPlayer == null) return;

        String playerColor = currentPlayer.getColor(); // 현재 플레이어의 색상 (예: "red", "blue" 등)
        int horseCount = currentPlayer.getHorseCount();  // 플레이어가 가진 말의 개수

        int startX = 327;
        int startY = 353;
        int gap = 93;

        for (int i = 1; i <= horseCount; i++) {
            String imagePath = "image/말 버튼/" + playerColor + "/" + i + ".png";  // 말 이미지 경로 (예: horse1.png, horse2.png 등)
            JButton horseButton = createButton(imagePath, startX + (gap * (i - 1)), startY);

            int horseIndex = i;
            horseButton.addActionListener(e -> {
                gameController.applySelectedResult(selectedResult, horseIndex);
            });

            add(horseButton);
            horseButtons.add(horseButton);
        }

        repaint();
    }

    public void applySelectedResult(int selectedResult, int selectedHorse) {
        gameController.applySelectedResult(selectedResult, selectedHorse);
    }

    // clearButtons 메서드 정의 (버튼들 숨기기)
    private void clearButtons() {
        for (JButton button : horseButtons) {
            button.setVisible(false);
        }
        for (JButton button : resultButtons) {
            button.setVisible(false);
        }
    }

    public void addThrowButtonListener(ActionListener listener) {
        throwButton.addActionListener(listener);
    }

    public void setCurrentImage(Image image) {
        currentImage = image;
        repaint();
    }

    /*
    public void setPlayer(Player player) {
        this.currentPlayer = player;
    }
    */

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