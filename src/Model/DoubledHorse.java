package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoubledHorse extends Horse{
    public int createdOrder;
    private List<Horse> carriedHorses = new ArrayList<>();
    public int horseCount = 0;
//    public int createdOrder;
    private int imageType; // 0: 첫번째(연한색-2개.png), 1: 두번째(진한색-1개.png)

    // 색깔별로 연한 업힌말이 이미 만들어졌는지 추적 (ex: red → true)
    private static Map<String, Boolean> lightDoubleHorseUsed = new HashMap<>();

    public DoubledHorse(int id, Horse horse1, Horse horse2) {
        super(id, horse1.color, horse1.currentNode);
//        this.createdOrder = createdOrder;
        this.state = true;

        // horseCount 누적 초기화
        horseCount = 0;

        if (horse1 instanceof DoubledHorse) {
            carriedHorses.addAll(((DoubledHorse) horse1).carriedHorses);
            this.horseCount += ((DoubledHorse) horse1).horseCount;
        } else {
            carriedHorses.add(horse1);
            this.horseCount = horseCount + 1;
        }

        if (horse2 instanceof DoubledHorse) {
            carriedHorses.addAll(((DoubledHorse) horse2).carriedHorses);
            this.horseCount += ((DoubledHorse) horse2).horseCount;
        } else {
            carriedHorses.add(horse2);
            this.horseCount = horseCount + 1;
        }

        // 2마리 업힘일 때만 색상별로 이미지 판단
        if (horseCount == 2) {
            if (!lightDoubleHorseUsed.getOrDefault(color, false)) {
                imageType = 0;  // 연한색 (2개.png)
                lightDoubleHorseUsed.put(color, true);  // 이미 하나 생성했다고 표시
            } else {
                imageType = 1;  // 진한색 (1개.png)
            }
        } else {
            imageType = -1;  // 의미 없음
        }

        System.out.println("!!!업기 발생!!!" + horseCount + " horses and " + carriedHorses.size());
    }

    public int getImageType() {
        return imageType;
    }

    public List<Horse> getCarriedHorses() {
        return carriedHorses;
    }

    // 테스트나 게임 재시작 시 초기화 필요할 수 있음
    public static void resetLightDoubleHorseMap() {
        lightDoubleHorseUsed.clear();
    }

    public static void releaseLightImageForColor(String color) {
        lightDoubleHorseUsed.put(color, false);
    }


}


