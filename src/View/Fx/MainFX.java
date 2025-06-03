package View.Fx;

import Controller.FXUIRoot;
import Controller.IGameController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFX extends Application {
    @Override
    public void start(Stage primaryStage) {
        FXUIRoot uiRoot = new FXUIRoot(primaryStage);
        StartView startView = new StartView();
        GameView gameView = new GameView();
        EndView endView = new EndView();

        Scene scene = new Scene(startView, 1100, 700);

        new IGameController(uiRoot, startView, gameView, endView);

        primaryStage.setTitle("Horse Game(JavaFX)");
        primaryStage.setScene(scene);
        primaryStage.show();

//        // Stage 설정
//        primaryStage.setTitle("윷놀이 게임");
        primaryStage.setResizable(false);
    }

    public static void launchApp() {
        launch();  // 내부적으로 main() 없이 실행 가능
    }
}