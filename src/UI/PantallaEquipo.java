package UI;

import logica.algoritmo.AlgoritmoBacktracking;
import logica.modelo.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class PantallaEquipo extends JFrame {
    private static final Color BG       = new Color(18, 18, 19);
    private static final Color GREEN    = new Color(83, 141, 78);
    private static final Color INPUT_BG = new Color(26, 26, 27);
    private static final Color BORDER   = new Color(58, 58, 60);
    private static final Color GRAY     = new Color(129, 131, 132);

    private final List<Persona>          candidatos         = new ArrayList<>();
    private final List<Incompatibilidad> incompatibilidades = new ArrayList<>();
    private final Requerimiento          requerimiento      = new Requerimiento();

    private final DefaultListModel<String> modelPersonas = new DefaultListModel<>();
    private final DefaultListModel<String> modelIncompat = new DefaultListModel<>();

    private JSpinner spLider, spArquitecto, spProgramador, spTester;
    private JTextArea areaResultado;
    private JButton   btnBuscar;
    private JTabbedPane tabs;

    public PantallaEquipo() {
        super("El equipo ideal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(860, 620);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);
        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildTabs(),   BorderLayout.CENTER);
        root.add(buildFooter(), BorderLayout.SOUTH);

        setContentPane(root);
    }

    // ─── Header ────────────────────────────────────────────────────────────────

    private JPanel buildHeader() {
        JPanel header = new JPanel(null);
        header.setBackground(BG);
        header.setPreferredSize(new Dimension(860, 56));

        JLabel title = new JLabel("EL EQUIPO IDEAL");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(GREEN);
        title.setBounds(28, 10, 300, 26);

        JLabel sub = new JLabel("Programacion III  ·  Com 01");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(GRAY);
        sub.setBounds(28, 36, 300, 16);

        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER);
        sep.setBounds(0, 55, 860, 1);

        header.add(title);
        header.add(sub);
        header.add(sep);
        return header;
    }

    // ─── Tabs ──────────────────────────────────────────────────────────────────

    private JTabbedPane buildTabs() {
        tabs = new JTabbedPane();
        tabs.setBackground(BG);
        tabs.setForeground(Color.WHITE);
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        tabs.addTab("  Personas  ",           buildTabPersonas());
        tabs.addTab("  Incompatibilidades  ", buildTabIncompatibilidades());
        tabs.addTab("  Requerimientos  ",     buildTabRequerimientos());
        tabs.addTab("  Resultado  ",          buildTabResultado());
        return tabs;
    }

    private JPanel buildTabPersonas() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 22, 16, 22));

        JLabel label = new JLabel("Candidatos disponibles");
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(GRAY);

        JList<String> list = new JList<>(modelPersonas);
        list.setBackground(INPUT_BG);
        list.setForeground(Color.WHITE);
        list.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        list.setSelectionBackground(BORDER);
        list.setFixedCellHeight(32);

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER));
        scroll.getViewport().setBackground(INPUT_BG);

        JButton btnAgregar = buildGreenButton("+ Agregar persona", 170);
        btnAgregar.addActionListener(e -> {
            AgregarPersonaDialog dlg = new AgregarPersonaDialog(this);
            dlg.setVisible(true);
            Persona p = dlg.getPersona();
            if (p != null) {
                candidatos.add(p);
                modelPersonas.addElement(personaToString(p));
            }
        });

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(BG);
        top.add(label, BorderLayout.WEST);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        bottom.setBackground(BG);
        bottom.add(btnAgregar);

        panel.add(top,    BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildTabIncompatibilidades() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 22, 16, 22));

        JLabel label = new JLabel("Pares de personas incompatibles");
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(GRAY);

        JList<String> list = new JList<>(modelIncompat);
        list.setBackground(INPUT_BG);
        list.setForeground(Color.WHITE);
        list.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        list.setSelectionBackground(BORDER);
        list.setFixedCellHeight(32);

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER));
        scroll.getViewport().setBackground(INPUT_BG);

        JButton btnAgregar = buildGreenButton("+ Agregar incompatibilidad", 210);
        btnAgregar.addActionListener(e -> {
            if (candidatos.size() < 2) {
                JOptionPane.showMessageDialog(this,
                    "Necesitas al menos 2 candidatos para definir incompatibilidades.",
                    "Sin candidatos", JOptionPane.WARNING_MESSAGE);
                return;
            }
            AgregarIncompatibilidadDialog dlg = new AgregarIncompatibilidadDialog(this, candidatos);
            dlg.setVisible(true);
            Incompatibilidad inc = dlg.getIncompatibilidad();
            if (inc != null) {
                incompatibilidades.add(inc);
                modelIncompat.addElement(
                    inc.getPersona1().getNombre() + "  <>  " + inc.getPersona2().getNombre()
                );
            }
        });

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(BG);
        top.add(label, BorderLayout.WEST);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        bottom.setBackground(BG);
        bottom.add(btnAgregar);

        panel.add(top,    BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildTabRequerimientos() {
        JPanel panel = new JPanel(null);
        panel.setBackground(BG);

        JLabel title = new JLabel("Cantidad de personas requeridas por rol");
        title.setFont(new Font("Segoe UI", Font.BOLD, 13));
        title.setForeground(GRAY);
        title.setBounds(80, 30, 400, 20);

        String[] labels = {"Lider de proyecto", "Arquitecto", "Programador", "Tester"};
        JSpinner[] spinners = new JSpinner[4];
        int y = 75;
        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lbl.setForeground(Color.WHITE);
            lbl.setBounds(130, y, 200, 30);

            JSpinner sp = new JSpinner(new SpinnerNumberModel(1, 0, 20, 1));
            sp.setBounds(340, y, 80, 30);
            styleSpinner(sp);
            spinners[i] = sp;

            panel.add(lbl);
            panel.add(sp);
            y += 52;
        }

        spLider       = spinners[0];
        spArquitecto  = spinners[1];
        spProgramador = spinners[2];
        spTester      = spinners[3];

        panel.add(title);
        return panel;
    }

    private JPanel buildTabResultado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 28, 20, 28));

        areaResultado = new JTextArea(
            "El resultado del equipo ideal aparecera aqui luego de ejecutar el algoritmo."
        );
        areaResultado.setBackground(INPUT_BG);
        areaResultado.setForeground(GRAY);
        areaResultado.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        areaResultado.setEditable(false);
        areaResultado.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        areaResultado.setLineWrap(true);
        areaResultado.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(areaResultado);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER));
        scroll.getViewport().setBackground(INPUT_BG);

        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ─── Footer ────────────────────────────────────────────────────────────────

    private JPanel buildFooter() {
        JPanel footer = new JPanel(null);
        footer.setBackground(BG);
        footer.setPreferredSize(new Dimension(860, 66));

        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER);
        sep.setBounds(0, 0, 860, 1);

        JButton btnVolver = new JButton("< Volver");
        btnVolver.setBounds(28, 15, 110, 36);
        btnVolver.setBackground(BORDER);
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnVolver.setFocusPainted(false);
        btnVolver.setBorderPainted(false);
        btnVolver.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVolver.addActionListener(e -> {
            Main.main(new String[0]);
            dispose();
        });

        btnBuscar = buildGreenButton("Buscar equipo ideal", 200);
        btnBuscar.setBounds(330, 14, 200, 40);
        btnBuscar.addActionListener(e -> buscarEquipo());

        footer.add(sep);
        footer.add(btnVolver);
        footer.add(btnBuscar);
        return footer;
    }

    // ─── Lógica de búsqueda ────────────────────────────────────────────────────

    private void buscarEquipo() {
        requerimiento.setCupo(Rol.LIDER_DE_PROYECTO, (int) spLider.getValue());
        requerimiento.setCupo(Rol.ARQUITECTO,        (int) spArquitecto.getValue());
        requerimiento.setCupo(Rol.PROGRAMADOR,       (int) spProgramador.getValue());
        requerimiento.setCupo(Rol.TESTER,            (int) spTester.getValue());

        if (candidatos.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Agrega al menos un candidato antes de buscar el equipo.",
                "Sin candidatos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        areaResultado.setForeground(GRAY);
        areaResultado.setText("Buscando el equipo ideal... esto puede tardar unos momentos.");
        tabs.setSelectedIndex(3);
        btnBuscar.setEnabled(false);

        SwingWorker<ResultadoEquipo, Void> worker = new SwingWorker<>() {
            @Override
            protected ResultadoEquipo doInBackground() {
                AlgoritmoBacktracking algo = new AlgoritmoBacktracking(
                    candidatos, incompatibilidades, requerimiento
                );
                return algo.buscar();
            }

            @Override
            protected void done() {
                btnBuscar.setEnabled(true);
                try {
                    mostrarResultado(get());
                } catch (ExecutionException | InterruptedException ex) {
                    areaResultado.setForeground(new Color(200, 80, 80));
                    areaResultado.setText("Error al ejecutar el algoritmo: " + ex.getCause().getMessage());
                }
            }
        };
        worker.execute();
    }

    private void mostrarResultado(ResultadoEquipo resultado) {
        if (resultado == null || resultado.getIntegrantes().isEmpty()) {
            areaResultado.setForeground(new Color(200, 80, 80));
            areaResultado.setText(
                "No se encontro ningun equipo valido con los candidatos e incompatibilidades cargados."
            );
            return;
        }

        int maxCalif = resultado.getIntegrantes().size() * 5;
        StringBuilder sb = new StringBuilder();
        sb.append("EQUIPO IDEAL ENCONTRADO\n");
        sb.append("=".repeat(42)).append("\n\n");
        sb.append("Calificacion total: ")
          .append(resultado.getCalificacionTotal())
          .append(" / ").append(maxCalif).append("\n\n");

        Rol[]    roles  = {Rol.LIDER_DE_PROYECTO, Rol.ARQUITECTO, Rol.PROGRAMADOR, Rol.TESTER};
        String[] nombres = {"Lider de proyecto", "Arquitecto", "Programador", "Tester"};

        for (int i = 0; i < roles.length; i++) {
            final int idx = i;
            List<Persona> grupo = resultado.getIntegrantes().stream()
                .filter(p -> p.getRol() == roles[idx])
                .sorted((a, b) -> b.getCalificacion() - a.getCalificacion())
                .collect(Collectors.toList());
            if (!grupo.isEmpty()) {
                sb.append(nombres[i].toUpperCase()).append("\n");
                for (Persona p : grupo) {
                    sb.append("  - ").append(p.getNombre())
                      .append("  (").append(p.getCalificacion()).append("/5)\n");
                }
                sb.append("\n");
            }
        }

        areaResultado.setForeground(Color.WHITE);
        areaResultado.setText(sb.toString());
        areaResultado.setCaretPosition(0);
    }

    // ─── Helpers ───────────────────────────────────────────────────────────────

    private String personaToString(Persona p) {
        return p.getNombre() + "  —  " + rolToString(p.getRol()) + "  —  " + p.getCalificacion() + "/5";
    }

    private String rolToString(Rol rol) {
        return switch (rol) {
            case LIDER_DE_PROYECTO -> "Lider de proyecto";
            case ARQUITECTO        -> "Arquitecto";
            case PROGRAMADOR       -> "Programador";
            case TESTER            -> "Tester";
        };
    }

    private JButton buildGreenButton(String text, int width) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(width, 38));
        btn.setBackground(GREEN);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
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
