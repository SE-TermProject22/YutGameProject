import Model.*;
import Controller.YutResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

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

    // 백도 확인(사각형)
    @ParameterizedTest
    @MethodSource("backDoCases_squareBoard")
    void move_shouldHandleBackDo_onVariousNodes(Node start, Node expected) {
        Horse horse = new Horse(1, "red", start);
        horse.state = true;

        horse.move(YutResult.BackDo);

        assertSame(expected, horse.currentNode,
                String.format("[사각형 백도]: %d → %d 기대", start.id, expected.id));
    }

    static Stream<Arguments> backDoCases_squareBoard() {
        Board board = new Board("square");
        List<Node> n = board.nodes;
        return Stream.of(
                Arguments.of(n.get(6), n.get(5)),    // 6 → 5
                Arguments.of(n.get(1), n.get(30)),   // 1 → 30 (예외)
                Arguments.of(n.get(0), n.get(0)),    // 0 → 0
                Arguments.of(n.get(36), n.get(24)),  // 36 → 24
                Arguments.of(n.get(37), n.get(29)),  // 37 → 29
                Arguments.of(n.get(20), n.get(5)),    // 20 → 5 (DaegakNode)
                Arguments.of(n.get(30), n.get(19))    // 30 → 19 (DaegakNode)
        );
    }
    // 백도 확인(오각형)
    @ParameterizedTest
    @MethodSource("backDoCases_pentagonBoard")
    void move_shouldHandleBackDo_onPentagonBoard(Node start, Node expected) {
        Horse horse = new Horse(1, "blue", start);
        horse.state = true;

        horse.move(YutResult.BackDo);

        assertSame(expected, horse.currentNode,
            String.format("[오각형 백도] %d → %d 기대", start.id, expected.id));
    }

    static Stream<Arguments> backDoCases_pentagonBoard() {
        Board board = new Board("pentagon");
        List<Node> n = board.nodes;
        return Stream.of(
                Arguments.of(n.get(6), n.get(5)),      // 일반 백도
                Arguments.of(n.get(1), n.get(25)),     // 예외 케이스: 1 → 25
                Arguments.of(n.get(0), n.get(0)),      // 시작점 → 자기자신
                Arguments.of(n.get(31), n.get(5)),     // 대각선 진입 → 원래 자리
                Arguments.of(n.get(36), n.get(10)),     // 대각선 진입 → 원래 자리
                Arguments.of(n.get(38), n.get(15)),    // 중심에서 백도
                Arguments.of(n.get(41), n.get(40)),    // 중심에서 하나 간 후에 백도
                Arguments.of(n.get(45), n.get(35)),    // 예외 경로 35->45
                Arguments.of(n.get(44), n.get(42)),    // 중심에서 백도
                Arguments.of(n.get(43), n.get(37))     // 여러 분기 경로 후 백도
        );
    }

    // 백도 확인(육각형)
    @ParameterizedTest
    @MethodSource("backDoCases_hexagonBoard")
    void move_shouldHandleBackDo_onHexagonBoard(Node start, Node expected) {
        Horse horse = new Horse(1, "green", start);
        horse.state = true;

        horse.move(YutResult.BackDo);

        assertSame(expected, horse.currentNode,
                String.format("[육각형 백도] %d → %d 기대", start.id, expected.id));
    }

    static Stream<Arguments> backDoCases_hexagonBoard() {
        Board board = new Board("hexagon");
        List<Node> n = board.nodes;
        return Stream.of(
                Arguments.of(n.get(6), n.get(5)),       // 일반 백도
                Arguments.of(n.get(1), n.get(30)),      // 예외 케이스: 1 → 30
                Arguments.of(n.get(0), n.get(0)),       // 시작점 → 자기자신
                Arguments.of(n.get(36), n.get(5)),      // 대각선 진입점 → 원래 자리
                Arguments.of(n.get(41), n.get(10)),      // 대각선 진입점 → 원래 자리
                Arguments.of(n.get(43), n.get(15)),      // 대각선 진입점 → 원래 자리
                Arguments.of(n.get(45), n.get(20)),      // 대각선 진입점 → 원래 자리
                Arguments.of(n.get(49), n.get(42)),     // 중심에서 백도
                Arguments.of(n.get(39), n.get(49)),     // 중심에서 하나더 간후 백도
                Arguments.of(n.get(47), n.get(50)),     // 중심에서 하나더 간후 백도
                Arguments.of(n.get(52), n.get(40)),     // 외부 갈래 → 중심
                Arguments.of(n.get(43), n.get(15))      // 대각선 끝에서 백도
        );
    }

    // 대각 노드로 이동하는가?
    @Test
    void move_shouldGoThroughDaegak() {
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

    // 업은말이 잡혔을 때
    @Test
    void catched_doubledHorse_shouldResetAllHorses() {
        Horse h1 = new Horse(1, "red", nodes.get(5));
        Horse h2 = new Horse(2, "red", nodes.get(5));
        h1.state = true;
        h2.state = true;

        player.addHorse(h1);
        player.addHorse(h2);

        DoubledHorse dh = h1.stack(999, player, h2);

        // 어떤 플레이어가 잡았다고 가정하고, 시작 노드로 되돌림
        Node startNode = nodes.get(0);
        dh.catched(startNode, player);

        // 두 말 모두 state = false, 위치 = 시작 노드
        for (Horse h : dh.getCarriedHorses()) {
            assertFalse(h.state);
            assertEquals(startNode, h.currentNode);
            assertEquals(startNode.x, h.x);
            assertEquals(startNode.y, h.y);
        }
    }

    // 업은 말이 들어왔을 떄 (finish)
    @Test
    void finish_doubledHorse_shouldScoreAndRemoveAll() {
        Horse h1 = new Horse(1, "red", nodes.get(31)); // EndNode
        Horse h2 = new Horse(2, "red", nodes.get(31));
        h1.state = true;
        h2.state = true;

        player.addHorse(h1);
        player.addHorse(h2);

        DoubledHorse dh = h1.stack(999, player, h2);
        dh.currentNode = nodes.get(31);

        dh.finish(player);

        // 상태 변화 확인
        for (Horse h : dh.getCarriedHorses()) {
            assertFalse(h.state);
        }

        // 점수 2점 올라갔는지
        assertEquals(2, player.getScore());

        // 플레이어 말 목록에서 제거됐는지
        for (Horse h : dh.getCarriedHorses()) {
            assertFalse(player.getHorseList().contains(h));
        }
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
