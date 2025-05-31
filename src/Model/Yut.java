package Model;

import Controller.YutResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Yut {
    private ArrayList<YutResult> yutResultList;

    // 생성자
    public Yut(){
        yutResultList = new ArrayList<>();
    }

    // 던진 윷의 결과값(int)을 반환하는 함수
    public YutResult throwYut() {
        Random rand = new Random();
        int r = rand.nextInt(100); // 0~99
        YutResult result;
        if (r < 5) {             // 0~4 : 5%
            result = YutResult.BackDo;
        } else if (r < 12) {     // 5~11 : 7%
            result = YutResult.MO;
        } else if (r < 20) {     // 12~19 : 8%
            result = YutResult.YUT;
        } else if (r < 45) {     // 20~44 : 25%
            result = YutResult.GEOL;
        } else if (r < 75) {     // 45~74 : 30%
            result = YutResult.GAE;
        } else {                 // 75~99 : 25%
            result = YutResult.DO;
        }
        yutResultList.add(result);
        return result;
    }

    // 지정 윷 던지기
    public void throwYut(YutResult result) {
        yutResultList.add(result);
    }
    public ArrayList<YutResult> getYutResultList() {
        return yutResultList;
    }
    public int getYutResultListSize() {
        return yutResultList.size();
    }
    public void clearYutResultList() {
        yutResultList.clear();
    }
    public void removeYutResult(YutResult result) {
        yutResultList.remove(result);
    }
    public boolean isEmptyYutResultList() {
        return yutResultList.isEmpty();
    }
}
