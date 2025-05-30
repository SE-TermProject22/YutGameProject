package View.Fx;

import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.Objects;

public class EndView extends AnchorPane {
    private AnchorPane anchorRoot;

    private Button restartButton;
    private Button exitButton;

    private ImageView background, winnerView;
    private Image endBackground;
    private Image[] winnerImages = new Image[4];
    private int winnerId = 1;

    public EndView() {
        loadImages();

        anchorRoot = new AnchorPane();

        background = new ImageView();
        winnerView = new ImageView();
        winnerView.setVisible(false);

        anchorRoot.getChildren().addAll(background, winnerView);

        initUI();
    }

    private void loadImages() {
        try {
            endBackground = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/종료 화면.png")));
            for (int i = 0; i < 4; i++) {
                winnerImages[i] = new Image(getClass().getResourceAsStream("/image/Winner" + (i + 1) + ".png"));
            }
        } catch (Exception e) {
            System.err.println("이미지 로드 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }

//    private void loadImages() {
//        endBackground = new Image(getClass().getResourceAsStream("/image/종료 화면.png"));
//        for (int i = 0; i < 4; i++) {
//            winnerImages[i] = new Image(getClass().getResourceAsStream("/image/Winner" + (i + 1) + ".png"));
//        }
//    }

    private Button createButton(String imagePath) {
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        ImageView imageView = new ImageView(image);

        imageView.setFitWidth(image.getWidth());
        imageView.setFitHeight(image.getHeight());

        Button button = new Button("", imageView);
        button.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-border-color: transparent;" +
                        "-fx-focus-color: transparent;" +
                        "-fx-faint-focus-color: transparent;"
        );
        return button;
    }

    private void initUI() {
        // 버튼 생성 및 이미지 설정
        restartButton = createButton("/image/재시작버튼.png");
        AnchorPane.setLeftAnchor(restartButton, 573.0);
        AnchorPane.setTopAnchor(restartButton, 84.0);
        anchorRoot.getChildren().add(restartButton);

        exitButton = createButton("/image/종료버튼.png");
        AnchorPane.setLeftAnchor(exitButton, 353.0);
        AnchorPane.setTopAnchor(exitButton, 84.0);
        anchorRoot.getChildren().add(exitButton);

        this.getChildren().add(anchorRoot);
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

    public void updateEndView(Image endBackground, Image[] winnerImages, int winnerId) {
        background.setImage(endBackground);

        if (winnerId >= 1 && winnerId <= winnerImages.length) {
            winnerView.setImage(winnerImages[winnerId-1]);
            winnerView.setVisible(true);
            AnchorPane.setLeftAnchor(winnerView, 440.0);
            AnchorPane.setTopAnchor(winnerView, 455.0);
        } else {
            winnerView.setVisible(false);
        }
    }
}