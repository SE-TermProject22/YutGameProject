package Model;

import java.util.ArrayList;
import java.util.List;

public class DoubledHorse extends Horse{
    private List<Horse> carriedHorses = new ArrayList<>();
    public int HorseCount = 1;

    public DoubledHorse(int id, String color, Node currentNode) {
        super(id, color, currentNode);
        this.state = true;
    }
    public void addHorse(Horse h) {
        carriedHorses.add(h);
    }

    public List<Horse> getCarriedHorses() {
        return carriedHorses;
    }

    public int getCarriedCount() {
        return carriedHorses.size();
    }
}
