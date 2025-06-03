import Model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerSelectableHorseTest {

    private Player player;
    private Node node;

    @BeforeEach
    void setUp() {
        player = new Player(1, "blue");
        node = new Node(0, 0, 0);
    }

    @Test
    void selectableHorse_shouldReturnOnlyNonDoubledHorses() {
        Horse h1 = new Horse(1, "blue", node);
        Horse h2 = new Horse(2, "blue", node);
        Horse h3 = new Horse(3, "blue", node);
        h2.isDoubled = true; // 제외 대상

        player.addHorse(h1);
        player.addHorse(h2);
        player.addHorse(h3);

        List<Horse> result = player.selectableHorse();

        assertEquals(2, result.size());
        assertTrue(result.contains(h1));
        assertFalse(result.contains(h2));
        assertTrue(result.contains(h3));
    }
}
