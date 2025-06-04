package View.Swing.animation;

import Controller.YutResult;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationView {
    private JPanel parentPanel;
    private JLabel notifyingImage;
    private JLabel eventNotifyingImage;

    public NotificationView(JPanel parentPanel) {
        this.parentPanel = parentPanel;
    }

    public void scheduleNotifyingImage(YutResult result) {
        String imagePath;
        if(result == YutResult.YUT)
            imagePath = "/image/윷 한번더.png";
        else
            imagePath = "/image/모 한번더.png";

        if (notifyingImage != null) {
            parentPanel.remove(notifyingImage);
            notifyingImage = null;
            parentPanel.revalidate();
            parentPanel.repaint();
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    notifyingImage = new JLabel(new ImageIcon(getClass().getResource(imagePath)));
                    notifyingImage.setBounds(291, 294, 519, 113);
                    parentPanel.add(notifyingImage);
                    parentPanel.setComponentZOrder(notifyingImage, 0);

                    parentPanel.revalidate();
                    parentPanel.repaint();
                });

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        SwingUtilities.invokeLater(() -> {
                            if (notifyingImage != null) {
                                parentPanel.remove(notifyingImage);
                                notifyingImage = null;
                                parentPanel.revalidate();
                                parentPanel.repaint();
                            }
                        });
                    }
                }, 1700);
            }
        }, 1600);
    }

    public void showEventImage(String imagePath) {
        if (eventNotifyingImage != null) {
            parentPanel.remove(eventNotifyingImage);
            eventNotifyingImage = null;
            parentPanel.revalidate();
            parentPanel.repaint();
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    eventNotifyingImage = new JLabel(new ImageIcon(getClass().getResource(imagePath)));
                    eventNotifyingImage.setBounds(291, 294, 519, 113);
                    parentPanel.add(eventNotifyingImage);
                    parentPanel.setComponentZOrder(eventNotifyingImage, 0);

                    parentPanel.revalidate();
                    parentPanel.repaint();
                });

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        SwingUtilities.invokeLater(() -> {
                            if (eventNotifyingImage != null) {
                                parentPanel.remove(eventNotifyingImage);
                                eventNotifyingImage = null;
                                parentPanel.revalidate();
                                parentPanel.repaint();
                            }
                        });
                    }
                }, 800);
            }
        }, 400);
    }

    public void reset() {
        if (notifyingImage != null) {
            parentPanel.remove(notifyingImage);
            notifyingImage = null;
        }
        if (eventNotifyingImage != null) {
            parentPanel.remove(eventNotifyingImage);
            eventNotifyingImage = null;
        }
    }
}