package View.Fx;

import Controller.YutResult;
import Model.DoubledHorse;
import Model.Horse;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import java.util.function.Consumer;
import java.util.*;

public class GameView extends Pane {
    private ImageView boardView, currentImageView, notifyingImageView;
    private Image board, notifyingImage, currentImage;
    private Button throwButton;
    private Button specialThrowButton;
    private Map<String, Image> horseImages = new HashMap<>();
    private Map<String, Point2D> horsePositions = new HashMap<>();

    private Map<Integer, ImageView> horseComponents = new HashMap<>();
    private List<Image> yutImages = new ArrayList<>();
    private List<Image> resultImages = new ArrayList<>();

    private Timeline animationTimeline;
    private int yutIndex;

    private Map<Integer, ImageView> waitingHorseLabels = new HashMap<>();

    public GameView() {
        setPrefSize(1200, 700);  // 패널 크기
        loadImages();
        initUI();
    }

    private void loadImages() {
        horseImages.clear();

        String[] colors = {"red", "blue", "yellow", "green"};

        for (String color : colors) {
            for (int i = 1; i <= 5; i++) {
                String key = color + i;
                Image image = new Image(getClass().getResourceAsStream("/image/말 이동/" + color + "/" + i + ".png"));
                if (image != null && !image.isError()) {
                    horseImages.put(key, image);
                } else {
                    System.out.println("Failed to load iamge: horse" + colors + ".png");
                }
            }
        }

        yutImages = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            Image image = new Image(getClass().getResourceAsStream("/image/yut/yut" + i + ".png"));
            if (image != null && !image.isError()) {
                yutImages.add(image);
            } else {
                System.out.println("Failed to load image: yut" + i + ".png");
            }
        }

        resultImages = new ArrayList<>();
        String[] resultImageNames = {"1.png", "2.png", "3.png", "4.png", "5.png", "-1.png"};
        for (String imageName : resultImageNames) {
            Image image = new Image(getClass().getResourceAsStream("/image/" + imageName));
            if (image != null && !image.isError()) {
                resultImages.add(image);
            } else {
                System.out.println("Failed to load image: yut" + imageName);
            }
        }
    }

    private Button createButton(String imagePath) {
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        ImageView imageView = new ImageView(image);

        imageView.setFitWidth(image.getWidth());
        imageView.setFitHeight(image.getHeight());

        Button button = new Button("", imageView);
        button.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-border-color: transparnet;" +
                        "-fx-focus-color: transparent;" +
                        "-fx-faint-focus-color: transparent;"
        );

//        AnchorPane.setLeftAnchor(button, x);
//        AnchorPane.setTopAnchor(button, y);

        return button;
    }

    private void initUI() {
        boardView = new ImageView();
        this.getChildren().add(boardView);

        currentImageView = new ImageView();
        currentImageView.setLayoutX(670);
        currentImageView.setLayoutY(40);

        notifyingImageView = new ImageView();
        notifyingImageView.setLayoutX(291);
        notifyingImageView.setLayoutY(294);

        AnchorPane anchorRoot = new AnchorPane();

        throwButton = createButton("/image/윷 던지기.png");
        AnchorPane.setLeftAnchor(throwButton, 744.0);
        AnchorPane.setTopAnchor(throwButton, 405.0);
        anchorRoot.getChildren().add(throwButton);

        // 지정던지기 버튼
        specialThrowButton = createButton("/image/지정던지기 버튼.png");
        AnchorPane.setLeftAnchor(specialThrowButton, 940.0);
        AnchorPane.setTopAnchor(specialThrowButton, 405.0);
        anchorRoot.getChildren().add(specialThrowButton);

        this.getChildren().add(anchorRoot);
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

    public void setBoardType(String boardType) {
        Image board = new Image(getClass().getResourceAsStream("/image/" + boardType + " board.png"));
        boardView.setImage(board);
    }

    public void displayPlayers(int playerCount) {
        Point2D[] playerPositions = {
                new Point2D(692, 502),
                new Point2D(898, 502),
                new Point2D(692, 578),
                new Point2D(898, 578)
        };

        for (int i = 1; i <= playerCount; i++) {
            Image playerImage = new Image(getClass().getResourceAsStream("image/player" + i + ".png"));
            ImageView playerView = new ImageView(playerImage);
            Point2D pos = playerPositions[i-1];

            AnchorPane.setLeftAnchor(playerView, pos.getX());
            AnchorPane.setTopAnchor(playerView, pos.getY());
            this.getChildren().add(playerView);
        }
    }

    public void displayHorses(List<String> selectedColors, int playerCount, int horseCount) {
        Point2D[] horsePositions = {
                new Point2D(682, 522),
                new Point2D(888, 522),
                new Point2D(682, 598),
                new Point2D(888, 598),
        };

        int horseId = 0;

        for (int i = 0; i < playerCount; i++) {
            String color = selectedColors.get(i);
            Point2D playerHorsePosition = horsePositions[i];

            for (int j = 1; j <= horseCount; j++) {
                String key = color + j;
                Image horseImage = horseImages.get(key);
                if (horseImage != null) {
                    ImageView horseView = new ImageView(horseImage);
                    horseView.setFitWidth(40);
                    horseView.setFitHeight(40);

                    double horseX = playerHorsePosition.getX() + (j-1) * 34;
                    double horseY = playerHorsePosition.getY();

                    AnchorPane.setLeftAnchor(horseView, horseX);
                    AnchorPane.setTopAnchor(horseView, horseY);
                    this.getChildren().add(horseView);

                    waitingHorseLabels.put(horseId, horseView);
                    horseId++;
                }
            }
        }
    }

    public void initHorses(List<String> colors, int horseCount) {
        int idCounter = 0;

        for (String color : colors) {
            for (int j = 1; j <= horseCount; j++) {
                String key = color + j;
                Image horseImage = horseImages.get(key);
                if (horseImage != null) {
                    ImageView horseView = new ImageView(horseImage);

                    horseView.setFitWidth(40);
                    horseView.setFitHeight(40);

                    AnchorPane.setLeftAnchor(horseView, 0.0);
                    AnchorPane.setTopAnchor(horseView, 0.0);

                    horseView.setVisible(false);
                    horseComponents.put(idCounter, horseView);
                    this.getChildren().add(horseView);

                    idCounter++;
                }
            }
        }
    }

    public void mkDoubled(int horse_id, String color, int horseCount, int x, int y) {
        Image horseImage = new Image(getClass().getResourceAsStream("image/업힌 말/" + color + "/" + horseCount + "개" + ".png"));
        ImageView horseView = new ImageView(horseImage);

        horseView.setFitWidth(40);
        horseView.setFitHeight(40);

        AnchorPane.setLeftAnchor(horseView, (double)x);
        AnchorPane.setTopAnchor(horseView, (double)y);

        horseView.setVisible(true);
        horseComponents.put(horse_id, horseView);
        this.getChildren().add(horseView);
    }

    public void setHorseToGray(int horse_id) {
        ImageView horseView = waitingHorseLabels.get(horse_id);
        if (horseView != null) {
            Image grayImage = new Image(getClass().getResourceAsStream("image/끝난 말.png"));
            horseView.setImage(grayImage);
        } else {
            System.out.println("❌ 회색으로 바꿀 horseView를 찾지 못함. horseId = " + horse_id);
        }
    }

    public void setHorseVisible(int horse_id) {
        ImageView horseView = horseComponents.get(horse_id);
        if (horseView != null) {
            horseView.setVisible(true);
        }
    }

    public void setHorseInvisible(int horse_id) {
        ImageView horseView = horseComponents.get(horse_id);
        if (horseView != null) {
            horseView.setVisible(false);
        }
    }

    public void moveHorse(int horse_id, int x, int y) {
        ImageView horseView = horseComponents.get(horse_id);
        if (horseView != null) {
            horseView.setLayoutX(x);
            horseView.setLayoutY(y);
        }
    }

    public void setHorsePosition(String color, int x, int y) {
        horsePositions.put(color, new Point2D(x, y));
    }

    public void startYutAnimation(YutResult result) {
        yutIndex = 0;

        if (animationTimeline != null) {
            animationTimeline.stop();
        }

        animationTimeline = new Timeline(new KeyFrame(Duration.millis(300), e -> {
            if (yutIndex < yutImages.size()) {
                currentImageView.setImage(yutImages.get(yutIndex));
                yutIndex++;
            } else {
                animationTimeline.stop();
                showResultImage(result);
            }
        }));
        animationTimeline.setCycleCount(Timeline.INDEFINITE);
        animationTimeline.play();
    }

    private void showResultImage(YutResult result) {
        Image resultImage = getResultImagePathForYutValue(result);
        if (resultImage != null) {
            currentImageView.setImage(resultImage);
        }
    }

    public void scheduleNotifyingImage(YutResult result) {
        String imagePath;
        if (result == YutResult.YUT) {
            imagePath = "image/윷 한번더.png";
        } else {
            imagePath = "image/모 한번더.png";
        }

        PauseTransition delayBeforeShow = new PauseTransition(Duration.seconds(1));
        delayBeforeShow.setOnFinished(event -> {
            notifyingImage = new Image(getClass().getResourceAsStream(imagePath));
            notifyingImageView.setImage(notifyingImage);

            PauseTransition delayBeforeClear = new PauseTransition(Duration.seconds(1.1));
            delayBeforeClear.setOnFinished(e -> {
                notifyingImage = null;
                notifyingImageView.setImage(null);
            });
            delayBeforeClear.play();
        });
        delayBeforeShow.play();
    }

    private Image getResultImagePathForYutValue(YutResult result) {
        switch (result) {
            case DO:
                return resultImages.get(0);
            case GAE:
                return resultImages.get(1);
            case GEOL:
                return resultImages.get(2);
            case YUT:
                return resultImages.get(3);
            case MO:
                return resultImages.get(4);
            case BackDo:
                return resultImages.get(5);
            default:
                return null;
        }
    }

    public void addThrowButtonListener(EventHandler<ActionEvent> handler) {
        throwButton.setOnAction(handler);
    }

    public void addSpecialThrowListener(EventHandler<ActionEvent> handler) {
        specialThrowButton.setOnAction(handler);
    }

    public void setCurrentImage(Image image) {
        currentImageView.setImage(image);
    }

    public void showYutResultChoiceDialog(List<YutResult> yutResults, Consumer<YutResult> onSelected) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.TRANSPARENT);

        AnchorPane root = new AnchorPane();
        root.setPrefSize(665, 298);
        root.setStyle("-fx-background-color: transparent;");

        //모달창 배경
        Image modalImage = new Image(getClass().getResourceAsStream("/image/결과 적용.png"));
        ImageView modalView = new ImageView(modalImage);
        modalView.setFitHeight(665);
        modalView.setFitHeight(298);
        root.getChildren().add(modalView);

        double x = 100;
        double y = 100;

        for (YutResult result : yutResults) {
            String imagePath = "/image/선택 윷 결과/선택 " + getKoreanName(result) + ".png";
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            ImageView imageView = new ImageView(image);

            Button btn = new Button();
            btn.setGraphic(imageView);
            btn.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
            btn.setLayoutX(x);
            btn.setLayoutY(y);

            btn.setOnAction(e -> {
                dialog.close();
                onSelected.accept(result);
            });
            root.getChildren().add(btn);
            x += image.getWidth() + 20;
        }
        Scene scene = new Scene(root, 665, 298);
        scene.setFill(Color.TRANSPARENT);

        dialog.setScene(scene);
        dialog.showAndWait();
    }

    public void showHorseSelectionDialog(List<Horse> horses, int horseCount, Consumer<Horse> onSelected) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.TRANSPARENT);

        AnchorPane root = new AnchorPane();
        root.setPrefSize(665, 298);
        root.setStyle("-fx-background-color: transparent;");

        Image modalImage = new Image(getClass().getResourceAsStream("/image/말 적용.png"));
        ImageView modalView = new ImageView(modalImage);
        modalView.setFitWidth(665);
        modalView.setFitHeight(298);
        root.getChildren().add(modalView);

        double x = 100;
        double y = 100;

        for (Horse horse : horses) {
            String imagePath;
            if (horse.id < 20) {
                imagePath = "image/선택 " + horse.color + "/" + (horse.id % horseCount + 1) + ".png";
                System.out.println(horse.id);
            } else {
                imagePath = "image/업힌 말 버튼/" + horse.color + "/" + ((DoubledHorse) horse).horseCount + "개.png";
            }

            Image image = new Image(getClass().getResourceAsStream(imagePath));
            ImageView imageView = new ImageView(image);

            Button btn = new Button();
            btn.setGraphic(imageView);
            btn.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
            btn.setLayoutX(x);
            btn.setLayoutY(y);

            btn.setOnAction(e -> {
                dialog.close();  // FX에서 창 닫기
                onSelected.accept(horse);
            });

            root.getChildren().add(btn);
            x += image.getWidth() + 20;  // 버튼 간 간격 조정
        }


        Scene scene = new Scene(root, 665, 298);
        scene.setFill(null);

        dialog.setScene(scene);
        dialog.showAndWait();
    }

    public void showFixedYutChoiceDialog(Consumer<YutResult> onSelected) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.TRANSPARENT);

        AnchorPane root = new AnchorPane();
        root.setPrefSize(665, 298);
        root.setStyle("-fx-background-color: transparent");

        Image modalImage = new Image(getClass().getResourceAsStream("/image/결과 적용.png"));
        ImageView modalImageView = new ImageView(modalImage);
        modalImageView.setFitWidth(665);
        modalImageView.setFitHeight(298);
        root.getChildren().add(modalImageView);

        YutResult[] fixedResults = {
                YutResult.DO, YutResult.GAE, YutResult.GEOL, YutResult.YUT, YutResult.MO, YutResult.BackDo
        };

        double x = 20;
        double y = 110;

        for (YutResult result : fixedResults) {
            String imagePath = "image/선택 윷 결과/선택 " + getKoreanName(result) + ".png";
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            ImageView imageView = new ImageView(image);

            Button btn = new Button();
            btn.setGraphic(imageView);
            btn.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
            btn.setLayoutX(x);
            btn.setLayoutY(y);

            btn.setOnAction(e -> {
                dialog.close();  // FX에서 창 닫기
                onSelected.accept(result);
            });

            root.getChildren().add(btn);
            x += image.getWidth() + 20;  // 버튼 간 간격 조정
        }


        Scene scene = new Scene(root, 665, 298);
        scene.setFill(null);

        dialog.setScene(scene);
        dialog.showAndWait();
    }

    public Button getSpecialThrowButton() {
        return specialThrowButton;
    }
}