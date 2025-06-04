package View.Fx.pane;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.HashMap;
import java.util.Map;

public class PlayerPane extends AnchorPane {

    private final Map<Integer, ImageView> playerViews = new HashMap<>();

    public void displayPlayers(int playerCount) {
        Point2D[] playerPositions = {
                new Point2D(692, 512),
                new Point2D(898, 512),
                new Point2D(692, 588),
                new Point2D(898, 588)
        };

        for (int i = 1; i <= playerCount; i++) {
            Image playerImage = new Image(getClass().getResourceAsStream("/image/player" + i + ".png"));
            ImageView playerView = new ImageView(playerImage);
            Point2D pos = playerPositions[i - 1];

            AnchorPane.setLeftAnchor(playerView, pos.getX());
            AnchorPane.setTopAnchor(playerView, pos.getY());

            this.getChildren().add(playerView);
            playerViews.put(i, playerView);
        }
    }

    public void clearPlayers() {
        for (ImageView view : playerViews.values()) {
            this.getChildren().remove(view);
        }
        playerViews.clear();
    }
}

