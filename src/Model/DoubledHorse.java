package Model;

import java.util.ArrayList;
import java.util.List;

public class DoubledHorse extends Horse{
    private List<Horse> carriedHorses = new ArrayList<>();
    public int horseCount = 0;

    public DoubledHorse(int id, Horse horse1, Horse horse2) {
        super(id, horse1.color, horse1.currentNode);
        this.state = true;
        if(horse1 instanceof DoubledHorse) {
            carriedHorses.add((DoubledHorse) ((DoubledHorse) horse1).carriedHorses);
            horseCount = ((DoubledHorse) horse1).horseCount;
        }
        else{
            carriedHorses.add(horse1);
            horseCount++;
        }
        if(horse2 instanceof DoubledHorse) {
            carriedHorses.add((DoubledHorse) ((DoubledHorse) horse2).carriedHorses);
            horseCount = ((DoubledHorse) horse2).horseCount;
        }
        else{
            carriedHorses.add(horse2);
            horseCount++;
        }
    }
    public List<Horse> getCarriedHorses() {
        return carriedHorses;
    }
}
