package View.Swing;

import Controller.YutResult;
import Model.Horse;
import View.Swing.animation.NotificationView;
import View.Swing.animation.YutAnimationView;
import View.Swing.pane.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

public class GameView extends JPanel {
    // 기본 구성 요소
    private final JPanel layeredPane;

    private final BoardPane boardPane;
    private final HorsePane horsePane;
    private final PlayerPane playerPane;
    private final ThrowButtonPane throwButtonPane;
    private final YutAnimationView yutAnimationView;
    private final NotificationView notificationView;

    public GameView() {
        setLayout(null);
        setPreferredSize(new Dimension(1100, 700));

        // layeredPane: 모든 하위 컴포넌트 담는 패널
        layeredPane = new JPanel(null);
        layeredPane.setBounds(0, 0, 1100, 700);
        layeredPane.setOpaque(false); // 투명하게 설정하여 배경 보드가 보이도록
        add(layeredPane);

        // Pane 및 View 구성 요소 초기화
        boardPane = new BoardPane();
        horsePane = new HorsePane(layeredPane);
        playerPane = new PlayerPane(layeredPane);
        throwButtonPane = new ThrowButtonPane(layeredPane);
        yutAnimationView = new YutAnimationView(layeredPane);
        notificationView = new NotificationView(layeredPane);

//        setBackground(Color.WHITE);
    }

    // board
    public void setBoardType(String boardType) {
        boardPane.setBoardType(boardType);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        boardPane.paintBoard(g, getWidth(), getHeight());

        Image animImage = yutAnimationView.getCurrentImage();
        if (animImage != null) {
            g.drawImage(animImage, 670, 40, null);
        }
    }

    //player
    public void displayPlayers(int playerCount) {
        playerPane.displayPlayers(playerCount);
    }

    // horse
    public void displayHorses(List<String> colors, int playerCount, int horseCount) {
        horsePane.displayHorses(colors, playerCount, horseCount);
    }

    public void initHorses(List<String> colors, int horseCount) {
        horsePane.initHorses(colors, horseCount);
    }

    public void moveHorse(int horseId, int x, int y) {
        horsePane.moveHorse(horseId, x, y);
    }

    public void moveHorse(List<Integer> horseIds, int x, int y) {
        horsePane.moveHorse(horseIds, x, y);
    }

    public void setHorseVisible(int horseId) {
        horsePane.setHorseVisible(horseId);
    }

    public void setHorseInvisible(int horseId) {
        horsePane.setHorseInvisible(horseId);
    }

    public void setHorseToGray(int horseId) {
        horsePane.setHorseToGray(horseId);
    }

    public void mkDoubled(int horseId, String color, int count, int x, int y, int type) {
        horsePane.mkDoubled(horseId, color, count, x, y, type);
    }

    public void resetView() {
        horsePane.reset();
        playerPane.reset();
        yutAnimationView.reset();
        notificationView.reset();
    }

    // throwButtonpane
    public void addThrowButtonListener(java.awt.event.ActionListener listener) {
        throwButtonPane.addThrowButtonListener(listener);
    }

    public void addSpecialThrowButtonListener(java.awt.event.ActionListener listener) {
        throwButtonPane.addSpecialThrowListener(listener);
    }

    // dialog
    public void showHorseSelectionDialog(List<Horse> horses, int horseCount, Consumer<Horse> onSelected) {
        View.Swing.dialog.HorseSelectionDialog.show(horses, horseCount, onSelected);
    }

    public void showYutResultChoiceDialog(List<YutResult> yutResults, Consumer<YutResult> onSelected) {
        View.Swing.dialog.YutResultChoiceDialog.show(yutResults, onSelected);
    }

    public void showFixedYutChoiceDialog(Consumer<YutResult> onSelected) {
        View.Swing.dialog.FixedYutChoiceDialog.show(onSelected);
    }

    // animation
    public void startYutAnimation(YutResult result) {
        yutAnimationView.startYutAnimation(result);
    }

    public void scheduleNotifyingImage(YutResult result) {
        notificationView.scheduleNotifyingImage(result);
    }

    public void showEventImage(String imagePath) {
        notificationView.showEventImage(imagePath);
    }
}
