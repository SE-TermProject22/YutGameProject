package View.Swing;

import View.IEndView;

import java.awt.Image;

public interface ISwingEndView extends IEndView {
    void updateEndView(java.awt.Image background, java.awt.Image[] winnerImages, int winnerId);
    void addRestartButtonListener(java.awt.event.ActionListener handler);
    void addExitButtonListener(java.awt.event.ActionListener handler);
}
