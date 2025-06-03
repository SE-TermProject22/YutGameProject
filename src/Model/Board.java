package Model;

import java.util.ArrayList;
import java.util.List;
import Model.Node;

public class Board {
    public List<Node> nodes = new ArrayList<>();
    //
    public Board(String boardType) {
        if(boardType == "square") {
            createNode_square();
            connectNode_square();
            backDo_connect_square();
        }
        else if(boardType == "pentagon") {
            createNode_pentagon();
            connectNode_pentagon();
            backDo_connect_pentagon();
        }
        else if(boardType == "hexagon") {
            createNode_hexagon();
            connectNode_hexagon();
            backDo_connect_hexagon();
        }
    }

    public void createNode_square(){
        nodes.add(new Node(0, 555, 560));
        //nodes.get(0).isFirstNode = true;
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
        nodes.add(new Node(30, 555, 560));

        // nodes.get(30).isLastNode = true;

        // EndNode - node 위치 x만 살짝 옮길게요~!!
        nodes.add(new EndNode(31, 556, 560));
        nodes.add(new EndNode(32, 556, 560));
        nodes.add(new EndNode(33, 556, 560));
        nodes.add(new EndNode(34, 556, 560));
        nodes.add(new EndNode(35, 556, 560));

        // 15부분에 노드 하나더 추가
        nodes.add(new Node(36, 93, 562));

        // 출발점 노드
        nodes.add(new Node(37, 555, 560));
    }

    public void connectNode_square(){
        for(int i = 0; i < 35; i++){
            nodes.get(i).nextNode = nodes.get(i+1);
        }
        ((DaegakNode)nodes.get(5)).DNode = nodes.get(20);
        ((DaegakNode)nodes.get(10)).DNode = nodes.get(25);
        ((DaegakNode)nodes.get(22)).DNode = nodes.get(28);

        nodes.get(29).nextNode = nodes.get(37);
        nodes.get(24).nextNode = nodes.get(36);
        nodes.get(36).nextNode = nodes.get(16);
        nodes.get(19).nextNode = nodes.get(30);
        nodes.get(35).nextNode = null;
        nodes.get(37).nextNode = nodes.get(31);
    }
    public void backDo_connect_square(){
        for(int i = 29; i > 1; i--){
            nodes.get(i).backDoNode = nodes.get(i-1);
        }
        nodes.get(1).backDoNode = nodes.get(30);
        nodes.get(0).backDoNode = nodes.get(0);
        nodes.get(37).backDoNode = nodes.get(29);
        nodes.get(20).backDoNode = nodes.get(5);
        nodes.get(25).backDoNode = nodes.get(10);
        nodes.get(36).backDoNode = nodes.get(24);
        nodes.get(30).backDoNode = nodes.get(19);
    }

    // x,y 지시
    public void createNode_pentagon(){
        nodes.add(new Node(0, 67, 257));
        // nodes.get(0).isFirstNode = true;
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
        nodes.add(new Node(25, 67, 257));

        //EndNode - x위치만 살짝 옮길게요~!!
        nodes.add(new EndNode(26, 68, 257));
        nodes.add(new EndNode(27, 68, 257));
        nodes.add(new EndNode(28, 68, 257));
        nodes.add(new EndNode(29, 68, 257));
        nodes.add(new EndNode(30, 68, 257));
        /////////////////////////////////////////////

        nodes.add(new Node(31, 216, 494));
        nodes.add(new Node(32, 268, 419));
        nodes.add(new DaegakNode(33, 328, 341)); // center
        nodes.add(new Node(34, 328, 253));
        nodes.add(new Node(35, 328, 163));
        nodes.add(new Node(36, 436, 493));
        nodes.add(new Node(37, 382, 419));
        nodes.add(new DaegakNode(38, 500, 287));
        nodes.add(new Node(39, 414, 314));
        nodes.add(new DaegakNode(40, 328, 341)); // center
        nodes.add(new Node(41, 238, 316));
        nodes.add(new Node(42,150,286));
        nodes.add(new DaegakNode(43, 328, 341)); // center
        nodes.add(new Node(44, 67, 257)); // 끝부분
        nodes.add(new Node(45,328, 76)); // 20번 노드랑 겹침
    }
    public void connectNode_pentagon(){
        for(int i = 0; i < 42; i++){
            nodes.get(i).nextNode = nodes.get(i+1); // 일단 다 nextNode만들어두기
        }
        ((DaegakNode)nodes.get(5)).DNode = nodes.get(31);
        ((DaegakNode)nodes.get(10)).DNode = nodes.get(36);
        ((DaegakNode)nodes.get(15)).DNode = nodes.get(38);
        ((DaegakNode)nodes.get(33)).DNode = nodes.get(41); // center
        ((DaegakNode)nodes.get(40)).DNode = nodes.get(41); // center
        ((DaegakNode)nodes.get(43)).DNode = nodes.get(41); // center

        nodes.get(37).nextNode = nodes.get(43);
        nodes.get(43).nextNode = nodes.get(34); // center
        nodes.get(40).nextNode = nodes.get(34); // center
        nodes.get(35).nextNode = nodes.get(45);
        nodes.get(42).nextNode = nodes.get(44);
        nodes.get(44).nextNode = nodes.get(26);
        nodes.get(45).nextNode = nodes.get(21); // 20번

        nodes.get(30).nextNode = null; //마지막 EndNode는 null

    }
    public void backDo_connect_pentagon(){
        for(int i = 25; i > 0; i--){
            nodes.get(i).backDoNode = nodes.get(i-1);
        }
        for(int i = 35; i>30; i--){
            nodes.get(i).backDoNode = nodes.get(i-1);
        }
        for(int i = 42; i>37; i--){
            nodes.get(i).backDoNode = nodes.get(i-1);
        }
        nodes.get(0).backDoNode = nodes.get(0);
        nodes.get(1).backDoNode = nodes.get(25);
        nodes.get(31).backDoNode = nodes.get(5); // 예외처리
        nodes.get(38).backDoNode = nodes.get(15);
        nodes.get(37).backDoNode = nodes.get(36);
        nodes.get(36).backDoNode = nodes.get(10);
        nodes.get(43).backDoNode = nodes.get(37);
        nodes.get(44).backDoNode = nodes.get(42);
        nodes.get(45).backDoNode = nodes.get(35); // 20번
    }

    // 육각형 보드 수정해야함

    public void createNode_hexagon(){
        nodes.add(new Node(0, 52, 323));
        // nodes.get(0).isFirstNode = true;
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
        nodes.add(new Node(30, 52, 323));

        //EndNode - x위치만 살짝 옮길게요~
        nodes.add(new EndNode(31, 53, 323));
        nodes.add(new EndNode(32, 53, 323));
        nodes.add(new EndNode(33, 53, 323));
        nodes.add(new EndNode(34, 53, 323));
        nodes.add(new EndNode(35, 53, 323));
        /////////////////////////////////////////////

        nodes.add(new Node(36, 232, 480));
        nodes.add(new Node(37, 272, 407));
        nodes.add(new DaegakNode(38, 317, 322)); // center
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
        nodes.add(new DaegakNode(49, 317, 322)); // center
        nodes.add(new DaegakNode(50, 317, 322)); // center
        nodes.add(new DaegakNode(51, 317, 322)); // center

        nodes.add(new Node(52, 191, 99)); // 25랑 겹치는
        nodes.add(new Node(53, 52, 323)); // 0이랑 겹치는
    }

    public void connectNode_hexagon(){
        for(int i = 0; i < 48; i++){
            nodes.get(i).nextNode = nodes.get(i+1);
        }
        ((DaegakNode)nodes.get(5)).DNode = nodes.get(36);
        ((DaegakNode)nodes.get(10)).DNode = nodes.get(41);
        ((DaegakNode)nodes.get(15)).DNode = nodes.get(43);
        ((DaegakNode)nodes.get(20)).DNode = nodes.get(45);
        ((DaegakNode)nodes.get(38)).DNode = nodes.get(47); // center
        ((DaegakNode)nodes.get(49)).DNode = nodes.get(47); // center
        ((DaegakNode)nodes.get(50)).DNode = nodes.get(47); // center
        ((DaegakNode)nodes.get(51)).DNode = nodes.get(47); // center

        nodes.get(49).nextNode = nodes.get(39); // center
        nodes.get(51).nextNode = nodes.get(39); // center
        nodes.get(50).nextNode = nodes.get(39); // center

        nodes.get(42).nextNode = nodes.get(49); // to center
        nodes.get(44).nextNode = nodes.get(50); // to center
        nodes.get(46).nextNode = nodes.get(51); // to center

        nodes.get(40).nextNode = nodes.get(52);
        nodes.get(52).nextNode = nodes.get(26); // 25와 겹치는

        nodes.get(48).nextNode = nodes.get(53);
        nodes.get(53).nextNode = nodes.get(31); // 0과 겹치는

        nodes.get(35).nextNode = null; //마지막 EndNode는 null
    }

    public void backDo_connect_hexagon(){
        for(int i = 35; i > 0; i--){
            nodes.get(i).backDoNode = nodes.get(i-1);
        }
        nodes.get(52).backDoNode = nodes.get(40); // 25의 백도는 40
        nodes.get(40).backDoNode = nodes.get(39);
        nodes.get(39).backDoNode = nodes.get(49);
        nodes.get(49).backDoNode = nodes.get(42);
        nodes.get(42).backDoNode = nodes.get(41);
        nodes.get(41).backDoNode = nodes.get(10);

        nodes.get(51).backDoNode = nodes.get(46);
        nodes.get(46).backDoNode = nodes.get(45);
        nodes.get(45).backDoNode = nodes.get(20);

        nodes.get(53).backDoNode = nodes.get(48);
        nodes.get(48).backDoNode = nodes.get(47);
        nodes.get(47).backDoNode = nodes.get(50); // to center
        nodes.get(50).backDoNode = nodes.get(44);
        nodes.get(44).backDoNode = nodes.get(43);
        nodes.get(43).backDoNode = nodes.get(15);

        nodes.get(38).backDoNode = nodes.get(37);
        nodes.get(37).backDoNode = nodes.get(36);
        nodes.get(36).backDoNode = nodes.get(5);

        nodes.get(0).backDoNode = nodes.get(0);
        nodes.get(1).backDoNode = nodes.get(30); // 출발점

    }
}