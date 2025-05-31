package Model;
import java.util.List;
import Controller.YutResult;

public class Horse {
    public int id;
    public int x, y;
    public Node currentNode;
    public String color;
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

    // 같은 node에 있는지 확인
    public boolean checkSameNode(Horse other) {
        if (other == this || !other.state || other.isDoubled) return false;
        if (this.x == other.x && this.y == other.y) return true;
        return false;
    }

    // 같은 팀인지 확인
    public boolean checkSameTeam(Horse other) {
        return this.color.equals(other.color);
    }

    // 같은 node에 있는 Horse 찾기
    public Horse findSameNodeHorse(List<Player> players) {
        for (Player player : players) {
            for (Horse other : player.getHorseList()) {
                if(this.checkSameNode(other))
                    return other;
            }
        }
        return null;
    }

    public DoubledHorse stack(int new_id, Player player, Horse other) {
        DoubledHorse dh = new DoubledHorse(new_id, this, other);
        player.addHorse(dh);
        return dh;
    }

    public void catched(Node firstNode, Player player){
        this.state = false;
        this.currentNode = firstNode; // 시작점으로
        this.x = this.currentNode.x;
        this.y = this.currentNode.y;
    }


    public void move(YutResult result) {
        if(result==YutResult.BackDo){
            System.out.println("백도 처리 시작");
            this.currentNode = currentNode.backDoNode;
            this.x = currentNode.x;
            this.y = currentNode.y;
            return;
        }

        if(currentNode.isDaegak) {
            this.currentNode = ((DaegakNode)currentNode).DNode;
        }
        else {
            this.currentNode = currentNode.nextNode;
        }
        for(int i=0; i< result.ordinal(); i++){
            if(result == YutResult.BackDo) break; // 여기는 백도 처리 안함
            this.currentNode = currentNode.nextNode;
        }
        // 원래 이부분은 마지막에만 해주면 됨
        this.x = currentNode.x;
        this.y = currentNode.y;
    }

    public void finish(Player player){
        if(!currentNode.isEndNode) return;
        this.state = false;
        player.removeHorse(this);
        player.addScore();
    }

    public Player getPlayer(List<Player> players) {
        for (Player player : players) {
            if(player.getColor().equals(this.color)) return player;
        }
        return null;
    }
}
