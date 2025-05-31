package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoubledHorse extends Horse {
    public int createdOrder;
    private List<Horse> carriedHorses = new ArrayList<>();
    public int horseCount = 0;
    private int imageType; // 0: 첫번째(연한색-2개.png), 1: 두번째(진한색-1개.png)
    private static Map<String, Boolean> lightDoubleHorseUsed = new HashMap<>();

    public DoubledHorse(int id, Horse horse1, Horse horse2) {
        super(id, horse1.color, horse1.currentNode);
        this.state = true;

        // horseCount 누적 초기화
        // horseCount = 0;

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
        horse1.isDoubled = true;
        horse2.isDoubled = true;
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

    // 테스트나 게임 재시작 시 초기화 필요할 수 있음
    public static void resetLightDoubleHorseMap() {
        lightDoubleHorseUsed.clear();
    }

    public static void releaseLightImageForColor(String color) {
        lightDoubleHorseUsed.put(color, false);
    }

    public List<Horse> getCarriedHorses() {
        return carriedHorses;
    }

    public void finish(Player player) {
        if (!currentNode.isEndNode) return;
        for (Horse horse : carriedHorses) {
            horse.state = false;
            player.removeHorse(horse);
            player.addScore();
        }

        this.state = false;
        player.removeHorse(this);
    }

    public void catched(Node firstNode, Player player) {
        for (Horse horse : carriedHorses) {
            horse.state = false;
            horse.currentNode = firstNode; // 시작점으로
            horse.x = horse.currentNode.x;
            horse.y = horse.currentNode.y;
            horse.isDoubled = false;
        }
        player.removeHorse(this);
    }
}