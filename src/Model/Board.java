package Model;

import java.util.ArrayList;
import java.util.List;
import Model.Node;

public class Board {
    public int n_diagram;
    public List<Node> nodes;
    // Node ArrayList로 구현하고 나중에 DaegakNode일 때는
    //
    public Board(String boardType) {
        nodes = new ArrayList<Node>();
        createNode_square();
        if(boardType == "square") {
            //createNode_square();
            connectNode_square();
        }
        else if(boardType == "pentagon") {
            createNode_pentagon();
            connectNode_pentagon();
        }
        else if(boardType == "hexagon") {
            createNode_Hexagon();
            connectNode_Hexagon();
        }
    }

    public void createNode_square(){
        nodes.add(new Node(0, 555, 560));
        nodes.add(new Node(1, 555, 462));
        nodes.add(new Node(2, 555, 376));
        nodes.add(new Node(3, 555, 288));
        nodes.add(new Node(4, 555, 198));
        nodes.add(new DaegakNode(5, 555, 98));
        nodes.add(new Node(6, 456, 99));
        nodes.add(new Node(7, 368, 99));
        nodes.add(new Node(8, 279, 99));
        nodes.add(new Node(9, 192, 99));
        nodes.add(new DaegakNode(10, 93, 99));
        nodes.add(new Node(11, 93, 197));
        nodes.add(new Node(12, 93, 288));
        nodes.add(new Node(13, 93, 376));
        nodes.add(new Node(14, 93, 463));
        nodes.add(new Node(15, 93, 562));
        nodes.add(new Node(16, 192, 565));
        nodes.add(new Node(17, 281, 565));
        nodes.add(new Node(18, 369, 565));
        nodes.add(new Node(19, 456, 565));
        nodes.add(new Node(20, 470, 183));
        nodes.add(new Node(21, 402, 253));
        nodes.add(new DaegakNode(22, 326, 330));
        nodes.add(new Node(23, 247, 407));
        nodes.add(new Node(24, 176, 480));
        nodes.add(new Node(25, 177, 181));
        nodes.add(new Node(26, 248, 252));
        nodes.add(new Node(27, 326, 330));
        nodes.add(new Node(28, 401, 409));
        nodes.add(new Node(29, 474, 480));

        // EndNode
        nodes.add(new EndNode(30, 555, 560));
        nodes.add(new EndNode(31, 555, 560));
        nodes.add(new EndNode(32, 555, 560));
        nodes.add(new EndNode(33, 555, 560));
        nodes.add(new EndNode(34, 555, 560));
        nodes.add(new EndNode(35, 555, 560));

    }

    public void connectNode_square(){
        for(int i = 0; i < 30; i++){
            nodes.get(i).nextNode = nodes.get(i+1);
        }
        ((DaegakNode)nodes.get(5)).DNode = nodes.get(20);
        ((DaegakNode)nodes.get(10)).DNode = nodes.get(25);
        ((DaegakNode)nodes.get(22)).DNode = nodes.get(28);

        nodes.get(24).nextNode = nodes.get(15);
        nodes.get(19).nextNode = nodes.get(30);

    }

    // x,y 지시
    public void createNode_pentagon(){

    }
    public void connectNode_pentagon(){

    }

    public void createNode_Hexagon(){

    }

    public void connectNode_Hexagon(){

    }
}

// Board를 생성할 때 Node도 같이 생성!
// 보드 선택에 따라서 적절하게 Node를 생성