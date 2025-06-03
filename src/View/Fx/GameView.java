package View.Fx;

import Controller.YutResult;
import Model.Horse;

import View.Fx.animation.NotificationView;
import View.Fx.animation.YutAnimationView;
import View.Fx.pane.BoardPane;
import View.Fx.pane.HorsePane;
import View.Fx.pane.PlayerPane;
import View.Fx.pane.ThrowButtonPane;
import View.Fx.dialog.HorseSelectionDialog;
import View.Fx.dialog.FixedYutChoiceDialog;
import View.Fx.dialog.YutResultChoiceDialog;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.function.Consumer;
import java.util.*;

public class GameView extends AnchorPane {

    private Map<String, Image> horseImages = new HashMap<>();
    private Map<String, Image> scoreHorseImages = new HashMap<>();
    private Map<String, Point2D> horsePositions = new HashMap<>();

    //
    private HorsePane horsePane;
    private ThrowButtonPane throwButtonPane;
    private PlayerPane playerPane;
    private BoardPane boardPane;
    private YutAnimationView yutAnimationView;
    private NotificationView notificationView;

    public GameView() {
        loadImages();
        initUI();
    }

    private void loadImages() {
        horseImages.clear();
        scoreHorseImages.clear();

        String[] colors = {"red", "blue", "yellow", "green"};

        for (String color : colors) {
            for (int i = 1; i <= 5; i++) {
                String key = color + i;

                Image horseImg = new Image(getClass().getResourceAsStream("/image/말 이동/" + color + "/" + i + ".png"));
                if (horseImg != null && !horseImg.isError()) {
                    horseImages.put(key, horseImg);
                }

                Image scoreImg = new Image(getClass().getResourceAsStream("/image/스코어 말/" + color + "/" + i + ".png"));
                if (scoreImg != null && !scoreImg.isError()) {
                    scoreHorseImages.put(key, scoreImg);
                }
            }
        }
    }

    private void initUI() {
//        boardView = new ImageView();
//        this.getChildren().add(boardView);

        horsePane = new HorsePane();
        this.getChildren().add(horsePane);

        throwButtonPane = new ThrowButtonPane();
        this.getChildren().add(throwButtonPane);

        playerPane = new PlayerPane();
        this.getChildren().add(playerPane);

        boardPane = new BoardPane();
        this.getChildren().add(boardPane);

        yutAnimationView = new YutAnimationView();
        this.getChildren().add(yutAnimationView);

        notificationView = new NotificationView();
        this.getChildren().add(notificationView);

        boardPane.toBack();  // 가장 뒤로 보내서 말, 버튼 위에 겹치지 않게
        throwButtonPane.toFront(); //이거 추가해야지 윷 던지기 버튼 눌림

    }

    public void setBoardType(String boardType) {
        boardPane.setBoardImage(boardType);
    }

    //HorsePane 파일
    public void displayHorses(List<String> selectedColors, int playerCount, int horseCount) {
        horsePane.displayWaitingHorses(selectedColors, playerCount, horseCount, scoreHorseImages);
    }

    public void initHorses(List<String> colors, int horseCount) {
        horsePane.initHorses(colors, horseCount, horseImages);
    }

    public void setHorseToGray(int horseId) {
        horsePane.setHorseToGray(horseId);
    }

    public void mkDoubled(int horse_id, String color, int horseCount, int x, int y, int imageType) {
        horsePane.mkDoubled(horse_id, color, horseCount, x, y, imageType);
    }

    public void clearHorses() {
        horsePane.clear();
    }


    public void setHorseVisible(int horse_id) {
        horsePane.setHorseVisible(horse_id);
    }

    public void setHorseInvisible(int horse_id) {
        horsePane.setHorseInvisible(horse_id);
    }

    public void moveHorse(int horse_id, int x, int y) {
        horsePane.moveHorse(horse_id, x, y);
    }

    public void setHorsePosition(String color, int x, int y) {
        horsePositions.put(color, new Point2D(x, y));
    }

    //throwButtonpane 파일
    public void addThrowButtonListener(EventHandler<ActionEvent> handler) {
        throwButtonPane.setThrowButtonHandler(handler);
    }

    public void addSpecialThrowListener(EventHandler<ActionEvent> handler) {
        throwButtonPane.setSpecialThrowButtonHandler(handler);
    }

    public Button getSpecialThrowButton() {
        return throwButtonPane.getSpecialThrowButton();
    }

    //playerPane
    public void displayPlayers(int playerCount) {
        playerPane.displayPlayers(playerCount);
    }

    public void clearPlayers() {
        playerPane.clearPlayers();
    }

    //YutAnimation
    public void startYutAnimation(YutResult result) {
        yutAnimationView.startAnimation(result);
    }

    public void setCurrentImage(YutResult result) {
        yutAnimationView.setResultDirectly(result);
    }

    //Notification
    public void scheduleNotifyingImage(YutResult result) {
        notificationView.showThrowAgain(result);
    }

    public void showEventImage(String path) {
        notificationView.showEvent(path);
    }

    //
    public void showHorseSelectionDialog(List<Horse> horses, int horseCount, Consumer<Horse> onSelected) {
        HorseSelectionDialog.show(horses, horseCount, onSelected);
    }

    //
    public void showFixedYutChoiceDialog(Consumer<YutResult> onSelected) {
        FixedYutChoiceDialog.show(onSelected);
    }

    //
    public void showYutResultChoiceDialog(List<YutResult> yutResults, Consumer<YutResult> onSelected) {
        YutResultChoiceDialog.show(yutResults, onSelected);
    }
}