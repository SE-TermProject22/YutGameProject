import javax.swing.*;

import View.GameView;
import View.StartView;
import Controller.GameController;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Board Game");
        frame.setSize(1100, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        StartView startView = new StartView();
        startView.setBounds(0, 0, 1100, 700);

        GameView gameView = new GameView();
        gameView.setBounds(0, 0, 1100, 700);
        gameView.setVisible(false);

        frame.add(startView);
        frame.add(gameView);

        new GameController(startView, gameView);

        frame.setVisible(true);
    }
}