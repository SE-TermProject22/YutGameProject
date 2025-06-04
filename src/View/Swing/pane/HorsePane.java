package View.Swing.pane;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HorsePane {
    private JPanel parentPanel;
    private Map<String, Image> horseImages;
    private Map<String, Image> scoreHorseImages;
    private Map<String, Point> horsePositions;
    private Map<Integer, JLabel> horseComponents = new HashMap<>();
    private Map<Integer, JLabel> waitingHorseLabels = new HashMap<>();

    public HorsePane(JPanel parentPanel) {
        this.parentPanel = parentPanel;
        this.horsePositions = new HashMap<>();
        this.horseImages = new HashMap<>();
        this.scoreHorseImages = new HashMap<>();
        loadImages();
    }

    private void loadImages() {
        horseImages.clear();
        scoreHorseImages.clear();

        String[] colors = {"red", "blue", "yellow", "green"};

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
    }

    public void displayHorses(List<String> selectedColors, int playerCount, int horseCount) {
        Point[] horsePositions = {
                new Point(685, 522),
                new Point(891, 522),
                new Point(685, 598),
                new Point(891, 598),
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
                    parentPanel.add(scoreHorseLabel);

                    waitingHorseLabels.put(horseId, scoreHorseLabel);
                    horseId++;
                }
            }
        }
        parentPanel.repaint();
    }

    public void initHorses(List<String> colors, int horseCount) {
        int idCounter = 0;

        for (String color : colors) {
            for (int j = 1; j <= horseCount; j++) {
                String key = color + j;
                Image horseImage = horseImages.get(key);
                if (horseImage != null) {
                    JLabel horseLabel = new JLabel(new ImageIcon(horseImage));
                    horseLabel.setBounds(0, 0, 40, 40);
                    horseLabel.setVisible(false);

                    horseComponents.put(idCounter, horseLabel);
                    parentPanel.add(horseLabel);
                    idCounter++;
                }
            }
        }
        parentPanel.repaint();
    }

    public void mkDoubled(int horse_id, String color, int horseCount, int x, int y, int imageType) {
        String imagePath;

        if (imageType == 0) {
            imagePath = "/image/업힌 말/" + color + "/2개.png";
        } else if (imageType == 1) {
            imagePath = "/image/업힌 말/" + color + "/1개.png";
        } else {
            imagePath = "/image/업힌 말/" + color + "/" + horseCount + "개.png";
        }

        Image horseImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
        JLabel horseLabel = new JLabel(new ImageIcon(horseImage));
        horseLabel.setBounds(x, y, 40, 40);
        horseLabel.setVisible(true);
        horseComponents.put(horse_id, horseLabel);
        parentPanel.add(horseLabel);
        parentPanel.repaint();
    }

    public void setHorseToGray(int horse_id) {
        String imagePath = "/image/끝난 말.png";
        JLabel horseLabel = waitingHorseLabels.get(horse_id);
        if (horseLabel != null) {
            Image grayImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
            horseLabel.setIcon(new ImageIcon(grayImage));
            parentPanel.repaint();
        } else {
            System.out.println("❌ 회색으로 바꿀 horseLabel을 찾지 못함. horseId = " + horse_id);
        }
        parentPanel.repaint();
    }

    public void setHorseVisible(int horse_id) {
        horseComponents.get(horse_id).setVisible(true);
        parentPanel.repaint();
    }

    public void setHorseInvisible(int horse_id) {
        horseComponents.get(horse_id).setVisible(false);
        parentPanel.repaint();
    }

    public void moveHorse(int horse_id, int x, int y) {
        horseComponents.get(horse_id).setLocation(x, y);
        parentPanel.repaint();
    }

    public void moveHorse(List<Integer> horse_id_list, int x, int y) {
        int i = 0;
        for (Integer horse_id : horse_id_list) {
            horseComponents.get(horse_id_list.get(i)).setLocation(x, y);
            i++;
        }
        parentPanel.repaint();
    }

    public void setHorsePosition(String color, int x, int y) {
        horsePositions.put(color, new Point(x, y));
        parentPanel.repaint();
    }

    public void paintHorses(Graphics g) {
        for (String color : horsePositions.keySet()) {
            Image horseImage = horseImages.get(color);
            Point position = horsePositions.get(color);

            if (horseImage != null && position != null) {
                g.drawImage(horseImages.get(color), position.x, position.y, 40, 40, null);
            }
        }
    }

    public void reset() {
        for (JLabel horseLabel : horseComponents.values()) {
            parentPanel.remove(horseLabel);
        }
        horseComponents.clear();

        for (JLabel waiting : waitingHorseLabels.values()) {
            parentPanel.remove(waiting);
        }
        waitingHorseLabels.clear();

        horsePositions.clear();
    }
}