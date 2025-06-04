package View.Swing.dialog;

import Controller.YutResult;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class YutResultChoiceDialog {

    public static void show(List<YutResult> yutResults, Consumer<YutResult> onSelected) {
        JDialog dialog = new JDialog((JFrame) null, "결과 적용 선택", true);
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

        System.out.println("전달된 결과 리스트: " + yutResults);
        panel.setLayout(null);
        panel.setOpaque(false);
        panel.setBounds(0, 0, 800, 600);

        int spacing = 20;
        int y = 115;

        List<ImageIcon> icons = new ArrayList<>();
        int totalWidth = 0;

        for (YutResult result : yutResults) {
            String imagePath = "/image/선택 윷 결과/선택 " + getKoreanName(result) + ".png";
            ImageIcon icon = new ImageIcon(YutResultChoiceDialog.class.getResource(imagePath));
            icons.add(icon);
            totalWidth += icon.getIconWidth();
        }

        totalWidth += spacing * (icons.size()-1);

        int panelWidth = dialog.getWidth();
        int x = (panelWidth - totalWidth)/2;

        for (int i = 0; i < yutResults.size(); i++) {
            YutResult result = yutResults.get(i);
            ImageIcon icon = icons.get(i);

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
            x += icon.getIconWidth() + spacing;
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