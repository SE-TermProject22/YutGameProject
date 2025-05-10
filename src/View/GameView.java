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
import java.util.function.Consumer;
import Model.Horse;

public class GameView  extends JPanel {
    private Image board, currentImage;
    private JButton throwButton;
    private List<JLabel> playerImages = new ArrayList<>();
    private Map<String, Image> horseImages;
    private Map<String, Point> horsePositions;
    // 말의 쌓인 개수를 관리하는 Map
    private Map<Point, List<Integer>> stackedHorses = new HashMap<>();

    private List<Image> yutImages;
    private List<Image> resultImages;
    private Image notifyingImage;

    private Timer animationTimer;
    private int yutIndex;


    // private Player currentPlayer;

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

        // horseImage setting
        for (String color : colors) {
            for (int i = 1; i <= 5; i++) {
                String key = color + i;
                Image img = new ImageIcon("image/" + color + "/" + color + i + ".png").getImage();
                if (img != null) {
                    horseImages.put(key, img);
                }
            }
        }
        // 윷 던지는 과정 animation 과정들 사진
        yutImages = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            Image img = new ImageIcon("image/yut/yut" + i + ".png").getImage();
            if (img != null) {
                yutImages.add(img);
            }
        }
        // 윷 결과 이미지
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

    // 만약 말이 finish 처리되면 말 하나 사라지게 해야 함 - 이거 어떻게 할지 - 위에 display 그거 음음


    /*
    //말 위치 초기화 메서드
    public void placeHorses(List<String> colors) {
        for (String color : colors) {
            //setHorsePosition(color, , );
        }
        repaint();
    }
    */
    /*
    // 처음에 horses들 다 만들기
    public void initHorses(List<String> colors){
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
    */
    private Map<Integer, JLabel> horseComponents = new HashMap<>();

    // 처음 말들을 다 만들기
    public void initHorses(List<String> colors, int horseCount) {
        int idCounter = 0;

        for (String color : colors) {
            for (int j = 1; j <= horseCount; j++) {
                String key = color + j;
                Image horseImage = horseImages.get(key);
                if (horseImage != null) {
                    JLabel horseLabel = new JLabel(new ImageIcon(horseImage));

                    // 예시 초기 위치: 플레이어 말 대기 구역
                    // 초기 위치 관계 없음 - 디버깅
                    /*
                    int horseX = 50 + j * 34;  // X좌표는 적당히 간격 조정
                    int horseY = color.equals("RED") ? 400 : 450; // 플레이어 색상별 초기 Y좌표
                    switch(color){
                        case "red": horseY = 400; break;
                        case "blue": horseY = 450; break;
                        case "green": horseY = 550; break;
                        case "yellow": horseY = 600; break;
                    }
                    */

                    // horseLabel.setBounds(horseX, horseY, 40, 40); // 디버깅
                    horseLabel.setBounds(0, 0, 40, 40);
                    horseLabel.setVisible(false); // 처음엔 보이지 않게
                    // horseLabel.setVisible(true); // 디버깅

                    horseComponents.put(idCounter, horseLabel); // model과 연동되는 고유 id = idCounter
                    add(horseLabel);
                    idCounter++;
                }
            }
        }
        repaint();
    }

    // horse를 add하는 함수 - 엎기 할 때 - color, x, y,
    public void addHorseComponent(int horse_id) {
        JLabel horseLabel = new JLabel(new ImageIcon(horseImages.get(horse_id))); // 약간 이런식으로해서
    }

    // horse를 remove하는 함수 - 필요할까? 일단은


    // horse를 setvisible하게 하는 함수
    public void setHorseVisible(int horse_id) {
        horseComponents.get(horse_id).setVisible(true);
        repaint();
    }

    // horse를 setInvisible 하는 함수
    public void setHorseInvisible(int horse_id) {
        horseComponents.get(horse_id).setVisible(false);
        repaint();
    }

    // horse를 move하는 함수
    public void moveHorse(int horse_id, int x, int y) {
        horseComponents.get(horse_id).setLocation(x, y);
        repaint();
    }

    // 혹시 몰라서 여러 개 받으면 이렇게 처리
    public void moveHorse(List<Integer> horse_id_list, int x, int y) {
        int i = 0;
        for (Integer horse_id : horse_id_list) {
            horseComponents.get(horse_id_list.get(i)).setLocation(x, y);
            i++;
        }
        repaint();
    }

    // 그러면 나중에 마지막에 moveHorse()를 하면 됨

    // 설명 필요 - 아래 두 함수
    public void placeHorses(List<String> colors) {
        int x = 50;  // x 좌표를 50부터 시작
        int y = 50;  // y 좌표를 50으로 고정 (필요에 따라 조정 가능)

        for (String color : colors) {
            setHorsePosition(color, x, y);
            x += 100;  // 각 말의 x 좌표를 100씩 증가시켜서 수평으로 배치
        }
        repaint();
    }

    //말 위치를 업데이트하는 메서드
    public void setHorsePosition(String color, int x, int y) {
        horsePositions.put(color, new Point(x, y));
        repaint();
    }


    // 윷 관련
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
//        Random random = new Random();
//        int index = random.nextInt(resultImages.size());
//        setCurrentImage(resultImages.get(index));

        // int yutResult = currentPlayer.throwYut();


        // yutResult에 맞는 이미지 경로를 얻고, 그 경로로 Image 객체를 만듬
//        String resultImagePath = getResultImagePathForYutValue(yutResult);
//
//        // resultImagePath를 ImageIcon으로 변환하고 Image를 얻음
//        ImageIcon imageIcon = new ImageIcon(resultImagePath);
//        Image resultImage = imageIcon.getImage();
//
//        // 화면에 현재 이미지 표시
//        setCurrentImage(resultImage);
        Image resultImage = getResultImagePathForYutValue(result);

        if (resultImage != null) {
            setCurrentImage(resultImage);
        }
        /*
        if (result == YutResult.YUT) {
            scheduleNotifyingImage("image/윷 한번더.png");
        } else if (result == YutResult.MO) {
            scheduleNotifyingImage("image/모 한번더.png");
        } else {
            notifyingImage = null;
            repaint();
        }

        */
    }

    //notifyingImage 출력 시간 제어
    // public 으로 바꿈
    public void scheduleNotifyingImage(YutResult result) {
        String imagePath;
        if (result == YutResult.YUT)
            imagePath = "image/윷 한번더.png";
        else
            imagePath = "image/모 한번더.png";

        Timer notifyingTimer = new Timer();
        notifyingTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                notifyingImage = new ImageIcon(imagePath).getImage();
                // repaint();

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        notifyingImage = null;
                        // repaint();
                    }
                }, 1100); //1.1초 뒤에 사라지기
            }
        }, 1000); //윷 결과 출력되고 1초 뒤에 출력
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

    private String getKoreanName(YutResult result) {
        return switch (result) {
            case DO -> "도";
            case GAE -> "개";
            case GEOL -> "걸";
            case YUT -> "윷";
            case MO -> "모";
            case BackDo -> "백도";
        };
    }


    public void showYutResultChoiceDialog(List<YutResult> yutResults, Consumer<YutResult> onSelected) {
        JDialog dialog = new JDialog((JFrame) null, "결과 적용 선택", true);  // 모달창
        dialog.setSize(665, 298);
        dialog.setLocationRelativeTo(null);
        dialog.setUndecorated(true); // ✨ 윈도우 테두리 없애기
        dialog.setBackground(new Color(0, 0, 0, 0));  // 모달 창을 완전히 투명하게

        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image bg = new ImageIcon("image/결과 적용.png").getImage();
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
            }
        };
        System.out.println("전달된 결과 리스트: " + yutResults);
        panel.setLayout(null);
        panel.setBounds(0, 0, 800, 600);
        panel.setOpaque(false);  // 패널도 투명하게

        int x = 100;
        int y = 100;

        for (YutResult result : yutResults) {
            String imagePath = "image/선택 윷 결과/선택 " + getKoreanName(result) + ".png";
            ImageIcon icon = new ImageIcon(imagePath);
            JButton btn = new JButton(icon);
            btn.setBounds(x, y, icon.getIconWidth(), icon.getIconHeight());
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);

            btn.addActionListener(e -> {
                dialog.dispose();
                onSelected.accept(result);
            });

            panel.add(btn);
            x += icon.getIconWidth() + 20;
        }

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    public void showHorseSelectionDialog(List<Horse> horses, Consumer<Horse> onSelected) {
        JDialog dialog = new JDialog((JFrame) null, "말 선택", true);
        dialog.setSize(665, 298);
        dialog.setLocationRelativeTo(null);
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0, 0, 0, 0));  // 모달 창을 완전히 투명하게

        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image bg = new ImageIcon("image/말 적용.png").getImage();
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
            }
        };
        panel.setLayout(null);
        panel.setOpaque(false);  // 패널도 투명하게

        int x = 100;
        int y = 100;
        int i = 1;

        for (Horse horse : horses) {
            String imagePath = "image/말 버튼/" + horse.color + "/" + (i++) + ".png";
            ImageIcon icon = new ImageIcon(imagePath);

            JButton btn = new JButton(icon);
            btn.setBounds(x, y, icon.getIconWidth(), icon.getIconHeight());
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);

            btn.addActionListener(e -> {
                dialog.dispose();
                onSelected.accept(horse);
            });

            panel.add(btn);
            x += icon.getIconWidth() + 20;
        }

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    private JLabel findLabelForPosition(Point position) {
        for (Component comp : getComponents()) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                // 위치를 정확히 맞추려면 +10을 빼는 것이 맞을 수 있음
                if (label.getBounds().x == position.x && label.getBounds().y == position.y) {
                    return label;  // 해당 위치의 숫자 라벨 반환
                }
            }
        }
        return null;
    }

    public void stackHorseAtPosition(int horseId, Point position) {
        // 해당 위치에 이미 쌓인 말이 있는지 확인
        List<Integer> stackedHorsesList = stackedHorses.getOrDefault(position, new ArrayList<>());

        // 말을 쌓기
        stackedHorsesList.add(horseId);
        stackedHorses.put(position, stackedHorsesList);

        // 말을 해당 위치에 추가하기 (업힌 말)
        moveHorse(horseId, position.x, position.y);  // 말 이동

        // 업힌 말이 2개 이상이라면 UI에서 겹쳐있는 말의 개수로 처리
        if (stackedHorsesList.size() > 1) {
            JLabel stackedLabel = findLabelForPosition(position);
            if (stackedLabel == null) {
                // 새로운 레이블을 추가하여 쌓인 말의 수를 표시
                stackedLabel = new JLabel(String.valueOf(stackedHorsesList.size()));
                stackedLabel.setBounds(position.x, position.y - (stackedHorsesList.size() * 10), 40, 40); // 말 위에 표시되도록 위치 조정
                stackedLabel.setFont(new Font("Arial", Font.BOLD, 16)); // 글자 크기 조정
                stackedLabel.setForeground(Color.BLACK); // 글자 색 설정
                add(stackedLabel);
            } else {
                // 이미 쌓인 말이 있으면 숫자 업데이트
                stackedLabel.setText(String.valueOf(stackedHorsesList.size()));
                stackedLabel.setBounds(position.x, position.y - (stackedHorsesList.size() * 10), 40, 40); // 겹쳐서 표시
            }
        }

        repaint();
    }


}