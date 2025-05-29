package View.Interface;

import Controller.YutResult;
import Model.Horse;

import java.util.List;
import java.util.function.Consumer;

public interface IShowMessageView {
    void showOneMoreMessage(YutResult result);
    void showEventMessage(String imagePath);

    void showYutResultChoiceDialog(List<YutResult> yutResults, Consumer<YutResult> onSelected);
    void showHorseChoiceDialog(List<Horse> horses, int horseCount, Consumer<Horse> onSelected);
    void showFixedYutChoiceDialog(Consumer<YutResult> onSelected);
}