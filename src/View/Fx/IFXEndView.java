package View.Fx;

import View.IEndView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;

public interface IFXEndView extends IEndView {
    void updateEndView(Image endBackground, Image[] winnerImages, int winnerId);
    void addRestartButtonListener(EventHandler<ActionEvent> handler);
    void addExitButtonListener(EventHandler<ActionEvent> handler);
}
