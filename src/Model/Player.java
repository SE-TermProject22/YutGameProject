package Model;

import java.util.List;
import java.util.Random;

// Player Class
public class Player {
    public int id;  // player 고유 id
    private int horseType;
    private boolean isMyTurn;
    public int score;
    public List<Horse> horseList;  // 말 목록

    public String color;
    public String getColor() {
        return this.color;
    }// 말 색상 (private → getColor()로 접근)

    // 생성자
    public Player(String color) {
        this.color = color;
    }

    // 윷 던지기
    public int throwYut() {
        int[] values = {1, 2, 3, 4, 5, -1};
        Random rand = new Random();
        int r = rand.nextInt(100);
        int value;

        if (r < 5) {
            value = -1;        // 빽도
        } else if (r < 12) {
            value = 5;         // 모
        } else if (r < 20) {
            value = 4;         // 윷
        } else if (r < 45) {
            value = 3;         // 걸
        } else if (r < 75) {
            value = 2;         // 개
        } else {
            value = 1;         // 도
        }
        return value;
    }

    public void chooseResultOrder() {
        System.out.println("적용할 윷 결과 순서를 선택합니다.");
    }

    public void chooseHorse() {
        System.out.println("적용할 말을 선택합니다.");
    }

    public void selectOrder() {
        System.out.println("순서를 선택합니다.");
    }

    // 필요한 경우: horseList 초기화 메서드 등 추가 가능
}
