package View.Interface;

import Controller.GameState;

import javax.swing.*;
import java.util.List;
import java.util.Map;

public interface IStartView {
    void setState(GameState state);

    void addStartButtonListener(Object listener);
    void addNextButtonListener(Object listener);
    void addBoardSelectionListeners(Object square, Object pentagon, Object hexagon);
    void addHorseSelectionListener(String color);

    int getPlayerCount();
    int getHorseCount();

    void selectBoard(String boardType);
    void toggleHorseSelection(String color);

    List<String> getSelectedColors();
    String getSelectedBoard();

    //void resetSelectionState();
    void resetUI(JFrame frame);
}