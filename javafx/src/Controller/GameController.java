package Controller;

import Model.*;
import View.*;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    private StartView startView;
    private GameView gameView;
    private EndView endView;

    private Player currentPlayer;
    private Board board;
    private List<Player> players = new ArrayList<>();
    private List<Horse> horses = new ArrayList<>();
    private int horseCount;
    private int playerCount;
    private boolean throwState = true;
    private List<YutResult> yutList = new ArrayList<>();
    private GameState currentState = GameState.START_SCREEN;
    private int turn = 0;
    private int d_init = 100;

    public GameController(StartView startView, GameView gameView, EndView endView) {
        this.startView = startView;
        this.gameView = gameView;
        this.endView = endView;

        initializeListeners();
        updateViewState();
    }

    private void initializeListeners() {
        startView.addStartButtonListener(e -> setState(GameState.HORSE_SELECTION));

        startView.setHorseSelectionListener(e -> {
            String color = e.getSource().toString(); // JavaFX Button command 대체 필요
            startView.toggleHorseSelection(color);

            int playerCount = startView.getPlayerCount();
            int selectedHorseCount = startView.getSelectedColors().size();

            if (selectedHorseCount == playerCount) {
                setState(GameState.BOARD_SELECTION);
            } else if (selectedHorseCount > playerCount) {
                showAlert("플레이어 수에 맞게 말을 선택해주세요.");
                startView.toggleHorseSelection(""); // 선택 취소
            }
        });

        startView.setBoardSelectionListeners(
                e -> startView.selectBoard("square"),
                e -> startView.selectBoard("pentagon"),
                e -> startView.selectBoard("hexagon")
        );

        startView.addNextButtonListener(e -> startGame());

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
                    // JavaFX 타이머 대체
                    PauseTransition delay = new PauseTransition(Duration.millis(1700));
                    delay.setOnFinished(ev -> move());
                    delay.play();
                }
            }
        });

        gameView.addSpecialThrowListener(e -> {
            gameView.showFixedYutChoiceDialog(selectedResult -> {
                System.out.println("🔧 지정 윷 결과 선택됨: " + selectedResult);
                yutList.clear();
                yutList.add(selectedResult);
                move();
            });
        });

        endView.addRestartButtonListener(e -> restartGame());

        endView.addExitButtonListener(e -> Platform.exit());
    }

    private void startGame() {
        String selectedBoard = startView.getSelectedBoard();
        board = new Board(selectedBoard);

        playerCount = startView.getPlayerCount();
        horseCount = startView.getHorseCount();
        List<String> selectedColors = startView.getSelectedColors();

        for(int i = 0; i < playerCount; i++) {
            String color = selectedColors.get(i);
            players.add(new Player(i, color));
            for(int j = 0; j < horseCount; j++) {
                horses.add(new Horse((i*horseCount+j), color, board.nodes.get(0)));
                players.get(i).horseList.add(horses.get(i*horseCount+j));
            }
        }

        if (selectedBoard == null || selectedColors.size() != playerCount) {
            showAlert("보드와 말 선택이 완료되지 않았습니다.");
            return;
        }

        gameView.initHorses(selectedColors, horseCount);
        currentPlayer = players.get(0);

        setState(GameState.GAME_PLAY);
        startView.setVisible(false);
        gameView.setVisible(true);
        gameView.setBoardType(selectedBoard);
        gameView.displayPlayers(playerCount);
        gameView.displayHorses(selectedColors, playerCount, horseCount);
    }

    private void setState(GameState newState) {
        currentState = newState;
        updateViewState();
    }

    private void updateViewState() {
        startView.setState(currentState);
    }

    public void move() {
        while (!yutList.isEmpty()) {
            gameView.showYutResultChoiceDialog(yutList, chosenResult -> {
                List<Horse> selectableHorseList = new ArrayList<>();
                for (Horse horse : currentPlayer.horseList) {
                    if (!horse.isDoubled) selectableHorseList.add(horse);
                }

                gameView.showHorseSelectionDialog(selectableHorseList, horseCount, selectedHorse -> {
                    YutResult result = chosenResult;
                    yutList.remove(result);
                    selectedHorse.move(result);

                    if (!selectedHorse.state) {
                        selectedHorse.state = true;
                        gameView.setHorseVisible(selectedHorse.id);
                    }

                    gameView.moveHorse(selectedHorse.id, selectedHorse.x, selectedHorse.y);

                    if (selectedHorse.currentNode.isEndNode || selectedHorse.isFinished) {
                        gameView.setHorseToGray(selectedHorse.id);
                        selectedHorse.state = false;
                        currentPlayer.horseList.remove(selectedHorse);
                        currentPlayer.score++;
                        gameView.setHorseInvisible(selectedHorse.id);

                        if (currentPlayer.score >= horseCount) {
                            endView.setWinner(currentPlayer.id + 1);
                            setState(GameState.GAME_OVER);
                            gameView.setVisible(false);
                            endView.setVisible(true);
                            return;
                        }
                    }

                    for (Horse other : horses) {
                        if (other == selectedHorse || !other.state) continue;
                        int check = selectedHorse.checkSameNodeAndTeam(other);

                        if (check == 1) {
                            DoubledHorse dh = new DoubledHorse(d_init++, selectedHorse, other);
                            selectedHorse.isDoubled = true;
                            other.isDoubled = true;

                            gameView.mkDoubled(dh.id, dh.color, dh.horseCount, dh.currentNode.x, dh.currentNode.y);
                            gameView.setHorseInvisible(other.id);
                            gameView.setHorseInvisible(selectedHorse.id);
                            currentPlayer.horseList.add(dh);
                            break;
                        } else if (check == 0) {
                            other.state = false;
                            gameView.setHorseInvisible(other.id);
                            other.currentNode = board.nodes.get(0);
                            other.x = other.currentNode.x;
                            other.y = other.currentNode.y;
                            gameView.moveHorse(other.id, other.x, other.y);
                            break;
                        }
                    }
                });
            });
        }
        throwState = true;
        turn++;
        currentPlayer = players.get(turn % playerCount);
    }

    private void resetGame() {
        currentPlayer = null;
        players.clear();
        horses.clear();
        horseCount = 0;
        playerCount = 0;
        throwState = true;
        yutList.clear();
        turn = 0;

        endView.clearBoard();
        endView.clearHorses();
    }

    private void restartGame() {
        resetGame();
        setState(GameState.START_SCREEN);
        startView.setVisible(true);
        gameView.setVisible(false);
        endView.setVisible(false);
    }

    private void showAlert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("경고");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}