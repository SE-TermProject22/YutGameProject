package Controller;

import Model.Board;
import View.StartView;
import View.GameView;
import Model.Player;
import Model.Horse;
import Model.Node;
import Model.EndNode;
import Model.Board;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class GameController {
    private StartView startView;
    private GameView gameView;

    private GameState currentState = GameState.START_SCREEN;
    private List<Player> players;
    private Board board;
    private Player currentPlayer;
    private int currentPlayerIndex = 0;
    private List<Integer> accumulatedResults = new ArrayList<>();

    public GameController(StartView startView, GameView gameView) {
        this.startView = startView;
        this.gameView = gameView;

        initializeListeners();
        updateViewState();
    }

    private void initializeListeners() {
        startView.addStartButtonListener(e -> setState(GameState.HORSE_SELECTION));

        startView.setHorseSelectionListener(e -> {
            String color = e.getActionCommand();
            startView.toggleHorseSelection(color);

            int playerCount = startView.getPlayerCount();
            int selectedHorseCount = startView.getSelectedColors().size();

            if (selectedHorseCount == playerCount) {
                setState(GameState.BOARD_SELECTION);
            } else if (selectedHorseCount > playerCount) {
                JOptionPane.showMessageDialog(null, "플레이어 수에 맞게 말을 선택해주세요.");
                startView.toggleHorseSelection("");
            }
        });

        startView.setBoardSelectionListeners(
                e -> startView.selectBoard("square"),
                e -> startView.selectBoard("pentagon"),
                e -> startView.selectBoard("hexagon")
        );

        startView.addNextButtonListener(e -> startGame());
        gameView.addThrowButtonListener(e -> handleYutThrow());
    }

    private void startGame() {
        String selectedBoard = startView.getSelectedBoard();

        // new 보드 연결
        // board = new Board();
        // square board
        // pentagon board
        // hexagon board
        Board board = new Board(selectedBoard);

        int playerCount = startView.getPlayerCount();
        int horseCount = startView.getHorseCount();
        List<String> selectedColors = startView.getSelectedColors();

        if (selectedBoard == null || selectedColors.size() != playerCount) {
            JOptionPane.showMessageDialog(null, "보드와 말 선택이 완료되지 않았습니다.");
            return;
        }

        List<Player> players = new ArrayList<>();

        for (String color : selectedColors) {
            Player player = new Player(color);
            List<Horse> horseList = new ArrayList<>();

            for (int i = 0; i < horseCount; i++) {
                Horse h = new Horse();
                h.id = i;
                h.color = color;
                h.currentNode = board.nodes.get(0);
                horseList.add(h);
            }
            player.horseList = horseList;
            players.add(player);
        }

        currentPlayerIndex = 0;
        currentPlayer = players.get(currentPlayerIndex);
        gameView.setPlayer(currentPlayer);

        setState(GameState.GAME_PLAY); // 게임 상태로 전환
        startView.setVisible(false); // StartView 숨기기
        gameView.setVisible(true);   // GameView 보이기

        gameView.setBoardType(selectedBoard);
        gameView.displayPlayers(playerCount);
        gameView.displayHorses(selectedColors, playerCount, horseCount);

      //gameView.placeHorses(selectedColors, playerCount);


    private void setState(GameState newState) {
        currentState = newState;
        updateViewState();
    }

    private void updateViewState() {
        startView.setState(currentState);
    }

    private void handleYutThrow() {
        accumulatedResults.clear();
        while (true) {
            int result = currentPlayer.throwYut();
            accumulatedResults.add(result);
            System.out.println("던진 윷 결과: " + result);

            if (result == 4) {
                JOptionPane.showMessageDialog(null, "윷! 한 번 더 던집니다."); // view로 빼기
            } else if (result == 5) {
                JOptionPane.showMessageDialog(null, "모! 한 번 더 던집니다."); // view로 빼기
            } else {
                break;
            }
        }
        promptYutResultSelection();
    }

    private void promptYutResultSelection() {
        while (!accumulatedResults.isEmpty()) {
            String[] resultOptions = new String[accumulatedResults.size()];
            for (int i = 0; i < accumulatedResults.size(); i++) {
                resultOptions[i] = convertResultToName(accumulatedResults.get(i));
            }

            int selected = JOptionPane.showOptionDialog(
                    null,
                    "적용할 윷 결과를 선택하세요:",
                    "윷 결과 선택",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    resultOptions,
                    resultOptions[0]
            );

            if (selected != -1) {
                int selectedResult = accumulatedResults.remove(selected);
                promptHorseSelection(selectedResult);
            } else {
                // 사용자가 취소를 선택한 경우 루프 종료
                break;
            }
        }
    }


    private String convertResultToName(int value) {
        return switch (value) {
            case -1 -> "빽도";
            case 1 -> "도";
            case 2 -> "개";
            case 3 -> "걸";
            case 4 -> "윷";
            case 5 -> "모";
            default -> value + "";
        };
    }

    private void promptHorseSelection(int selectedResult) {
        if (currentPlayer.horseList == null || currentPlayer.horseList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "이 플레이어는 말을 가지고 있지 않습니다.");
            return;
        }

        String[] horseOptions = new String[currentPlayer.horseList.size()];
        for (int i = 0; i < horseOptions.length; i++) {
            horseOptions[i] = "말 " + (i + 1);
        }

        int selected = JOptionPane.showOptionDialog(
                null,
                "어떤 말을 움직이시겠습니까?",
                "말 선택",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                horseOptions,
                horseOptions[0]
        );

        if (selected != -1) {
            moveHorse(currentPlayer.horseList.get(selected), selectedResult);
        }
    }

    private void moveHorse(Horse horse, int steps) {
        if (horse.currentNode == null) return;

        Node current = horse.currentNode;

        for (int i = 0; i < Math.abs(steps); i++) {
            if (steps > 0) {
                if (current.nextNode != null) current = current.nextNode;
            } else {
                if (horse.prevNode != null) current = horse.prevNode;
            }
        }

        horse.prevNode = horse.currentNode;
        horse.currentNode = current;
        gameView.setHorsePosition(horse.color, current.x, current.y);

        // ✅ 업기 (같은 팀 말 함께 이동)
        for (Player player : players) {
            for (Horse teammate : player.horseList) {
                if (teammate == horse) continue;
                if (teammate.currentNode == horse.prevNode && teammate.color.equals(horse.color)) {
                    teammate.prevNode = teammate.currentNode;
                    teammate.currentNode = horse.currentNode;
                    gameView.setHorsePosition(teammate.color, teammate.currentNode.x, teammate.currentNode.y);
                }
            }
        }

        // ✅ 완주 처리
        if (horse.currentNode instanceof EndNode || horse.currentNode.isEndNode) {
            horse.currentNode = null; // 말 제거
            currentPlayer.score++;
        }

        // ✅ 말 잡기
        checkHorseCollision(horse);

        // ✅ 턴 넘기기 및 승리 확인
        nextPlayer();
        checkVictoryCondition();
    }

    private void checkHorseCollision(Horse movingHorse) {
        for (Player player : players) {
            for (Horse other : player.horseList) {
                if (other == movingHorse) continue;
                if (other.currentNode == movingHorse.currentNode) {
                    if (!other.color.equals(movingHorse.color)) {
                        other.currentNode = getStartNode();
                        gameView.setHorsePosition(other.color, other.currentNode.x, other.currentNode.y);
                    }
                }
            }
        }
    }

    private void checkVictoryCondition() {
        for (Player player : players) {
            boolean allFinished = true;
            for (Horse horse : player.horseList) {
                if (horse.currentNode != null) {
                    allFinished = false;
                    break;
                }
            }
            if (allFinished) {
                JOptionPane.showMessageDialog(null, "🎉 " + player.color + " 플레이어 승리!");
                System.exit(0);
            }
        }
    }

    private Node getStartNode() {
        return board.nodes.get(0);
    }

    private void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        currentPlayer = players.get(currentPlayerIndex);
        gameView.setPlayer(currentPlayer);
        JOptionPane.showMessageDialog(null, "다음 턴: " + currentPlayer.color + " 플레이어");
    }
}
