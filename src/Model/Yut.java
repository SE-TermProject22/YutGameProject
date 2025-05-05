package Model;

import java.util.ArrayList;
import java.util.List;

public class Yut {
    // 도 개 걸 윷 모
    public enum yutResult{
        BAEKDO,   // 빽도: -1칸 이동
        DO,        // 도: 1칸 이동
        GAE,       // 개: 2칸 이동
        GEOL,      // 걸: 3칸 이동
        YUT,       // 윷: 4칸 이동
        MO        // 모: 5칸 이동
    }

    // 랜덤하게 던저져서 결과 반환
    public List<yutResult> throwYut(){
        // 여기서 list를 만들어서
        List<yutResult> yutResults = new ArrayList<yutResult>();
        // YutList에 랜덤으로 결과 넣기
        // 결과가 모나 윷이면
        // 내부적으로 또 던지는 로직 추가하기
        return yutResults;
    }
}
