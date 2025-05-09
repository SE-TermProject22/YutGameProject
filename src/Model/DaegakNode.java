
package Model;

public class DaegakNode extends Node {
    public Node DNode;   // 대각 node를 저장 -> 경로 지정

    public DaegakNode(int id, int x, int y) {
        super(id,x,y);
        this.isDaegak = true;
    }
}
