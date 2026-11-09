package vista;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Estilo {
    public static final Color FONDO = new Color(10, 9, 11);
    public static final Color PANEL = new Color(24, 21, 23);
    public static final Color PANEL_CLARO = new Color(37, 31, 32);
    public static final Color AZUL = new Color(67, 57, 58); 
    public static final Color ROJO = new Color(190, 28, 45);
    public static final Color ROJO_OSCURO = new Color(112, 15, 27);
    public static final Color DORADO = new Color(239, 190, 79);
    public static final Color TEXTO = new Color(250, 247, 242);
    public static final Color SUAVE = new Color(202, 190, 184);

    public static JPanel fondo() {
        return new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setPaint(new GradientPaint(0, 0, new Color(8, 7, 9),
                    getWidth(), getHeight(), new Color(31, 16, 20)));
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.setColor(new Color(202, 35, 48, 24));
                for (int y = 28; y < getHeight(); y += 19) {
                    for (int x = 18; x < getWidth() / 3; x += 19) {
                        int d = 2 + ((x + y) % 3);
                        g2.fillOval(x, y, d, d);
                    }
                }
                Polygon p = new Polygon();
                p.addPoint(getWidth() - 220, getHeight());
                p.addPoint(getWidth(), getHeight() - 175);
                p.addPoint(getWidth(), getHeight());
                g2.setColor(new Color(145, 18, 34, 45));
                g2.fillPolygon(p);
                g2.dispose();
            }
        };
    }

    private static class BotonMarvel extends JButton {
        private Color base;

        BotonMarvel(String texto, Color color) {
            super(texto);
            setUI(new javax.swing.plaf.basic.BasicButtonUI());
            base = color;
            setFont(new Font("SansSerif", Font.BOLD, 13));
            setForeground(Color.WHITE);
            setHorizontalAlignment(SwingConstants.CENTER);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setBorder(new EmptyBorder(12, 18, 12, 18));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setRolloverEnabled(true);
            setMargin(new Insets(12, 18, 12, 18));
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color top = base;
            Color bottom = base.darker();
            if (!isEnabled()) {
                top = new Color(72, 65, 65);
                bottom = new Color(43, 38, 39);
            } else if (getModel().isPressed()) {
                top = base.darker().darker();
                bottom = base.darker();
            } else if (getModel().isRollover()) {
                top = base.brighter();
                bottom = base;
            }
            g2.setPaint(new GradientPaint(0, 0, top, 0, getHeight(), bottom));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            g2.setColor(new Color(255, 255, 255, getModel().isRollover() ? 115 : 53));
            g2.setStroke(new BasicStroke(1.1f));
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 12, 12);
            if (base.getRed() > 130 && base.getGreen() < 80) {
                g2.setColor(new Color(255, 205, 115, 120));
                g2.drawLine(11, 5, getWidth() - 12, 5);
            }
            if (hasFocus()) {
                g2.setColor(DORADO);
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(3, 3, getWidth() - 7, getHeight() - 7, 10, 10);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static JButton boton(String texto, Color color) {
        return new BotonMarvel(texto, color);
    }

    public static JLabel titulo(String texto, int size) {
        JLabel l = new JLabel(texto);
        l.setForeground(TEXTO);
        l.setFont(new Font("SansSerif", Font.BOLD, size));
        return l;
    }

    public static JLabel texto(String texto) {
        JLabel l = new JLabel(texto);
        l.setForeground(SUAVE);
        l.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return l;
    }

    public static JTextField campo() {
        JTextField f = new JTextField();
        configurarCampo(f);
        return f;
    }

    public static JPasswordField password() {
        JPasswordField f = new JPasswordField();
        configurarCampo(f);
        return f;
    }

    private static void configurarCampo(JTextField f) {
        f.setFont(new Font("SansSerif", Font.PLAIN, 15));
        f.setForeground(TEXTO);
        f.setCaretColor(DORADO);
        f.setBackground(new Color(13, 12, 14));
        f.setSelectionColor(new Color(135, 29, 43));
        f.setSelectedTextColor(Color.WHITE);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(111, 77, 70), 1),
            new EmptyBorder(10, 12, 10, 12)));
    }

    public static JPanel tarjeta() {
        JPanel p = new JPanel();
        p.setBackground(PANEL);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(102, 58, 57), 1),
            new EmptyBorder(22, 24, 22, 24)));
        return p;
    }

    public static JPanel tarjetaSuave() {
        JPanel p = new JPanel();
        p.setBackground(PANEL_CLARO);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(91, 67, 62), 1),
            new EmptyBorder(16, 18, 16, 18)));
        return p;
    }

    public static JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setForeground(DORADO);
        l.setFont(new Font("SansSerif", Font.BOLD, 11));
        return l;
    }

    public static ImageIcon imagenEscalada(String recurso, int ancho, int alto) {
        try {
            java.net.URL url = Estilo.class.getResource(recurso);
            if (url == null) return null;
            ImageIcon icon = new ImageIcon(url);
            Image image = icon.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            return new ImageIcon(image);
        } catch (Exception ex) {
            return null;
        }
    }
}
