package Controller;

import Model.*;
import View.Fx.EndView;
import View.Fx.GameView;
import View.Fx.StartView;
import javafx.animation.*;
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


    // 업기 구현을 위한 initial_id
    // private int doubleHorseOrderCounter = 0;

    //나중에 필요하면 swing이랑 공통되는 부분만 넣은 컨트롤러로 변경
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

        System.out.println("🎯 addThrowButtonListener 호출됨");
        gameView.addThrowButtonListener(e -> {
            System.out.println("🔥 윷던지기 버튼 클릭됨");
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
                    delay.setOnFinished(e2 -> {
                        Platform.runLater(() -> move());
                    });
                    delay.play();
                }

            }
        });

        gameView.addSpecialThrowListener(e -> {
            //일반 윷 던지기 비활성화 시켜놓고
            throwState = false;
            YutResult result;
            gameView.showFixedYutChoiceDialog(selectedResult -> {
                System.out.println("지정 윷 결과: " + selectedResult);
                yut.throwYut(selectedResult);
            });
            result = yut.getYutResultList().get(yut.getYutResultListSize() - 1);
            // 윷 던지기 애니메이션 실행
            gameView.startYutAnimation(result);

            // 윷 or 모 나오면 한 번 더
            if (result == YutResult.MO || result == YutResult.YUT) {
                throwState = true;
                gameView.scheduleNotifyingImage(result);
                PauseTransition pause = new PauseTransition(Duration.seconds(1700));
                pause.setOnFinished(ev -> move());
                pause.play();
            } else {
                PauseTransition pause = new PauseTransition(Duration.millis(1700));
                pause.setOnFinished(ev -> move());
                pause.play();
            }
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

        if (selectedBoard == null || selectedColors.size() != startView.getPlayerCount()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("선택 오류");
            alert.setHeaderText(null);
            alert.setContentText("보드와 말 선택이 완료되지 않았습니다.");
            alert.showAndWait();
            return;
        }


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

        Scene scene = startView.getScene(); // 현재 View에서 Scene 가져오기
        if (scene != null) {
            scene.setRoot(gameView); // GameView로 루트 교체
        }

        currentPlayer = players.getFirst();  // 첫 번째 플레이어로 시작

        yut = new Yut();

        setState(GameState.GAME_PLAY);


        gameView.setBoardType(selectedBoard);
        gameView.displayPlayers(playerCount);
        gameView.displayHorses(selectedColors, playerCount, horseCount);

        /*
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
        */
    }

    private void setState(GameState newState) {
        currentState = newState;
        updateViewState();
    }

    private void updateViewState() {
        startView.setState(currentState);
    }

//    public void move() {
//        System.out.println("🔥 move 호출됨 / yutList 크기: " + yutList.size());
//        System.out.println("현재 플레이어: " + currentPlayer.id);
//        System.out.println("throwState: " + throwState);
//
//        if (currentPlayer == null || yutList == null) {
//            System.err.println("❌ 게임 상태 오류 - 초기화 필요");
//            return;
//        }
//
//        if (yutList.isEmpty()) {
//            System.out.println("yutList가 비어있음 - 턴 종료");
//            throwState = true;
//            turn++;
//            currentPlayer = players.get(turn % playerCount);
//            return;
//        }
//
//        Platform.runLater(() -> {
//            // 1. 윷 결과 선택 다이얼로그
//            gameView.showYutResultChoiceDialog(yutList, chosenResult -> {
//
//                // 2. 말 선택 다이얼로그 (업기/도착한 말 제외)
//                List<Horse> selectableHorses = currentPlayer.horseList.stream()
//                        .filter(h -> !h.isDoubled && !h.isFinished)
//                        .toList();
//
//                gameView.showHorseSelectionDialog(selectableHorses, horseCount, selectedHorse -> {
//                    System.out.println("✅ 말 선택됨: ID " + selectedHorse.id);
//
//                    // 3. 윷 결과 적용 및 말 이동
//                    yutList.remove(chosenResult);
//                    System.out.println("윷 결과 제거 후 yutList 크기: " + yutList.size());
//                    selectedHorse.move(chosenResult);
//                    System.out.println("말 이동 완료: " + selectedHorse.id);
//
//                    if (!selectedHorse.state) {
//                        selectedHorse.state = true;
//                        gameView.setHorseVisible(selectedHorse.id);
//                    }
//
//                    gameView.moveHorse(selectedHorse.id, selectedHorse.x, selectedHorse.y);
//
//                    // 4. 도착 처리
////
//                    if (selectedHorse.currentNode.isEndNode || selectedHorse.isFinished) {
//                        System.out.printf("🏁 말 %d finish 처리됨 (EndNode)\n", selectedHorse.id);
//
//                        gameView.setHorseToGray(selectedHorse.id);
//                        selectedHorse.state = false;
//                        gameView.setHorseInvisible(selectedHorse.id);
//
//                        int gainedScore = 1;
//
//                        // 업힌 말까지 점수 계산
//                        if (selectedHorse instanceof DoubledHorse dh) {
//                            gainedScore = dh.getCarriedHorses().size() + 1;
//                            for (Horse h : dh.getCarriedHorses()) {
//                                h.state = false;
//                                gameView.setHorseInvisible(h.id);
//                            }
//                        }
//
//                        currentPlayer.horseList.remove(selectedHorse);
//                        currentPlayer.score += gainedScore;
//
//                        if (currentPlayer.score >= horseCount) {
//                            System.out.printf("🎉 플레이어 %d 승리!\n", currentPlayer.id + 1);
//                            endView.setWinner(currentPlayer.id + 1);
//
//                            setState(GameState.GAME_OVER);
//                            gameView.setVisible(false);
////                            endView.setVisible(true);
//
//                            Scene scene = gameView.getScene();  // GameView로부터 Scene을 받아와야 함
//                            if (scene != null) {
//                                scene.setRoot(endView);  // setVisible이 아니라 setRoot로 교체
//                            }
//
//                            return;
//                        }
//                    }
//
//
//                    // 5. 잡기 / 업기 처리
//                    for (Horse other : horses) {
//                        if (other == selectedHorse || !other.state) continue;
//
//                        int check = selectedHorse.checkSameNodeAndTeam(other);
//
//                        if (check == 1) { // 업기
//                            DoubledHorse dh = new DoubledHorse(d_init++, selectedHorse, other, doubleHorseOrderCounter++);
//                            selectedHorse.isDoubled = true;
//                            other.isDoubled = true;
//                            currentPlayer.horseList.add(dh);
//
//                            gameView.setHorseInvisible(selectedHorse.id);
//                            gameView.setHorseInvisible(other.id);
//                            gameView.mkDoubled(dh.id, dh.color, dh.horseCount, dh.x, dh.y);
//                            gameView.showEventImage("/image/업었다.png");
//                            break;
//
//                        } else if (check == 0) { // 잡기
//                            other.state = false;
//                            other.currentNode = board.nodes.get(0);
//                            other.x = other.currentNode.x;
//                            other.y = other.currentNode.y;
//                            gameView.setHorseInvisible(other.id);
//                            gameView.moveHorse(other.id, other.x, other.y);
//                            gameView.showEventImage("/image/잡았다.png");
//                            break;
//                        }
//                    }
//
//                    // 6. 남은 윷 결과 처리
//                    if (!yutList.isEmpty()) {
//                        PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
//                        delay.setOnFinished(e -> Platform.runLater(this::move)); // 애니메이션 중 show 방지
//                        delay.play();
//                    } else {
//                        throwState = true;
//                        turn++;
//                        currentPlayer = players.get(turn % playerCount);
//                    }
//                });
//            });
//        });
//    }

    public void move() {
        Platform.runLater(() -> {
            processNextYutResult(); // 첫번째 윷 결과 처리 시작
        });
    }

    private void processNextYutResult() {
        if(yut.isEmptyYutResultList()){ //!yutList.isEmpty()){
            throwState = true;
            turn++;
            currentPlayer = players.get(turn%playerCount);
            return;
        }

        //처리할 윷 결과가 있으면 1. 윷 선택 창 보여주고
        gameView.showYutResultChoiceDialog(yut.getYutResultList(), chosenResult -> {
            Platform.runLater(() -> {
                List<Horse> selectableHorseList = currentPlayer.selectableHorse();
                gameView.showHorseSelectionDialog(selectableHorseList, horseCount, selectedHorse -> {
                    Platform.runLater(() -> {
                        executeMove(chosenResult, selectedHorse); //실제 이동 실행
                    });
                });
            });
        });
    }

    //실제 말 이동 실행 함수
    private void executeMove(YutResult chosenResult, Horse selectedHorse){
        System.out.println("선택된 말: " + selectedHorse.id);

        // 1. 말 이동 처리
        YutResult result = chosenResult;
        yut.removeYutResult(result);
        selectedHorse.move(result); // 말 위치 이동 시킴

        // 2. 처음 사용되는 보이게 처리
        if (selectedHorse.state == false) {
            selectedHorse.state = true;
            gameView.setHorseVisible(selectedHorse.id);
        }


        // 3. 화면에서 말 이동 애니메이션
        // DoubledHorse인지 일반 말인지 구분해서 이동 처리
        if (selectedHorse instanceof DoubledHorse) {
            // DoubledHorse의 경우: 업힌 말 이미지를 이동시킴
            DoubledHorse doubledHorse = (DoubledHorse) selectedHorse;

            gameView.moveHorse(doubledHorse.id, doubledHorse.x, doubledHorse.y);
            System.out.println("DoubledHorse 이동: " + doubledHorse.id + " → (" + doubledHorse.x + ", " + doubledHorse.y + ")");

            // 기존 이미지 제거
            gameView.setHorseInvisible(doubledHorse.id);

            // 새 위치에 다시 생성
            gameView.mkDoubled(doubledHorse.id, doubledHorse.color, doubledHorse.horseCount, doubledHorse.x, doubledHorse.y, doubledHorse.getImageType());
        } else {
            // 일반 말의 경우: 기존대로 처리
            gameView.moveHorse(selectedHorse.id, selectedHorse.x, selectedHorse.y);
            System.out.println("일반 말 이동: " + selectedHorse.id + " → (" + selectedHorse.x + ", " + selectedHorse.y + ")");
        }

        // 4. 도착 지점 체크
        /*
        if (selectedHorse.currentNode.isEndNode) {
            handleFinish(selectedHorse); //도착한 말 피니시 처리
            return; // 도착 지점 도착 시 처리 종료
        }
        */
        horseFinishCheck(selectedHorse);


        //5. 업기/잡기 처리
        //도착 지점이 아닌 말은 업기/잡기 항상 확인
        // handleHorseInteraction(selectedHorse);
        horseStackCheck(selectedHorse);
        processNextYutResult();
    }

    private void horseFinishCheck(Horse selectedHorse) {
        // EndNode라면
        selectedHorse.finish(currentPlayer);
        if (selectedHorse.currentNode.isEndNode) {
            System.out.printf("🏁 말 %d finish 처리됨 (EndNode)\n", selectedHorse.id);
            // 업힌 말 처리
            if (selectedHorse instanceof DoubledHorse) {
                DoubledHorse dh = (DoubledHorse) selectedHorse;

                if (dh.getImageType() == 0) {  // 0이면 연한색
                    DoubledHorse.releaseLightImageForColor(dh.color);
                }
                //업힌 말 가져와서 리스트에 넣기
                ArrayList<Horse> doubleHorseList = new ArrayList<>();
                doubleHorseList.addAll(dh.getCarriedHorses());

                // 업힌 말 각각 하나씩 피니시 처리 (회색으로 만듦)
                //✔️이거 잘되는지 확인 필요!!!!
                for (Horse horse : doubleHorseList) {
                    // 말 상태 변경
                    gameView.setHorseToGray(horse.id);
                    gameView.setHorseInvisible(horse.id);
                }
                gameView.setHorseInvisible(selectedHorse.id);
            }
            else {
                // 일반 말 피니시 처리
                // 말 상태 변경
                gameView.setHorseToGray(selectedHorse.id);
                selectedHorse.state = false;
                gameView.setHorseInvisible(selectedHorse.id);
            }
            checkWinner();
            // 아직 말이 남았다면 다음 윷 결과 처리
            // processNextYutResult();
        }
    }

    public void checkWinner(){
        // 승리 조건 체크
        if (currentPlayer.getScore() >= horseCount) {
            System.out.printf("🎉 플레이어 %d 승리!\n", currentPlayer.getId() + 1);
            // ✅ [1] 윷 리스트 모두 비우기
            // yutList.clear();
            yut.clearYutResultList();
            // ✅ [2] 남아있는 팝업 모두 닫기
            // gameView.disposeAllDialogs();

            endView.setWinner(currentPlayer.getId() + 1); // 승리자 정보 전달
            setState(GameState.GAME_OVER);
            Scene endscene = gameView.getScene();
            if (endscene != null) {
                endscene.setRoot(endView);
            }
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
            // 디버그 출력
            System.out.printf("🔗 업기 발생: %s 업힌 대상: %s 만들어진 대상: %s\n",
                    selectedHorse.id, other.id, dh.id);

            // 업기 이벤트 이미지 표시
            gameView.showEventImage("/image/업었다.png");
        }
        else{
            // 잡기
            other.catched(board.nodes.getFirst(), other.getPlayer(players));
            System.out.printf("💥 잡기 발생: %s가 %s 잡음\n", selectedHorse.id, other.id);

            if (other instanceof DoubledHorse) {
                DoubledHorse dh = (DoubledHorse) other;

                //업힌 말2가 잡힐경우 이미지 컬러 조건 초기화 (서로 다른 색이 될 수 있도록)
                if (dh.getImageType() == 0) {  // 0이면 연한색
                    DoubledHorse.releaseLightImageForColor(dh.color);
                }

                //업힌 말 잡히면 업힌 모든 말 시작점으로
                ArrayList<Horse> doubleHorseList = new ArrayList<>();
                doubleHorseList.addAll(dh.getCarriedHorses());

                for (Horse horse : doubleHorseList) {
                    // 말 상태 초기화
                    gameView.setHorseInvisible(horse.id);
                    gameView.moveHorse(horse.id, horse.x, horse.y);
                }

                //doublehorse도 안 보이게
                gameView.setHorseInvisible(other.id);

            } else {
                gameView.setHorseInvisible(other.id);
                gameView.moveHorse(other.id, other.x, other.y);
            }

            gameView.showEventImage("/image/잡았다.png");
        }
        // processNextYutResult();
    }













    /*

    private void handleFinish(Horse selectedHorse) {
        System.out.printf("🏁 말 %d finish 처리 완료 (EndNode)\n", selectedHorse.id);

        // 업힌 말 처리
        if (selectedHorse instanceof DoubledHorse) {
            DoubledHorse dh = (DoubledHorse) selectedHorse;

            if (dh.getImageType() == 0) {  // 0이면 연한색
                DoubledHorse.releaseLightImageForColor(dh.color);
            }
            //업힌 말 가져와서 리스트에 넣기
            ArrayList<Horse> doubleHorseList = new ArrayList<>();
            doubleHorseList.addAll(dh.getCarriedHorses());

            // 업힌 말 각각 하나씩 피니시 처리 (회색으로 만듦)
            //✔️이거 잘되는지 확인 필요!!!!
            for (Horse horse : doubleHorseList) {

                // 말 상태 변경
                gameView.setHorseToGray(horse.id);
                selectedHorse.state = false;
                gameView.setHorseInvisible(horse.id);

                currentPlayer.horseList.remove(horse);
                currentPlayer.score += 1;

            }

            // DoubleHorse도 안보이게
            selectedHorse.state = false;
            gameView.setHorseInvisible(selectedHorse.id);
            currentPlayer.horseList.remove(selectedHorse);

        } else {
            // 일반 말 피니시 처리
            // 말 상태 변경
            gameView.setHorseToGray(selectedHorse.id);
            selectedHorse.state = false;
            currentPlayer.horseList.remove(selectedHorse);
            currentPlayer.score += 1;
            gameView.setHorseInvisible(selectedHorse.id);
        }

        // 승리 조건 체크
        if (currentPlayer.score >= horseCount) {
            System.out.printf("🎉 플레이어 %d 승리!\n", currentPlayer.id + 1);
            endView.setWinner(currentPlayer.id + 1); // 승리자 정보 전달
            setState(GameState.GAME_OVER);

//            // 화면 전환
//            gameView.setVisible(false);
//            endView.setVisible(true);
            Scene endscene = gameView.getScene();
            if (endscene != null) {
                endscene.setRoot(endView);
            }
        }
        // 아직 말이 남았다면 다음 윷 결과 처리
        processNextYutResult();
    }

    // 업기 잡기 판단 함수
    private void handleHorseInteraction(Horse selectedHorse) {
        boolean interactionDoubled = false;

        // 모든 플레이어 모든 말들과 비교
        for (Player player : players) {
            for (Horse other : player.horseList) {
                // 자기 자신/ 비활성화 말/ 업은 말 제외
                if (other == selectedHorse || !other.state || other.isDoubled)
                    continue;

                // 같은 위치 & 팀인지 체크
                int check = selectedHorse.checkSameNodeAndTeam(other);
                // 반환값: 1=같은팀, 0=다른팀, -1=다른위치

                // 같은팀 -> 업기
                if (check == 1){
                    handleDouble(selectedHorse, other);
                    interactionDoubled = true;
                    return;
                }
                //상대 팀 -> 잡기
                else if (check == 0) {
                    handleCapture(selectedHorse, other, player);
                    interactionDoubled = true;
                    return;
                }
            }
        }
        processNextYutResult();
    }

    //업기 처리 (같은 팀 말이 만났을 때)
    private void handleDouble(Horse selectedHorse, Horse other){
        //업은 말 생성
        DoubledHorse dh = new DoubledHorse(d_init++, selectedHorse, other);

        //원래 말들 업힌 상태로 표시
        selectedHorse.isDoubled = true;
        other.isDoubled = true;

        //doubledHorse 화면에 표시
        gameView.setHorseInvisible(selectedHorse.id);
        gameView.setHorseInvisible(other.id);
        gameView.mkDoubled(dh.id, dh.color, dh.horseCount, dh.x, dh.y, dh.getImageType());

        //플레이어 말 목록에 doublehorse 추가
        currentPlayer.horseList.add(dh);

        // 디버그 출력
        System.out.printf("🔗 업기 발생: %s 업힌 대상: %s 만들어진 대상: %s\n",
                selectedHorse.id, other.id, dh.id);

        // 업기 이벤트 이미지 표시
        gameView.showEventImage("/image/업었다.png");

        // 업기 후 윷 결과가 남아있으면 계속 진행, 없으면 턴 종료
        if (!yutList.isEmpty()) {
            Platform.runLater(() -> processNextYutResult());
        } else {
            // 윷 결과가 없으면 다음 턴으로
            throwState = true;
            turn++;
            currentPlayer = players.get(turn % playerCount);
            System.out.println("업기 후 윷 결과 없음 - 다음 턴으로");
        }
    }

    private void handleCapture(Horse selectedHorse, Horse other, Player player){
        System.out.printf("💥 잡기 발생: %s가 %s 잡음\n", selectedHorse.id, other.id);

        if (other instanceof DoubledHorse) {
            DoubledHorse dh = (DoubledHorse) other;

            //업힌 말2가 잡힐경우 이미지 컬러 조건 초기화 (서로 다른 색이 될 수 있도록)
            if (dh.getImageType() == 0) {  // 0이면 연한색
                DoubledHorse.releaseLightImageForColor(dh.color);
            }

            //업힌 말 잡히면 업힌 모든 말 시작점으로
            ArrayList<Horse> doubleHorseList = new ArrayList<>();
            doubleHorseList.addAll(dh.getCarriedHorses());

            for (Horse horse : doubleHorseList) {
                // 말 상태 초기화
                horse.state = false;
                horse.isDoubled = false;  // 업힌 상태 해제
                gameView.setHorseInvisible(horse.id);
                horse.currentNode = board.nodes.get(0);
                horse.x = horse.currentNode.x;
                horse.y = horse.currentNode.y;
            }

            //doublehorse도 안 보이게
            gameView.setHorseInvisible(other.id);
            player.horseList.remove(other);

        } else {
            //일반 잡힌 경우
            other.state = false;
            gameView.setHorseInvisible(other.id);
            other.currentNode = board.nodes.get(0);
            other.x = other.currentNode.x;
            other.y = other.currentNode.y;
        }

        gameView.showEventImage("/image/잡았다.png");

        // 잡기 후 윷 결과가 남아있으면 계속 진행, 없으면 턴 종료
        if (!yutList.isEmpty()) {
            Platform.runLater(() -> processNextYutResult());
        } else {
            // 윷 결과가 없으면 다음 턴으로
            throwState = true;
            turn++;
            currentPlayer = players.get(turn % playerCount);
            System.out.println("잡기 후 윷 결과 없음 - 다음 턴으로");
        }
    }
*/
    // 게임 데이터를 초기화하는 메서드
    private void resetGame() {
        currentPlayer = null;
        players.clear();
        horses.clear();
        horseCount = 0;
        playerCount = 0;
        throwState = true;
        yut.clearYutResultList();
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
        DoubledHorse.resetLightDoubleHorseMap();

        startView.resetSelection();
        setState(GameState.START_SCREEN);
        showStartView();
    }
}