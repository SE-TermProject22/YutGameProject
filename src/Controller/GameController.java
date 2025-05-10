package Controller;

import Model.Board;
import Model.Horse;
import View.StartView;
import View.GameView;
import Model.Player;
import Model.Node;

import java.awt.*;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameController {
    private StartView startView;
    private GameView gameView;
    private Player currentPlayer;

    private Board board;            // borad 지정
    private List<Player> players = new ArrayList<>();   // players
    private List<Horse> horses = new ArrayList<>();         // 전체 horse
    private int horseCount;
    private int playerCount;
    private boolean throwState = true;
    private List<YutResult> yutList = new ArrayList<>();
    ;    //나중에 turn이 바뀔 때마다 currentPlayer 하면서 같이 .clear()

    // 말들이 겹쳐서 쌓이는 칸을 추적하는 맵
    private Map<Point, List<Integer>> stackedHorses = new HashMap<>();

    private GameState currentState = GameState.START_SCREEN;

    // turn 구현을 위한 1차례 2차례 이렇계 계속 늘어나는 변수
    private int turn = 0;

    public GameController(StartView startView, GameView gameView) {
        this.startView = startView;
        this.gameView = gameView;

        initializeListeners();
        updateViewState();
    }

    private void initializeListeners() {
        startView.addStartButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setState(GameState.HORSE_SELECTION);
            }
        });

        // Horse Selection Listener
        startView.setHorseSelectionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 말 버튼 클릭 시, 말 선택 및 취소 처리
                String color = e.getActionCommand(); // 버튼에서 말 색상 가져오기
                startView.toggleHorseSelection(color);

                int playerCount = startView.getPlayerCount();
                int selectedHorseCount = startView.getSelectedColors().size();

                if (selectedHorseCount == playerCount) {
                    // 말 수가 일치하면 보드 선택 화면으로 넘어감
                    setState(GameState.BOARD_SELECTION);
                } else if (selectedHorseCount > playerCount) {
                    // 플레이어 수를 초과하면 경고
                    JOptionPane.showMessageDialog(null, "플레이어 수에 맞게 말을 선택해주세요.");
                    startView.toggleHorseSelection(""); // 선택 취소
                }
            }
        });

        startView.setBoardSelectionListeners(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Square 버튼 클릭 시
                        startView.selectBoard("square");
                    }
                },
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Pentagon 버튼 클릭 시
                        startView.selectBoard("pentagon");
                    }
                },
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Hexagon 버튼 클릭 시
                        startView.selectBoard("hexagon");
                    }
                }
        );

        startView.addNextButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        gameView.addThrowButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(throwState) {
                    throwState = false;
                    YutResult result = currentPlayer.throwYut();
                    System.out.println(result);
                    yutList.add(result);
                    gameView.startYutAnimation(result);

                    if (result == YutResult.MO || result == YutResult.YUT) {
                        throwState = true;
                        gameView.scheduleNotifyingImage(result);
                    }

                    else{

                        javax.swing.Timer delayTimer = new javax.swing.Timer(1250, e2 -> {
                            move();
                        });
                        delayTimer.setRepeats(false);
                        delayTimer.start();



                        // move();
                        // 다시 throwYut true 처리하고 turn 넘겨주기 - 원래는 move 안에서 하려고 해는데 계속 버튼이 눌려서 여기로 옮김
                        /*
                        throwState = true;
                        turn++;
                        currentPlayer = players.get(turn%playerCount);
                        */
                    }

                }
            }
        });
    }


    private void startGame() {
        String selectedBoard = startView.getSelectedBoard();

        // new 보드 연결
        // board = new Board();
        // square board
        // pentagon board
        // hexagon board
        board = new Board(selectedBoard);

        playerCount = startView.getPlayerCount();
        horseCount = startView.getHorseCount();
        List<String> selectedColors = startView.getSelectedColors();

        // 모든 말 생성
        for(int i = 0; i < playerCount; i++) {
            String color = selectedColors.get(i);
            players.add(new Player(i, color));
            for(int j = 0; j < horseCount; j++) {
                horses.add(new Horse((i*horseCount+j), color, board.nodes.get(0)));
                players.get(i).horseList.add(horses.get(i*horseCount+j));   // 일단 이렇게 바로 add를 하는데 나중에는 함수를 만들어서 하던지 합시다^
            }
        }


        System.out.println("===== 생성된 말(Horses) =====");
        for (Horse horse : horses) {
            System.out.printf("Horse ID: %d, Color: %s, StartNode: (%d, %d)\n",
                    horse.id, horse.color, horse.currentNode.x, horse.currentNode.y);
        }

        // 디버깅: 생성된 모든 플레이어 및 보유 말 출력
        System.out.println("\n===== 생성된 플레이어(Players) 및 보유 말 log=====");
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            System.out.printf("Player ID: %d, Color: %s, Horse Count: %d\n",
                    player.id, player.color, player.horseList.size());

            for (Horse horse : player.horseList) {
                System.out.printf("  └─ Horse ID: %d, Color: %s, StartNode: (%d, %d)\n",
                        horse.id, horse.color, horse.currentNode.x, horse.currentNode.y);
            }
        }

        // 말 component 생성
        gameView.initHorses(selectedColors, horseCount);



        if (selectedBoard == null || selectedColors.size() != startView.getPlayerCount()) {
            JOptionPane.showMessageDialog(null, "보드와 말 선택이 완료되지 않았습니다.");
            return;
        }



        currentPlayer = players.get(0);  // 첫 번째 플레이어로 시작
        // gameView.setPlayer(currentPlayer);

        setState(GameState.GAME_PLAY); // 게임 상태로 전환
        startView.setVisible(false); // StartView 숨기기
        gameView.setVisible(true);   // GameView 보이기

        gameView.setBoardType(selectedBoard);
        gameView.displayPlayers(playerCount);
        gameView.displayHorses(selectedColors, playerCount, horseCount);
        //gameView.placeHorses(selectedColors, playerCount);
    }

    private void setState(GameState newState) {
        currentState = newState;
        updateViewState();
    }

    private void updateViewState() {
        startView.setState(currentState);
    }

    // 말을 업히는 메서드
    public void stackHorseAtPosition(Horse horse, Point position) {
        // 해당 위치에 이미 쌓인 말의 ID 목록을 가져옴
        List<Integer> horsesAtPosition = stackedHorses.getOrDefault(position, new ArrayList<>());

        horsesAtPosition.add(horse.id);  // 말을 해당 위치에 추가
        stackedHorses.put(position, horsesAtPosition);  // 맵에 저장

        // 뷰에 업힌 말 업데이트 (위치에 업힌 말들의 개수 표시 등)
        gameView.stackHorseAtPosition(horse.id, position);
    }

    // 말을 이동시킬 때 그룹 내 모든 말들이 함께 이동하도록
    public void moveGroupedHorses(Point position, Node targetNode) {
        // 해당 위치에 있는 말들이 있는지 확인
        List<Integer> horsesAtPosition = stackedHorses.get(position);

        if (horsesAtPosition != null) {
            for (int horseId : horsesAtPosition) {
                Horse horse = findHorseById(horseId);

                if (horse != null && !horse.isFinished) {
                    // 말의 노드 위치 업데이트
                    horse.currentNode = targetNode;

                    // 화면에서도 이동
                    gameView.moveHorse(horse.id, targetNode.x, targetNode.y);
                }
            }

            // 그룹 이동 후, 새 위치에 해당 그룹을 업데이트
            stackedHorses.remove(position);
            stackedHorses.put(new Point(targetNode.x, targetNode.y), horsesAtPosition);
        }
    }

    // Horse ID로 Horse 객체를 찾는 메서드
    private Horse findHorseById(int horseId) {
        for (Horse horse : horses) {
            if (horse.id == horseId) {
                return horse;
            }
        }
        return null; // Horse가 없는 경우
    }


    public void move() {

        while (!yutList.isEmpty()) {

            // 윷 결과 선택
            gameView.showYutResultChoiceDialog(yutList, chosenResult -> {
                System.out.println("선택된 결과: " + chosenResult);
                yutList.remove(chosenResult);

                // 말 선택
                gameView.showHorseSelectionDialog(currentPlayer.horseList, selectedHorse -> {
                    System.out.println("선택된 말: " + selectedHorse.id);

                    Node targetNode = selectedHorse.currentNode;

                    // 이동할 거리만큼 노드 이동
                    for (int i = 0; i < chosenResult.ordinal(); i++) {
                        if (targetNode != null && targetNode.nextNode != null) {
                            targetNode = targetNode.nextNode;
                        }
                    }

                    System.out.println("이동할 노드: " + targetNode.id);

                    // 말이 화면에 나타나지 않는 문제를 해결
                    if (selectedHorse.state == false) {
                        selectedHorse.state = true;  // 말 활성화
                        gameView.setHorseVisible(selectedHorse.id);  // 말이 화면에 보이게 설정
                    }

                    // 현재 위치에 같은 색의 말이 있는지 확인 (업힐 수 있는지 체크)
                    List<Horse> horsesOnSameNode = new ArrayList<>();
                    for (Horse horse : horses) {
                        if (horse.currentNode == targetNode && horse.color.equals(selectedHorse.color) && horse != selectedHorse) {
                            horsesOnSameNode.add(horse);
                        }
                    }

                    // 말이 업힐 경우 처리
                    if (!horsesOnSameNode.isEmpty()) {
                        // 두 번째로 온 말이 원래 있던 말에 업히는 방식으로 처리
                        Horse carriedHorse = horsesOnSameNode.get(0);  // 첫 번째로 겹친 말 (즉, 업힐 대상 말)
                        System.out.println("말이 업혔습니다: " + carriedHorse.id);

                        // 업힌 말의 위치를 이동할 노드(targetNode)로 설정
                        carriedHorse.currentNode = targetNode;

                        // UI 업데이트: 업힌 말의 위치도 이동시키기
                        gameView.moveHorse(carriedHorse.id, carriedHorse.x, carriedHorse.y);
                    }

                    // 선택된 말 이동
                    if (!selectedHorse.isFinished) {
                        System.out.println("말 이동: " + selectedHorse.id);
                        selectedHorse.move(chosenResult);
                        gameView.moveHorse(selectedHorse.id, selectedHorse.x, selectedHorse.y);

                        // 도착 지점에 도착했을 경우 점수 처리
                        if (selectedHorse.currentNode.isEndNode) {
                            selectedHorse.isFinished = true;
                            selectedHorse.state = false;
                            currentPlayer.score++;
                            currentPlayer.horseList.remove(selectedHorse);
                            System.out.println("말이 끝에 도착했습니다: " + selectedHorse.id);
                        }
                    }

                    // 게임 종료 조건 체크
                    if (currentPlayer.score == horseCount) {
                        System.out.println("플레이어 " + currentPlayer.id + "의 모든 말이 도착했습니다. 게임 종료.");
                        return;
                    }

                    // targetNode의 x, y를 사용하여 Point 객체를 생성
                    Point targetPosition = new Point(targetNode.x, targetNode.y);

                    // targetPosition을 moveGroupedHorses로 전달
                    List<Integer> horsesAtNewPosition = stackedHorses.get(targetPosition);  // 위치에 쌓인 말들 가져오기
                    if (horsesAtNewPosition != null) {
                        // 그룹으로 이동
                        moveGroupedHorses(targetPosition, targetNode);
                    }

                });
            });
        }

        throwState = true;
        turn++;
        currentPlayer = players.get(turn % playerCount);
    }


//    public void move(){
//
//        while(!yutList.isEmpty()){
//
//            //윷 결과 선택창
//            gameView.showYutResultChoiceDialog(yutList, chosenResult -> {
//                // yutList.remove(chosenResult); // 선택한 결과 제거
//                System.out.println("선택된 결과: " + chosenResult);
//
//                //말 적용 선택창 - 이거 나중에 list로 주는거 따로 처리하기
//                gameView.showHorseSelectionDialog(currentPlayer.horseList, selectedHorse -> {
//                    System.out.println("선택된 말: " + selectedHorse.id);
//                    //이동 구현 필요
//                    // yutList.clear();
//                    // throwState = true;
//
//
//                });
//            });
//
//
//            // 윷 선택
//            // YutResult result = gameView.selectYutResult(yutList);
//
//            YutResult result = yutList.get(0); // 위에거 test 용
//            yutList.remove(result);
//
//            // 말 선택
//            // int horse_id = view.selectHorse(currentPlayer.getHorseListID());
//            int horse_id = currentPlayer.horseList.get(0).id;
//
//            System.out.println("horse_id" + horse_id);
//            Horse selectedHorse = horses.get(horse_id);
//            System.out.println("selected horse" + selectedHorse.id);
//
//            System.out.println("현재 : horse x: " + selectedHorse.x + "y: "+ selectedHorse.y);
//
//            if(selectedHorse.state == false){
//                selectedHorse.state = true;
//                gameView.setHorseVisible(selectedHorse.id);
//            }
//
//            selectedHorse.move(result);
//            // view 구현해보자
//            gameView.moveHorse(selectedHorse.id, selectedHorse.x, selectedHorse.y);
//            System.out.println("horse 움직임");
//            System.out.println("horse x: " + selectedHorse.x + "y: "+ selectedHorse.y);
//
//            // 여기서 한번 repaint() 해 줄 지 고민
//
//            // finish 처리
//            if(selectedHorse.currentNode.isEndNode){
//                selectedHorse.isFinished = true;
//                selectedHorse.state = false;
//                currentPlayer.score++;
//                currentPlayer.horseList.remove(selectedHorse); // test 용임
//            }
//
//            if(currentPlayer.score==horseCount){
//                // view.finish 처리
//                System.out.println("끝남");
//                break;
//            }
//        }
//        throwState = true;
//        turn++;
//        currentPlayer = players.get(turn%playerCount);
//
//    }
}

/*
// 뭐 이런식으로 turn 넘기고 한다는데 잘 모르겠고 일단 보자^^
public void keyPressed(KeyEvent e) {
    if (!turnController.isTurnActive()) return;

    Player player = playerController.getCurrentPlayer();
    String pieceId = player.getPieceId();

    switch (e.getKeyCode()) {
        case KeyEvent.VK_RIGHT -> pieceController.movePiece(pieceId, 10, 0);
        case KeyEvent.VK_LEFT -> pieceController.movePiece(pieceId, -10, 0);
        case KeyEvent.VK_UP -> pieceController.movePiece(pieceId, 0, -10);
        case KeyEvent.VK_DOWN -> pieceController.movePiece(pieceId, 0, 10);
        case KeyEvent.VK_ENTER -> {
            turnController.endTurn();
            playerController.nextPlayer();
            turnController.startTurn();
        }
    }

 */