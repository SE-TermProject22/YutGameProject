package Controller;

import View.Fx.EndView;
import View.Fx.GameView;
import View.Fx.StartView;
import Controller.GameController;

public class FXGameController {
    private View.Fx.StartView startView;
    private View.Fx.GameView gameView;
    private View.Fx.EndView endView;

    //나중에 필요하면 swing이랑 공통되는 부분만 넣은 컨트롤러로 변경
    private GameController gameController;

    public FXGameController(StartView startView, GameView gameView, EndView endView) {
        this.startView = startView;
        this.gameView = gameView;
        this.endView = endView;

//        this.gameController = new GameController();

        initializeFXListeners();
    }

    private void initializeFXListeners() {
        startView.addStartButtonListener(e -> {
            //System.out.println("✅ 시작 버튼 눌림!");
            startView.setState(GameState.HORSE_SELECTION);
        });
    }
}
