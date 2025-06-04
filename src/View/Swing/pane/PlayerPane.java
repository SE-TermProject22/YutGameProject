package View.Swing.pane;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerPane {
    private JPanel parentPanel;
    private List<JLabel> playerImages = new ArrayList<>();

    public PlayerPane(JPanel parentPanel) {
        this.parentPanel = parentPanel;
    }

    public void displayPlayers(int playerCount) {
        Point[] playerPositions = {
                new Point(692, 502),
                new Point(898, 502),
                new Point(692, 578),
                new Point(898, 578)
        };

        for (int i = 1; i <= playerCount; i++) {
            ImageIcon playerIcon = new ImageIcon(getClass().getResource("/image/player" + i + ".png"));
            JLabel playerLabel = new JLabel(playerIcon);

            Point pos = playerPositions[i - 1];
            playerLabel.setBounds(pos.x, pos.y, playerIcon.getIconWidth(), playerIcon.getIconHeight());

            parentPanel.add(playerLabel);
            playerImages.add(playerLabel);
        }
        parentPanel.repaint();
    }

    public void reset() {
        for (JLabel playerLabel : playerImages) {
            parentPanel.remove(playerLabel);
        }
        playerImages.clear();
    }
}