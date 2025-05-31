package Model;

import Controller.YutResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Player Class
public class Player {
    private int id;  // player 고유 id
    // -- private int horseType; // 해당 player가 가지는 horse 색
    // -- private boolean isMyTurn; // 현재 턴인지 여부
    private int score;   // 점수 -  몇 개의 말이 들어왔는지
    private List<Horse> horseList = new ArrayList<>();   // 보유 말들 list - id만 가지고 있어도 되고 Horse 자체를 가지고 있어도 됨!

    //추가
    private String color;

    public Player(int id, String color) {
        //추가
        this.id = id;
        this.color = color;
    }
    public void addHorse(Horse horse) {
        horseList.add(horse);   // 일단 이렇게 바로 add를 하는데 나중에는 함수를 만들어서 하던지 합시다^
    }
    public List<Horse> getHorseList(){
        return horseList;
    }

    public List<Horse> selectableHorse(){
        List<Horse> selectableHorses = new ArrayList<>();
        for (Horse horse : horseList) {
            if(horse.isDoubled)
                continue;
            selectableHorses.add(horse);
        }
        return selectableHorses;
    }

    public void removeHorse(Horse horse) {
        horseList.remove(horse);
    }

    public int getId() {
        return id;
    }

    public void addScore() {
        score++;
    }

    public int getScore() {
        return score;
    }

    public String getColor() {
        return color;
    }
}


