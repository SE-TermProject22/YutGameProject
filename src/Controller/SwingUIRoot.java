package Controller;

import javax.swing.*;

public class SwingUIRoot implements IGameUIRoot {
    private final JFrame frame;

    public SwingUIRoot(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void setContent(Object view) {
        System.out.println("🪄 [SwingUIRoot] setContent 호출됨 - 타입: " + view.getClass().getName());

        if (view instanceof JPanel panel) {
            frame.getContentPane().removeAll();
            frame.setContentPane(panel);
            frame.revalidate();
            frame.repaint();
        }
    }
}

