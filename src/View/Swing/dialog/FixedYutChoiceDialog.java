package View.Swing.dialog;

import Controller.YutResult;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class FixedYutChoiceDialog {

    public static void show(Consumer<YutResult> onSelected) {
        JDialog dialog = new JDialog((JFrame) null, "윷 선택", true);
        dialog.setSize(665, 298);
        dialog.setLocationRelativeTo(null);
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0, 0, 0, 0));

        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image bg = new ImageIcon(getClass().getResource("/image/결과 적용.png")).getImage();
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
            }
        };
        panel.setLayout(null);
        panel.setOpaque(false);

        YutResult[] fixedResults = {
                YutResult.DO, YutResult.GAE, YutResult.GEOL, YutResult.YUT, YutResult.MO, YutResult.BackDo
        };

        int x = 20;
        int y = 110;

        for (YutResult result : fixedResults) {
            String imagePath = "/image/선택 윷 결과/선택 " + getKoreanName(result) + ".png";
            ImageIcon icon = new ImageIcon(FixedYutChoiceDialog.class.getResource(imagePath));

            JButton btn = new JButton(icon);
            btn.setBounds(x, y, icon.getIconWidth(), icon.getIconHeight());
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);

            btn.addActionListener(e -> {
                dialog.dispose();
                onSelected.accept(result);
            });

            panel.add(btn);
            x += icon.getIconWidth() + 20;
        }

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    private static String getKoreanName(YutResult result) {
        return switch (result) {
            case DO -> "도";
            case GAE -> "개";
            case GEOL -> "걸";
            case YUT -> "윷";
            case MO -> "모";
            case BackDo -> "백도";
        };
    }
}