package Controller;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class FXUIRoot implements IGameUIRoot {
    private Stage stage;

    public FXUIRoot(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void setContent(Object view) {
        System.out.println("🪄 [FXUIRoot] setContent 호출됨 - 타입: " + view.getClass().getName());

        if (view instanceof Scene scene) {
            stage.setScene(scene);
            stage.show();
        }
    }
}
