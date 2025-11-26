/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
 *
 * @author HP
 */
public class ToastUtils {

    public static void showToast(JFrame frame, String message, int duration) {
        // Create borderless window
        JWindow toast = new JWindow(frame);
        toast.setLayout(new BorderLayout());

        // Label for message
        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(new Color(0, 0, 0, 170)); // semi-transparent black
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        toast.add(label, BorderLayout.CENTER);

        // Pack and set location at bottom-center
        toast.pack();
        int x = frame.getX() + frame.getWidth() / 2 - toast.getWidth() / 2;
        int y = frame.getY() + frame.getHeight() - toast.getHeight() - 50; // 50px above bottom
        toast.setLocation(x, y);

        toast.setVisible(true);

        // Close after duration
        new Timer(duration, e -> toast.dispose()).start();
    }

}
