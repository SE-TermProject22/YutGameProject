package Controller;

import View.Fx.EndView;
import View.Fx.GameView;
import View.Fx.StartView;
import Controller.GameController;

import java.util.EventObject;
import java.util.Map;

public class FXGameController {
    private StartView startView;
    private GameView gameView;
    private EndView endView;

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

        startView.setHorseSelectionListener(e -> {
            javafx.scene.control.Button clickedButton = (javafx.scene.control.Button) e.getSource();
            String color = null;

            for (Map.Entry<String, javafx.scene.control.Button> entry : startView.getHorseButtons().entrySet()) {
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
                } else if  (selectedHorseCount > playerCount) {
                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
                    alert.setTitle("경고");
                    alert.setHeaderText("null");
                    alert.setContentText("플레이어 수에 맞게 말을 선택해주세요.");
                    alert.showAndWait();

                    //선택 취소
                    startView.toggleHorseSelection(color);
                }
            }
        });
    }
}
