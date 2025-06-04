import javax.swing.*;
import Controller.SwingGameController;
import View.Swing.StartView;
import View.Swing.GameView;

import View.Fx.MainFX;
import View.Swing.EndView;

public class Main {

    public static void main(String[] args) {

        String uiMode = "swing";

        if (uiMode.equalsIgnoreCase("swing")) {
            SwingUI();
        } else if (uiMode.equalsIgnoreCase("fx")) {
            MainFX.launchApp();
        }
    }

    private static void SwingUI() {
        // Swing UI는 이벤트 디스패치 스레드에서 실행해야 안전
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Horse Game(javaSwing)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1100, 700);
            frame.setLocationRelativeTo(null); // 화면 중앙에 배치
            frame.setResizable(false);

            // 뷰들 생성
            StartView startView = new StartView();
            GameView gameView = new GameView();
            EndView endView = new EndView();

            gameView.setVisible(false); // 처음엔 안 보이게
            endView.setVisible(false);

            // 패널들을 JFrame에 추가
            frame.setLayout(null);
            startView.setBounds(0, 0, 1100, 700);
            gameView.setBounds(0, 0, 1100, 700);
            endView.setBounds(0, 0, 1100, 700);

            frame.add(startView);
            frame.add(gameView);
            frame.add(endView);

            // 컨트롤러 생성
            new SwingGameController(frame, startView, gameView, endView);

            frame.setVisible(true);
        });
    }
}