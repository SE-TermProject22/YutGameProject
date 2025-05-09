package Model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    public int n_diagram;
    public List<Node> nodes;
    // Node ArrayList로 구현하고 나중에 DaegakNode일 때는
    public Board(int n) {
        n_diagram = n;
        nodes = new ArrayList<Node>();
        if(n==4){
            createNode_square();
            //connectNode_square();
        }
        else if(n==5){
            createNode_pentagon();
            connectNode_pentagon();
        }
        else if(n==6){
            createNode_Hexagon();
            connectNode_Hexagon();
        }
    }

    public void createNode_square(){
        int x = 1;
        int y = 1;
        for(int i = 0; i < 25; i++){
            if(i==10)
                nodes.add(new DaegakNode(i, x++, y++)); //x, y는 디자인 분들이 알려주세요
            //if(i==15)
        }
    }

  /*  public void connectNode_square(){
        for(int i = 0; i < 25; i++){
            nodes.get(i).nextNode = nodes.get(nodes.get(i++));
        }
    }*/

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