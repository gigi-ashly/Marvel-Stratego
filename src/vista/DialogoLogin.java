package vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import logica.GestorJugadores;
import modelo.Jugador;
import modelo.excepciones.CredencialesInvalidasException;

public class DialogoLogin extends JDialog {
    private JTextField usuario = Estilo.campo();
    private JPasswordField password = Estilo.password();
    private GestorJugadores gestor;
    private MenuInicial menu;

    public DialogoLogin(MenuInicial menu, GestorJugadores gestor) {
        super(menu, "Marvel Stratego | Acceso", true);
        this.menu = menu;
        this.gestor = gestor;
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(560, 700);
        setMinimumSize(new Dimension(520, 670));
        setResizable(false);
        setLocationRelativeTo(menu);

        MarvelArtworkPanel fondo = new MarvelArtworkPanel(false, true);
        fondo.setLayout(new GridBagLayout());
        fondo.setBorder(new EmptyBorder(20, 28, 20, 28));

        JPanel tarjeta = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(13, 11, 13, 239));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 19, 19);
                g2.setColor(new Color(167, 35, 45, 235));
                g2.setStroke(new BasicStroke(1.3f));
                g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 19, 19);
                g2.setColor(Estilo.DORADO);
                g2.fillRoundRect(22, 0, 82, 4, 4, 4);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        tarjeta.setOpaque(false);
        tarjeta.setBorder(new EmptyBorder(23, 30, 23, 30));
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setPreferredSize(new Dimension(455, 415));

        JPanel insignia = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        insignia.setOpaque(false);
        JLabel marvel = new JLabel("MARVEL");
        marvel.setOpaque(true);
        marvel.setBackground(Estilo.ROJO);
        marvel.setForeground(Color.WHITE);
        marvel.setFont(new Font("SansSerif", Font.BOLD, 13));
        marvel.setBorder(new EmptyBorder(4, 9, 4, 9));
        JLabel stratego = Estilo.titulo("  STRATEGO", 17);
        stratego.setForeground(Estilo.DORADO);
        insignia.add(marvel);
        insignia.add(stratego);
        insignia.setAlignmentX(Component.LEFT_ALIGNMENT);
        tarjeta.add(insignia);
        tarjeta.add(Box.createVerticalStrut(19));

        JLabel titulo = Estilo.titulo("INICIA TU MISIÓN", 26);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        tarjeta.add(titulo);
        tarjeta.add(Box.createVerticalStrut(6));
        JLabel subtitulo = Estilo.texto("Identifícate para entrar al campo de batalla.");
        subtitulo.setForeground(new Color(205, 190, 184));
        subtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        tarjeta.add(subtitulo);
        tarjeta.add(Box.createVerticalStrut(17));

        JLabel userLabel = Estilo.etiqueta("NOMBRE DE JUGADOR");
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tarjeta.add(userLabel);
        tarjeta.add(Box.createVerticalStrut(7));
        usuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        tarjeta.add(usuario);
        tarjeta.add(Box.createVerticalStrut(17));

        JLabel passLabel = Estilo.etiqueta("CONTRASEÑA");
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tarjeta.add(passLabel);
        tarjeta.add(Box.createVerticalStrut(7));
        password.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        tarjeta.add(password);
        tarjeta.add(Box.createVerticalStrut(17));

        JButton entrar = Estilo.boton("ENTRAR AL UNIVERSO", Estilo.ROJO);
        JButton cancelar = Estilo.boton("VOLVER", new Color(65, 55, 55));
        entrar.setPreferredSize(new Dimension(220, 49));
        cancelar.setPreferredSize(new Dimension(130, 49));
        JPanel botones = new JPanel(new GridLayout(1, 2, 11, 0));
        botones.setOpaque(false);
        botones.add(entrar);
        botones.add(cancelar);
        botones.setAlignmentX(Component.LEFT_ALIGNMENT);
        tarjeta.add(botones);

        fondo.add(tarjeta);
        setContentPane(fondo);

        entrar.addActionListener(e -> ingresar());
        cancelar.addActionListener(e -> dispose());
        getRootPane().setDefaultButton(entrar);
    }

    private void ingresar() {
        try {
            Jugador jugador = gestor.iniciarSesion(usuario.getText().trim(), new String(password.getPassword()));
            new MenuPrincipal(gestor, jugador).setVisible(true);
            menu.dispose();
            dispose();
        } catch (CredencialesInvalidasException ex) {
            DialogoMarvel.mensaje(this, "ACCESO", "No se pudo iniciar sesión", ex.getMessage());
            password.setText("");
        }
    }
}
