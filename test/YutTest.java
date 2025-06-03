import Model.Yut;
import Controller.YutResult;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class YutTest {

    // 결과가 null이 아닌 정상적인 YutResult enum값을 반환하는가?
    @Test
    void throwYut_returnsValidResult() {
        Yut yut = new Yut();
        YutResult result = yut.throwYut();

        assertNotNull(result); // null 아님
        boolean isValid = false;
        for (YutResult r : YutResult.values()) {
            if (r == result) {
                isValid = true;
                break;
            }
        }
        assertTrue(isValid, "결과가 YutResult enum 내 값이어야 합니다.");
    }

    // 랜덤 윷 던지기 test(1000번 중 한번은 나오는가)
    @Test
    void throwYut_probabilityDistribution() {
        Yut yut = new Yut();
        Set<YutResult> appearedResults = new HashSet<>();

        for (int i = 0; i < 1000; i++) {
            YutResult result = yut.throwYut();
            appearedResults.add(result);
        }

        for (YutResult expected : YutResult.values()) {
            assertTrue(appearedResults.contains(expected),
                    expected.name() + " 결과가 1000번 안에 적어도 한 번은 나와야 합니다.");
        }
    }
}
