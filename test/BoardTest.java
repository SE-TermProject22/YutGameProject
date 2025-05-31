import Model.Board;
import Model.Node;
import Model.DaegakNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    public void testSquareBoardCreation() {
        Board squareBoard = new Board("square");

        // 노드 수 확인
        assertEquals(38, squareBoard.nodes.size());

        // 시작 노드 좌표 확인
        Node start = squareBoard.nodes.get(0);
        assertEquals(555, start.x);
        assertEquals(560, start.y);

        // 연결 확인
        Node second = squareBoard.nodes.get(1);
        assertEquals(second, start.nextNode);

        // backDoNode 확인
        Node last = squareBoard.nodes.get(30);
        assertEquals(squareBoard.nodes.get(29), last.backDoNode);

        // DaegakNode DNode 연결 확인
        DaegakNode d1 = (DaegakNode) squareBoard.nodes.get(5);
        assertEquals(squareBoard.nodes.get(20), d1.DNode);
    }
    @Test
    public void testSquareBackDoConnections() {
        Board squareBoard = new Board("square");

        // 1번 노드의 backDoNode는 30번 노드
        assertEquals(squareBoard.nodes.get(30), squareBoard.nodes.get(1).backDoNode);

        // 2 ~ 30번까지는 직전 노드를 backDoNode로 가리켜야 함
        for (int i = 30; i > 1; i--) {
            assertEquals(squareBoard.nodes.get(i - 1), squareBoard.nodes.get(i).backDoNode,
                    "Node " + i + "의 backDoNode가 Node " + (i - 1) + "이어야 합니다.");
        }

        // 0번 노드는 자기 자신을 backDoNode로 가리켜야 함
        assertEquals(squareBoard.nodes.get(0), squareBoard.nodes.get(0).backDoNode);

        // 37번 노드는 19번을 backDoNode로 가져야 함
        assertEquals(squareBoard.nodes.get(19), squareBoard.nodes.get(37).backDoNode);

        // DaegakNode 5번 → DNode 20번 → backDoNode는 5번
        DaegakNode d5 = (DaegakNode) squareBoard.nodes.get(5);
        Node d20 = squareBoard.nodes.get(20);
        assertEquals(d5, d20.backDoNode);

        // DaegakNode 10번 → DNode 25번 → backDoNode는 10번
        DaegakNode d10 = (DaegakNode) squareBoard.nodes.get(10);
        Node d25 = squareBoard.nodes.get(25);
        assertEquals(d10, d25.backDoNode);

        // 노드 36번은 24번을 backDoNode로 가져야 함
        assertEquals(squareBoard.nodes.get(24), squareBoard.nodes.get(36).backDoNode);
    }


    @Test
    public void testPentagonBoardCreation() {
        Board pentagonBoard = new Board("pentagon");

        // 노드 수 확인
        assertTrue(pentagonBoard.nodes.size() >= 46);

        // 시작 노드 좌표 확인
        Node start = pentagonBoard.nodes.get(0);
        assertEquals(67, start.x);
        assertEquals(257, start.y);

        // DaegakNode DNode 연결 확인
        DaegakNode d = (DaegakNode) pentagonBoard.nodes.get(5);
        assertNotNull(d.DNode);
    }
    @Test
    public void testPentagonBackDoConnections() {
        Board pentagonBoard = new Board("pentagon");

        // 노드 1의 backDoNode는 25번 노드여야 함
        Node n1 = pentagonBoard.nodes.get(1);
        Node n25 = pentagonBoard.nodes.get(25);
        assertEquals(n25, n1.backDoNode);

        // DaegakNode 5 → Node 31 연결 확인 (DNode), backDo도 확인
        DaegakNode d5 = (DaegakNode) pentagonBoard.nodes.get(5);
        Node n31 = pentagonBoard.nodes.get(31);
        assertEquals(n31, d5.DNode);
        assertEquals(d5, n31.backDoNode);

        // DaegakNode 15 → Node 38 연결 확인 (DNode), backDo도 확인
        DaegakNode d15 = (DaegakNode) pentagonBoard.nodes.get(15);
        Node n38 = pentagonBoard.nodes.get(38);
        assertEquals(n38, d15.DNode);
        assertEquals(d15, n38.backDoNode);

        // 꼬리 노드 확인
        Node n45 = pentagonBoard.nodes.get(45);
        Node n35 = pentagonBoard.nodes.get(35);
        assertEquals(n35, n45.backDoNode);
    }


    @Test
    public void testHexagonBoardCreation() {
        Board hexBoard = new Board("hexagon");

        // 노드 수 확인
        assertTrue(hexBoard.nodes.size() >= 54);

        // 특정 노드 좌표
        Node n25 = hexBoard.nodes.get(25);
        assertEquals(191, n25.x);
        assertEquals(99, n25.y);

        // backDoNode 연결 확인
        Node n40 = hexBoard.nodes.get(40);
        Node n52 = hexBoard.nodes.get(52);
        assertEquals(n40, n52.backDoNode);

        // DaegakNode DNode 연결 확인
        DaegakNode center = (DaegakNode) hexBoard.nodes.get(38);
        assertNotNull(center.DNode);
    }
    @Test
    public void testHexagonBackDoConnections() {
        Board hexBoard = new Board("hexagon");

        // Node 52의 backDoNode는 Node 40
        Node n52 = hexBoard.nodes.get(52);
        Node n40 = hexBoard.nodes.get(40);
        assertEquals(n40, n52.backDoNode);

        // Node 40 → 39 → center(49) → 42 → 41 → 10번 DaegakNode까지 확인
        assertEquals(hexBoard.nodes.get(39), n40.backDoNode);
        assertEquals(hexBoard.nodes.get(49), hexBoard.nodes.get(39).backDoNode);
        assertEquals(hexBoard.nodes.get(42), hexBoard.nodes.get(49).backDoNode);
        assertEquals(hexBoard.nodes.get(41), hexBoard.nodes.get(42).backDoNode);
        assertEquals(hexBoard.nodes.get(10), hexBoard.nodes.get(41).backDoNode);

        // 51 → 46 → 45 → 20 확인
        assertEquals(hexBoard.nodes.get(46), hexBoard.nodes.get(51).backDoNode);
        assertEquals(hexBoard.nodes.get(45), hexBoard.nodes.get(46).backDoNode);
        assertEquals(hexBoard.nodes.get(20), hexBoard.nodes.get(45).backDoNode);
    }

}
