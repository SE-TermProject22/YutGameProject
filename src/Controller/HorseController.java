package Controller;

import Model.Horse;
import View.GameView;

import java.util.List;

public class HorseController {
    public List<Horse>  everyHorse;
    public GameView gameView;
    public HorseController(List<Horse> everyHorse, GameView gameView) {
        this.everyHorse = everyHorse;
        this.gameView = gameView;
    }

    /*public void moveHorse(Horse horse) {
        horse.move();
        gameView.updateHorse(horse);
    }*/

}