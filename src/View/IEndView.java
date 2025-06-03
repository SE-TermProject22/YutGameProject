package View;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;

public interface IEndView {
    Object getRoot();
    void setWinner(int playerId);
    void setOnRestart(Runnable handler);
    void setOnExit(Runnable handler);
}


