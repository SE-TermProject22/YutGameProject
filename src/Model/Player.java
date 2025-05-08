package Model;

import java.util.List;
import java.util.Random;

// Player Class
public class Player {
    public int id;  // player 고유 id
    private int horseType;
    private boolean isMyTurn; // 현재 턴인지 여부
    public int score;   // 점수 -  몇 개의 말이 들어왔는지
    public List<Horse> horseList;   // 보유 말들 list - id만 가지고 있어도 되고 Horse 자체를 가지고 있어도 됨!


    public Player() {

    }

    // 던진 윷의 결과값(int)을 반환하는 함수
    public int throwYut() {
        int[] values = { 1, 2, 3, 4, 5, -1 };
        Random rand = new Random();
        int r = rand.nextInt(100); // 0~99
        int value;
        if (r < 5) {             // 0~4 : 5%
            value = -1;
        } else if (r < 12) {     // 5~11 : 7%
            value = 5;
        } else if (r < 20) {     // 12~19 : 8%
            value = 4;
        } else if (r < 45) {     // 20~44 : 25%
            value = 3;
        } else if (r < 75) {     // 45~74 : 30%
            value = 2;
        } else {                 // 75~99 : 25%
            value = 1;
        }
        return value;
    }
    public void chooseResultOrder() {
        // 윷 결과 중 어느 것을 선택할지 결정
        System.out.println("적용할 윷 결과 순서를 선택합니다.");
    }

    public void chooseHorse() {
        // 어떤 말을 사용할지 결정
        System.out.println("적용할 말을 선택합니다.");
    }

    public void selectOrder() {
        // 순서 선택 로직
        System.out.println("순서를 선택합니다.");
    }

    public void selectHorseType(String horseType) {
        // 말 종류를 설정
        this.horseType = horseType;
        System.out.println("선택한 말 종류: " + horseType);
    }


}

// player가 승리했는지 확인
// 말의 움직임 하나가 끝날 때마다 말의 finish를 처리하면서 남은 말이 있는지 check
// -> 움직힐 수 있는 말이 없다면 승리?!! 일단 보류




