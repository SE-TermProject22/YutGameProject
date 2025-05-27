package Controller;

import Model.Board;
import Model.Horse;
import Model.Player;
import View.Fx.EndView;
import View.Fx.GameView;
import View.Fx.StartView;
import Controller.GameController;
import javafx.scene.Scene;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Map;

public class FXGameController {
    private StartView startView;
    private GameView gameView;
    private EndView endView;

    private Board board;            // borad 지정
    private List<Player> players = new ArrayList<>();   // players
    private List<Horse> horses = new ArrayList<>();

    private int horseCount;
    private int playerCount;

    private Player currentPlayer;

    private boolean throwState = true;
    private List<YutResult> yutList = new ArrayList<>();

    private GameState currentState = GameState.START_SCREEN;
    //나중에 필요하면 swing이랑 공통되는 부분만 넣은 컨트롤러로 변경
    private GameController gameController;

    public FXGameController(StartView startView, GameView gameView, EndView endView) {
        this.startView = startView;
        this.gameView = gameView;
        this.endView = endView;

//        this.gameController = new GameController();

        initializeFXListeners();
    }

    private void initializeFXListeners() {
        startView.addStartButtonListener(e -> {
            System.out.println("시작 버튼 눌림 체크용");
            startView.setState(GameState.HORSE_SELECTION);
        });

        startView.setHorseSelectionListener(e -> {
            javafx.scene.control.Button clickedButton = (javafx.scene.control.Button) e.getSource();
            String color = null;

            for (Map.Entry<String, javafx.scene.control.Button> entry : startView.getHorseButtons().entrySet()) {
                if (entry.getValue() == clickedButton) {
                    color = entry.getKey();
                    break;
                }
            }

            if (color != null) {
                startView.toggleHorseSelection(color);

                int playerCount = startView.getPlayerCount();
                int selectedHorseCount = startView.getSelectedColors().size();

                if (selectedHorseCount == playerCount) {
                    startView.setState(GameState.BOARD_SELECTION);
                } else if  (selectedHorseCount > playerCount) {
                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
                    alert.setTitle("경고");
                    alert.setHeaderText("null");
                    alert.setContentText("플레이어 수에 맞게 말을 선택해주세요.");
                    alert.showAndWait();

                    //선택 취소
                    startView.toggleHorseSelection(color);
                }
            }
        });

        startView.setBoardSelectionListeners(
                e -> startView.selectBoard("square"),
                e -> startView.selectBoard("pentagon"),
                e -> startView.selectBoard("hexagon")
        );

        startView.addNextButtonListener(e -> {
            startGame();
        });

        gameView.addThrowButtonListener(e -> {
            if (throwState) {
                throwState = false;
                YutResult result = currentPlayer.throwYut();
                System.out.println(result);
                yutList.add(result);
                gameView.startYutAnimation(result);

                if (result == YutResult.MO || result == YutResult.YUT) {
                    throwState = true;
                    gameView.scheduleNotifyingImage(result);
                } else {
                    javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(javafx.util.Duration.millis(1700));
                    delay.setOnFinished(e2 -> move());
                    delay.play();
                }

            }
        });

        gameView.addSpecialThrowListener(e -> {
            gameView.showFixedYutChoiceDialog(selectedResult -> {
                System.out.println("지정 윷 결과: " + selectedResult);

                yutList.clear();
                yutList.add(selectedResult);

                move();
            });
        });
    }

    private void startGame() {
        String selectedBoard = startView.getSelectedBoard();

        board = new Board(selectedBoard);
        playerCount = startView.getPlayerCount();
        horseCount = startView.getHorseCount();
        List<String> selectedColors = startView.getSelectedColors();

        players.clear();
        horses.clear();

        for (int i = 0; i < playerCount; i++) {
            String color = selectedColors.get(i);
            Player player = new Player(i, color);
            players.add(player);

            for (int j = 0; j < horseCount; j++) {
                Horse horse = new Horse(i * horseCount + j, color, board.nodes.get(0));
            }
        }

        // 디버깅 로그
        System.out.println("===== 생성된 말(Horses) =====");
        for (Horse horse : horses) {
            System.out.printf("Horse ID: %d, Color: %s, StartNode: (%d, %d)\n",
                    horse.id, horse.color, horse.currentNode.x, horse.currentNode.y);
        }

        System.out.println("\n===== 생성된 플레이어 및 말 목록 =====");
        for (Player player : players) {
            System.out.printf("Player ID: %d, Color: %s, Horse Count: %d\n",
                    player.id, player.color, player.horseList.size());
        }

        gameView.initHorses(selectedColors, horseCount);

        Scene scene = startView.getScene(); // 현재 View에서 Scene 가져오기
        if (scene != null) {
            scene.setRoot(gameView); // GameView로 루트 교체
        }


        currentPlayer = players.get(0);
        gameView.setBoardType(selectedBoard);
        gameView.displayPlayers(playerCount);
        gameView.displayHorses(selectedColors, playerCount, horseCount);

        setState(GameState.GAME_PLAY);
    }

    private void setState(GameState newState) {
        currentState = newState;
        updateViewState();
    }

    private void updateViewState() {
        startView.setState(currentState);
    }

    public void move() {

    }
}


