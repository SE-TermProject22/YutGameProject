package Model;

import java.util.ArrayList;
import Controller.YutResult;

public class Horse {
    public int id;
    public int x, y;
    public Node currentNode;
    public Node prevNode;
    public String color;
    // public boolean isFinished = false; - 그냥 list에서 pop 하기로 함. 이때 꼭 setInvisable 해주기
    public boolean state = false;
    public boolean isDoubled = false;
    private boolean HorseBackDoState = false;
    public boolean isFinished = false;

    // 생성자
    public Horse(int id, String color, Node currentNode) {
        this.id = id;
        this.color = color;
        this.currentNode = currentNode;
        this.x = currentNode.x;
        this.y = currentNode.y;

    }

    // 턴이 끝날때마다
    public int checkSameNodeAndTeam (Horse other){
        if (this.currentNode != other.currentNode) {
            return -1; // 노드 다름 → 업기 or 잡기 불가
        }

        if (this.color.equals(other.color)) {
            return 1;  // 같은 위치 + 같은 팀 → 업기 가능
        } else {
            return 0;  // 같은 위치 + 다른 팀 → 잡기 대상
        }
    }


    public void move(YutResult result) {
        if(result==YutResult.BackDo){
            System.out.println("백도 처리 시작");
            if(prevNode == null) {
                System.out.println("출발점임");//출발점임
            } else{
                Node temp = currentNode;
                currentNode = prevNode;
                prevNode = temp;
                x = currentNode.x;
                y = currentNode.y;
                if(currentNode.isFirstNode){
                    HorseBackDoState = true;
                }
            }
            return;
        }
        if(HorseBackDoState && result.ordinal() < 5 ){
            //finish처리
            isFinished = true;
            state = false;  // 컨트롤러가 score++ 해줄 수 있도록
            HorseBackDoState = false;
            return;
        }

        if(currentNode.isDaegak) {
            this.prevNode = currentNode; // 말이 자신의 prevNode 기억
            this.currentNode = ((DaegakNode)currentNode).DNode;
            // 원래 이부분은 마지막에만 해주면 됨
            this.x = currentNode.x;
            this.y = currentNode.y;
            System.out.println("대각 : HORSECLASS : horse x: " + this.x + "y: %d"+ this.y);
        }
        else {
            this.prevNode = currentNode; // 말이 자신의 prevNode 기억
            this.currentNode = currentNode.nextNode;
            // 원래 이부분은 마지막에만 해주면 됨
            this.x = currentNode.x;
            this.y = currentNode.y;
            System.out.println("HORSECLASS : horse x: " + this.x + "y: %d"+ this.y);

        }
        for(int i=0; i< result.ordinal(); i++){
            if(result == YutResult.BackDo) break; // 여기는 백도 처리 안함
            this.prevNode = currentNode; // 말이 자신의 prevNode 기억
            this.currentNode = currentNode.nextNode;
            // 원래 이부분은 마지막에만 해주면 됨
            this.x = currentNode.x;
            this.y = currentNode.y;
            System.out.println("HORSECLASS : horse x: " + this.x + "y: %d"+ this.y);
        }
        if (!currentNode.isFirstNode) {
            HorseBackDoState = false;
        }

    }

}
