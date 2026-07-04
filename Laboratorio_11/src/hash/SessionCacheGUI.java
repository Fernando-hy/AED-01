package hash;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SessionCacheGUI extends JFrame {

    private SessionCache cache = new SessionCache();

    private JTextField dniField, userField, roleField, ttlField;
    private JPasswordField passField;
    private JTextArea logArea;
    private DefaultTableModel tableModel;
    private JLabel activeLabel, statusLabel;
    private JTable table;
    private Timer refreshTimer;

    private static final Color BG        = new Color(245, 247, 250);
    private static final Color WHITE     = Color.WHITE;
    private static final Color INDIGO    = new Color(79, 70, 229);
    private static final Color INDIGO_D  = new Color(55, 48, 163);
    private static final Color SUCCESS   = new Color(22, 163, 74);
    private static final Color SUCCESS_L = new Color(220, 252, 231);
    private static final Color DANGER    = new Color(220, 38, 38);
    private static final Color DANGER_L  = new Color(254, 226, 226);
    private static final Color WARN      = new Color(217, 119, 6);
    private static final Color TEXT      = new Color(30, 30, 40);
    private static final Color MUTED     = new Color(100, 100, 120);
    private static final Color BORDER    = new Color(220, 220, 235);
    private static final Font  SANS      = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  SANS_B    = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font  SMALL     = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Font  TITLE     = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font  MONO      = new Font("Consolas", Font.PLAIN, 12);

    public SessionCacheGUI() {
        setTitle("Session Cache — Simulador");
        setSize(1000, 640);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(BG);
        setLayout(new GridLayout(1, 2, 0, 0));
        add(buildUserPanel());
        add(buildSystemPanel());

        // refresca la tabla cada segundo para mostrar TTL en tiempo real
        refreshTimer = new Timer(1000, e -> refreshTable());
        refreshTimer.start();
    }

    // ── Panel izquierdo: vista del usuario ───────────────────────────────────
    private JPanel buildUserPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(INDIGO);

        // header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(INDIGO_D);
        header.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        JLabel title = new JLabel("SocialApp");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(WHITE);
        JLabel sub = new JLabel("Inicia sesion para continuar");
        sub.setFont(SMALL);
        sub.setForeground(new Color(165, 180, 252));
        header.add(title, BorderLayout.NORTH);
        header.add(sub,   BorderLayout.SOUTH);

        // formulario centrado
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(INDIGO);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER),
            BorderFactory.createEmptyBorder(28, 28, 28, 28)
        ));
        form.setMaximumSize(new Dimension(320, 500));

        JLabel formTitle = new JLabel("Bienvenido");
        formTitle.setFont(TITLE);
        formTitle.setForeground(TEXT);
        formTitle.setAlignmentX(LEFT_ALIGNMENT);

        JLabel formSub = new JLabel("Ingresa tus datos para iniciar sesion");
        formSub.setFont(SMALL);
        formSub.setForeground(MUTED);
        formSub.setAlignmentX(LEFT_ALIGNMENT);

        dniField  = formField("DNI");
        passField = new JPasswordField();
        stylePassField(passField, "Contrasena");
        userField = formField("Nombre completo");
        roleField = formField("Rol  (admin / user)");
        ttlField  = formField("Duracion de sesion (ms)");
        ttlField.setText("10000");

        JButton loginBtn = new JButton("Iniciar sesion");
        loginBtn.setBackground(INDIGO);
        loginBtn.setForeground(WHITE);
        loginBtn.setFont(SANS_B);
        loginBtn.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        loginBtn.setFocusPainted(false);
        loginBtn.setOpaque(true);
        loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        loginBtn.setAlignmentX(LEFT_ALIGNMENT);
        loginBtn.addActionListener(e -> login());

        JButton logoutBtn = new JButton("Cerrar sesion activa");
        logoutBtn.setBackground(new Color(248, 248, 252));
        logoutBtn.setForeground(DANGER);
        logoutBtn.setFont(SANS);
        logoutBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER),
            BorderFactory.createEmptyBorder(8, 0, 8, 0)
        ));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setOpaque(true);
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        logoutBtn.setAlignmentX(LEFT_ALIGNMENT);
        logoutBtn.addActionListener(e -> doLogout());

        // estado de sesion actual
        statusLabel = new JLabel("Sin sesion activa");
        statusLabel.setFont(SMALL);
        statusLabel.setForeground(MUTED);
        statusLabel.setAlignmentX(LEFT_ALIGNMENT);

        form.add(formTitle);
        form.add(Box.createVerticalStrut(4));
        form.add(formSub);
        form.add(Box.createVerticalStrut(20));
        form.add(formLabel("DNI"));
        form.add(Box.createVerticalStrut(4));
        form.add(dniField);
        form.add(Box.createVerticalStrut(10));
        form.add(formLabel("Contrasena"));
        form.add(Box.createVerticalStrut(4));
        form.add(passField);
        form.add(Box.createVerticalStrut(10));
        form.add(formLabel("Nombre completo"));
        form.add(Box.createVerticalStrut(4));
        form.add(userField);
        form.add(Box.createVerticalStrut(10));
        form.add(formLabel("Rol"));
        form.add(Box.createVerticalStrut(4));
        form.add(roleField);
        form.add(Box.createVerticalStrut(10));
        form.add(formLabel("Duracion de sesion (ms)"));
        form.add(Box.createVerticalStrut(4));
        form.add(ttlField);
        form.add(Box.createVerticalStrut(20));
        form.add(loginBtn);
        form.add(Box.createVerticalStrut(8));
        form.add(logoutBtn);
        form.add(Box.createVerticalStrut(12));
        form.add(statusLabel);

        center.add(form);

        p.add(header, BorderLayout.NORTH);
        p.add(center, BorderLayout.CENTER);
        return p;
    }

    // ── Panel derecho: vista del sistema ─────────────────────────────────────
    private JPanel buildSystemPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 12));
        p.setBackground(BG);
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // header sistema
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG);
        JLabel title = new JLabel("Panel del sistema — Hash Table");
        title.setFont(TITLE);
        title.setForeground(TEXT);
        activeLabel = new JLabel("Sesiones activas: 0");
        activeLabel.setFont(SANS);
        activeLabel.setForeground(MUTED);
        header.add(title,       BorderLayout.WEST);
        header.add(activeLabel, BorderLayout.EAST);

        // tabla
        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(WHITE);
        tableCard.setBorder(BorderFactory.createLineBorder(BORDER));

        String[] cols = {"DNI", "Usuario", "Rol", "TTL restante", "Estado"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        table.setFont(SANS);
        table.setRowHeight(34);
        table.setBackground(WHITE);
        table.setForeground(TEXT);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionBackground(new Color(238, 238, 255));
        table.getTableHeader().setBackground(new Color(248, 248, 255));
        table.getTableHeader().setForeground(MUTED);
        table.getTableHeader().setFont(SANS_B);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
        table.setDefaultRenderer(Object.class, new StatusRenderer());

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(WHITE);
        tableCard.add(scroll);

        // botones del sistema
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btns.setBackground(BG);
        JButton cleanBtn = btn("Limpiar expiradas", WARN);
        cleanBtn.addActionListener(e -> cleanExpired());
        btns.add(cleanBtn);

        // log
        JPanel logCard = new JPanel(new BorderLayout());
        logCard.setBackground(WHITE);
        logCard.setBorder(BorderFactory.createLineBorder(BORDER));
        logCard.setPreferredSize(new Dimension(0, 120));
        JLabel logTitle = new JLabel("  Log del sistema");
        logTitle.setFont(SANS_B);
        logTitle.setForeground(MUTED);
        logTitle.setBorder(BorderFactory.createEmptyBorder(6, 0, 4, 0));
        logArea = new JTextArea();
        logArea.setFont(MONO);
        logArea.setBackground(new Color(248, 248, 252));
        logArea.setForeground(new Color(60, 60, 120));
        logArea.setEditable(false);
        logArea.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(BorderFactory.createEmptyBorder());
        logCard.add(logTitle,  BorderLayout.NORTH);
        logCard.add(logScroll, BorderLayout.CENTER);

        p.add(header,    BorderLayout.NORTH);
        p.add(tableCard, BorderLayout.CENTER);
        p.add(btns,      BorderLayout.CENTER);

        JPanel south = new JPanel(new BorderLayout(0, 8));
        south.setBackground(BG);
        south.add(btns,    BorderLayout.NORTH);
        south.add(logCard, BorderLayout.CENTER);

        p.add(header,    BorderLayout.NORTH);
        p.add(tableCard, BorderLayout.CENTER);
        p.add(south,     BorderLayout.SOUTH);
        return p;
    }

    // ── Acciones ──────────────────────────────────────────────────────────────
    private void login() {
        String dni  = dniField.getText().trim();
        String user = userField.getText().trim();
        String role = roleField.getText().trim();
        if (dni.isEmpty() || user.isEmpty() || role.isEmpty()) {
            statusLabel.setText("Completa todos los campos");
            statusLabel.setForeground(DANGER);
            return;
        }
        long ttl;
        try { ttl = Long.parseLong(ttlField.getText().trim()); }
        catch (NumberFormatException e) {
            statusLabel.setText("TTL invalido");
            statusLabel.setForeground(DANGER);
            return;
        }
        cache.login(dni, user, role, ttl);
        statusLabel.setText("Sesion iniciada: " + user + " (DNI: " + dni + ")");
        statusLabel.setForeground(SUCCESS);
        log("Login: " + user + " | DNI=" + dni + " | ttl=" + ttl + "ms");
        dniField.setText(""); userField.setText(""); roleField.setText("");
        refreshTable();
    }

    private void doLogout() {
        int row = table.getSelectedRow();
        if (row < 0) {
            statusLabel.setText("Selecciona una sesion en el panel del sistema");
            statusLabel.setForeground(WARN);
            return;
        }
        String dni = tableModel.getValueAt(row, 0).toString();
        cache.logout(dni);
        statusLabel.setText("Sesion cerrada: DNI=" + dni);
        statusLabel.setForeground(MUTED);
        log("Logout: DNI=" + dni);
        refreshTable();
    }

    private void cleanExpired() {
        cache.cleanExpired();
        log("Limpieza: activas=" + cache.activeSessions());
        refreshTable();
    }

    private void refreshTable() {
        int selectedRow = table.getSelectedRow();
        tableModel.setRowCount(0);
        long now = System.currentTimeMillis();
        int active = 0;
        for (int i = 0; i < SessionCache.SIZE; i++) {
            ListLinked.Node<Register<Session>> n = cache.table[i].getFirst();
            while (n != null) {
                Session s      = n.getValue().value;
                long remaining = s.expiresAt - now;
                String rem     = remaining > 0 ? (remaining / 1000) + "s" : "expirado";
                String state   = remaining > 0 ? "Activa" : "Expirada";
                if (remaining > 0) active++;
                tableModel.addRow(new Object[]{ s.token, s.username, s.role, rem, state });
                n = n.getNext();
            }
        }
        activeLabel.setText("Sesiones activas: " + active);
        if (selectedRow >= 0 && selectedRow < tableModel.getRowCount())
            table.setRowSelectionInterval(selectedRow, selectedRow);
    }

    private void log(String msg) {
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        logArea.append("[" + time + "] " + msg + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private JLabel formLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(SMALL);
        l.setForeground(MUTED);
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }

    private JTextField formField(String placeholder) {
        JTextField f = new JTextField();
        f.setFont(SANS);
        f.setForeground(TEXT);
        f.setBackground(new Color(248, 248, 252));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER),
            BorderFactory.createEmptyBorder(7, 10, 7, 10)
        ));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        f.setAlignmentX(LEFT_ALIGNMENT);
        return f;
    }

    private void stylePassField(JPasswordField f, String placeholder) {
        f.setFont(SANS);
        f.setForeground(TEXT);
        f.setBackground(new Color(248, 248, 252));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER),
            BorderFactory.createEmptyBorder(7, 10, 7, 10)
        ));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        f.setAlignmentX(LEFT_ALIGNMENT);
    }

    private JButton btn(String text, Color color) {
        JButton b = new JButton(text);
        b.setBackground(color);
        b.setForeground(WHITE);
        b.setFont(SANS_B);
        b.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    class StatusRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable t, Object v,
                boolean sel, boolean foc, int row, int col) {
            super.getTableCellRendererComponent(t, v, sel, foc, row, col);
            setBackground(sel ? new Color(238, 238, 255) : (row % 2 == 0 ? WHITE : new Color(250, 250, 255)));
            setForeground(TEXT);
            setFont(SANS);
            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            if (col == 4) {
                String val = v != null ? v.toString() : "";
                if (val.equals("Activa")) {
                    setForeground(SUCCESS);
                    setBackground(SUCCESS_L);
                    setFont(SANS_B);
                } else {
                    setForeground(DANGER);
                    setBackground(DANGER_L);
                    setFont(SANS_B);
                }
            }
            return this;
        }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new SessionCacheGUI().setVisible(true));
    }
}