import View.StartView;
import View.GameView;
import Controller.GameController;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Yut Nori Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 600);

            StartView startView = new StartView();
            GameView gameView = new GameView();
            GameController controller = new GameController(startView, gameView, frame);

            frame.add(startView);  // 최초에는 StartView만 보여줌
            frame.setVisible(true);
        });
    }
}