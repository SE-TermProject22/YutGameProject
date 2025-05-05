package Controller;

import java.awt.event.ActionListener;
import View.StartView;
import View.GameView;

public class GameController implements ActionListener {
    private final GameView view;
    private final Yut yut;

    private boolean isGameStarted = false;
    private boolean isHorseSelected = false;
    private boolean isGameOver = false;

    public GameController(GameView view, Yut yut) {
        this.view = view;
        this.yut = yut;

        this.view.setController(this); //버튼 클릭 위임

        view.addStartButtonListener(e -> handleStart());
    }

    public void actionPerformed(ActionEvent e) {

    }

    public void handleStart() {
        isGameStarted = true;
        view.showHorseSelection();
    }

//    public void onConfirmYes() {
//        isHorseSelected = true;
//        view.startGame();
//    }
//
//    public void onConfirmNo() {
//
//    }

    public void handleRetry() {
        isGameStarted = false;
        isHorseSelected = false;
        isGameOver = false;
        horse.reset();
        view.resetGame();
    }

    public void handleExit() {
        System.exit(0);
    }

    public void onGameOver() {
        isGameOver = true;
        view.showGameOver();
    }

    startView.addStartButtonListener(e -> {
        frame.remove(startView);
        frame.add(gameView);
        frame.revalidate();
        frame.repaint();
    });
}