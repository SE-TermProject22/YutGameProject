package Controller;

import Model.*;
import View.Swing.*;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameController {

    // frame
    private JFrame frame;

    // view
    private StartView startView;
    private GameView gameView;
    private EndView endView;

    // gameState
    private GameState currentState = GameState.START_SCREEN;

    // Player
    private List<Player> players = new ArrayList<>();   // players
    private int playerCount;
    private Player currentPlayer;
    // turn 구현을 위한 1차례 2차례 이렇계 계속 늘어나는 변수
    private int turn = 0;

    // Board - 현재 선택된 board
    private Board board;            // borad 지정

    // Horse
    private List<Horse> horses = new ArrayList<>();         // 전체 horse
    private int horseCount;
    // 업기 구현을 위한 initial_id
    private int d_init = 100;

    // Yut 윷 던지기
    private boolean throwState = true;
    private Yut yut;

    public GameController(JFrame frame, StartView startView, GameView gameView, EndView endView) {
        this.frame = frame;
        this.startView = startView;
        this.gameView = gameView;
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
                if (throwState) {
                    throwState = false;
                    // YutResult result = currentPlayer.throwYut();
                    // yutList.add(result);
                    YutResult result = yut.throwYut();
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
                    }
                }

            }
        });

        // 지정윷던지기 버튼 리스너
        gameView.addSpecialThrowListener(e -> {
            if (throwState) {
                throwState = false;
                YutResult result;
                gameView.showFixedYutChoiceDialog(selectedResult -> {
                    // yutList.add(selectedResult);
                    System.out.println("🔧 지정 윷 결과 선택됨: " + selectedResult);
                    yut.throwYut(selectedResult);
                });
                // result = yutList.get(yutList.size() - 1);
                result = yut.getYutResultList().get(yut.getYutResultListSize() - 1);
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
                }
            }

        });

        // EndView - 재시작 버튼 리스너
        endView.addRestartButtonListener(e -> restartGame());

        endView.addExitButtonListener(e -> {
            System.exit(0); // 종료 버튼 클릭 시 시스템 종료
        });

    }

    private void startGame() {

        String selectedBoard = startView.getSelectedBoard();

        board = new Board(selectedBoard);
        playerCount = startView.getPlayerCount();
        horseCount = startView.getHorseCount();
        List<String> selectedColors = startView.getSelectedColors();

        if (selectedBoard == null || selectedColors.size() != startView.getPlayerCount()) {
            JOptionPane.showMessageDialog(null, "보드와 말 선택이 완료되지 않았습니다.");
            return;
        }

        // 모든 말 생성
        for(int i = 0; i < playerCount; i++) {
            String color = selectedColors.get(i);
            players.add(new Player(i, color));
            for(int j = 0; j < horseCount; j++) {
                horses.add(new Horse((i*horseCount+j), color, board.nodes.getFirst()));
                players.get(i).addHorse(horses.get(i*horseCount+j));
            }
        }

        // 말 component 생성
        gameView.initHorses(selectedColors, horseCount);

        currentPlayer = players.getFirst();  // 첫 번째 플레이어로 시작

        yut = new Yut();

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
        processNextYutResult(); // 첫번째 윷 결과 처리 시작
    }


    private void processNextYutResult() {
        if(yut.isEmptyYutResultList()){ //!yutList.isEmpty()){
            throwState = true;
            turn++;
            currentPlayer = players.get(turn%playerCount);
            return;
        }
        // 윷 결과 선택창
        gameView.showYutResultChoiceDialog(yut.getYutResultList(), chosenResult -> { // yutList, chosenResult -> {
            List<Horse> selectableHorseList = currentPlayer.selectableHorse();
            gameView.showHorseSelectionDialog(selectableHorseList, horseCount, selectedHorse -> {
                executeMove(chosenResult, selectedHorse); //실제 이동 실행
            });
        });
    }

    //실제 말 이동 실행 함수
    private void executeMove(YutResult chosenResult, Horse selectedHorse){
        System.out.println("선택된 말: " + selectedHorse.id);

        YutResult result = chosenResult;
        // yutList.remove(result);
        yut.removeYutResult(result);

        // 말 움직이기 - model
        selectedHorse.move(result);
        // 말 움직이기 - view

        if(selectedHorse.state == false){
            selectedHorse.state = true;
            gameView.setHorseVisible(selectedHorse.id);
        }

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

        // finish
        horseFinishCheck(selectedHorse);

        // 업기 & 잡기
        horseStackCheck(selectedHorse);
        processNextYutResult();
    }


    private void horseFinishCheck(Horse selectedHorse) {
        // EndNode라면
        selectedHorse.finish(currentPlayer);
        if (selectedHorse.currentNode.isEndNode) {
            System.out.printf("🏁 말 %d finish 처리됨 (EndNode)\n", selectedHorse.id);
            if(selectedHorse instanceof DoubledHorse) {
                DoubledHorse dh = (DoubledHorse) selectedHorse;
                if (dh.getImageType() == 0) {  // 0이면 연한색
                    DoubledHorse.releaseLightImageForColor(dh.color);
                }
                ArrayList<Horse> doubledHorseList = new ArrayList<>();
                doubledHorseList.addAll(dh.getCarriedHorses());
                for(Horse horse : doubledHorseList){
                    gameView.setHorseToGray(horse.id); // 원래 이거 안햇었음 -> 이번에 추가(예나-5/23)
                    gameView.setHorseInvisible(horse.id);
                }

                gameView.setHorseInvisible(selectedHorse.id);
            }
            else{
                gameView.setHorseToGray(selectedHorse.id); // 원래 이거 안햇었음 -> 이번에 추가(예나-5/23)
                gameView.setHorseInvisible(selectedHorse.id);
            }
            checkWinner();
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
            endView.setWinner(currentPlayer.getId() + 1); // 승리자 id넘겨주기 // 원래 이거 안했었음 -> 이번에 추가(예나-5/23)
            setState(GameState.GAME_OVER);    // ✅ 게임 종료 상태로 전환
            gameView.setVisible(false);
            endView.setVisible(true);
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
            gameView.mkDoubled(dh.id, dh.color, dh.horseCount, dh.currentNode.x, dh.currentNode.y, dh.getImageType()); // - 여기서 comonet 만들고 x, y, id 지정, setVisible도 하기
            gameView.setHorseInvisible(other.id);
            gameView.setHorseInvisible(selectedHorse.id);
            gameView.showEventImage("/image/업었다.png");

        }
        else{
            // 잡기
            other.catched(board.nodes.getFirst(), other.getPlayer(players));
            if(other instanceof DoubledHorse) {
                DoubledHorse dh = (DoubledHorse) other;
                //업힌 말2가 잡힐경우 이미지 컬러 조건 초기화 (서로 다른 색이 될 수 있도록)
                if (dh.getImageType() == 0) {  // 0이면 연한색
                    DoubledHorse.releaseLightImageForColor(dh.color);
                }
                ArrayList<Horse> doubledHorseList = new ArrayList<>();
                doubledHorseList.addAll(dh.getCarriedHorses());
                for(Horse horse : doubledHorseList){
                    gameView.setHorseInvisible(horse.id);
                    gameView.moveHorse(horse.id, horse.x, horse.y);  // 잡힌 말 다시 그리기
                }
                // 사용자 list에서도 없어지고, setInVisible
                gameView.setHorseInvisible(other.id);
            }
            else{
                gameView.setHorseInvisible(other.id);
                gameView.moveHorse(other.id, other.x, other.y);  // 잡힌 말 다시 그리기
            }
            gameView.showEventImage("/image/잡았다.png");
        }
    }

    private void resetGame() {
        currentPlayer = null;
        players.clear();
        horses.clear();
        horseCount = 0;
        playerCount = 0;
        throwState = true;
        yut.clearYutResultList();
        turn = 0;
        d_init = 100;
    }
    private void restartGame(){
        // gameView.disposeAllDialogs(); // ✅ 재시작 시에도 팝업 다 닫기
        resetGame();
        setState(GameState.START_SCREEN);

        frame.setVisible(false);

        // JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(startView);
        frame.getContentPane().removeAll(); // 모든 컴포넌트 제거
        System.out.println("컴포넌트 수: " + frame.getContentPane().getComponentCount());

        startView = null;
        gameView = null;
        endView = null;

        startView = new StartView();
        startView.setVisible(true);

        gameView = new GameView();
        gameView.setVisible(false); // 처음엔 안 보이게

        endView = new EndView();
        endView.setVisible(false);

        // frame.setLayout(null);
        startView.setBounds(0, 0, 1100, 700);
        gameView.setBounds(0, 0, 1100, 700);
        endView.setBounds(0, 0, 1100, 700);

        frame.add(startView);
        frame.revalidate();
        frame.repaint();

        frame.add(gameView);
        frame.revalidate();
        frame.repaint();

        frame.add(endView);
        frame.revalidate();
        frame.repaint();

        frame.setVisible(true);

        System.out.println("컴포넌트 수: " + frame.getContentPane().getComponentCount());

        initializeListeners();
        updateViewState();
    }
}


// 버튼 못 누르게 하고 싶어...!!!!! >3< 이런 짜증나!!!
