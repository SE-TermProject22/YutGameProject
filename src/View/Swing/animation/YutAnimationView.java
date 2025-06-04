package View.Swing.animation;

import Controller.YutResult;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class YutAnimationView {
    private JPanel parentPanel;
    private List<Image> yutImages;
    private List<Image> resultImages;
    private Timer animationTimer;
    private int yutIndex;
    private Image currentImage;

    public YutAnimationView(JPanel parentPanel) {
        this.parentPanel = parentPanel;
        loadImages();
    }

    private void loadImages() {
        yutImages = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            Image img = new ImageIcon(getClass().getResource("/image/yut/yut" + i + ".png")).getImage();
            if (img != null) {
                yutImages.add(img);
            }
        }

        resultImages = new ArrayList<>();
        String[] resultImageNames = {"1.png", "2.png", "3.png", "4.png", "5.png", "-1.png"};
        for (String imageName : resultImageNames) {
            Image resultImg = new ImageIcon(getClass().getResource("/image/" + imageName)).getImage();
            if (resultImg != null) {
                resultImages.add(resultImg);
            }
        }
    }

    public void startYutAnimation(YutResult result) {
        yutIndex = 0;

        if (animationTimer != null) {
            animationTimer.cancel();
        }

        animationTimer = new Timer();
        animationTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (yutIndex < yutImages.size()) {
                    setCurrentImage(yutImages.get(yutIndex));
                    yutIndex++;
                } else {
                    animationTimer.cancel();
                    showResultImage(result);
                }
            }
        }, 0, 300);
    }

    private void setCurrentImage(Image image) {
        currentImage = image;
        parentPanel.repaint();
    }

    private Image getResultImagePathForYutValue(YutResult result) {
        switch (result) {
            case YutResult.DO:
                return resultImages.get(0);
            case YutResult.GAE:
                return resultImages.get(1);
            case YutResult.GEOL:
                return resultImages.get(2);
            case YutResult.YUT:
                return resultImages.get(3);
            case YutResult.MO:
                return resultImages.get(4);
            case YutResult.BackDo:
                return resultImages.get(5);
            default:
                return null;
        }
    }

    private void showResultImage(YutResult result) {
        Image resultImage = getResultImagePathForYutValue(result);
        if (resultImage != null) {
            setCurrentImage(resultImage);
        }
    }

    public Image getCurrentImage() {
        return currentImage;
    }

    public void reset() {
        if (animationTimer != null) {
            animationTimer.cancel();
        }
        currentImage = null;
    }
}