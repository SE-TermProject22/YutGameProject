package Controller;

import Model.Board;
import Model.Horse;
import Model.DoubledHorse;
import Model.Player;
import View.Fx.EndView;
import View.Fx.GameView;
import View.Fx.StartView;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
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
    private int doubleHorseOrderCounter = 0;

    private GameState currentState = GameState.START_SCREEN;
    //나중에 필요하면 swing이랑 공통되는 부분만 넣은 컨트롤러로 변경
    private GameController gameController;

    private StackPane mainStackPane;
    private Scene mainScene;
    private Stage primaryStage;

    public FXGameController(Stage primaryStage, StartView startView, GameView gameView, EndView endView) {
        this.primaryStage = primaryStage;
        this.startView = startView;
        this.gameView = gameView;
        this.endView = endView;

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

        // EndView를 만든 쪽 (예: MainFX 또는 Controller)에서
        endView.addRestartButtonListener(e -> {
            System.out.println("🔁 재시작 버튼 눌림");
            restartGame();
        });

        endView.addExitButtonListener(e -> {
            System.out.println("❌ 종료 버튼 눌림");
            Platform.exit(); // 또는 System.exit(0);
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

        // startGame() 내부 또는 move() 이후
        gameView.addTestEndButton();  // 버튼 생성
        gameView.setTestEndButtonListener(e -> {
            System.out.println("💡 테스트 종료 버튼 클릭됨");
            endView.setWinner(currentPlayer.id + 1);  // 예시로 현재 플레이어를 승자로 설정
            Scene endscene = gameView.getScene();
            if (endscene != null) {
                endscene.setRoot(endView);
            }
        });
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
//
                    if (selectedHorse.currentNode.isEndNode || selectedHorse.isFinished) {
                        System.out.printf("🏁 말 %d finish 처리됨 (EndNode)\n", selectedHorse.id);

                        gameView.setHorseToGray(selectedHorse.id);
                        selectedHorse.state = false;
                        gameView.setHorseInvisible(selectedHorse.id);

                        int gainedScore = 1;

                        // 업힌 말까지 점수 계산
                        if (selectedHorse instanceof DoubledHorse dh) {
                            gainedScore = dh.getCarriedHorses().size() + 1;
                            for (Horse h : dh.getCarriedHorses()) {
                                h.state = false;
                                gameView.setHorseInvisible(h.id);
                            }
                        }

                        currentPlayer.horseList.remove(selectedHorse);
                        currentPlayer.score += gainedScore;

                        if (currentPlayer.score >= horseCount) {
                            System.out.printf("🎉 플레이어 %d 승리!\n", currentPlayer.id + 1);
                            endView.setWinner(currentPlayer.id + 1);

                            setState(GameState.GAME_OVER);
                            gameView.setVisible(false);
//                            endView.setVisible(true);

                            Scene scene = gameView.getScene();  // GameView로부터 Scene을 받아와야 함
                            if (scene != null) {
                                scene.setRoot(endView);  // setVisible이 아니라 setRoot로 교체
                            }

                            return;
                        }
                    }


                    // 5. 잡기 / 업기 처리
                    for (Horse other : horses) {
                        if (other == selectedHorse || !other.state) continue;

                        int check = selectedHorse.checkSameNodeAndTeam(other);

                        if (check == 1) { // 업기
                            DoubledHorse dh = new DoubledHorse(d_init++, selectedHorse, other, doubleHorseOrderCounter++);
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

    // 게임 데이터를 초기화하는 메서드
    private void resetGame() {
        currentPlayer = null;
        players.clear();
        horses.clear();
        horseCount = 0;
        playerCount = 0;
        throwState = true;
        yutList.clear();
        turn = 0;
    }

    //재시작 구현부분
    // 방법 1: StackPane을 직접 초기화하는 경우
//    public void initializeMainView(Stage stage) {
//        this.primaryStage = stage;
//
//        startView = new StartView();
//        gameView = new GameView();
//        endView = new EndView();
//
//        showStartView();
//    }
//
    public void showStartView() {
        if (mainStackPane != null) {
            mainStackPane.getChildren().clear();
            mainStackPane.getChildren().add(startView);
        } else {
            Scene startScene = new Scene(startView);
            primaryStage.setScene(startScene);
        }
    }

//    private void restartGame() {
//        //1. 게임 데이터 초기화
//        resetGame();
//
//        //2. 뷰 요소 초기화
//        if (startView == null) {
//            startView = new StartView();
//            initializeFXListeners();
//        }
//
//        startView.resetSelection();
//        gameView.clearHorses();
//        gameView.clearPlayers();
//
//        initializeFXListeners();
//        setState(GameState.START_SCREEN);
//        showStartView();
//    }
    private void restartGame() {
        resetGame();

        startView = new StartView();
        gameView = new GameView();
        endView = new EndView();

        initializeFXListeners();

        startView.resetSelection();
        setState(GameState.START_SCREEN);
        showStartView();
    }
}