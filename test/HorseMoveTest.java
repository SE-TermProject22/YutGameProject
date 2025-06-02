import Model.*;
import Controller.YutResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class HorseMoveTest {

    private Board board;
    private List<Node> nodes;
    private Horse horse;
    private Player player;

    @BeforeEach
    void setUp() {
        // 사각형 board일 경우 test
        board = new Board("square");
        nodes = board.nodes;
        horse = new Horse(1, "red", nodes.get(0));
        horse.state = true;
        player = new Player(1, "red");
    }
    @Test
    void constructorInitialize() {
        assertEquals("red", horse.color);
        assertEquals(nodes.get(0), horse.currentNode);
        assertEquals(555, horse.x);
        assertEquals(560, horse.y);
    }

    static List<Arguments> yutMoves() {
        return List.of(
                Arguments.of(YutResult.DO, 1),
                Arguments.of(YutResult.GAE, 2),
                Arguments.of(YutResult.GEOL, 3),
                Arguments.of(YutResult.YUT, 4),
                Arguments.of(YutResult.MO, 5)
        );
    }

    // "도" 등이 나왔을 때 올바른 노드에 가는가?
    @ParameterizedTest
    @MethodSource("yutMoves")
    void move_shouldLandOnCorrectNode(YutResult result, int expectedIndex) {
        horse.currentNode = nodes.get(0);
        horse.move(result);

        Node expectedNode = nodes.get(expectedIndex);

        assertEquals(expectedNode.x, horse.x, "말의 x 좌표가 도착 노드와 달라요");
        assertEquals(expectedNode.y, horse.y, "말의 y 좌표가 도착 노드와 달라요");
    }

    // 백도 확인
    @Test
    void move_shouldHandleBackDo() {
        horse.state = true;
        horse.currentNode = nodes.get(20); // has backDoNode
        horse.move(YutResult.BackDo);

        assertEquals(nodes.get(5), horse.currentNode); // manually verify
    }

    @Test
    void move_shouldGoThroughDaegak() {
        // 대각 노드로 이동하는가?
        horse.currentNode = nodes.get(5);
        horse.state = true;

        horse.move(YutResult.DO); // 5칸이지만, DaegakNode라 중심으로 빠짐
        assertSame(nodes.get(20), horse.currentNode); // 중심 노드로 진입했는지 확인

    }

    @Test
    void stack_shouldCreateDoubledHorseAndAddToPlayer() {
        Horse other = new Horse(2, "red", nodes.get(0));
        DoubledHorse dh = horse.stack(100, player, other);

        assertNotNull(dh);
        assertTrue(player.getHorseList().contains(dh));
        assertEquals(2, dh.getCarriedHorses().size());
    }

    // 잡히면 reset
    @Test
    void catched_shouldResetHorse() {
        Node startNode = nodes.get(0);
        horse.state = true;
        horse.currentNode = nodes.get(5);

        horse.catched(startNode, player);

        assertFalse(horse.state);
        assertEquals(startNode, horse.currentNode);
        assertEquals(startNode.x, horse.x);
        assertEquals(startNode.y, horse.y);
    }

    // end node에 있으면 add score
    @Test
    void finish_shouldRemoveHorseAndAddScoreIfAtEndNode() {
        Node endNode = nodes.get(31); // EndNode
        horse.currentNode = endNode;
        player.addHorse(horse);

        horse.finish(player);

        assertFalse(horse.state);
        assertFalse(player.getHorseList().contains(horse));
        assertEquals(1, player.getScore());
    }

}
