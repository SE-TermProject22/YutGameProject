package Controller;

import Model.Board;
import Model.Horse;
import Model.DoubledHorse;
import Model.Player;
import View.Fx.EndView;
import View.Fx.GameView;
import View.Fx.StartView;
import Controller.GameController;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.util.Duration;

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

    private int turn = 0;

    // 업기 구현을 위한 initial_id
    private int d_init = 100;

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
            Button clickedButton = (Button) e.getSource();
            String color = null;

            for (Map.Entry<String, Button> entry : startView.getHorseButtons().entrySet()) {
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
                } else if (selectedHorseCount > playerCount) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
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
                    PauseTransition delay = new PauseTransition(Duration.millis(1700));
                    delay.setOnFinished(e2 -> {
                        Platform.runLater(() -> move());
                    });
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
                horses.add(horse);
                player.horseList.add(horse);
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
        System.out.println("🔥 move 호출됨 / yutList 크기: " + yutList.size());

        if (yutList.isEmpty()) {
            throwState = true;
            turn++;
            currentPlayer = players.get(turn % playerCount);
            return;
        }

        Platform.runLater(() -> {
            // 1. 윷 결과 선택 다이얼로그
            gameView.showYutResultChoiceDialog(yutList, chosenResult -> {

                // 2. 말 선택 다이얼로그 (업기/도착한 말 제외)
                List<Horse> selectableHorses = currentPlayer.horseList.stream()
                        .filter(h -> !h.isDoubled && !h.isFinished)
                        .toList();

                gameView.showHorseSelectionDialog(selectableHorses, horseCount, selectedHorse -> {
                    // 3. 윷 결과 적용 및 말 이동
                    yutList.remove(chosenResult);
                    selectedHorse.move(chosenResult);

                    if (!selectedHorse.state) {
                        selectedHorse.state = true;
                        gameView.setHorseVisible(selectedHorse.id);
                    }

                    gameView.moveHorse(selectedHorse.id, selectedHorse.x, selectedHorse.y);

                    // 4. 도착 처리
                    if (selectedHorse.currentNode.isEndNode || selectedHorse.isFinished) {
                        selectedHorse.state = false;
                        selectedHorse.isFinished = true;
                        gameView.setHorseToGray(selectedHorse.id);
                        gameView.setHorseInvisible(selectedHorse.id);

                        currentPlayer.score++;

                        if (currentPlayer.score >= horseCount) {
                            System.out.println("🎉 플레이어 " + (currentPlayer.id + 1) + " 승리!");
                            endView.setWinner(currentPlayer.id + 1);
                            setState(GameState.GAME_OVER);
                            gameView.setVisible(false);
                            endView.setVisible(true);
                            return;
                        }
                    }

                    // 5. 잡기 / 업기 처리
                    for (Horse other : horses) {
                        if (other == selectedHorse || !other.state) continue;

                        int check = selectedHorse.checkSameNodeAndTeam(other);

                        if (check == 1) { // 업기
                            DoubledHorse dh = new DoubledHorse(d_init++, selectedHorse, other);
                            selectedHorse.isDoubled = true;
                            other.isDoubled = true;
                            currentPlayer.horseList.add(dh);

                            gameView.setHorseInvisible(selectedHorse.id);
                            gameView.setHorseInvisible(other.id);
                            gameView.mkDoubled(dh.id, dh.color, dh.horseCount, dh.x, dh.y);
                            gameView.showEventImage("/image/업었다.png");
                            break;

                        } else if (check == 0) { // 잡기
                            other.state = false;
                            other.currentNode = board.nodes.get(0);
                            other.x = other.currentNode.x;
                            other.y = other.currentNode.y;
                            gameView.setHorseInvisible(other.id);
                            gameView.moveHorse(other.id, other.x, other.y);
                            gameView.showEventImage("/image/잡았다.png");
                            break;
                        }
                    }

                    // 6. 남은 윷 결과 처리
                    if (!yutList.isEmpty()) {
                        PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
                        delay.setOnFinished(e -> Platform.runLater(this::move)); // 애니메이션 중 show 방지
                        delay.play();
                    } else {
                        throwState = true;
                        turn++;
                        currentPlayer = players.get(turn % playerCount);
                    }
                });
            });
        });
    }

}


