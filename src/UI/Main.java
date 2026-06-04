package UI;

import javax.swing.*;
import java.awt.*;

public class Main {
    private static final Color BG    = new Color(18, 18, 19);
    private static final Color GREEN = new Color(83, 141, 78);
    private static final Color GRAY  = new Color(129, 131, 132);

    private JFrame frame;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            UIManager.put("TabbedPane.background",      new Color(18, 18, 19));
            UIManager.put("TabbedPane.foreground",      Color.WHITE);
            UIManager.put("TabbedPane.selected",        new Color(26, 26, 27));
            UIManager.put("TabbedPane.contentAreaColor",new Color(18, 18, 19));
            UIManager.put("TabbedPane.tabAreaBackground",new Color(18, 18, 19));
            UIManager.put("TabbedPane.shadow",          new Color(58, 58, 60));
            UIManager.put("TabbedPane.darkShadow",      new Color(58, 58, 60));
            UIManager.put("TabbedPane.light",           new Color(26, 26, 27));
            UIManager.put("TabbedPane.highlight",       new Color(58, 58, 60));
            UIManager.put("TabbedPane.focus",           new Color(83, 141, 78));
            UIManager.put("ComboBox.background",        new Color(26, 26, 27));
            UIManager.put("ComboBox.foreground",        Color.WHITE);
            UIManager.put("ComboBox.selectionBackground", new Color(58, 58, 60));
            UIManager.put("ComboBox.selectionForeground", Color.WHITE);
            UIManager.put("List.background",            new Color(26, 26, 27));
            UIManager.put("List.foreground",            Color.WHITE);
            UIManager.put("List.selectionBackground",   new Color(58, 58, 60));
            UIManager.put("List.selectionForeground",   Color.WHITE);
            UIManager.put("ScrollBar.background",       new Color(18, 18, 19));
            UIManager.put("ScrollBar.thumb",            new Color(58, 58, 60));
            UIManager.put("ScrollBar.track",            new Color(26, 26, 27));
            UIManager.put("OptionPane.background",      new Color(18, 18, 19));
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            UIManager.put("Panel.background",           new Color(18, 18, 19));
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new Main().initialize());
    }

    private void initialize() {
        frame = new JFrame("TP3 — El equipo ideal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(720, 540);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        JPanel panel = new JPanel(null);
        panel.setBackground(BG);

        JLabel title = new JLabel("EL EQUIPO IDEAL", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(GREEN);
        title.setBounds(0, 130, 720, 45);

        JLabel subtitle = new JLabel("Armado de equipos de desarrollo optimos", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitle.setForeground(GRAY);
        subtitle.setBounds(0, 183, 720, 24);

        JLabel course = new JLabel("TP3 Programacion III  ·  Com 01", SwingConstants.CENTER);
        course.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        course.setForeground(GRAY);
        course.setBounds(0, 210, 720, 20);

        JLabel team = new JLabel("Gomez Rodriguez  ·  Rocha  ·  Sangueso  ·  Taibo Cruz", SwingConstants.CENTER);
        team.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        team.setForeground(GRAY);
        team.setBounds(0, 233, 720, 20);

        JButton btn = new JButton("Iniciar planificacion");
        btn.setBounds(260, 315, 200, 44);
        btn.setBackground(GREEN);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> {
            new PantallaEquipo().setVisible(true);
            frame.setVisible(false);
        });

        panel.add(title);
        panel.add(subtitle);
        panel.add(course);
        panel.add(team);
        panel.add(btn);

        frame.setContentPane(panel);
        frame.setVisible(true);
    }
}
