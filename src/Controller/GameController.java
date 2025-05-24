package Controller;

import Model.Board;
import Model.DoubledHorse;
import Model.Player;
import Model.Horse;

import View.StartView;
import View.GameView;
import View.EndView;

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
    private EndView endView;

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

    // 업기 구현을 위한 initial_id
    private int d_init = 100;
    //
    public GameController(StartView startView, GameView gameView, EndView endView) {
        this.startView = startView;
        this.gameView = gameView;

        //
        this.endView = endView;

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

        // 지정윷던지기 버튼 리스너
        gameView.addSpecialThrowListener(e -> {
            gameView.showFixedYutChoiceDialog(selectedResult -> {
                System.out.println("🔧 지정 윷 결과 선택됨: " + selectedResult);

                yutList.clear();
                yutList.add(selectedResult);

                move();
            });
        });

        // EndView - 재시작 버튼 리스너
        endView.addRestartButtonListener(e -> restartGame());

        endView.addExitButtonListener(e -> {
            System.exit(0); // 종료 버튼 클릭 시 시스템 종료
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

    // 팝업창 너무 빨리뜨는거 나중에 해결했으면 좋겠어요!
    public void move(){

        while(!yutList.isEmpty()){
                //윷 결과 선택창
                gameView.showYutResultChoiceDialog(yutList, chosenResult -> {
                    // yutList.remove(chosenResult); // 선택한 결과 제거
                    // System.out.println("선택된 결과: " + chosenResult);

                    //말 적용 선택창 - 이거 나중에 list로 주는거 따로 처리하기
                    List<Horse> selectableHorseList = new ArrayList<>();
                    for (Horse horse : currentPlayer.horseList) {
                        if(horse.isDoubled)
                            continue;
                        selectableHorseList.add(horse);
                    }

                    gameView.showHorseSelectionDialog(selectableHorseList, horseCount, selectedHorse -> {
                        // System.out.println("선택된 말: " + selectedHorse.id);
                        //이동 구현 필요
                        // yutList.clear();
                        // throwState = true;
                        YutResult result = chosenResult;
                        yutList.remove(result);
                        selectedHorse.move(result);
                        if(selectedHorse.state == false){
                            selectedHorse.state = true;
                            gameView.setHorseVisible(selectedHorse.id);
                        }
                        gameView.moveHorse(selectedHorse.id, selectedHorse.x, selectedHorse.y);

                        ////////// finish 처리 /////////
                        if (selectedHorse.currentNode.isEndNode || selectedHorse.isFinished) {
                            System.out.printf("🏁 말 %d finish 처리됨 (EndNode)\n", selectedHorse.id);

                            gameView.setHorseToGray(selectedHorse.id); // 원래 이거 안햇었음 -> 이번에 추가(예나-5/23)

                            // 말 상태 변경
                            // selectedHorse.state = false; -> 한 번 더 가야

                            // 플레이어 점수 +1
                            // 업기 할때 수정필요
                            currentPlayer.horseList.remove(selectedHorse);
                            currentPlayer.score++;

                            // 말 숨기기
                            // gameView.setHorseInvisible(selectedHorse.id); -> 그 다음 움직임부터 invisible 필요 **

//                            // View에게 점수 갱신 알림 갱신하는건가???
//                            gameView.updatePlayerScore(currentPlayer.id, currentPlayer.score);

                            // 승리 조건 체크
                            if (currentPlayer.score >= horseCount) {
                                System.out.printf("🎉 플레이어 %d 승리!\n", currentPlayer.id + 1);
                                endView.setWinner(currentPlayer.id + 1); // 승리자 id넘겨주기 // 원래 이거 안했었음 -> 이번에 추가(예나-5/23)

                                setState(GameState.GAME_OVER);    // ✅ 게임 종료 상태로 전환

                                gameView.setVisible(false);
                                endView.setVisible(true);

                                return;
                            }
                        }

                        // 업기 처리
                        for (Horse other : horses) {
                            if (other == selectedHorse || !other.state) continue;

                            int check = selectedHorse.checkSameNodeAndTeam(other);

                            // 같은 말 - 업기
                            if (check == 1) {
                                DoubledHorse dh = new DoubledHorse(d_init++, selectedHorse, other);

                                selectedHorse.isDoubled = true;
                                other.isDoubled = true;

                                // view 건들기
                                gameView.mkDoubled(dh.id, dh.color, dh.horseCount, dh.currentNode.x, dh.currentNode.y); // - 여기서 comonet 만들고 x, y, id 지정, setVisible도 하기
                                gameView.setHorseInvisible(other.id);
                                gameView.setHorseInvisible(selectedHorse.id);
                                currentPlayer.horseList.add(dh);

                                System.out.printf("🔗 업기 발생: %s 업힌 대상: %s 만들어진 대상: %s\n", selectedHorse.id, other.id, dh.id);

                                // TODO: DoubledHorse 처리 로직
                                break;

                            }
                            // 다른 말 - 잡기
                            else if (check == 0) {
                                System.out.printf("💥 잡기 발생: %s가 %s 잡음\n", selectedHorse.id, other.id);
                                other.state = false;
                                gameView.setHorseInvisible(other.id);
                                other.currentNode = board.nodes.get(0); // 시작점으로
                                other.x = other.currentNode.x;
                                other.y = other.currentNode.y;
                                gameView.moveHorse(other.id, other.x, other.y);  // 잡힌 말 다시 그리기
                                break;
                            }
                        }


                    });

                });


            // 윷 선택
            // YutResult result = gameView.selectYutResult(yutList);

            // YutResult result = yutList.get(0); // 위에거 test 용
            // yutList.remove(result);

            // 말 선택
            // int horse_id = view.selectHorse(currentPlayer.getHorseListID());
            // int horse_id = currentPlayer.horseList.get(0).id;

            // System.out.println("horse_id" + horse_id);
            // Horse selectedHorse = horses.get(horse_id);
            // System.out.println("selected horse" + selectedHorse.id);

            // System.out.println("현재 : horse x: " + selectedHorse.x + "y: "+ selectedHorse.y);
            /*
            if(selectedHorse.state == false){
                selectedHorse.state = true;
                gameView.setHorseVisible(selectedHorse.id);
            }
            */

            // selectedHorse.move(result);
            // view 구현해보자
            // gameView.moveHorse(selectedHorse.id, selectedHorse.x, selectedHorse.y);
            // System.out.println("horse 움직임");
            // System.out.println("horse x: " + selectedHorse.x + "y: "+ selectedHorse.y);

            // 여기서 한번 repaint() 해 줄 지 고민

            // finish 처리
            /*
            if(selectedHorse.currentNode.isEndNode){
                selectedHorse.isFinished = true;
                selectedHorse.state = false;
                currentPlayer.score++;
                currentPlayer.horseList.remove(selectedHorse); // test 용임
            }

            if(currentPlayer.score==horseCount){
                // view.finish 처리
                System.out.println("끝남");
                break;
            }

            */
        }
        throwState = true;
        turn++;
        currentPlayer = players.get(turn%playerCount);

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

        // 뷰 초기화
        endView.clearBoard();
        endView.clearHorses();
        endView.clearBoard();   // EndView의 보드 초기화
        endView.clearHorses();  // EndView의 말 초기화
        //endView.setWinner(currentPlayer.id);   // 초기값으로 설정 (1번 플레이어로 설정)
    }

    // 게임이 끝났을 때 재시작 버튼을 띄우고, 클릭 시 게임을 초기화하고 재시작
    private void restartGame() {
        resetGame();
        setState(GameState.START_SCREEN);
        startView.setVisible(true);
        gameView.setVisible(false);
        endView.setVisible(false);
    }


}


