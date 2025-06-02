import Model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
public class SamePositionTest {
    private Board board;
    private List<Node> nodes;
    private Horse horse;


    @BeforeEach
    void setUp() {
        board = new Board("square");
        nodes = board.nodes;
        horse = new Horse(1, "red", nodes.get(0));
    }
    // 다른 노드
    @Test
    void checkSameNode_shouldReturnFalseIfDifferentCoordinates() {
        Horse other = new Horse(2, "blue", nodes.get(5)); // x, y 다름
        horse.state = true;
        other.state = true;

        assertFalse(horse.checkSameNode(other)); // 좌표 다르므로 false
    }

    // 같은 노드, 다른 팀
    @Test
    void checkSameNode_shouldReturnTrueIfSamePositionAndActive() {
        Horse other = new Horse(2, "blue", nodes.get(0));
        horse.state = true;
        other.state = true;

        assertTrue(horse.checkSameNode(other));
    }

    // 같은 노드, 같은 팀
    @Test
    void checkSameTeam_shouldReturnTrueIfSameColor() {
        Horse other = new Horse(2, "red", nodes.get(5));
        assertTrue(horse.checkSameTeam(other));
    }
}
