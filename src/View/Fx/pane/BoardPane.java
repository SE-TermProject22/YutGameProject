package View.Fx.pane;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class BoardPane extends AnchorPane {

    private final ImageView boardView;

    public BoardPane() {
        boardView = new ImageView();
        this.getChildren().add(boardView);
    }

    public void setBoardImage(String boardType) {
        String path = "/image/new " + boardType + " board.png";
        Image image = new Image(getClass().getResourceAsStream(path));
        boardView.setImage(image);
    }

    public ImageView getBoardView() {
        return boardView;
    }
}
