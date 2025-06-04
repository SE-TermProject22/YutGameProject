package View.Fx.pane;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class ThrowButtonPane extends AnchorPane {

    private Button throwButton;
    private Button specialThrowButton;

    public ThrowButtonPane() {
        initButtons();
    }

    private void initButtons() {
        throwButton = createButton("/image/윷 던지기.png");
        AnchorPane.setLeftAnchor(throwButton, 735.0);
        AnchorPane.setTopAnchor(throwButton, 405.0);
        this.getChildren().add(throwButton);

        specialThrowButton = createButton("/image/지정던지기 버튼.png");
        AnchorPane.setLeftAnchor(specialThrowButton, 920.0);
        AnchorPane.setTopAnchor(specialThrowButton, 405.0);
        this.getChildren().add(specialThrowButton);
    }

    private Button createButton(String imagePath) {
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        ImageView imageView = new ImageView(image);

        imageView.setFitWidth(image.getWidth());
        imageView.setFitHeight(image.getHeight());

        Button button = new Button("", imageView);
        button.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-border-color: transparent;" +
                        "-fx-focus-color: transparent;" +
                        "-fx-faint-focus-color: transparent;"
        );
        return button;
    }

    public void setThrowButtonHandler(EventHandler<ActionEvent> handler) {
        throwButton.setOnAction(handler);
    }


    public void setSpecialThrowButtonHandler(EventHandler<ActionEvent> handler) {
        specialThrowButton.setOnAction(handler);
    }

    public Button getSpecialThrowButton() {
        return specialThrowButton;
    }
}
