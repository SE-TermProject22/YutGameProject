package View.Interface;

import java.util.List;

public interface IAboutHorseView {
    void initHorses(List<String> colors, int horseCount);
    void mkDoubled(int horseId, String color, int horseCount, int x, int y);

    void setHorseVisible(int horseId);
    void setHorseInvisible(int horseId);

    void moveHorse(int horseId, int x, int y);
    void setHorsePosition(String color, int x, int y); //사용 되는 곳 없긴 함
}