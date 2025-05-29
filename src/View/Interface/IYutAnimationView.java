package View.Interface;

import Controller.YutResult;

import java.awt.Image;

public interface IYutAnimationView {
    void startYutAnimation(YutResult result);
    void showResultImage(YutResult result);
    Image getResultImagePathForYutValue(YutResult result);
    default void setCurrentImage(Image image) {}
}