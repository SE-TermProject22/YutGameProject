package View.Swing.pane;

import javax.swing.*;
import java.awt.*;

public class BoardPane {
    private Image board;

    public void setBoardType(String boardType) {
        board = new ImageIcon(getClass().getResource("/image/new " + boardType + " board.png")).getImage();
    }

    public void paintBoard(Graphics g, int width, int height) {
        if (board != null) {
            g.drawImage(board, 0, 0, width, height, null);
        }
    }

    public void reset() {
        board = null;
    }
}