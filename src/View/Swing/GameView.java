package View.Swing;

// import Controller.GameController;
import Controller.YutResult;
import Model.DoubledHorse;
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
    private JButton specialThrowButton; //지정던지기 버튼 추가
    private List<JLabel> playerImages = new ArrayList<>();
    private Map<String, Image> horseImages;

    private Map<String, Image> scoreHorseImages;

    private Map<String, Point> horsePositions;

    private Map<Integer, JLabel> horseComponents = new HashMap<>();

    private List<Image> yutImages;
    private List<Image> resultImages;

    private Timer animationTimer;
    private int yutIndex;

    private JLabel notifyingImage;
    private JLabel eventNotifyingImage;

    private Map<Integer, JLabel> waitingHorseLabels = new HashMap<>();
    private int bdouble = 0;
    private int ydouble = 0;
    private int rdouble = 0;
    private int gdouble = 0;

    private int bnum, ynum, rnum, gnum;

    public GameView() {
        setLayout(null);
        horsePositions = new HashMap<>();
        horseImages = new HashMap<>();

        scoreHorseImages = new HashMap<>();

        loadImages();
        initUI();
    }

    private void loadImages() {
        horseImages.clear();
        scoreHorseImages.clear();

        String[] colors = {"red", "blue", "yellow", "green"};

        // horseImage setting
        for (String color : colors) {
            for (int i = 1; i <= 5; i++) {
                String key = color + i;

                Image horseImg = new ImageIcon(getClass().getResource("/image/말 이동/" + color + "/" + i + ".png")).getImage();
                if (horseImg != null) {
                    horseImages.put(key, horseImg);
                }

                Image scoreImg = new ImageIcon(getClass().getResource("/image/스코어 말/" + color + "/" + i + ".png")).getImage();
                if (scoreImg != null) {
                    scoreHorseImages.put(key, scoreImg);
                }
            }
        }

        // 윷 던지는 과정 animation 과정들 사진
        yutImages = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            Image img = new ImageIcon(getClass().getResource("/image/yut/yut" + i + ".png")).getImage();
            if (img != null) {
                yutImages.add(img);
            }
        }
        // 윷 결과 이미지
        resultImages = new ArrayList<>();
        String[] resultImageNames = {"1.png", "2.png", "3.png", "4.png", "5.png", "-1.png"};
        for (String imageName : resultImageNames) {
            Image resultImg = new ImageIcon(getClass().getResource("/image/" + imageName)).getImage();

            if (resultImg != null) {
                resultImages.add(resultImg);
            }
        }
    }

    //버튼 생성 메서드
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

    //기본 세팅
    private void initUI() {
        throwButton = createButton("image/윷 던지기.png", 744, 405);
        add(throwButton);

        //지정던지기 버튼 화면에 띄우기
        specialThrowButton = createButton("image/지정던지기 버튼.png", 940, 405);
        add(specialThrowButton);

//        // Test
//        JButton testEndButton = new JButton("종료 테스트"); //테스트용 버튼 나중에 지워야함
//        testEndButton.setBounds(798, 470, 120, 40);
//        add(testEndButton);
//
//        testEndButton.addActionListener(e -> {
//            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
//            EndView endView = new EndView();
////            endView.setWinner(1); // 예시
//            frame.setContentPane(endView);
//            //
//            frame.pack(); // preferredSize 반영
//            frame.setVisible(true); // 혹시라도 프레임이 안 보이는 경우
//            frame.revalidate();
//        });

        repaint();
    }

    public void setBoardType(String boardType) {
        board = new ImageIcon(getClass().getResource("/image/" + boardType + " board.png")).getImage();
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
            ImageIcon playerIcon = new ImageIcon(getClass().getResource("/image/player" + i + ".png"));
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

        int horseId = 0;

        for (int i = 0; i < playerCount; i++) {
            String color = selectedColors.get(i);
            Point playerHorsePosition = horsePositions[i];

            for (int j = 1; j <= horseCount; j++) {
                String key = color + j;

                Image scoreHorse = scoreHorseImages.get(key);
                if (scoreHorse != null) {
                    JLabel scoreHorseLabel = new JLabel(new ImageIcon(scoreHorse));

                    int horseX = playerHorsePosition.x + (j - 1) * 34;
                    int horseY = playerHorsePosition.y;


                    scoreHorseLabel.setBounds(horseX, horseY, 40, 40);
                    add(scoreHorseLabel);

                    // Map에 저장 (말 ID 기준)
                    waitingHorseLabels.put(horseId, scoreHorseLabel);
                    horseId++;
                }
            }
        }
        repaint();
    }

    // finish처리 된 말 색깔 회색으로 변경
    // 업은 말 들어올 때는 horse_id를 list으로 받거나 해야할 듯
    public void setHorseToGray(int horse_id){
        JLabel horseLabel = waitingHorseLabels.get(horse_id);
        if (horseLabel != null) {
            Image grayImage = new ImageIcon("image/끝난 말.png").getImage();
            horseLabel.setIcon(new ImageIcon(grayImage));
            repaint();
        } else {
            System.out.println("❌ 회색으로 바꿀 horseLabel을 찾지 못함. horseId = " + horse_id);
        }
        repaint();
    }

    // 만약 말이 finish 처리되면 말 하나 사라지게 해야 함 - 이거 어떻게 할지 - 위에 display 그거 음음

    // 처음 말들을 다 만들기
    public void initHorses(List<String> colors, int horseCount) {
        int idCounter = 0;

        for (String color : colors) {
            for (int j = 1; j <= horseCount; j++) {
                String key = color + j;
                Image horseImage = horseImages.get(key);
                if (horseImage != null) {
                    JLabel horseLabel = new JLabel(new ImageIcon(horseImage));

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
    public void mkDoubled(int horse_id, String color, int horseCount, int x, int y) {
        Image horseImage;
        if (horseCount == 2) {
            switch (color) {
                case "blue":
                    bdouble++;
                    horseImage = new ImageIcon(getClass().getResource("/image/업힌 말/" + color + "/" + (horseCount - bdouble % 2) + "개" + ".png")).getImage();
                    break;
                case "yellow":
                    ydouble++;
                    horseImage = new ImageIcon(getClass().getResource("/image/업힌 말/" + color + "/" + (horseCount - ydouble % 2) + "개" + ".png")).getImage();
                    break;
                case "green":
                    gdouble++;
                    horseImage = new ImageIcon(getClass().getResource("/image/업힌 말/" + color + "/" + (horseCount - gdouble % 2) + "개" + ".png")).getImage();
                    break;
                case "red":
                    rdouble++;
                    horseImage = new ImageIcon(getClass().getResource("/image/업힌 말/" + color + "/" + (horseCount - rdouble % 2) + "개" + ".png")).getImage();
                    break;
                default:
                    horseImage = new ImageIcon(getClass().getResource("/image/업힌 말/" + color + "/" + horseCount + "개" + ".png")).getImage();
                    break;
            }
        } else {
            horseImage = new ImageIcon(getClass().getResource("/image/업힌 말/" + color + "/" + horseCount + "개" + ".png")).getImage(); //fallback
        }

        JLabel horseLabel = new JLabel(new ImageIcon(horseImage));
        horseLabel.setBounds(x, y, 40, 40);
        horseLabel.setVisible(true); // 디버깅
        horseComponents.put(horse_id, horseLabel); // model과 연동되는 고유 id = idCounter
        add(horseLabel);
        repaint();
    }

    // horse를 remove하는 함수 - 필요할까? 일단은

    // horse를 setvisible하게 하는 함수
    public void setHorseVisible(int horse_id){
        horseComponents.get(horse_id).setVisible(true);
        repaint();
    }
    // horse를 setInvisible 하는 함수
    public void setHorseInvisible(int horse_id){
        horseComponents.get(horse_id).setVisible(false);
        repaint();
    }

    // horse를 move하는 함수
    public void moveHorse(int horse_id, int x, int y){
        horseComponents.get(horse_id).setLocation(x, y);
        repaint();
    }
    // 혹시 몰라서 여러 개 받으면 이렇게 처리
    public void moveHorse(List<Integer> horse_id_list, int x, int y){
        int i = 0;
        for(Integer horse_id : horse_id_list){
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

    public void setCurrentImage(Image image) {
        currentImage = image;
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

    private void showResultImage(YutResult result) {
       // 화면에 현재 이미지 표시
        Image resultImage = getResultImagePathForYutValue(result);

        if (resultImage != null) {
            setCurrentImage(resultImage);
        }
    }

    //notifyingImage 출력 시간 제어
    // public 으로 바꿈
    public void scheduleNotifyingImage(YutResult result) {
        String imagePath;
        if(result == YutResult.YUT)
            imagePath = "/image/윷 한번더.png";
        else
            imagePath = "/image/모 한번더.png";

        if (notifyingImage != null) {
            remove(notifyingImage);
            notifyingImage = null;
            revalidate();
            repaint();
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    notifyingImage = new JLabel(new ImageIcon(getClass().getResource(imagePath)));
                    notifyingImage.setBounds(291, 294, 519, 113);
                    add(notifyingImage);
                    setComponentZOrder(notifyingImage, 0);

                    revalidate();
                    repaint();
                });

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        SwingUtilities.invokeLater(() -> {
                            if (notifyingImage != null) {
                                remove(notifyingImage);
                                notifyingImage = null;
                                revalidate();
                                repaint();
                            }
                        });
                    }
                }, 1700); //1.1초 뒤에 사라지기
            }
        }, 1600); //윷 결과 출력되고 1초 뒤에 출력
    }

    //잡기/업기 이미지 창
    public void showEventImage(String imagePath) {
        if (eventNotifyingImage != null) {
            remove(eventNotifyingImage);
            eventNotifyingImage = null;
            revalidate();
            repaint();
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    eventNotifyingImage = new JLabel(new ImageIcon(getClass().getResource(imagePath)));
                    eventNotifyingImage.setBounds(291, 294, 519, 113);
                    add(eventNotifyingImage);
                    setComponentZOrder(eventNotifyingImage, 0);

                    revalidate();
                    repaint();
                });

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        SwingUtilities.invokeLater(() -> {
                            if (eventNotifyingImage != null) {
                                remove(eventNotifyingImage);
                                eventNotifyingImage = null;
                                revalidate();
                                repaint();
                            }
                        });
                    }
                }, 800);
            }
        }, 400);
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
                Image bg = new ImageIcon(getClass().getResource("/image/결과 적용.png")).getImage();
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
            }
        };

        System.out.println("전달된 결과 리스트: " + yutResults);
        panel.setLayout(null);
        panel.setOpaque(false);  // 패널도 투명하게
        panel.setBounds(0, 0, 800, 600);

        int spacing = 20;
        int y = 115;

        List<ImageIcon> icons = new ArrayList<>();
        int totalWidth = 0;

        for (YutResult result : yutResults) {
            String imagePath = "/image/선택 윷 결과/선택 " + getKoreanName(result) + ".png";
            ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
            icons.add(icon);
            totalWidth += icon.getIconWidth();
        }

        totalWidth += spacing * (icons.size()-1);

        int panelWidth = dialog.getWidth();
        int x = (panelWidth - totalWidth)/2;

        for (int i = 0; i < yutResults.size(); i++) {
            YutResult result = yutResults.get(i);
            ImageIcon icon = icons.get(i);

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
            x += icon.getIconWidth() + spacing;
        }

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    public void showHorseSelectionDialog(List<Horse> horses, int horseCount, Consumer<Horse> onSelected) {  // horses : selectedHorses, horseCount -> 찐 말의 개수 -> 게임 시작할 때
        int bi = -1;
        int bii = 0;

        int ri = 0;
        int yi = 0;
        int gi = 0;
        int twoHorseCounter = 0;

        JDialog dialog = new JDialog((JFrame) null, "말 선택", true);
        dialog.setSize(665, 298);
        dialog.setLocationRelativeTo(null);
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0, 0, 0, 0));  // 모달 창을 완전히 투명하게

        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image bg = new ImageIcon(getClass().getResource("/image/말 적용.png")).getImage();
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
            }
        };

        panel.setLayout(null);
        panel.setOpaque(false);  // 패널도 투명하게

        int x = 100;
        int y = 100;

        for (Horse horse : horses) {
            try {
                System.out.println("🐎 처리 중인 말 ID: " + horse.id + ", Class: " + horse.getClass().getSimpleName());

                String imagePath = null;

                if (horse.id < 20) {
                    int index = (horse.id % horseCount) + 1;
                    imagePath = "/image/선택 " + horse.color + "/" + index + ".png";
                    System.out.println("📌 일반 말 - 색상: " + horse.color + ", 이미지 경로: " + imagePath);
                } else {
                    // 업힌 말 처리
                    if (!(horse instanceof DoubledHorse)) {
                        System.out.println("⚠️ 오류: ID >= 20인데 DoubledHorse 아님. ID: " + horse.id);
                        continue;
                    }

                    DoubledHorse dh = (DoubledHorse) horse;

                    System.out.println("📦 업힌 말 처리 중 - ID: " + dh.id +
                            ", createdOrder: " + dh.createdOrder +
                            ", horseCount: " + dh.horseCount +
                            ", color: " + dh.color);

                    if (dh.horseCount == 2) {
                        String imageCount;
                        if (dh.createdOrder % 2 == 0) {
                            imageCount = "2개";
                        } else {
                            imageCount = "1개";
                        }
                        System.out.println("🎯 업힌 말 이미지 선택 - createdOrder: " + dh.createdOrder +
                                ", 선택된 이미지: " + imageCount);

                        imagePath = "/image/업힌 말 버튼/" + dh.color + "/" + imageCount + ".png";
                    } else {
                        imagePath = "/image/업힌 말 버튼/" + dh.color + "/" + dh.horseCount + "개.png";
                        System.out.println("🎯 업힌 말 이미지 선택 - horseCount 사용: " + imagePath);
                    }
                }

                // 이미지 경로 확인
                if (imagePath == null) {
                    System.out.println("❌ 이미지 경로 null - 말 ID: " + horse.id);
                    continue;
                }

                java.net.URL imageURL = getClass().getResource(imagePath);
                if (imageURL == null) {
                    System.out.println("❗ 이미지 경로 오류: 존재하지 않음 -> " + imagePath);
                    continue;
                }

                ImageIcon icon = new ImageIcon(imageURL);
                System.out.println("✅ 이미지 로드 성공: " + imagePath);

                JButton btn = new JButton(icon);
                btn.setBounds(x, y, icon.getIconWidth(), icon.getIconHeight());
                btn.setContentAreaFilled(false);
                btn.setBorderPainted(false);
                btn.setFocusPainted(false);

                btn.addActionListener(e -> {
                    System.out.println("🖱️ 선택된 말 ID: " + horse.id);
                    dialog.dispose();
                    onSelected.accept(horse);
                });

                panel.add(btn);
                x += icon.getIconWidth() + 20;
            } catch (Exception ex) {
                System.out.println("❌ 예외 발생 - 말 ID: " + horse.id + ", 메시지: " + ex.getMessage());
                ex.printStackTrace();
            }
        }

//        for (Horse horse : horses) {
//            System.out.println(horse.id);
//            String imagePath;
//            // 여기서 id가 크면은 color.count로 해서 파일 받기
//            if(horse.id < 20) {
//                imagePath = "/image/선택 " + horse.color + "/" + (horse.id % horseCount+1) + ".png";
//                System.out.println(horse.id);
//            }
//            else {
//                // 업은 말 선택
//                DoubledHorse dh = (DoubledHorse) horse;
//                System.out.println("업힌 말 ID: " + dh.id + ", createdOrder: " + dh.createdOrder + ", horseCount: " + dh.horseCount);
//
//                if (dh.horseCount == 2) {
//                    String imageCount;
//                    if (dh.createdOrder % 2 == 0) {
//                        imageCount = "1개";
//                    } else {
//                        imageCount = "2개";
//                    }
//
//                    System.out.println("이미지 선택: " + imageCount);
//
//                    imagePath = "/image/업힌 말 버튼/" + dh.color + "/" + imageCount + ".png";
//                } else {
//                    imagePath = "/image/업힌 말 버튼/" + dh.color + "/" + dh.horseCount + "개.png";
//                }
////                    switch (horse.color) {
////                        case "blue" :
////                            bi++;
////                            bii+=2;
////                            System.out.println("전"+ bdouble + "/" + bi);
////                            imagePath = "/image/업힌 말 버튼/" + horse.color + "/" + (((DoubledHorse)horse).horseCount-(bdouble+bi+bii) % 2) + "개"+ ".png"; // 업힌 말의 (몇 개 업었는지 나타내는 horseCount)
////                            System.out.println("후"+ bdouble + "/" + bi);
////                            break;
////                        case "red" :
////                            ri++;
////                            imagePath = "/image/업힌 말 버튼/" + horse.color + "/" + (((DoubledHorse)horse).horseCount-(rdouble+ri) % 2) + "개"+ ".png";  // 업힌 말의 (몇 개 업었는지 나타내는 horseCount)
////                            break;
////                        case "green" :
////                            gi++;
////                            imagePath = "/image/업힌 말 버튼/" + horse.color + "/" + (((DoubledHorse)horse).horseCount-(gdouble+gi) % 2) + "개"+ ".png";  // 업힌 말의 (몇 개 업었는지 나타내는 horseCount)
////                            break;
////                        case "yellow" :
////                            yi++;
////                            imagePath = "/image/업힌 말 버튼/" + horse.color + "/" + (((DoubledHorse)horse).horseCount-(ydouble+yi) % 2) + "개"+ ".png";  // 업힌 말의 (몇 개 업었는지 나타내는 horseCount)
////                            break;
////                        default :
////                            imagePath = "/image/업힌 말 버튼/" + horse.color + "/" + ((DoubledHorse)horse).horseCount + "개"+ ".png";  // 업힌 말의 (몇 개 업었는지 나타내는 horseCount)
////                            break;
////                    }
////                } else {
////                    imagePath = "/image/업힌 말 버튼/" + horse.color + "/" + ((DoubledHorse) horse).horseCount + "개" + ".png";
////                }// 업힌 말의 (몇 개 업었는지 나타내는 horseCount)
//            }
//            ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
//
//            JButton btn = new JButton(icon);
//            btn.setBounds(x, y, icon.getIconWidth(), icon.getIconHeight());
//            btn.setContentAreaFilled(false);
//            btn.setBorderPainted(false);
//            btn.setFocusPainted(false);
//
//            btn.addActionListener(e -> {
//                dialog.dispose();
//                onSelected.accept(horse);
//            });
//
//            panel.add(btn);
//            x += icon.getIconWidth() + 20;
//        }

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    //지정윷던지기 창 구현
    public void showFixedYutChoiceDialog(Consumer<YutResult> onSelected) {
        JDialog dialog = new JDialog((JFrame) null, "윷 선택", true);
        dialog.setSize(665, 298);
        dialog.setLocationRelativeTo(null);
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0, 0, 0, 0));  // 모달 창을 완전히 투명하게

        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image bg = new ImageIcon(getClass().getResource("/image/결과 적용.png")).getImage();
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
            }
        };
        panel.setLayout(null);
        panel.setOpaque(false);  // 패널도 투명하게

        YutResult[] fixedResults = {
                YutResult.DO, YutResult.GAE, YutResult.GEOL, YutResult.YUT, YutResult.MO, YutResult.BackDo
        };

        int x = 20;
        int y = 110;

        for (YutResult result : fixedResults) {
            String imagePath = "/image/선택 윷 결과/선택 " + getKoreanName(result) + ".png";
            ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));

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

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (board != null) {
            g.drawImage(board, 0, 0, getWidth(), getHeight(), null);
        }

        if (currentImage != null) {
            g.drawImage(currentImage, 670, 40, null);
        }

        for (String color : horsePositions.keySet()) {
            Image horseImage = horseImages.get(color);
            Point position = horsePositions.get(color);

            if (horseImage != null && position != null) {
                g.drawImage(horseImages.get(color), position.x, position.y, 40, 40, null);
            }
        }
    }

    public void addThrowButtonListener(ActionListener listener) {
        throwButton.addActionListener(listener);
    }

    public void addSpecialThrowListener(ActionListener listener) {
        specialThrowButton.addActionListener(listener);
    }

    public JButton getSpecialThrowButton() {
        return specialThrowButton;
    }

    public void resetView() {

        for (JLabel horseLabel : horseComponents.values()) {
            remove(horseLabel);
        }
        horseComponents.clear();

        for (JLabel waiting : waitingHorseLabels.values()) {
            remove(waiting);
        }
        waitingHorseLabels.clear();

        for (JLabel playerLabel : playerImages) {
            remove(playerLabel);
        }
        playerImages.clear();

        currentImage = null;
        notifyingImage = null;
        eventNotifyingImage = null;

        repaint();
    }
}