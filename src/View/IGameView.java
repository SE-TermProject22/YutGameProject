package View;

import Controller.YutResult;
import Model.Horse;
import javafx.scene.Scene;

import java.util.List;
import java.util.function.Consumer;

public interface IGameView {
    Object getRoot();
    void initHorses(List<String> colors, int horseCount);
    void displayPlayers(int playerCount);
    void displayHorses(List<String> colors, int playerCount, int horseCount);
    void setBoardType(String type);

    void setHorseVisible(int horseId);
    void setHorseInvisible(int horseId);
    void setHorseToGray(int horseId);

    void moveHorse(int horseId, int x, int y);
    void mkDoubled(int horseId, String color, int count, int x, int y, int imageType);

    void showEventImage(String imagePath);
    void showYutResultChoiceDialog(List<YutResult> yutResults, Consumer<YutResult> onSelected);
    void showHorseSelectionDialog(List<Horse> horses, int horseCount, Consumer<Horse> onSelected);

    void setOnThrow(Runnable handler);
    void setOnSpecialThrow(Runnable handler);

    void setOnHorseSelected(Consumer<String> handler);

    void startYutAnimation(YutResult result);

    void showFixedYutChoiceDialog(Consumer<YutResult> onSelected);

    void scheduleNotifyingImage(YutResult selectedResult);

}

