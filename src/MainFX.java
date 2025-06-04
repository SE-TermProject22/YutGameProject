import Controller.FXGameController;
import View.Fx.EndView;
import View.Fx.GameView;
import View.Fx.StartView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFX {

    public static void main(String[] args) {
        Application.launch(FXApp.class, args);  // 내부 FXApp 실행
    }

    public static class FXApp extends Application {
        @Override
        public void start(Stage primaryStage) {
            StartView startView = new StartView();
            GameView gameView = new GameView();
            EndView endView = new EndView();

            Scene scene = new Scene(startView, 1100, 700);
            new FXGameController(primaryStage, startView, gameView, endView);

            primaryStage.setTitle("Horse Game (JavaFX)");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        }
    }
}
