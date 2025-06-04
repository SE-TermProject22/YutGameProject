import javax.swing.*;
import Controller.SwingGameController;
import View.Swing.StartView;
import View.Swing.GameView;
import View.Swing.EndView;

public class MainSwing {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Horse Game (Swing)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1100, 700);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);

            StartView startView = new StartView();
            GameView gameView = new GameView();
            EndView endView = new EndView();

            gameView.setVisible(false);
            endView.setVisible(false);

            frame.setLayout(null);
            startView.setBounds(0, 0, 1100, 700);
            gameView.setBounds(0, 0, 1100, 700);
            endView.setBounds(0, 0, 1100, 700);

            frame.add(startView);
            frame.add(gameView);
            frame.add(endView);

            new SwingGameController(frame, startView, gameView, endView);

            frame.setVisible(true);
        });
    }
}
