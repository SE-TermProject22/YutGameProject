package View.Fx.pane;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HorsePane extends AnchorPane {

    private final Map<Integer, ImageView> horseComponents = new HashMap<>();
    private final Map<Integer, ImageView> waitingHorseLabels = new HashMap<>();

    // 말 생성 및 초기 표시
    public void initHorses(List<String> colors, int horseCount, Map<String, Image> horseImages) {
        int idCounter = 0;

        for (String color : colors) {
            for (int j = 1; j <= horseCount; j++) {
                String key = color + j;
                Image horseImage = horseImages.get(key);
                if (horseImage != null) {
                    ImageView horseView = new ImageView(horseImage);
                    horseView.setFitWidth(40);
                    horseView.setFitHeight(40);
                    horseView.setLayoutX(0);
                    horseView.setLayoutY(0);
                    horseView.setVisible(false);
                    horseComponents.put(idCounter, horseView);
                    this.getChildren().add(horseView);
                    idCounter++;
                }
            }
        }
    }

    // 대기 말 표시
    public void displayWaitingHorses(List<String> selectedColors, int playerCount, int horseCount, Map<String, Image> scoreHorseImages) {
        Point2D[] horsePositions = {
                new Point2D(690, 539),
                new Point2D(896, 539),
                new Point2D(690, 615),
                new Point2D(896, 615),
        };

        int horseId = 0;

        for (int i = 0; i < playerCount; i++) {
            String color = selectedColors.get(i);
            Point2D playerHorsePosition = horsePositions[i];

            for (int j = 1; j <= horseCount; j++) {
                String key = color + j;
                Image scoreHorse = scoreHorseImages.get(key);
                if (scoreHorse != null) {
                    ImageView scorehorseView = new ImageView(scoreHorse);
                    double horseX = playerHorsePosition.getX() + (j - 1) * 34;
                    double horseY = playerHorsePosition.getY();

                    scorehorseView.setLayoutX(horseX);
                    scorehorseView.setLayoutY(horseY);

                    this.getChildren().add(scorehorseView);
                    waitingHorseLabels.put(horseId, scorehorseView);
                    horseId++;
                }
            }
        }
    }

    public void setHorseToGray(int horseId) {
        ImageView horseView = waitingHorseLabels.get(horseId);
        if (horseView != null) {
            Image grayImage = new Image(getClass().getResourceAsStream("/image/끝난 말.png"));
            horseView.setImage(grayImage);
        } else {
            System.out.println("❌ 회색으로 바꿀 horseView를 찾지 못함. horseId = " + horseId);
        }
    }

    public void setHorseVisible(int horseId) {
        ImageView horseView = horseComponents.get(horseId);
        if (horseView != null) {
            horseView.setVisible(true);
        }
    }

    public void setHorseInvisible(int horseId) {
        ImageView horseView = horseComponents.get(horseId);
        if (horseView != null) {
            horseView.setVisible(false);
        }
    }

    public void moveHorse(int horseId, int x, int y) {
        ImageView horseView = horseComponents.get(horseId);
        if (horseView != null) {
            horseView.setLayoutX(x);
            horseView.setLayoutY(y);
        }
    }

    // 업힌 말 생성
    public void mkDoubled(int horseId, String color, int horseCount, int x, int y, int imageType) {
        String imagePath;

        if (imageType == 0) {
            imagePath = "/image/업힌 말/" + color + "/2개.png";
        } else if (imageType == 1) {
            imagePath = "/image/업힌 말/" + color + "/1개.png";
        } else {
            imagePath = "/image/업힌 말/" + color + "/" + horseCount + "개.png";
        }

        Image horseImage = new Image(getClass().getResourceAsStream(imagePath));
        ImageView horseView = new ImageView(horseImage);

        AnchorPane.setLeftAnchor(horseView, (double)x);
        AnchorPane.setTopAnchor(horseView, (double)y);

        horseView.setVisible(true);
        horseComponents.put(horseId, horseView);
        this.getChildren().add(horseView);

        System.out.println("업힌말 이미지 경로: " + imagePath);
    }

    public void clear() {
        for (ImageView horseView : horseComponents.values()) {
            this.getChildren().remove(horseView);
        }
        horseComponents.clear();

        for (ImageView waitingHorseView : waitingHorseLabels.values()) {
            this.getChildren().remove(waitingHorseView);
        }
        waitingHorseLabels.clear();
    }
}
