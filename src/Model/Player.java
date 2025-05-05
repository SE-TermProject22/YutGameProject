package Model;

import java.util.List;

// Player Class
public class Player {
    public int id;  // player 고유 id
    public int score;   // 점수 -  몇 개의 말이 들어왔는지
    public List<Horse> horseList;   // 보유 말들 list - id만 가지고 있어도 되고 Horse 자체를 가지고 있어도 됨!


    public Player() {

    }

    public void throwYut(Yut yut) {
        // 이런 식? yut.throwYut();
    }

}

// player가 승리했는지 확인
// 말의 움직임 하나가 끝날 때마다 말의 finish를 처리하면서 남은 말이 있는지 check
// -> 움직힐 수 있는 말이 없다면 승리?!! 일단 보류




