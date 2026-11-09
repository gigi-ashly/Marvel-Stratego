package vista;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DialogoMarvel extends JDialog {
    private JPanel cuerpo;
    private JPanel acciones;

    private static class TarjetaMarvel extends JPanel {
        TarjetaMarvel() {
            setOpaque(false);
            setBorder(new EmptyBorder(16, 19, 16, 19));
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(16, 13, 15, 242));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            g2.setColor(new Color(164, 35, 45, 225));
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 18, 18);
            g2.setColor(Estilo.DORADO);
            g2.fillRoundRect(22, 0, 78, 4, 4, 4);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public DialogoMarvel(Window owner, String seccion, String titulo,
                         String subtitulo, int ancho, int alto) {
        super(owner, titulo, Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(ancho, alto);
        setMinimumSize(new Dimension(Math.min(ancho, 460), Math.min(alto, 360)));
        setResizable(false);

        MarvelArtworkPanel fondo = new MarvelArtworkPanel(false, true);
        fondo.setLayout(new BorderLayout(0, 10));
        fondo.setBorder(new EmptyBorder(13, 16, 13, 16));

        JPanel encabezado = new JPanel();
        encabezado.setOpaque(true);
        encabezado.setBackground(new Color(13, 10, 12));
        encabezado.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, Estilo.ROJO),
            new EmptyBorder(9, 13, 9, 13)));
        encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.Y_AXIS));
        JPanel marca = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        marca.setOpaque(false);
        JLabel logo = new JLabel("MARVEL");
        logo.setOpaque(true);
        logo.setBackground(Estilo.ROJO);
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("SansSerif", Font.BOLD, 12));
        logo.setBorder(new EmptyBorder(4, 9, 4, 9));
        JLabel juego = Estilo.titulo("  STRATEGO", 16);
        juego.setForeground(Estilo.DORADO);
        marca.add(logo);
        marca.add(juego);
        marca.setAlignmentX(Component.LEFT_ALIGNMENT);
        encabezado.add(marca);
        encabezado.add(Box.createVerticalStrut(8));
        JLabel etiqueta = Estilo.etiqueta(seccion.toUpperCase());
        etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);
        encabezado.add(etiqueta);
        encabezado.add(Box.createVerticalStrut(3));
        JLabel encabezadoTitulo = Estilo.titulo(titulo, 22);
        encabezadoTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        encabezado.add(encabezadoTitulo);
        if (subtitulo != null && !subtitulo.trim().isEmpty()) {
            encabezado.add(Box.createVerticalStrut(4));
            JLabel detalle = Estilo.texto(subtitulo);
            detalle.setAlignmentX(Component.LEFT_ALIGNMENT);
            encabezado.add(detalle);
        }
        fondo.add(encabezado, BorderLayout.NORTH);

        TarjetaMarvel tarjeta = new TarjetaMarvel();
        tarjeta.setLayout(new BorderLayout(0, 10));
        cuerpo = new JPanel(new BorderLayout(0, 10));
        cuerpo.setOpaque(false);
        tarjeta.add(cuerpo, BorderLayout.CENTER);
        fondo.add(tarjeta, BorderLayout.CENTER);

        acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 9, 0));
        acciones.setOpaque(false);
        fondo.add(acciones, BorderLayout.SOUTH);

        setContentPane(fondo);
        setLocationRelativeTo(owner);
    }

    public JPanel getCuerpo() {
        return cuerpo;
    }

    public JPanel getAcciones() {
        return acciones;
    }

    public JButton agregarBoton(String texto, Color color, ActionListener listener) {
        JButton b = Estilo.boton(texto, color);
        b.addActionListener(listener);
        acciones.add(b);
        return b;
    }

    public static JLabel crearEtiqueta(String texto) {
        return Estilo.etiqueta(texto);
    }

    public static JTextArea crearTexto(String texto) {
        JTextArea area = new JTextArea(texto);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setOpaque(false);
        area.setForeground(new Color(225, 215, 208));
        area.setFont(new Font("SansSerif", Font.PLAIN, 13));
        area.setRows(4);
        area.setColumns(34);
        area.setPreferredSize(new Dimension(390, 112));
        area.setBorder(new EmptyBorder(5, 2, 5, 2));
        return area;
    }

    public static JLabel crearMensaje(String texto) {
        JLabel l = new JLabel("<html><div style='width:340px'>" + texto + "</div></html>");
        l.setForeground(new Color(225, 215, 208));
        l.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return l;
    }

    public static void mensaje(Window owner, String seccion, String titulo, String texto) {
        DialogoMarvel d = new DialogoMarvel(owner, seccion, titulo, null, 510, 310);
        d.getCuerpo().add(crearTexto(texto), BorderLayout.CENTER);
        d.agregarBoton("ENTENDIDO", Estilo.ROJO, e -> d.dispose());
        d.setVisible(true);
    }

    public static boolean confirmar(Window owner, String seccion, String titulo,
                                    String texto, String confirmar) {
        final boolean[] respuesta = {false};
        DialogoMarvel d = new DialogoMarvel(owner, seccion, titulo, null, 540, 330);
        d.getCuerpo().add(crearTexto(texto), BorderLayout.CENTER);
        d.agregarBoton("CANCELAR", new Color(65, 55, 55), e -> d.dispose());
        d.agregarBoton(confirmar, Estilo.ROJO, e -> {
            respuesta[0] = true;
            d.dispose();
        });
        d.setVisible(true);
        return respuesta[0];
    }
}