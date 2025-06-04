package View.Fx.animation;

import Controller.YutResult;
import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class NotificationView extends AnchorPane {

    private final ImageView notifyingImageView = new ImageView();
    private final ImageView eventImageView = new ImageView();

    public NotificationView() {
        notifyingImageView.setLayoutX(291);
        notifyingImageView.setLayoutY(294);
        eventImageView.setLayoutX(291);
        eventImageView.setLayoutY(294);

        this.getChildren().addAll(notifyingImageView, eventImageView);
    }

    public void showThrowAgain(YutResult result) {
        String path = result == YutResult.YUT
                ? "/image/윷 한번더.png"
                : "/image/모 한번더.png";

        PauseTransition delayBeforeShow = new PauseTransition(Duration.seconds(2.3));
        delayBeforeShow.setOnFinished(e -> {
            Image image = new Image(getClass().getResourceAsStream(path));
            notifyingImageView.setImage(image);

            PauseTransition delayBeforeClear = new PauseTransition(Duration.seconds(1.1));
            delayBeforeClear.setOnFinished(ev -> notifyingImageView.setImage(null));
            delayBeforeClear.play();
        });
        delayBeforeShow.play();
    }

    public void showEvent(String path) {
        PauseTransition delayBeforeShow = new PauseTransition(Duration.millis(500));
        delayBeforeShow.setOnFinished(e -> {
            Image image = new Image(getClass().getResourceAsStream(path));
            eventImageView.setImage(image);
            eventImageView.setVisible(true);

            PauseTransition delayBeforeClear = new PauseTransition(Duration.millis(1100));
            delayBeforeClear.setOnFinished(ev -> {
                eventImageView.setImage(null);
                eventImageView.setVisible(false);
            });
            delayBeforeClear.play();
        });
        delayBeforeShow.play();
    }
}

