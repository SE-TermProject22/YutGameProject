package View.Swing.dialog;

import Model.DoubledHorse;
import Model.Horse;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class HorseSelectionDialog {

    public static void show(List<Horse> horses, int horseCount, Consumer<Horse> onSelected) {
        JDialog dialog = new JDialog((JFrame) null, "말 선택", true);
        dialog.setSize(665, 298);
        dialog.setLocationRelativeTo(null);
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0, 0, 0, 0));

        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image bg = new ImageIcon(getClass().getResource("/image/말 적용.png")).getImage();
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
            }
        };

        panel.setLayout(null);
        panel.setOpaque(false);
        panel.setBounds(0, 0, 800, 600);

        int spacing = 20;
        int y = 115;

        List<ImageIcon> icons = new ArrayList<>();
        int totalWidth = 0;

        // 먼저 아이콘 로드 및 너비 측정
        for (Horse horse : horses) {
            String imagePath = null;

            if (horse.id < 20) {
                imagePath = "/image/선택 " + horse.color + "/" + (horse.id % horseCount + 1) + ".png";
            } else {
                int imageType = ((DoubledHorse) horse).getImageType();
                String suffix = (imageType == 0) ? "2개" : (imageType == 1) ? "1개" : ((DoubledHorse) horse).horseCount + "개";
                imagePath = "/image/업힌 말 버튼/" + horse.color + "/" + suffix + ".png";
            }

            java.net.URL imageURL = HorseSelectionDialog.class.getResource(imagePath);
            if (imageURL == null) {
                System.out.println("❗ 이미지 경로 오류: 존재하지 않음 -> " + imagePath);
                continue;
            }

            ImageIcon icon = new ImageIcon(imageURL);
            icons.add(icon);
            totalWidth += icon.getIconWidth();
        }

        totalWidth += spacing * (icons.size() - 1);

        int panelWidth = dialog.getWidth();
        int x = (panelWidth - totalWidth) / 2;

        // 두 번째 루프: 버튼 실제 생성
        for (int i = 0; i < icons.size(); i++) {
            Horse horse = horses.get(i);
            ImageIcon icon = icons.get(i);

            JButton btn = new JButton(icon);
            btn.setBounds(x, y, icon.getIconWidth(), icon.getIconHeight());
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);

            Horse selectedHorse = horse;
            btn.addActionListener(e -> {
                System.out.println("🖱️ 선택된 말 ID: " + selectedHorse.id);
                dialog.dispose();
                onSelected.accept(selectedHorse);
            });

            panel.add(btn);
            x += icon.getIconWidth() + spacing;
        }

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }
}