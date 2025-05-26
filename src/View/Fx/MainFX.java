package View.Fx;

import Controller.FXGameController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFX extends Application {
    @Override
    public void start(Stage primaryStage) {
        StartView startView = new StartView();
        GameView gameView = new GameView();
        EndView endView = new EndView();

        Scene scene = new Scene(startView, 1100, 700);

        new FXGameController(startView, gameView, endView);

        primaryStage.setTitle("Horse Game(JavaFX)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void launchApp() {
        launch();  // 내부적으로 main() 없이 실행 가능
    }
}
