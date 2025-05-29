package View.Interface;

import java.util.List;

public interface IGameScreenView {
    void loadImages();

    void setBoardType(String boardType);

    void displayPlayers(int playerCount);
    void displayHorses(List<String> selectedColors, int playerCount, int horseCount);

    void setHorseToGray(int horseId);

    void clearHorses();

    void addThrowButtonListener(Object listener);
    void addSpecialThrowListener(Object Listener);
}
