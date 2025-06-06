package View.Fx;

import Controller.GameState;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.scene.layout.*;

import java.util.*;
import java.util.List;

public class StartView extends StackPane {
    private ImageView background;
    private Image startBackground, horseSelectionBackground, boardSelectionBackground;

    private AnchorPane anchorRoot;
    private Button startButton, nextButton;
    private Button squareBtn, pentagonBtn, hexagonBtn;

    private ComboBox<String> playerCountBox, horseCountBox;
    private Map<String, Button> horseButtons;

    private AnchorPane contentPane;

    private String selectedBoard = null;
    private List<String> selectedColors = new ArrayList<>();

    private GameState currentState = GameState.START_SCREEN;

    public StartView() {
        loadImages();

        anchorRoot = new AnchorPane();

        background = new ImageView(startBackground);
        anchorRoot.getChildren().add(background);

        setupInitialView();

        contentPane = anchorRoot;
        this.getChildren().add(anchorRoot);

        setState(currentState);

        startButton.setVisible(true);
        startButton.setDisable(false);
        startButton.setOnAction(e -> System.out.println("✅ 눌림!"));
    }

    private void loadImages() {
        try {
            startBackground = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/시작 화면.png")));
            horseSelectionBackground = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/말 선택.png")));
            boardSelectionBackground = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/판 선택.png")));

        } catch (Exception e) {
            System.err.println("이미지 로드 실패: " + e.getMessage());
            e.printStackTrace();
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
                        "-fx-border-color: transparent;" +
                        "-fx-focus-color: transparent;" +
                        "-fx-faint-focus-color: transparent;"
        );

        button.setLayoutX(x);
        button.setLayoutY(y);

        // 버튼 눌리는 효과 적용
        button.setOnMousePressed(e -> imageView.setOpacity(0.7));
        button.setOnMouseReleased(e -> imageView.setOpacity(1.0));

        AnchorPane.setLeftAnchor(button, x);
        AnchorPane.setTopAnchor(button, y);

        return button;
    }

    private void setupInitialView() {
        anchorRoot = new AnchorPane();

        // 배경을 먼저 추가
        anchorRoot.getChildren().add(background);

        startButton = createButton("/image/시작 버튼.png", 386, 420);
        anchorRoot.getChildren().add(startButton);

        squareBtn = createButton("/image/사각형.png", 50, 217);
        pentagonBtn = createButton("/image/오각형.png", 396, 217);
        hexagonBtn = createButton("/image/육각형.png", 742, 217);
        anchorRoot.getChildren().addAll(squareBtn, pentagonBtn, hexagonBtn);

        playerCountBox = new ComboBox<>();
        playerCountBox.getItems().addAll("2", "3", "4");
        playerCountBox.setVisible(false);
        playerCountBox.setPrefSize(280, 71);
        AnchorPane.setLeftAnchor(playerCountBox, 200.0);
        AnchorPane.setTopAnchor(playerCountBox, 265.0);
        anchorRoot.getChildren().add(playerCountBox);

        horseCountBox = new ComboBox<>();
        horseCountBox.getItems().addAll("2", "3", "4", "5");
        horseCountBox.setVisible(false);
        horseCountBox.setPrefSize(280, 71);
        AnchorPane.setLeftAnchor(horseCountBox, 621.0);
        AnchorPane.setTopAnchor(horseCountBox, 265.0);
        anchorRoot.getChildren().add(horseCountBox);

        horseButtons = new HashMap<>();
        addHorseButton("red", 148);
        addHorseButton("blue", 363);
        addHorseButton("yellow", 578);
        addHorseButton("green", 793);

        nextButton = createButton("/image/다음.png", 432, 575);
        nextButton.setVisible(false);
        anchorRoot.getChildren().add(nextButton);
    }

    private void addHorseButton(String color, double x) {
        Button btn = createButton("/image/" + color + " 말.png", x, 401);
        btn.setOnAction(e -> toggleHorseSelection(color)); //color 값을 그대로 전달
        btn.setVisible(false);
        horseButtons.put(color, btn);
        anchorRoot.getChildren().add(btn);
    }

    public void setState(GameState state) {
        this.currentState = state;

        switch (state) {
            case START_SCREEN:
                background.setImage(startBackground);
                showOnly(startButton);
                break;
            case HORSE_SELECTION:
                background.setImage(horseSelectionBackground);
                showOnly(horseButtons.values().toArray(new Button[0]));
                startButton.setVisible(false);
                playerCountBox.setVisible(true);
                horseCountBox.setVisible(true);
                break;
            case BOARD_SELECTION:
                background.setImage(boardSelectionBackground);
                showOnly(squareBtn, pentagonBtn, hexagonBtn, nextButton);
                startButton.setVisible(false);
                break;
        }
    }

    private void showOnly(Node... visibleNodes) {
        Set<Node> visibleSet = new HashSet<>(Arrays.asList(visibleNodes));
        visibleSet.add(background);
        visibleSet.add(startButton);

        for (Node node : contentPane.getChildren()) {
            node.setVisible(visibleSet.contains(node));
        }
    }

    public void addStartButtonListener(EventHandler<ActionEvent> handler) {
        startButton.setOnAction(handler);
    }

    public void addNextButtonListener(EventHandler<ActionEvent> handler) {
        nextButton.setOnAction(handler);
    }

    public void setBoardSelectionListeners(EventHandler<ActionEvent> square, EventHandler<ActionEvent> pentagon, EventHandler<ActionEvent> hexagon) {
        squareBtn.setOnAction(square);
        pentagonBtn.setOnAction(pentagon);
        hexagonBtn.setOnAction(hexagon);
    }

    public void setHorseSelectionListener(EventHandler<ActionEvent> handler) {
        for (Button btn : horseButtons.values()) {
            btn.setOnAction(handler);
        }
    }

    public int getPlayerCount() {
        return Integer.parseInt(playerCountBox.getValue());
    }

    public int getHorseCount() {
        return Integer.parseInt(horseCountBox.getValue());
    }

    public void selectBoard(String boardType) {
        this.selectedBoard = boardType;
    }

    public void toggleHorseSelection(String color) {
        if (selectedColors.contains(color)) {
            selectedColors.remove(color);
        } else if (selectedColors.size() < getPlayerCount()) {
            selectedColors.add(color);
        }
    }

    public List<String> getSelectedColors() {
        return selectedColors;
    }

    public String getSelectedBoard() {
        return selectedBoard;
    }

    public Map<String, Button> getHorseButtons() {
        return horseButtons;
    }

    public void resetSelection() {
        selectedColors.clear();
        selectedBoard = null;

        playerCountBox.getSelectionModel().clearSelection();
        horseCountBox.getSelectionModel().clearSelection();

        for (Button btn : horseButtons.values()) {
            btn.setDisable(false);
            btn.setVisible(false);
            btn.setStyle("");
        }

//         말 선택 버튼들의 선택 상태만 해제 (배경 스타일은 유지)
        for (Map.Entry<String, Button> entry : horseButtons.entrySet()) {
            Button button = entry.getValue();

            // 선택 효과만 제거하고 기존 배경 스타일은 유지
            button.getStyleClass().remove("selected"); // 선택 클래스만 제거

            // 원래 배경 제거 스타일 다시 적용
            if (!button.getStyle().contains("-fx-background-color: transparent")) {
                button.setStyle(button.getStyle() + "; -fx-background-color: transparent;");
            }
        }

        squareBtn.setVisible(false);
        pentagonBtn.setVisible(false);
        hexagonBtn.setVisible(false);
        nextButton.setVisible(false);

        setState(GameState.START_SCREEN);
    }
}