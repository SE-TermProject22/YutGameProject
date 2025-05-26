package Model;

public class Node {
    public int x, y;
    public int id;
    public boolean isDaegak = false;
    public boolean isEndNode = false; //EndNode - 마지막 5개 node
    public Node nextNode;   // 다음 node를 저장 -> 경로 지정, 이때 다음 node의 id만 저장할지 Node 자체를 저장할지는 미정, 일단 Node를 저장한다고 해 둠
    public Node backDoNode; // 백도 node를 저장
    // public boolean isFirstNode = false;
    // public boolean isLastNode = false;

    public Node(int id, int x, int y) {
        // width, heith 민정이랑 은지가 알려주면 위에서 초기에 setting 하려구!!
        this.x = x;
        this.y = y;
        this.id = id;
    }
}