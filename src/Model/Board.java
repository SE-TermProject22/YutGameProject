package Model;

import java.util.ArrayList;
import java.util.List;
import Model.Node;

public class Board {
    public List<Node> nodes;
    //
    public Board(String boardType) {
        nodes = new ArrayList<Node>();
        if(boardType == "square") {
            createNode_square();
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
        for(int i = 0; i < 35; i++){
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
        nodes.add(new Node(0, 67, 257));
        nodes.add(new Node(1, 85, 314));
        nodes.add(new Node(2, 109, 380));
        nodes.add(new Node(3, 127, 439));
        nodes.add(new Node(4, 145, 497));
        nodes.add(new DaegakNode(5, 163, 564));
        nodes.add(new Node(6, 224, 565));
        nodes.add(new Node(7, 294, 564));
        nodes.add(new Node(8, 361, 565));
        nodes.add(new Node(9, 430, 565));
        nodes.add(new DaegakNode(10, 490, 564));
        nodes.add(new Node(11, 509, 501));
        nodes.add(new Node(12, 529, 438));
        nodes.add(new Node(13, 548, 375));
        nodes.add(new Node(14, 567, 317));
        nodes.add(new DaegakNode(15, 581, 257));
        nodes.add(new Node(16, 535, 219));
        nodes.add(new Node(17, 482, 181));
        nodes.add(new Node(18, 429, 143));
        nodes.add(new Node(19, 382, 107));
        nodes.add(new Node(20, 328, 76));
        nodes.add(new Node(21, 272, 106));
        nodes.add(new Node(22, 224, 142));
        nodes.add(new Node(23, 170, 178));
        nodes.add(new Node(24, 119, 220));

        //EndNode
        nodes.add(new EndNode(25, 67, 257));
        nodes.add(new EndNode(26, 67, 257));
        nodes.add(new EndNode(27, 67, 257));
        nodes.add(new EndNode(28, 67, 257));
        nodes.add(new EndNode(29, 67, 257));
        nodes.add(new EndNode(30, 67, 257));
        /////////////////////////////////////////////

        nodes.add(new Node(31, 216, 494));
        nodes.add(new Node(32, 268, 419));
        nodes.add(new DaegakNode(33, 328, 341));
        nodes.add(new Node(34, 328, 253));
        nodes.add(new Node(35, 328, 163));
        nodes.add(new Node(36, 436, 493));
        nodes.add(new Node(37, 382, 419));
        nodes.add(new DaegakNode(38, 500, 287));
        nodes.add(new Node(39, 414, 314));
        nodes.add(new Node(40, 238, 316));
        nodes.add(new Node(41, 150, 286));

    }
    public void connectNode_pentagon(){
        for(int i = 0; i < 41; i++){
            nodes.get(i).nextNode = nodes.get(i+1); // 일단 다 nextNode만들어두기
        }
        ((DaegakNode)nodes.get(5)).DNode = nodes.get(31);
        ((DaegakNode)nodes.get(10)).DNode = nodes.get(36);
        ((DaegakNode)nodes.get(15)).DNode = nodes.get(38);
        ((DaegakNode)nodes.get(33)).DNode = nodes.get(40);

        nodes.get(39).nextNode = nodes.get(33);
        nodes.get(37).nextNode = nodes.get(33);
        nodes.get(35).nextNode = nodes.get(20);
        nodes.get(41).nextNode = nodes.get(25);

        nodes.get(30).nextNode = null; //마지막 EndNode는 null

    }

    public void createNode_Hexagon(){
        nodes.add(new Node(0, 52, 323));
        nodes.add(new Node(1, 76, 376));
        nodes.add(new Node(2, 103, 423));
        nodes.add(new Node(3, 129, 466));
        nodes.add(new Node(4, 156, 510));
        nodes.add(new DaegakNode(5, 192, 561));   // 대각선 진입점

        nodes.add(new Node(6, 251, 561));
        nodes.add(new Node(7, 303, 561));
        nodes.add(new Node(8, 355, 561));
        nodes.add(new Node(9, 405, 561));
        nodes.add(new DaegakNode(10, 457, 561));  // 대각선 진입점

        nodes.add(new Node(11, 483, 508));
        nodes.add(new Node(12, 509, 466));
        nodes.add(new Node(13, 534, 422));
        nodes.add(new Node(14, 560, 377));
        nodes.add(new DaegakNode(15, 579, 322));  // 대각선 진입점

        nodes.add(new Node(16, 550, 270));
        nodes.add(new Node(17, 523, 224));
        nodes.add(new Node(18, 499, 181));
        nodes.add(new Node(19, 475, 140));
        nodes.add(new DaegakNode(20, 449, 99));
        nodes.add(new Node(21, 397, 99));
        nodes.add(new Node(22, 347, 99));
        nodes.add(new Node(23, 296, 99));
        nodes.add(new Node(24, 243, 99));
        nodes.add(new Node(25, 191, 99));
        nodes.add(new Node(26, 159, 142));
        nodes.add(new Node(27, 134, 185));
        nodes.add(new Node(28, 109, 228));
        nodes.add(new Node(29, 82, 274));

        //EndNode
        nodes.add(new EndNode(30, 52, 323));
        nodes.add(new EndNode(31, 52, 323));
        nodes.add(new EndNode(32, 52, 323));
        nodes.add(new EndNode(33, 52, 323));
        nodes.add(new EndNode(34, 52, 323));
        nodes.add(new EndNode(35, 52, 323));
        /////////////////////////////////////////////

        nodes.add(new Node(36, 232, 480));
        nodes.add(new Node(37, 272, 407));
        nodes.add(new DaegakNode(38, 317, 322));
        nodes.add(new Node(39, 275, 245));
        nodes.add(new Node(40, 232, 172));

        nodes.add(new Node(41, 407, 479));
        nodes.add(new Node(42, 365, 407));

        nodes.add(new Node(43, 498, 322));
        nodes.add(new Node(44, 415, 322));

        nodes.add(new Node(45, 412, 172));
        nodes.add(new Node(46, 370, 245));

        nodes.add(new Node(47, 219, 324));
        nodes.add(new Node(48, 135, 324));
    }

    public void connectNode_Hexagon(){
        for(int i = 0; i < 48; i++){
            nodes.get(i).nextNode = nodes.get(i+1);
        }
        ((DaegakNode)nodes.get(5)).DNode = nodes.get(36);
        ((DaegakNode)nodes.get(10)).DNode = nodes.get(41);
        ((DaegakNode)nodes.get(15)).DNode = nodes.get(43);
        ((DaegakNode)nodes.get(20)).DNode = nodes.get(45);
        ((DaegakNode)nodes.get(38)).DNode = nodes.get(47);

        nodes.get(42).nextNode = nodes.get(38);
        nodes.get(44).nextNode = nodes.get(38);
        nodes.get(46).nextNode = nodes.get(38);
        nodes.get(40).nextNode = nodes.get(25);
        nodes.get(48).nextNode = nodes.get(30);

        nodes.get(35).nextNode = null; //마지막 EndNode는 null
    }
}

// Board를 생성할 때 Node도 같이 생성!
// 보드 선택에 따라서 적절하게 Node를 생성