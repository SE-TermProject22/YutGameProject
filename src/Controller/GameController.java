package Controller;

import View.StartView;
import View.GameView;
import Model.Player;

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

    private GameState currentState = GameState.START_SCREEN;

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
                gameView.startYutAnimation();
            }
        });
    }

    private void startGame() {
        long startTime = System.currentTimeMillis();

        String selectedBoard = startView.getSelectedBoard();
        int playerCount = startView.getPlayerCount();
        int horseCount = startView.getHorseCount();
        List<String> selectedColors = startView.getSelectedColors();

        if (selectedBoard == null || selectedColors.size() != startView.getPlayerCount()) {
            JOptionPane.showMessageDialog(null, "보드와 말 선택이 완료되지 않았습니다.");
            return;
        }

        long afterBoardCheckTime = System.currentTimeMillis();
        System.out.println("Board and color check time: " + (afterBoardCheckTime - startTime) + "ms");

        List<Player> players = new ArrayList<>();
        for (String color : selectedColors) {
            players.add(new Player(color));
        }

        currentPlayer = players.get(0);  // 첫 번째 플레이어로 시작
        gameView.setPlayer(currentPlayer);

        setState(GameState.GAME_PLAY); // 게임 상태로 전환
        startView.setVisible(false); // StartView 숨기기
        gameView.setVisible(true);   // GameView 보이기

        long afterVisibilityTime = System.currentTimeMillis();
        System.out.println("Set visibility time: " + (afterVisibilityTime - afterBoardCheckTime) + "ms");

        gameView.setBoardType(selectedBoard);
        gameView.displayPlayers(playerCount);
        gameView.displayHorses(selectedColors, playerCount, horseCount);
        //gameView.placeHorses(selectedColors, playerCount);

        long afterDisplayTime = System.currentTimeMillis();
        System.out.println("Display setup time: " + (afterDisplayTime - afterVisibilityTime) + "ms");
    }

    private void setState(GameState newState) {
        currentState = newState;
        updateViewState();
    }

    private void updateViewState() {
        startView.setState(currentState);
    }
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