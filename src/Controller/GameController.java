package Controller;

import View.GameView;

public class GameController {
    public GameView gameView;
    public HorseController horseController;
    // 생성자
    public GameController(GameView gameView, HorseController horseController) {
        this.gameView = gameView;
        this.horseController = horseController;
        this.
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