import Model.Board;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    // 사각형 보드가 생성은 되었는지
    @Test
    void testBoardCreationWithSquare() {
        Board board = new Board("square");
        assertNotNull(board, "Board 인스턴스가 null이 아니어야 합니다.");
        assertFalse(board.nodes.isEmpty(), "노드 리스트는 비어 있으면 안 됩니다.");
    }
    
    // 오각형 보드가 생성은 되었는지
    @Test
    void testBoardCreationWithPentagon() {
        Board board = new Board("pentagon");
        assertNotNull(board, "Board 인스턴스가 null이 아니어야 합니다.");
        assertFalse(board.nodes.isEmpty(), "노드 리스트는 비어 있으면 안 됩니다.");
    }

    // 육각형 보드가 생성은 되었는지
    @Test
    void testBoardCreationWithHexagon() {
        Board board = new Board("hexagon");
        assertNotNull(board, "Board 인스턴스가 null이 아니어야 합니다.");
        assertFalse(board.nodes.isEmpty(), "노드 리스트는 비어 있으면 안 됩니다.");
    }

}

