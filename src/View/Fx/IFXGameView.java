package View.Fx;

import View.IGameView;
import javafx.scene.Scene;

public interface IFXGameView extends IGameView {
    Scene getScene();
    void clearHorses();
    void clearPlayers();
}
