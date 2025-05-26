package Model;

public class DaegakNode extends Node {
    public Node DNode;
    DaegakNode(int id, int x, int y) {
        super(id, x, y);
        this.isDaegak = true;
    }
}