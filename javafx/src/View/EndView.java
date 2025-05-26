package View;

import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class EndView extends AnchorPane {
    private AnchorPane anchorRoot;
    private AnchorPane boardPane;
    private AnchorPane horsePane;

    private Button restartButton;
    private Button exitButton;

    private ImageView background, winnerView;
    private Image endBackground;
    private Image[] winnerImages = new Image[4];
    private int winnerId = 1;

    private Canvas canvas;

    public EndView() {
        loadImages();

        anchorRoot = new AnchorPane();
        boardPane = new AnchorPane();
        horsePane = new AnchorPane();

        background = new ImageView();
        winnerView = new ImageView();
        winnerView.setVisible(false);

        anchorRoot.getChildren().addAll(background, winnerView, boardPane, horsePane);

        initUI();
    }

    private void loadImages() {
        endBackground = new Image(getClass().getResourceAsStream("image/종료 화면.png"));
        for (int i = 0; i < 4; i++) {
            winnerImages[i] = new Image(getClass().getResourceAsStream("image/Winner" + (i + 1) + ".png"));
        }
    }

    private Button createButton(String imagePath, double x, double y) {
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

        AnchorPane.setLeftAnchor(button, x);
        AnchorPane.setTopAnchor(button, y);

        return button;
    }

    private void initUI() {
        // 버튼 생성 및 이미지 설정
        restartButton = createButton("image/재시작버튼.png", 573, 84);
        exitButton = createButton("image/종료버튼.png", 353, 84);

        anchorRoot.getChildren().addAll(restartButton, exitButton);
    }

    public void addRestartButtonListener(EventHandler<ActionEvent> handler) {
        restartButton.setOnAction(handler);
    }

    public void addExitButtonListener(EventHandler<ActionEvent> handler) {
        exitButton.setOnAction(handler);
    }

    public void setWinner(int playerId) {
        this.winnerId = playerId;
        updateEndView(endBackground, winnerImages, winnerId);
    }

    public void clearBoard() {
        boardPane.getChildren().clear();
    }

    public void clearHorses() {
        horsePane.getChildren().clear();
    }

    public void updateEndView(Image endBackground, Image[] winnerImages, int winnerId) {
        background.setImage(endBackground);

        if (winnerId >= 1 && winnerId <= winnerImages.length) {
            winnerView.setImage(winnerImages[winnerId-1]);
            winnerView.setVisible(true);
            AnchorPane.setLeftAnchor(winnerView, 516.0);
            AnchorPane.setTopAnchor(winnerView, 583.0);
        } else {
            winnerView.setVisible(false);
        }
    }
}