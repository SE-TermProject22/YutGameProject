import Model.Board;
import Model.DaegakNode;
import Model.Node;
import org.junit.jupiter.api.Test;

import java.util.List;
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

        // DaegakNode DNode 연결 확인
        DaegakNode d = (DaegakNode) squareBoard.nodes.get(5);
        assertNotNull(d.DNode);
    }

    @Test
    public void testSquareNextConnections() {
        Board squareBoard = new Board("square");

        // 0~28번까지는 i -> i+1 이어야 함
        for (int i = 0; i < 29; i++) {
            if(i == 19) continue;
            if(i == 24) continue;
            assertEquals(squareBoard.nodes.get(i + 1), squareBoard.nodes.get(i).nextNode,
                    "Node " + i + "의 nextNode는 Node " + (i + 1) + "이어야 합니다.");
        }

        // 5, 10, 22번 DaegakNode의 DNode 연결 확인
        assertEquals(squareBoard.nodes.get(20), ((DaegakNode) squareBoard.nodes.get(5)).DNode);
        assertEquals(squareBoard.nodes.get(25), ((DaegakNode) squareBoard.nodes.get(10)).DNode);
        assertEquals(squareBoard.nodes.get(28), ((DaegakNode) squareBoard.nodes.get(22)).DNode);

        // 예외 연결 확인
        assertEquals(squareBoard.nodes.get(36), squareBoard.nodes.get(24).nextNode); // 24 → 36
        assertEquals(squareBoard.nodes.get(16), squareBoard.nodes.get(36).nextNode); // 36 → 16
        assertEquals(squareBoard.nodes.get(30), squareBoard.nodes.get(19).nextNode); // 19 → 30
        assertEquals(squareBoard.nodes.get(37), squareBoard.nodes.get(29).nextNode); // 19 → 30

        // 마지막 EndNode는 nextNode가 null
        assertNull(squareBoard.nodes.get(35).nextNode);
    }

    @Test
    public void testSquareBackDoConnections() {
        Board squareBoard = new Board("square");

        // 1번 노드의 backDoNode는 30번 노드
        assertEquals(squareBoard.nodes.get(30), squareBoard.nodes.get(1).backDoNode);

        // 2 ~ 29번까지는 직전 노드를 backDoNode로 가리켜야 함
        for (int i = 29; i > 1; i--) {
            if(i == 20) continue;
            if(i == 25) continue;
            assertEquals(squareBoard.nodes.get(i - 1), squareBoard.nodes.get(i).backDoNode,
                    "Node " + i + "의 backDoNode가 Node " + (i - 1) + "이어야 합니다.");
        }

        // 0번 노드는 자기 자신을 backDoNode로 가리켜야 함
        assertEquals(squareBoard.nodes.get(0), squareBoard.nodes.get(0).backDoNode);

        // 37번 노드는 29번을 backDoNode로 가져야 함
        assertEquals(squareBoard.nodes.get(29), squareBoard.nodes.get(37).backDoNode);

        // 30번 노드는 19번을 backDoNode로 가져야 함
        assertEquals(squareBoard.nodes.get(19), squareBoard.nodes.get(30).backDoNode);

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
    public void testPentagonNextConnections() {
        Board pentagonBoard = new Board("pentagon");

        // 0~24까지 기본적인 연결 확인
        for (int i = 0; i < 25; i++) {
            assertEquals(pentagonBoard.nodes.get(i + 1), pentagonBoard.nodes.get(i).nextNode);
        }

        // 대각선 연결 확인
        // 5, 1, 15
        assertEquals(pentagonBoard.nodes.get(31), ((DaegakNode) pentagonBoard.nodes.get(5)).DNode);
        assertEquals(pentagonBoard.nodes.get(36), ((DaegakNode) pentagonBoard.nodes.get(10)).DNode);
        assertEquals(pentagonBoard.nodes.get(38), ((DaegakNode) pentagonBoard.nodes.get(15)).DNode);
        assertEquals(pentagonBoard.nodes.get(41), ((DaegakNode) pentagonBoard.nodes.get(33)).DNode);

        // 노드 35 → 45
        assertEquals(pentagonBoard.nodes.get(45), pentagonBoard.nodes.get(35).nextNode,
                "Node 35의 nextNode는 Node 45이어야 합니다.");

        // 노드 42 → 44
        assertEquals(pentagonBoard.nodes.get(44), pentagonBoard.nodes.get(42).nextNode,
                "Node 42의 nextNode는 Node 44이어야 합니다.");

        // 노드 39 → 40
        assertEquals(pentagonBoard.nodes.get(40), pentagonBoard.nodes.get(39).nextNode,
                "Node 39의 nextNode는 Node 40이어야 합니다.");

        // 노드 37 → 43
        assertEquals(pentagonBoard.nodes.get(43), pentagonBoard.nodes.get(37).nextNode,
                "Node 37의 nextNode는 Node 43이어야 합니다.");

        // 센터에 도착하면 → 41
        assertEquals(pentagonBoard.nodes.get(41), ((DaegakNode) pentagonBoard.nodes.get(33)).DNode);

        // 센터의 next는 34
        assertEquals(pentagonBoard.nodes.get(34), (pentagonBoard.nodes.get(33)).nextNode);
        assertEquals(pentagonBoard.nodes.get(34), (pentagonBoard.nodes.get(40)).nextNode);
        assertEquals(pentagonBoard.nodes.get(34), (pentagonBoard.nodes.get(43)).nextNode);
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

        // 예외들
        // 노드 41의 backDoNode는 40
        assertEquals(pentagonBoard.nodes.get(40), pentagonBoard.nodes.get(41).backDoNode,
                "Node 41의 backDoNode는 Node 40이어야 합니다.");

        // 노드 33의 backDoNode는 32
        assertEquals(pentagonBoard.nodes.get(32), pentagonBoard.nodes.get(33).backDoNode,
                "Node 33의 backDoNode는 Node 32이어야 합니다.");

        // 노드 43의 backDoNode는 37
        assertEquals(pentagonBoard.nodes.get(37), pentagonBoard.nodes.get(43).backDoNode,
                "Node 43의 backDoNode는 Node 37이어야 합니다.");

        // 노드 34의 backDoNode는 43
        assertEquals(pentagonBoard.nodes.get(33), pentagonBoard.nodes.get(34).backDoNode,
                "Node 34의 backDoNode는 Node 43이어야 합니다.");
    }


    @Test
    public void testHexagonBoardCreation() {
        Board hexBoard = new Board("hexagon");

        // 노드 수 확인
        assertTrue(hexBoard.nodes.size() >= 54);

        // 시작 노드 좌표 확인
        Node start = hexBoard.nodes.get(0);
        assertEquals(52, start.x);
        assertEquals(323, start.y);

        // DaegakNode DNode 연결 확인
        DaegakNode center = (DaegakNode) hexBoard.nodes.get(38);
        assertNotNull(center.DNode);
    }
    @Test
    public void testHexagonNextConnections() {
        Board hexBoard = new Board("hexagon");

        // 0 ~ 34번까지는 기본 순서 연결 확인
        for (int i = 0; i < 35; i++) {
            assertEquals(hexBoard.nodes.get(i + 1), hexBoard.nodes.get(i).nextNode,
                    "Node " + i + "의 nextNode는 Node " + (i + 1) + "이어야 합니다.");
        }

        // 대각선 DNode 연결 확인
        // 5, 10, 15, 20, 38
        assertEquals(hexBoard.nodes.get(36), ((DaegakNode) hexBoard.nodes.get(5)).DNode);
        assertEquals(hexBoard.nodes.get(41), ((DaegakNode) hexBoard.nodes.get(10)).DNode);
        assertEquals(hexBoard.nodes.get(43), ((DaegakNode) hexBoard.nodes.get(15)).DNode);
        assertEquals(hexBoard.nodes.get(45), ((DaegakNode) hexBoard.nodes.get(20)).DNode);
        assertEquals(hexBoard.nodes.get(47), ((DaegakNode) hexBoard.nodes.get(38)).DNode);

        // center(38, 49, 50, 51) → 39
        for (int i : List.of(38, 49, 50, 51)) {
            DaegakNode center = (DaegakNode) hexBoard.nodes.get(i);
            assertEquals(hexBoard.nodes.get(39), center.nextNode);
        }

        // 진입 노드들 → center 연결 확인
        assertEquals(hexBoard.nodes.get(49), hexBoard.nodes.get(42).nextNode); // → center
        assertEquals(hexBoard.nodes.get(50), hexBoard.nodes.get(44).nextNode); // → center
        assertEquals(hexBoard.nodes.get(51), hexBoard.nodes.get(46).nextNode); // → center

        // 이어지는 경로
        assertEquals(hexBoard.nodes.get(52), hexBoard.nodes.get(40).nextNode);
        assertEquals(hexBoard.nodes.get(26), hexBoard.nodes.get(52).nextNode);

        assertEquals(hexBoard.nodes.get(53), hexBoard.nodes.get(48).nextNode);
        assertEquals(hexBoard.nodes.get(31), hexBoard.nodes.get(53).nextNode);

        // 마지막 EndNode(35)는 null
        assertNull(hexBoard.nodes.get(35).nextNode);
    }

    @Test
    public void testHexagonBackDoConnections() {
        Board hexBoard = new Board("hexagon");

        // 0번 노드는 자기 자신 가리킴
        assertEquals(hexBoard.nodes.get(0), hexBoard.nodes.get(0).backDoNode);

        // 1번 → 30번 (출발점 순환)
        assertEquals(hexBoard.nodes.get(30), hexBoard.nodes.get(1).backDoNode);

        // 기본 직선 루프 (1 ~ 35)
        for (int i = 35; i > 1; i--) {
            assertEquals(hexBoard.nodes.get(i - 1), hexBoard.nodes.get(i).backDoNode,
                    "Node " + i + "의 backDoNode는 Node " + (i - 1) + "이어야 합니다.");
        }

        // 중심 경로: 52 → 40 → 39 → 49 → 42 → 41 → 10
        assertEquals(hexBoard.nodes.get(40), hexBoard.nodes.get(52).backDoNode);
        assertEquals(hexBoard.nodes.get(39), hexBoard.nodes.get(40).backDoNode);
        assertEquals(hexBoard.nodes.get(49), hexBoard.nodes.get(39).backDoNode);
        assertEquals(hexBoard.nodes.get(42), hexBoard.nodes.get(49).backDoNode);
        assertEquals(hexBoard.nodes.get(41), hexBoard.nodes.get(42).backDoNode);
        assertEquals(hexBoard.nodes.get(10), hexBoard.nodes.get(41).backDoNode);

        // 다른 중심 진입 경로: 51 → 46 → 45 → 20
        assertEquals(hexBoard.nodes.get(46), hexBoard.nodes.get(51).backDoNode);
        assertEquals(hexBoard.nodes.get(45), hexBoard.nodes.get(46).backDoNode);
        assertEquals(hexBoard.nodes.get(20), hexBoard.nodes.get(45).backDoNode);

        // 반대편 경로: 53 → 48 → 47 → 50 → 44 → 43 → 15
        assertEquals(hexBoard.nodes.get(48), hexBoard.nodes.get(53).backDoNode);
        assertEquals(hexBoard.nodes.get(47), hexBoard.nodes.get(48).backDoNode);
        assertEquals(hexBoard.nodes.get(50), hexBoard.nodes.get(47).backDoNode);
        assertEquals(hexBoard.nodes.get(44), hexBoard.nodes.get(50).backDoNode);
        assertEquals(hexBoard.nodes.get(43), hexBoard.nodes.get(44).backDoNode);
        assertEquals(hexBoard.nodes.get(15), hexBoard.nodes.get(43).backDoNode);

        // 대각선 경로 진입 후 backDo 연결
        assertEquals(hexBoard.nodes.get(37), hexBoard.nodes.get(38).backDoNode);
        assertEquals(hexBoard.nodes.get(36), hexBoard.nodes.get(37).backDoNode);
        assertEquals(hexBoard.nodes.get(5), hexBoard.nodes.get(36).backDoNode);
    }


}
