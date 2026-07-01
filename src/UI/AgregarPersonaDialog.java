package UI;

import logica.modelo.Rol;

import javax.swing.*;
import java.awt.*;

public class AgregarPersonaDialog extends JDialog {
    private static final Color BG       = new Color(18, 18, 19);
    private static final Color GREEN    = new Color(83, 141, 78);
    private static final Color INPUT_BG = new Color(26, 26, 27);
    private static final Color BORDER   = new Color(58, 58, 60);
    private static final Color GRAY     = new Color(129, 131, 132);

    private JTextField       fieldNombre;
    private JComboBox<String> comboRol;
    private JSpinner          spinnerCalif;
    private boolean           confirmado;
    private String            nombre;
    private Rol               rol;
    private int               calificacion;

    public AgregarPersonaDialog(JFrame parent) {
        super(parent, "Agregar persona", true);
        setSize(420, 310);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel panel = new JPanel(null);
        panel.setBackground(BG);

        JLabel title = new JLabel("Nueva persona");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        title.setBounds(30, 20, 220, 26);

        panel.add(buildLabel("Nombre", 68));
        fieldNombre = buildTextField(68);
        panel.add(fieldNombre);

        panel.add(buildLabel("Rol", 118));
        comboRol = new JComboBox<>(new String[]{
            "Lider de proyecto", "Arquitecto", "Programador", "Tester"
        });
        comboRol.setBounds(160, 118, 220, 30);
        comboRol.setBackground(INPUT_BG);
        comboRol.setForeground(Color.WHITE);
        comboRol.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(comboRol);

        panel.add(buildLabel("Calificacion (1-5)", 168));
        spinnerCalif = new JSpinner(new SpinnerNumberModel(3, 1, 5, 1));
        spinnerCalif.setBounds(160, 168, 80, 30);
        styleSpinner(spinnerCalif);
        panel.add(spinnerCalif);

        JButton btnCancelar = buildButton("Cancelar", BORDER, 80, 230);
        btnCancelar.addActionListener(e -> dispose());

        JButton btnAgregar = buildButton("Agregar", GREEN, 210, 230);
        btnAgregar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAgregar.addActionListener(e -> confirmar());

        panel.add(title);
        panel.add(btnCancelar);
        panel.add(btnAgregar);

        setContentPane(panel);
        getRootPane().setDefaultButton(btnAgregar);
    }

    private void confirmar() {
        String texto = fieldNombre.getText().trim();
        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Ingresa un nombre.", "Campo requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }
        nombre = texto;
        rol = switch (comboRol.getSelectedIndex()) {
            case 0  -> Rol.LIDER_DE_PROYECTO;
            case 1  -> Rol.ARQUITECTO;
            case 2  -> Rol.PROGRAMADOR;
            default -> Rol.TESTER;
        };
        calificacion = (int) spinnerCalif.getValue();
        confirmado = true;
        dispose();
    }

    public boolean fueConfirmado() { return confirmado; }
    public String getNombre()      { return nombre; }
    public Rol getRol()            { return rol; }
    public int getCalificacion()   { return calificacion; }

    // ─── Helpers ───────────────────────────────────────────────────────────────

    private JLabel buildLabel(String text, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(GRAY);
        lbl.setBounds(30, y + 6, 130, 18);
        return lbl;
    }

    private JTextField buildTextField(int y) {
        JTextField tf = new JTextField();
        tf.setBounds(160, y, 220, 30);
        tf.setBackground(INPUT_BG);
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER),
            BorderFactory.createEmptyBorder(0, 8, 0, 8)
        ));
        return tf;
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

    private void styleSpinner(JSpinner sp) {
        JComponent editor = sp.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
            tf.setBackground(INPUT_BG);
            tf.setForeground(Color.WHITE);
            tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        }
        sp.setBorder(BorderFactory.createLineBorder(BORDER));
    }
}
