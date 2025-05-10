package Controller;

import Model.Board;
import Model.Horse;
import Model.Yut;
import View.StartView;
import View.GameView;
import Model.Player;
import Model.DoubledHorse;

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

                        javax.swing.Timer delayTimer = new javax.swing.Timer(1700, e2 -> {
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
        for (int i = 0; i < playerCount; i++) {
            String color = selectedColors.get(i);
            players.add(new Player(i, color));
            for (int j = 0; j < horseCount; j++) {
                horses.add(new Horse((i * horseCount + j), color, board.nodes.get(0)));
                players.get(i).horseList.add(horses.get(i * horseCount + j));   // 일단 이렇게 바로 add를 하는데 나중에는 함수를 만들어서 하던지 합시다^
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

    // 팝업창 너무 빨리뜨는거 나중에 해결했으면 좋겠어요!
    public void move() {
        while (!yutList.isEmpty()) {
            gameView.showYutResultChoiceDialog(yutList, chosenResult -> {
                List<Horse> selectableHorseList = new ArrayList<>();

                for (Horse horse : currentPlayer.horseList) {
                    // 🔒 DoubledHorse에 업힌 말은 선택 불가능
                    boolean isCarried = false;
                    for (Horse h : horses) {
                        if (h instanceof DoubledHorse dh && dh.getCarriedHorses().contains(horse)) {
                            isCarried = true;
                            break;
                        }
                    }
                    if (!isCarried) {
                        selectableHorseList.add(horse);
                    }
                }

                gameView.showHorseSelectionDialog(selectableHorseList, selectedHorse -> {
                    YutResult result = chosenResult;
                    yutList.remove(result);

                    // ✅ 말 이동
                    selectedHorse.move(result);

                    if (!selectedHorse.state) {
                        selectedHorse.state = true;
                        gameView.setHorseVisible(selectedHorse.id);
                    }

                    gameView.moveHorse(selectedHorse.id, selectedHorse.x, selectedHorse.y);

                    // ✅ DoubledHorse라면 carriedHorses도 같이 이동
                    if (selectedHorse instanceof DoubledHorse dh) {
                        for (Horse carried : dh.getCarriedHorses()) {
                            carried.currentNode = selectedHorse.currentNode;
                            carried.x = selectedHorse.x;
                            carried.y = selectedHorse.y;
                            gameView.moveHorse(carried.id, carried.x, carried.y);
                        }
                    }

                    // ✅ 잡기 및 업기 처리
                    for (Horse other : new ArrayList<>(horses)) {
                        if (other == selectedHorse || !other.state) continue;

                        int check = selectedHorse.checkSameNodeAndTeam(other);

                        if (check == 1) {
                            System.out.printf("🔗 업기 발생: %s 업힌 대상: %s\n", selectedHorse.id, other.id);

                            // ✅ 업기 처리: DoubledHorse 생성
                            DoubledHorse newDh = new DoubledHorse(selectedHorse.id, selectedHorse.color, selectedHorse.currentNode);
                            if (selectedHorse instanceof DoubledHorse sh) {
                                for (Horse h : sh.getCarriedHorses()) newDh.addHorse(h);
                            } else {
                                newDh.addHorse(selectedHorse);
                            }

                            if (other instanceof DoubledHorse oh) {
                                for (Horse h : oh.getCarriedHorses()) newDh.addHorse(h);
                            } else {
                                newDh.addHorse(other);
                            }
                            horses.remove(selectedHorse);
                            horses.remove(other);
                            horses.add(newDh);

                            currentPlayer.horseList.remove(selectedHorse);
                            currentPlayer.horseList.remove(other);
                            currentPlayer.horseList.add(newDh);

                            gameView.setHorseInvisible(selectedHorse.id);
                            gameView.setHorseInvisible(other.id);
                            gameView.moveHorse(newDh.id, newDh.x, newDh.y);
                            gameView.setHorseVisible(newDh.id);
                          //  selectedHorse = newDh;

                            break;
                        } else if (check == 0) {
                            System.out.printf("💥 잡기 발생: %s가 %s 잡음\n", selectedHorse.id, other.id);

                            if (other instanceof DoubledHorse dh) {
                                List<Horse> carried = new ArrayList<>(dh.getCarriedHorses());

                                for (Horse h : carried) {
                                    h.state = false;
                                    h.currentNode = board.nodes.get(0);
                                    h.x = h.currentNode.x;
                                    h.y = h.currentNode.y;
                                    gameView.moveHorse(h.id, h.x, h.y);
                                    gameView.setHorseInvisible(h.id);

                                    if (!currentPlayer.horseList.contains(h)) {
                                        currentPlayer.horseList.add(h);
                                    }
                                    if (!horses.contains(h)) {
                                        horses.add(h);
                                    }
                                }

                                currentPlayer.horseList.remove(dh);
                                horses.remove(dh);

                            } else {
                                other.state = false;
                                gameView.setHorseInvisible(other.id);
                                other.currentNode = board.nodes.get(0);
                                other.x = other.currentNode.x;
                                other.y = other.currentNode.y;
                                gameView.moveHorse(other.id, other.x, other.y);

                                if (!currentPlayer.horseList.contains(other)) {
                                    currentPlayer.horseList.add(other);
                                }
                                if (!horses.contains(other)) {
                                    horses.add(other);
                                }
                            }
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
}