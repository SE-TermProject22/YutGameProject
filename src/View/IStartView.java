package View;

import Controller.GameState;
import javafx.scene.Scene;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface IStartView {
    Object getRoot();
    void setState(GameState state);

    int getPlayerCount();

    int getHorseCount();

    void selectBoard(String boardType);

    void toggleHorseSelection(String color);

    List<String> getSelectedColors();

    String getSelectedBoard();

    Map<String, ? extends Object> getHorseButtons();

    void resetSelection();

    void setOnStart(Runnable handler);

    void setOnNext(Runnable handler);

    void setOnHorseSelected(Consumer<String> handler);

    void setOnBoardSelected(Consumer<String> handler);
}
