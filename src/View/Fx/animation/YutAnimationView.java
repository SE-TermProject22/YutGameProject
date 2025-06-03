package View.Fx.animation;

import Controller.YutResult;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class YutAnimationView extends AnchorPane {

    private final ImageView imageView = new ImageView();
    private final List<Image> yutImages = new ArrayList<>();
    private final List<Image> resultImages = new ArrayList<>();
    private Timeline animationTimeline;
    private int yutIndex = 0;

    public YutAnimationView() {
        imageView.setLayoutX(670);
        imageView.setLayoutY(40);
        this.getChildren().add(imageView);

        loadImages();
    }

    private void loadImages() {
        for (int i = 1; i <= 4; i++) {
            Image image = new Image(getClass().getResourceAsStream("/image/yut/yut" + i + ".png"));
            if (!image.isError()) yutImages.add(image);
        }

        String[] resultNames = {"1.png", "2.png", "3.png", "4.png", "5.png", "-1.png"};
        for (String name : resultNames) {
            Image img = new Image(getClass().getResourceAsStream("/image/" + name));
            if (!img.isError()) resultImages.add(img);
        }
    }

    public void startAnimation(YutResult result) {
        yutIndex = 0;

        if (animationTimeline != null) {
            animationTimeline.stop();
        }

        animationTimeline = new Timeline(new KeyFrame(Duration.millis(300), e -> {
            if (yutIndex < yutImages.size()) {
                imageView.setImage(yutImages.get(yutIndex++));
            } else {
                animationTimeline.stop();
                showResultImage(result);
            }
        }));

        animationTimeline.setCycleCount(Timeline.INDEFINITE);
        animationTimeline.play();
    }

    private void showResultImage(YutResult result) {
        imageView.setImage(getResultImage(result));
    }

    private Image getResultImage(YutResult result) {
        return switch (result) {
            case DO -> resultImages.get(0);
            case GAE -> resultImages.get(1);
            case GEOL -> resultImages.get(2);
            case YUT -> resultImages.get(3);
            case MO -> resultImages.get(4);
            case BackDo -> resultImages.get(5);
        };
    }

    public void setResultDirectly(YutResult result) {
        imageView.setImage(getResultImage(result));
    }
}
