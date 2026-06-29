package UI;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AgregarIncompatibilidadDialog extends JDialog {
    private static final Color BG       = new Color(18, 18, 19);
    private static final Color GREEN    = new Color(83, 141, 78);
    private static final Color INPUT_BG = new Color(26, 26, 27);
    private static final Color BORDER   = new Color(58, 58, 60);
    private static final Color GRAY     = new Color(129, 131, 132);

    private JComboBox<String> combo1, combo2;
    private boolean           confirmado;
    private int               indice1, indice2;

    public AgregarIncompatibilidadDialog(JFrame parent, List<String> nombresCandidatos) {
        super(parent, "Agregar incompatibilidad", true);
        setSize(430, 250);
        setLocationRelativeTo(parent);
        setResizable(false);

        String[] nombres = nombresCandidatos.toArray(new String[0]);

        JPanel panel = new JPanel(null);
        panel.setBackground(BG);

        JLabel title = new JLabel("Nueva incompatibilidad");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        title.setBounds(30, 20, 300, 26);

        JLabel lbl1 = buildLabel("Primera persona", 72);
        combo1 = buildCombo(nombres, 72);

        JLabel lbl2 = buildLabel("Segunda persona", 122);
        combo2 = buildCombo(nombres, 122);

        JButton btnCancelar = buildButton("Cancelar", BORDER, 90, 180);
        btnCancelar.addActionListener(e -> dispose());

        JButton btnAgregar = buildButton("Agregar", GREEN, 220, 180);
        btnAgregar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAgregar.addActionListener(e -> confirmar());

        panel.add(title);
        panel.add(lbl1);  panel.add(combo1);
        panel.add(lbl2);  panel.add(combo2);
        panel.add(btnCancelar);
        panel.add(btnAgregar);

        setContentPane(panel);
        getRootPane().setDefaultButton(btnAgregar);
    }

    private void confirmar() {
        int idx1 = combo1.getSelectedIndex();
        int idx2 = combo2.getSelectedIndex();
        if (idx1 == idx2) {
            JOptionPane.showMessageDialog(this,
                "Selecciona dos personas distintas.",
                "Seleccion invalida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        indice1 = idx1;
        indice2 = idx2;
        confirmado = true;
        dispose();
    }

    public boolean fueConfirmado() { return confirmado; }
    public int getIndice1()        { return indice1; }
    public int getIndice2()        { return indice2; }

    // ─── Helpers ───────────────────────────────────────────────────────────────

    private JLabel buildLabel(String text, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(GRAY);
        lbl.setBounds(30, y + 5, 150, 20);
        return lbl;
    }

    private JComboBox<String> buildCombo(String[] items, int y) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setBounds(190, y, 210, 30);
        cb.setBackground(INPUT_BG);
        cb.setForeground(Color.WHITE);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return cb;
    }

    private JButton buildButton(String text, Color bg, int x, int y) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 110, 36);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
