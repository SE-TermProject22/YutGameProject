package Controller;

import Model.*;
import View.Fx.*;
import View.IEndView;
import View.IGameView;
import View.IStartView;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import Controller.GameState;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class IGameController {
    private final IGameUIRoot uiRoot;
    private IStartView startView;
    private IGameView gameView;
    private IEndView endView;

    // gameState
    private GameState currentState = GameState.START_SCREEN;

    // Player
    private List<Player> players = new ArrayList<>();   // players
    private int playerCount;
    private Player currentPlayer;

    private int turn = 0;

    private Board board;            // borad 지정

    private List<Horse> horses = new ArrayList<>();
    private int horseCount;

    private int d_init = 100;

    private boolean throwState = true;
    private Yut yut;

    // UI 관련 필드들
    private StackPane mainStackPane;
    private Scene mainScene;
    private Stage primaryStage;

    public IGameController(IGameUIRoot uiRoot, IStartView startView, IGameView gameView, IEndView endView) {
        this.uiRoot = uiRoot;
        this.startView = startView;
        this.gameView = gameView;
        this.endView = endView;

        setState(GameState.START_SCREEN);
        initializeListeners();
    }

    // 화면 전환 메서드 통일
    public void setState(GameState state) {
        this.currentState = state;

        switch (state) {
            case START_SCREEN -> {
                System.out.println("🔄 화면 전환: START_SCREEN");
                uiRoot.setContent(startView.getRoot());
            }
            case GAME_SCREEN, GAME_PLAY -> {
                System.out.println("🔄 화면 전환: GAME_SCREEN");
                uiRoot.setContent(gameView.getRoot());

            }
            case END_SCREEN, GAME_OVER -> {
                System.out.println("🔄 화면 전환: END_SCREEN");
                uiRoot.setContent(endView.getRoot());
            }
        }
    }

    private void initializeListeners() {
        startView.setOnStart(() -> {
            startView.setState(GameState.HORSE_SELECTION);
        });

        startView.setOnHorseSelected(color -> {
            startView.toggleHorseSelection(color);

            int playerCount = startView.getPlayerCount();
            int selectedHorseCount = startView.getSelectedColors().size();

            if (selectedHorseCount == playerCount) {
                startView.setState(GameState.BOARD_SELECTION);
            } else if (selectedHorseCount > playerCount) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("경고");
                alert.setHeaderText(null);
                alert.setContentText("플레이어 수에 맞게 말을 선택해주세요.");
                alert.showAndWait();

                startView.toggleHorseSelection(color);
            }
        });

        startView.setOnBoardSelected(boardType -> startView.selectBoard(boardType));

        startView.setOnNext(() -> startGame());

        gameView.setOnThrow(() -> {
            if (throwState) {
                throwState = false;
                YutResult result = yut.throwYut();
                gameView.startYutAnimation(result);

                System.out.println(result);

                if (result == YutResult.MO || result == YutResult.YUT) {
                    throwState = true;
                    gameView.scheduleNotifyingImage(result);
                } else {
                    PauseTransition delay = new PauseTransition(Duration.millis(1700));
                    delay.setOnFinished(e2 -> Platform.runLater(this::move));
                    delay.play();
                }
            }
        });

        gameView.setOnSpecialThrow(() -> {
            throwState = false;
            gameView.showFixedYutChoiceDialog(selectedResult -> {
                System.out.println("지정 윷 결과: " + selectedResult);
                yut.throwYut(selectedResult);
                gameView.startYutAnimation(selectedResult);

                if (selectedResult == YutResult.MO || selectedResult == YutResult.YUT) {
                    throwState = true;
                    gameView.scheduleNotifyingImage(selectedResult);
                    PauseTransition pause = new PauseTransition(Duration.seconds(1.7));
                    pause.setOnFinished(ev -> move());
                    pause.play();
                } else {
                    PauseTransition pause = new PauseTransition(Duration.millis(1700));
                    pause.setOnFinished(ev -> move());
                    pause.play();
                }
            });
        });

        endView.setOnRestart(() -> {
            System.out.println("🔁 재시작 버튼 눌림");
            restartGame();
        });

        endView.setOnExit(() -> {
            System.out.println("❌ 종료 버튼 눌림");
            if (Platform.isFxApplicationThread()) {
                Platform.exit();
            } else {
                System.exit(0);
            }
        });
    }

    private void startGame() {
        String selectedBoard = startView.getSelectedBoard();

        board = new Board(selectedBoard);
        playerCount = startView.getPlayerCount();
        horseCount = startView.getHorseCount();
        List<String> selectedColors = startView.getSelectedColors();

        if (selectedBoard == null || selectedColors.size() != startView.getPlayerCount()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("선택 오류");
            alert.setHeaderText(null);
            alert.setContentText("보드와 말 선택이 완료되지 않았습니다.");
            alert.showAndWait();
            return;
        }

        // 플레이어와 말 초기화
        for (int i = 0; i < playerCount; i++) {
            String color = selectedColors.get(i);
            Player player = new Player(i, color);
            players.add(player);
            for (int j = 0; j < horseCount; j++) {
                horses.add(new Horse((i*horseCount+j), color, board.nodes.getFirst()));
                players.get(i).addHorse(horses.get(i*horseCount+j));
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
                    player.getId(), player.getColor(), player.getHorseList().size());
        }

        gameView.initHorses(selectedColors, horseCount);

        currentPlayer = players.getFirst();
        yut = new Yut();

        // 게임 화면으로 전환
        setState(GameState.GAME_PLAY);

        gameView.setBoardType(selectedBoard);
        gameView.displayPlayers(playerCount);
        gameView.displayHorses(selectedColors, playerCount, horseCount);
    }

    public void move() {
        Platform.runLater(() -> {
            processNextYutResult();
        });
    }

    private void processNextYutResult() {
        if(yut.isEmptyYutResultList()){
            throwState = true;
            turn++;
            currentPlayer = players.get(turn%playerCount);
            return;
        }

        gameView.showYutResultChoiceDialog(yut.getYutResultList(), chosenResult -> {
            Platform.runLater(() -> {
                List<Horse> selectableHorseList = currentPlayer.selectableHorse();
                gameView.showHorseSelectionDialog(selectableHorseList, horseCount, selectedHorse -> {
                    Platform.runLater(() -> {
                        executeMove(chosenResult, selectedHorse);
                    });
                });
            });
        });
    }

    private void executeMove(YutResult chosenResult, Horse selectedHorse){
        System.out.println("선택된 말: " + selectedHorse.id);

        // 1. 말 이동 처리
        YutResult result = chosenResult;
        yut.removeYutResult(result);
        selectedHorse.move(result);

        // 2. 처음 사용되는 말 보이게 처리
        if (selectedHorse.state == false) {
            selectedHorse.state = true;
            gameView.setHorseVisible(selectedHorse.id);
        }

        // 3. 화면에서 말 이동 애니메이션
        if (selectedHorse instanceof DoubledHorse) {
            DoubledHorse doubledHorse = (DoubledHorse) selectedHorse;
            gameView.moveHorse(doubledHorse.id, doubledHorse.x, doubledHorse.y);
            System.out.println("DoubledHorse 이동: " + doubledHorse.id + " → (" + doubledHorse.x + ", " + doubledHorse.y + ")");

            gameView.setHorseInvisible(doubledHorse.id);
            gameView.mkDoubled(doubledHorse.id, doubledHorse.color, doubledHorse.horseCount, doubledHorse.x, doubledHorse.y, doubledHorse.getImageType());
        } else {
            gameView.moveHorse(selectedHorse.id, selectedHorse.x, selectedHorse.y);
            System.out.println("일반 말 이동: " + selectedHorse.id + " → (" + selectedHorse.x + ", " + selectedHorse.y + ")");
        }

        // 4. 도착 지점 체크
        horseFinishCheck(selectedHorse);

        // 5. 업기/잡기 처리
        horseStackCheck(selectedHorse);
        processNextYutResult();
    }

    private void horseFinishCheck(Horse selectedHorse) {
        selectedHorse.finish(currentPlayer);
        if (selectedHorse.currentNode.isEndNode) {
            System.out.printf("🏁 말 %d finish 처리됨 (EndNode)\n", selectedHorse.id);

            if (selectedHorse instanceof DoubledHorse) {
                DoubledHorse dh = (DoubledHorse) selectedHorse;

                if (dh.getImageType() == 0) {
                    DoubledHorse.releaseLightImageForColor(dh.color);
                }

                ArrayList<Horse> doubleHorseList = new ArrayList<>();
                doubleHorseList.addAll(dh.getCarriedHorses());

                for (Horse horse : doubleHorseList) {
                    gameView.setHorseToGray(horse.id);
                    gameView.setHorseInvisible(horse.id);
                }
                gameView.setHorseInvisible(selectedHorse.id);
            } else {
                gameView.setHorseToGray(selectedHorse.id);
                selectedHorse.state = false;
                gameView.setHorseInvisible(selectedHorse.id);
            }
            checkWinner();
        }
    }

    public void checkWinner(){
        if (currentPlayer.getScore() >= horseCount) {
            System.out.printf("🎉 플레이어 %d 승리!\n", currentPlayer.getId() + 1);

            // 윷 리스트 비우기
            yut.clearYutResultList();

            // 승리자 정보 설정 후 화면 전환
            endView.setWinner(currentPlayer.getId() + 1);
            setState(GameState.GAME_OVER);
        }
    }

    public void horseStackCheck(Horse selectedHorse){
        Horse other = selectedHorse.findSameNodeHorse(players);
        if(other == null)
            return;

        boolean sameTeam = selectedHorse.checkSameTeam(other);
        if(sameTeam){
            // 업기
            DoubledHorse dh = selectedHorse.stack(d_init++, currentPlayer, other);
            gameView.setHorseInvisible(selectedHorse.id);
            gameView.setHorseInvisible(other.id);
            gameView.mkDoubled(dh.id, dh.color, dh.horseCount, dh.x, dh.y, dh.getImageType());

            System.out.printf("🔗 업기 발생: %s 업힌 대상: %s 만들어진 대상: %s\n",
                    selectedHorse.id, other.id, dh.id);

            gameView.showEventImage("/image/업었다.png");
        } else {
            // 잡기
            other.catched(board.nodes.getFirst(), other.getPlayer(players));
            System.out.printf("💥 잡기 발생: %s가 %s 잡음\n", selectedHorse.id, other.id);

            if (other instanceof DoubledHorse) {
                DoubledHorse dh = (DoubledHorse) other;

                if (dh.getImageType() == 0) {
                    DoubledHorse.releaseLightImageForColor(dh.color);
                }

                ArrayList<Horse> doubleHorseList = new ArrayList<>();
                doubleHorseList.addAll(dh.getCarriedHorses());

                for (Horse horse : doubleHorseList) {
                    gameView.setHorseInvisible(horse.id);
                    gameView.moveHorse(horse.id, horse.x, horse.y);
                }

                gameView.setHorseInvisible(other.id);
            } else {
                gameView.setHorseInvisible(other.id);
                gameView.moveHorse(other.id, other.x, other.y);
            }

            gameView.showEventImage("/image/잡았다.png");
        }
    }

    // 게임 데이터를 초기화하는 메서드
    private void resetGame() {
        currentPlayer = null;
        players.clear();
        horses.clear();
        horseCount = 0;
        playerCount = 0;
        throwState = true;
        if (yut != null) {
            yut.clearYutResultList();
        }
        turn = 0;
    }

    // 재시작 메서드 개선
    private void restartGame() {
        System.out.println("🔄 게임 재시작 중...");

        // 1. 게임 데이터 초기화
        resetGame();

        // 2. 뷰 초기화 (새로 생성하지 말고 기존 것 재사용)
        if (startView != null) {
            startView.resetSelection();
        }

        if (gameView != null) {
            ((IFXGameView)gameView).clearHorses();
            ((IFXGameView)gameView).clearPlayers();
        }

        // 3. DoubledHorse 상태 초기화
        DoubledHorse.resetLightDoubleHorseMap();

        // 4. 시작 화면으로 전환
        setState(GameState.START_SCREEN);

        System.out.println("✅ 게임 재시작 완료");
    }

    // Scene 설정을 위한 메서드들 (필요시 사용)
    public void setMainScene(Scene scene) {
        this.mainScene = scene;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void setMainStackPane(StackPane stackPane) {
        this.mainStackPane = stackPane;
    }
}