package View;

import Model.Horse;

import javax.swing.*;
import java.util.Map;

public class GameView extends javax.swing.JPanel {
    public Map<Integer, JLabel> horseList;
    // 생성자
    public GameView() {

    }

    // 말 추가하기
    public void addHorse(Horse horse) {

    }

    //말 삭제하기 -> 필요할지는 모르겠지만 일단
    public void removeHorse(Horse horse) {}

    // 말 update 하기 - 말 움직이기
    public void updateHorse(Horse horse) {
        // 대충 이런 식으로 해서 말 하나 선정 -> 움직이도록
        JLabel movehorse = horseList.get(horse.id);
        movehorse.setLocation(horse.x, horse.y);
    }



    public Map<Integer, JLabel> getHorseList() {
        return horseList;
    }
    public void setHorseList(Map<Integer, JLabel> horseList) {}



}
