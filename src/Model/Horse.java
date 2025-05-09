package Model;

public class Horse {
    public int id;
    public String color;// ✅ 말 색상 (GameView에서 접근 가능하도록 public)
    public String getColor() {
        return this.color;
    }

    public int x, y, width, height;
    public Node currentNode;
    public Node prevNode;

    // 생성자
    public void Horse(){};

    public void move(){
        x = x++;     //
        y = y++;    // 이런 식으로 x, y,도 어떻게 움직일지 알아야

    }

}