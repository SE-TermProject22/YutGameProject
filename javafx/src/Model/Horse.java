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

    // 잡기 확인(같은 노드인지, 같은 팀인지)
    public int checkSameNodeAndTeam(Horse other) {
        boolean sameNode = this.currentNode == other.currentNode;
        boolean bothInCenter = currentNode.isCenterNode && other.currentNode.isCenterNode;
        boolean bothInStart = currentNode.isFirstNode && other.currentNode.isLastNode;
        boolean sameTeam = this.color.equals(other.color);
        boolean bothInEnd = currentNode.isLastNode && other.currentNode.isFirstNode;

        if (sameNode || bothInCenter || bothInStart || bothInEnd) {
            return sameTeam ? 1 : 0; // 1: 업기 가능, 0: 잡기 가능
        }
        // currentnode.x = other.x
        // currentnode.y = other.y

        return -1; // 서로 다른 위치, 상호작용 없음
    }


    public void move(YutResult result) {
        if(result==YutResult.BackDo){
            System.out.println("백도 처리 시작");
            if(currentNode.backDoNode == null) {
                System.out.println("출발점임");//출발점임
            } else if(currentNode.backDoPrev) {
                Node temp = currentNode;
                currentNode = prevNode;
                prevNode = temp;
                x = currentNode.x;
                y = currentNode.y;
            }
            else{
                this.prevNode = currentNode;
                this.currentNode = currentNode.backDoNode;

                this.x = currentNode.x;
                this.y = currentNode.y;
                if (currentNode.isFirstNode) {
                    HorseBackDoState = true; // 다음에 도~모가 나오면 Finish 처리
                }
            }


            return;
        }

        // 30번 노드로 확인
        // finish처리 다시 확인하기 -> endnode로하게끔
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
        }
        else {
            this.prevNode = currentNode; // 말이 자신의 prevNode 기억
            this.currentNode = currentNode.nextNode;
            // 원래 이부분은 마지막에만 해주면 됨
            this.x = currentNode.x;
            this.y = currentNode.y;
        }
        for(int i=0; i< result.ordinal(); i++){
            if(result == YutResult.BackDo) break; // 여기는 백도 처리 안함
            this.prevNode = currentNode; // 말이 자신의 prevNode 기억
            this.currentNode = currentNode.nextNode;
            // 원래 이부분은 마지막에만 해주면 됨
            this.x = currentNode.x;
            this.y = currentNode.y;
        }
        if (!currentNode.isFirstNode) {
            HorseBackDoState = false;
        }

    }

}