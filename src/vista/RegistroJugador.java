package vista;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import logica.GestorJugadores;
import modelo.Usuario;
import modelo.excepciones.PasswordInvalidoException;
import modelo.excepciones.RegistroInvalidoException;
import modelo.excepciones.UsuarioDuplicadoException;

public class RegistroJugador extends JFrame {
    private MenuInicial menu;
    private GestorJugadores gestor;
    private JTextField usuario = Estilo.campo();
    private JPasswordField password = Estilo.password();

    public RegistroJugador(MenuInicial menu, GestorJugadores gestor) {
        this.menu = menu;
        this.gestor = gestor;
        setTitle("Marvel Stratego | Crear Player");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 700);
        setMinimumSize(new Dimension(540, 640));
        setResizable(false);
        setLocationRelativeTo(menu);

        MarvelArtworkPanel fondo = new MarvelArtworkPanel(false, true);
        fondo.setLayout(new GridBagLayout());
        fondo.setBorder(new EmptyBorder(22, 28, 22, 28));

        JPanel card = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(14, 11, 13, 242));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(169, 34, 46, 235));
                g2.setStroke(new BasicStroke(1.4f));
                g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 20, 20);
                g2.setColor(Estilo.DORADO);
                g2.fillRoundRect(24, 0, 88, 4, 4, 4);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(27, 34, 27, 34));
        card.setPreferredSize(new Dimension(445, 485));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JPanel marca = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        marca.setOpaque(false);
        JLabel marvel = new JLabel("MARVEL");
        marvel.setOpaque(true);
        marvel.setBackground(Estilo.ROJO);
        marvel.setForeground(Color.WHITE);
        marvel.setFont(new Font("SansSerif", Font.BOLD, 13));
        marvel.setBorder(new EmptyBorder(4, 9, 4, 9));
        JLabel stratego = Estilo.titulo("  STRATEGO", 18);
        stratego.setForeground(Estilo.DORADO);
        marca.add(marvel);
        marca.add(stratego);
        marca.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(marca);
        card.add(Box.createVerticalStrut(23));

        JLabel titulo = Estilo.titulo("CREAR PLAYER", 27);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(titulo);
        card.add(Box.createVerticalStrut(6));
        JLabel descripcion = Estilo.texto("Crea tu cuenta y prepárate para elegir tu bando.");
        descripcion.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(descripcion);
        card.add(Box.createVerticalStrut(27));

        JLabel userLabel = Estilo.etiqueta("NOMBRE DE JUGADOR");
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(userLabel);
        card.add(Box.createVerticalStrut(7));
        usuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        card.add(usuario);
        card.add(Box.createVerticalStrut(19));

        JLabel passLabel = Estilo.etiqueta("CONTRASEÑA");
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(passLabel);
        card.add(Box.createVerticalStrut(7));
        password.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        card.add(password);
        card.add(Box.createVerticalStrut(9));
        JLabel ayuda = Estilo.texto("Debe contener exactamente " + Usuario.LONGITUD_PASSWORD + " caracteres.");
        ayuda.setFont(new Font("SansSerif", Font.PLAIN, 11));
        ayuda.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(ayuda);
        card.add(Box.createVerticalStrut(27));

        JButton registrar = Estilo.boton("CREAR CUENTA", Estilo.ROJO);
        JButton volver = Estilo.boton("VOLVER", new Color(65, 55, 55));
        registrar.setPreferredSize(new Dimension(175, 48));
        volver.setPreferredSize(new Dimension(145, 48));
        JPanel botones = new JPanel(new GridLayout(1, 2, 11, 0));
        botones.setOpaque(false);
        botones.add(registrar);
        botones.add(volver);
        botones.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(botones);

        fondo.add(card, new GridBagConstraints(0, 0, 1, 1, 1, 1,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets(0, 0, 0, 0), 0, 0));
        setContentPane(fondo);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                menu.setVisible(true);
            }
        });
        registrar.addActionListener(e -> registrar());
        volver.addActionListener(e -> regresar());
        getRootPane().setDefaultButton(registrar);
    }

    private void registrar() {
        try {
            gestor.registrarJugadorValidado(usuario.getText().trim(), new String(password.getPassword()));
            DialogoMarvel.mensaje(this, "PLAYER CREADO", "Cuenta registrada",
                "¡Tu cuenta se creó correctamente! Ya puedes iniciar sesión.");
            regresar();
        } catch (UsuarioDuplicadoException ex) {
            DialogoMarvel.mensaje(this, "REGISTRO", "Username no disponible",
                ex.getMessage() + " Elige otro username.");
            usuario.setText("");
            usuario.requestFocus();
        } catch (PasswordInvalidoException ex) {
            DialogoMarvel.mensaje(this, "REGISTRO", "Password inválido", ex.getMessage());
            password.setText("");
            password.requestFocus();
        } catch (RegistroInvalidoException ex) {
            DialogoMarvel.mensaje(this, "REGISTRO", "No se pudo crear la cuenta", ex.getMessage());
        }
    }

    private void regresar() {
        menu.setVisible(true);
        dispose();
    }
}
