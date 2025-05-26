package View.Fx;
// MainApp.java (JavaFX 애플리케이션 진입점)
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import View.Fx.StartView;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        StartView startView = new StartView();
        Scene scene = new Scene(startView, 1100, 700);

        primaryStage.setTitle("Horse Game (JavaFX)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}