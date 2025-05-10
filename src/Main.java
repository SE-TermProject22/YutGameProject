
import javax.swing.*;
import Controller.GameController;
import View.StartView;
import View.GameView;
import View.EndView; //추가

public class Main {
    private static JFrame frame;

    public static void main(String[] args) {

        SwingUtilities.invokeLater(Main::startApp);
    }

    public static void startApp() {
        frame = new JFrame("Horse Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 700);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

//public class Main {
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("Horse Game");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setSize(1100, 700);
//            frame.setLocationRelativeTo(null);
//            frame.setResizable(false);

            StartView startView = new StartView();
            GameView gameView = new GameView();
            EndView endView = new EndView(); //추가

            gameView.setVisible(false);
            endView.setVisible(false); //추가
            endView.setStartView(startView);  //추가: 재시작 시 StartView로 전환

            frame.setLayout(null);
            startView.setBounds(0, 0, 1100, 700);
            gameView.setBounds(0, 0, 1100, 700);
            endView.setBounds(0, 0, 1100, 700); //추가

            frame.add(startView);
            frame.add(gameView);
            frame.add(endView); //추가

//            new GameController(startView, gameView, endView); //추가
            GameController controller = new GameController(startView, gameView, endView);
            endView.setController(controller);
            endView.initButtonActions();


            frame.setVisible(true);
        }

    public static void restartApp() {
        frame.dispose();  // 기존 프레임 종료
        startApp();       // 새로운 프레임 및 컴포넌트 생성
    }

}




