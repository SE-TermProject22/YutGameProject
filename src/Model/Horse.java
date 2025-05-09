package Model;

import Controller.YutResult;

public class Horse {
    public int id;
    public int x, y, width, height;
    public Node currentNode;
    public Node prevNode;
    public String color;
    // 생성자
    public Horse(int id, String color, Node currentNode) {
        this.id = id;
        this.color = color;
        this.currentNode = currentNode;
        this.x = currentNode.x;
        this.y = currentNode.y;

    }

    public void move(YutResult result) {
        /*
        if(result==YutResult.BackDo)
        */

        if(currentNode.isDaegak) {
            this.currentNode = ((DaegakNode)currentNode).DNode;
            // 원래 이부분은 마지막에만 해주면 됨
            this.x = currentNode.x;
            this.y = currentNode.y;
            System.out.println("대각 : HORSECLASS : horse x: " + this.x + "y: %d"+ this.y);
        }
        else {
            this.currentNode = currentNode.nextNode;
            // 원래 이부분은 마지막에만 해주면 됨
            this.x = currentNode.x;
            this.y = currentNode.y;
            System.out.println("HORSECLASS : horse x: " + this.x + "y: %d"+ this.y);

        }
        for(int i=0; i< result.ordinal(); i++){
            this.currentNode = currentNode.nextNode;
            // 원래 이부분은 마지막에만 해주면 됨
            this.x = currentNode.x;
            this.y = currentNode.y;
            System.out.println("HORSECLASS : horse x: " + this.x + "y: %d"+ this.y);
        }

        prevNode = currentNode;
    }

}
