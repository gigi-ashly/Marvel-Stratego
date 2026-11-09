package vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import logica.GestorJugadores;

public class MenuInicial extends JFrame {
    private GestorJugadores gestor;

    public MenuInicial(GestorJugadores gestor) {
        this.gestor = gestor;
        setTitle("Marvel Stratego | Inicio");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(760, 690);
        setMinimumSize(new Dimension(650, 600));
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        MarvelArtworkPanel fondo = new MarvelArtworkPanel(false, true);
        fondo.setLayout(new GridBagLayout());
        fondo.setBorder(new EmptyBorder(20, 30, 20, 30));

        JPanel bienvenida = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(13, 11, 13, 226));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(170, 36, 45, 225));
                g2.setStroke(new BasicStroke(1.4f));
                g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 20, 20);
                g2.setColor(Estilo.DORADO);
                g2.fillRoundRect(24, 0, 86, 4, 4, 4);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        bienvenida.setOpaque(false);
        bienvenida.setBorder(new EmptyBorder(27, 35, 27, 35));
        bienvenida.setLayout(new BoxLayout(bienvenida, BoxLayout.Y_AXIS));
        bienvenida.setPreferredSize(new Dimension(450, 340));

        JPanel marca = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        marca.setOpaque(false);
        JLabel marvel = new JLabel("MARVEL");
        marvel.setOpaque(true);
        marvel.setBackground(Estilo.ROJO);
        marvel.setForeground(Color.WHITE);
        marvel.setFont(new Font("SansSerif", Font.BOLD, 18));
        marvel.setBorder(new EmptyBorder(5, 12, 5, 12));
        JLabel stratego = Estilo.titulo("  STRATEGO", 28);
        stratego.setForeground(Estilo.DORADO);
        marca.add(marvel);
        marca.add(stratego);
        marca.setAlignmentX(Component.CENTER_ALIGNMENT);
        bienvenida.add(marca);

        JLabel subtitulo = Estilo.titulo("HEROES GAME", 15);
        subtitulo.setForeground(new Color(228, 214, 207));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        bienvenida.add(Box.createVerticalStrut(13));
        bienvenida.add(subtitulo);

        JLabel claim = Estilo.texto("ELIGE TU BANDO. DEFINE TU LEYENDA.");
        claim.setForeground(Estilo.DORADO);
        claim.setFont(new Font("SansSerif", Font.BOLD, 10));
        claim.setAlignmentX(Component.CENTER_ALIGNMENT);
        bienvenida.add(Box.createVerticalStrut(6));
        bienvenida.add(claim);
        bienvenida.add(Box.createVerticalStrut(29));

        JButton login = Estilo.boton("LOG IN", Estilo.ROJO);
        JButton crear = Estilo.boton("CREAR PLAYER", new Color(66, 52, 52));
        login.setMaximumSize(new Dimension(330, 50));
        login.setPreferredSize(new Dimension(330, 50));
        crear.setMaximumSize(new Dimension(330, 50));
        crear.setPreferredSize(new Dimension(330, 50));
        login.setAlignmentX(Component.CENTER_ALIGNMENT);
        crear.setAlignmentX(Component.CENTER_ALIGNMENT);
        bienvenida.add(login);
        bienvenida.add(Box.createVerticalStrut(13));
        bienvenida.add(crear);
        bienvenida.add(Box.createVerticalStrut(20));

        JLabel pie = Estilo.texto("PROGRAMACIÓN II  •  MARVEL STRATEGO");
        pie.setFont(new Font("SansSerif", Font.BOLD, 9));
        pie.setAlignmentX(Component.CENTER_ALIGNMENT);
        bienvenida.add(pie);

        fondo.add(bienvenida);
        setContentPane(fondo);

        login.addActionListener(e -> {
            setVisible(false);
            DialogoLogin d = new DialogoLogin(this, gestor);
            d.setVisible(true);
            if (isDisplayable()) setVisible(true);
        });
        crear.addActionListener(e -> {
            setVisible(false);
            new RegistroJugador(this, gestor).setVisible(true);
        });
    }
}