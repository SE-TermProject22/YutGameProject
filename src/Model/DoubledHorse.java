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
            carriedHorses.addAll(((DoubledHorse) horse1).carriedHorses);
            this.horseCount += ((DoubledHorse) horse1).horseCount;
        }
        else{
            carriedHorses.add(horse1);
            this.horseCount = horseCount + 1;
        }
        if(horse2 instanceof DoubledHorse) {
            carriedHorses.addAll(((DoubledHorse) horse2).carriedHorses);
            this.horseCount += ((DoubledHorse) horse2).horseCount;
        }
        else{
            carriedHorses.add(horse2);
            this.horseCount = horseCount + 1;
        }
    }
    public List<Horse> getCarriedHorses() {
        return carriedHorses;
    }
}
