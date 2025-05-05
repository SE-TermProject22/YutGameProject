package Controller;

import java.util.List;
import javax.swing.JButton;

import View.StartView;
import View.GameView;

public class GameController {
    private StartView startView;
    private GameView gameView;

    private boolean isGameStarted = false;
    private boolean isHorseSelected = false;
    private boolean isGameOver = false;

    public GameController(StartView startView, GameView gameView) {
        this.startView = startView;
        this.gameView = gameView;

        setupListeners();
    }

    private void setupListeners() {
        startView.addStartButtonListener(e -> {

            startView.showSettings();
        });

        startView.setBoardSelectionListeners(
                e -> startView.selectBoard("square"),
                e -> startView.selectBoard("pentagon"),
                e -> startView.selectBoard("hexagon")
        );

        startView.setHorseSelectionListener(e -> {
            JButton source = (JButton) e.getSource();
            String color = getColorFromButton(source);
            startView.toggleHorseSelection(color);
        });

        startView.addNextButtonListener(e -> {
            // 설정 완료 후 GameView로 화면 전환
            startView.setVisible(false);
            gameView.setVisible(true);

            gameView.setBoardType(startView.getSelectedBoard());
            gameView.placeHorses(startView.getSelectedColors());
        });

        // 추가적인 리스너 (보드 버튼, 말 선택 등)도 설정 가능
    }

    private String getColorFromButton(JButton button) {
        for (var entry : startView.getHorseButtons().entrySet()) {
            if (entry.getValue().equals(button)) {
                return entry.getKey();
            }
        }
        return null;
    }
}