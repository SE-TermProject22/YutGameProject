package Model;

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

    // 생성자
    public Horse(int id, String color, Node currentNode) {
        this.id = id;
        this.color = color;
        this.currentNode = currentNode;
        this.x = currentNode.x;
        this.y = currentNode.y;

    }

    // 턴이 끝날때마다
    public int checkSameNodeAndTeam(Horse other){
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
        /*
        if(result==YutResult.BackDo)
        */

        // currentNode가 첫번째 node면 state = true 처리

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
            this.prevNode = currentNode; // 말이 자신의 prevNode 기억
            this.currentNode = currentNode.nextNode;
            // 원래 이부분은 마지막에만 해주면 됨
            this.x = currentNode.x;
            this.y = currentNode.y;
            System.out.println("HORSECLASS : horse x: " + this.x + "y: %d"+ this.y);
        }

        prevNode = currentNode;
    }

}
