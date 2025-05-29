package Controller;

import View.Interface.IEndView;
import View.Interface.IStartView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.awt.event.ActionListener;

public class GameController {
    public enum PlatformType {
        SWING,
        FX
    }

    private PlatformType platform;

    private final IStartView startView;
    private final IEndView endView;

    // Swing 관련 변수
    private javax.swing.JFrame frame;
    private View.Swing.StartView startViewSwing;
    private View.Swing.GameView gameViewSwing;
    private View.Swing.EndView endViewSwing;

    // JavaFX 관련 변수
    private javafx.stage.Stage primaryStage;
    private View.Fx.StartView startViewFx;
    private View.Fx.GameView gameViewFx;
    private View.Fx.EndView endViewFx;
    private javafx.scene.layout.StackPane mainStackPane;

    // 게임 상태 및 데이터 공통 변수 (대략적인 예)
    private Object currentPlayer;
    private java.util.List<Object> players;
    private java.util.List<Object> horses;
    private int horseCount;
    private int playerCount;
    private boolean throwState;
    private java.util.List<Object> yutList;
    private int turn;
    private int d_init;

    private GameState currentState;

    public GameController(IStartView startView, IEndView endView) {
        this.frame = frame;
        this.startView = startView;
        this.endView = endView;

        if (endView instanceof View.Swing.EndView) {
            this.platform = PlatformType.SWING;
        } else if (endView instanceof View.Fx.EndView) {
            this.platform = PlatformType.FX;
        }

        initializeListeners();
    }

    private void initializeListeners() {
        if (endView instanceof View.Swing.EndView) {
            endView.addExitButtonListener((ActionListener) e -> {
                System.exit(0);
            });
            endView.addRestartButtonListener((ActionListener) e -> {
                restartGame();
            });
        } else if (endView instanceof View.Fx.EndView) {
            endView.addExitButtonListener((EventHandler<ActionEvent>) e -> {
                Platform.exit();
            });
            endView.addRestartButtonListener((EventHandler<ActionEvent>) e -> {
                resetGameData();
                ((View.Fx.StartView) startViewFx).resetGame();
            });
        }
    }

    private boolean isSwing() {
        return platform == PlatformType.SWING;
    }

    private boolean isFx() {
        return platform == PlatformType.FX;
    }

    private void setState(GameState newState) {
        currentState = newState;
        updateViewState();
    }

    private void updateViewState() {
        if (startViewSwing != null) {
            startViewSwing.setState(currentState);
        }
        if (startViewFx != null) {
            startViewFx.setState(currentState);
        }
    }

    private void restartGame() {
        resetGameData();

        if (isSwing()) {
            startView.resetUI();
        } else if (isFx()) {
            startView.resetUI();
        }
    }

    private void resetGameData() {
        currentPlayer = null;
        players.clear();
        horses.clear();
        horseCount = 0;
        playerCount = 0;
        throwState = true;
        yutList.clear();
        turn = 0;
        d_init = 100;

        startView = null;
        //gameView = null;
        endView = null;
    }

    private void restartGame() {
        resetGameData();

        if (isSwing()) {
            startView.resetUI();
        } else if (isFx()) {
            startView.resetUI();
        }
        updateViewState();
    }

    private void restartSwingView() {
        setState(GameState.START_SCREEN);

        frame.setVisible(false);
        frame.getContentPane().removeAll();

        startViewSwing = new View.Swing.StartView();
        gameViewSwing = new View.Swing.GameView();
        endViewSwing = new View.Swing.EndView();

        startViewSwing.setVisible(true);
        gameViewSwing.setVisible(false);
        endViewSwing.setVisible(false);

        startViewSwing.setBounds(0, 0, 1100, 700);
        gameViewSwing.setBounds(0, 0, 1100, 700);
        endViewSwing.setBounds(0, 0, 1100, 700);

        frame.add(startViewSwing);
        frame.add(gameViewSwing);
        frame.add(endViewSwing);

        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);

        initializeListeners();
        updateViewState();
    }

    private void restartFxView() {
        setState(GameState.START_SCREEN);

        if (gameViewFx != null) gameViewFx.clearHorses();
        if (startViewFx != null) startViewFx.resetSelection();

        showStartViewFx();
    }

    private void showStartViewFx() {
        if (mainStackPane != null) {
            mainStackPane.getChildren().clear();
            mainStackPane.getChildren().add(startViewFx);
        } else if (primaryStage != null && startViewFx != null) {
            javafx.scene.Scene startScene = new javafx.scene.Scene(startViewFx);
            primaryStage.setScene(startScene);
        }
    }
}