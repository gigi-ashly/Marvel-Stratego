package vista;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.*;
import modelo.*;
import logica.GestorJugadores;
import modelo.excepciones.CredencialesInvalidasException;
import modelo.excepciones.PasswordInvalidoException;

public class MenuPrincipal extends JFrame {
    private GestorJugadores gestor;
    private Jugador jugador;

public MenuPrincipal(GestorJugadores gestor, Jugador jugador) {
    this.gestor = gestor;
    this.jugador = jugador;
    setTitle("Marvel Stratego | Menú principal");
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setSize(1320, 760);
    setMinimumSize(new Dimension(980, 650));
    setLocationRelativeTo(null);
    setExtendedState(JFrame.MAXIMIZED_BOTH);

    JPanel fondo = Estilo.fondo();
    fondo.setLayout(new BorderLayout(18, 16));
    fondo.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

    JPanel header = new JPanel(new BorderLayout());
    header.setOpaque(false);
    JPanel marca = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    marca.setOpaque(false);
    JLabel marvel = new JLabel("MARVEL");
    marvel.setOpaque(true);
    marvel.setBackground(Estilo.ROJO);
    marvel.setForeground(Color.WHITE);
    marvel.setFont(new Font("SansSerif", Font.BOLD, 17));
    marvel.setBorder(BorderFactory.createEmptyBorder(5, 11, 5, 11));
    JLabel stratego = Estilo.titulo("  STRATEGO", 23);
    stratego.setForeground(Estilo.DORADO);
    marca.add(marvel);
    marca.add(stratego);
    header.add(marca, BorderLayout.WEST);

    JPanel activo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 9, 3));
    activo.setOpaque(false);
    JLabel estado = Estilo.texto("AGENTE ACTIVO");
    estado.setForeground(new Color(215, 204, 194));
    JLabel nombre = new JLabel(jugador.getUsername().toUpperCase());
    nombre.setForeground(Color.WHITE);
    nombre.setFont(new Font("SansSerif", Font.BOLD, 13));
    nombre.setOpaque(true);
    nombre.setBackground(new Color(35, 26, 28));
    nombre.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(190, 28, 45), 1),
        BorderFactory.createEmptyBorder(7, 13, 7, 13)));
    activo.add(estado);
    activo.add(nombre);
    header.add(activo, BorderLayout.EAST);
    fondo.add(header, BorderLayout.NORTH);

    JPanel centro = new JPanel(new BorderLayout(17, 0));
    centro.setOpaque(false);

    JPanel navegacion = Estilo.tarjeta();
    navegacion.setBackground(new Color(19, 16, 17));
    navegacion.setPreferredSize(new Dimension(275, 0));
    navegacion.setLayout(new BoxLayout(navegacion, BoxLayout.Y_AXIS));
    JLabel navTitle = Estilo.titulo("CUARTEL", 18);
    navTitle.setForeground(Estilo.DORADO);
    navTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
    navegacion.add(navTitle);
    navegacion.add(Box.createVerticalStrut(5));
    JLabel navSub = Estilo.texto("CENTRO DE OPERACIONES");
    navSub.setFont(new Font("SansSerif", Font.BOLD, 9));
    navSub.setForeground(new Color(160, 145, 140));
    navSub.setAlignmentX(Component.LEFT_ALIGNMENT);
    navegacion.add(navSub);

    JButton partidaNueva = botonNavegacion("Partida Nueva", Estilo.ROJO);
    JButton historial = botonNavegacion("Log de mis últimos juegos", new Color(65, 49, 49));
    JButton cambiarClave = botonNavegacion("Cambiar mi password", new Color(65, 49, 49));
    JButton eliminar = botonNavegacion("Eliminar mi cuenta", new Color(65, 49, 49));
    JButton ranking = botonNavegacion("Ranking", new Color(65, 49, 49));
    JButton batallas = botonNavegacion("Batallas", new Color(65, 49, 49));
    JButton salir = botonNavegacion("Cerrar sesión", new Color(93, 35, 42));

    agregarSeccion(navegacion, "STRATEGO – MARVEL HEROES!", partidaNueva);
    agregarSeccion(navegacion, "MI PERFIL", historial, cambiarClave, eliminar);
    agregarSeccion(navegacion, "UNIVERSO MARVEL", ranking, batallas);
    navegacion.add(Box.createVerticalGlue());
    navegacion.add(salir);
    centro.add(navegacion, BorderLayout.WEST);

    JPanel contenido = new JPanel(new BorderLayout(0, 15));
    contenido.setOpaque(false);

    MarvelArtworkPanel hero = new MarvelArtworkPanel(true, false);
    hero.setLayout(new BorderLayout());
    hero.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(139, 42, 45), 1),
        BorderFactory.createEmptyBorder(22, 22, 22, 22)));
    hero.setPreferredSize(new Dimension(950, 385));

    JPanel bienvenida = new JPanel();
    bienvenida.setOpaque(false);
    bienvenida.setPreferredSize(new Dimension(360, 0));
    bienvenida.setBorder(BorderFactory.createEmptyBorder(18, 22, 12, 12));
    bienvenida.setLayout(new BoxLayout(bienvenida, BoxLayout.Y_AXIS));

    JLabel kicker = Estilo.texto("EL DESTINO DEL UNIVERSO ESTÁ EN TUS MANOS");
    kicker.setForeground(Estilo.DORADO);
    kicker.setFont(new Font("SansSerif", Font.BOLD, 10));
    kicker.setAlignmentX(Component.LEFT_ALIGNMENT);
    bienvenida.add(kicker);
    bienvenida.add(Box.createVerticalStrut(18));

    JLabel tituloHero = Estilo.titulo("<html>MARVEL<br>HEROES</html>", 36);
    tituloHero.setForeground(Color.WHITE);
    tituloHero.setAlignmentX(Component.LEFT_ALIGNMENT);
    bienvenida.add(tituloHero);
    JLabel linea = new JLabel("STRATEGO GAME");
    linea.setForeground(Estilo.DORADO);
    linea.setFont(new Font("Serif", Font.BOLD, 21));
    linea.setAlignmentX(Component.LEFT_ALIGNMENT);
    bienvenida.add(linea);
    bienvenida.add(Box.createVerticalStrut(15));

    JLabel hola = Estilo.titulo("BIENVENIDO, " + jugador.getUsername().toUpperCase(), 15);
    hola.setForeground(new Color(246, 226, 215));
    hola.setAlignmentX(Component.LEFT_ALIGNMENT);
    bienvenida.add(hola);
    bienvenida.add(Box.createVerticalStrut(8));

    JTextArea descripcion = new JTextArea(
        "Forma tu estrategia, descubre al rival y protege la Tierra. Cada decisión cambia la batalla.");
    descripcion.setWrapStyleWord(true);
    descripcion.setLineWrap(true);
    descripcion.setEditable(false);
    descripcion.setOpaque(false);
    descripcion.setForeground(new Color(218, 207, 200));
    descripcion.setFont(new Font("SansSerif", Font.PLAIN, 13));
    descripcion.setRows(3);
    descripcion.setAlignmentX(Component.LEFT_ALIGNMENT);
    bienvenida.add(descripcion);
    bienvenida.add(Box.createVerticalStrut(18));

    JButton nueva = Estilo.boton("▶  INICIAR PARTIDA", Estilo.ROJO);
    nueva.setAlignmentX(Component.LEFT_ALIGNMENT);
    nueva.setMaximumSize(new Dimension(245, 51));
    nueva.setPreferredSize(new Dimension(245, 51));
    bienvenida.add(nueva);
    bienvenida.add(Box.createVerticalStrut(9));
    JLabel modo = Estilo.texto("2 JUGADORES  ·  PARTIDA LOCAL");
    modo.setFont(new Font("SansSerif", Font.BOLD, 9));
    modo.setForeground(new Color(170, 151, 143));
    modo.setAlignmentX(Component.LEFT_ALIGNMENT);
    bienvenida.add(modo);
    bienvenida.add(Box.createVerticalGlue());
    hero.add(bienvenida, BorderLayout.EAST);

    boolean hayRival = gestor.getCantidadJugadores() >= 2;
    for (JButton b : new JButton[]{nueva, partidaNueva}) {
        b.setEnabled(hayRival);
        if (!hayRival) b.setToolTipText("Necesitas al menos otro jugador registrado");
    }

    JPanel inferior = new JPanel(new BorderLayout(0, 10));
    inferior.setOpaque(false);
    JPanel metricas = new JPanel(new GridLayout(1, 3, 12, 0));
    metricas.setOpaque(false);
    metricas.add(metrica("PUNTOS", String.valueOf(jugador.getPuntos()), "★"));
    metricas.add(metrica("VICTORIAS", String.valueOf(jugador.getVictorias()), "✓"));
    metricas.add(metrica("DERROTAS", String.valueOf(jugador.getDerrotas()), "×"));

    JPanel mision = Estilo.tarjetaSuave();
    mision.setBackground(new Color(31, 22, 23));
    mision.setLayout(new BorderLayout(14, 0));
    JLabel objetivo = Estilo.titulo("OBJETIVO DE MISIÓN", 14);
    objetivo.setForeground(Estilo.DORADO);
    mision.add(objetivo, BorderLayout.WEST);
    JTextArea regla = new JTextArea(
        "Captura la Tierra enemiga o elimina todas sus piezas móviles. Héroes y villanos se alternan los turnos.");
    regla.setEditable(false);
    regla.setLineWrap(true);
    regla.setWrapStyleWord(true);
    regla.setOpaque(false);
    regla.setForeground(new Color(210, 196, 188));
    regla.setFont(new Font("SansSerif", Font.PLAIN, 12));
    mision.add(regla, BorderLayout.CENTER);

    inferior.add(metricas, BorderLayout.NORTH);
    inferior.add(mision, BorderLayout.CENTER);
    contenido.add(hero, BorderLayout.CENTER);
    contenido.add(inferior, BorderLayout.SOUTH);

    centro.add(contenido, BorderLayout.CENTER);
    fondo.add(centro, BorderLayout.CENTER);
    setContentPane(fondo);

    addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
            new MenuInicial(gestor).setVisible(true);
        }
    });

    nueva.addActionListener(e -> iniciarPartida());
    partidaNueva.addActionListener(e -> iniciarPartida());
    historial.addActionListener(e -> new VentanaPerfil(this, jugador).setVisible(true));
    cambiarClave.addActionListener(e -> cambiarPassword());
    eliminar.addActionListener(e -> eliminarCuenta());
    ranking.addActionListener(e -> mostrarRanking());
    batallas.addActionListener(e -> mostrarBatallas());
    salir.addActionListener(e -> cerrarSesion());
}

    private JButton botonNavegacion(String texto, Color color) {
        JButton b = Estilo.boton(texto, color);
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setMaximumSize(new Dimension(215, 40));
        b.setPreferredSize(new Dimension(215, 40));
        return b;
    }

    private void agregarSeccion(JPanel navegacion, String titulo, JButton... botones) {
        navegacion.add(Box.createVerticalStrut(18));
        JLabel etiqueta = Estilo.etiqueta(titulo);
        etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);
        navegacion.add(etiqueta);
        for (JButton b : botones) {
            navegacion.add(Box.createVerticalStrut(7));
            navegacion.add(b);
        }
    }

    private JPanel metrica(String titulo, String valor, String simbolo) {
        JPanel p = Estilo.tarjetaSuave();
        p.setLayout(new BorderLayout(8, 4));
        JLabel encabezado = Estilo.texto(simbolo + "  " + titulo);
        encabezado.setFont(new Font("SansSerif", Font.BOLD, 11));
        JLabel numero = Estilo.titulo(valor, 29);
        numero.setForeground(Estilo.DORADO);
        p.add(encabezado, BorderLayout.NORTH);
        p.add(numero, BorderLayout.CENTER);
        return p;
    }

    
private void iniciarPartida() {
    if (gestor.getCantidadJugadores() < 2) {
        DialogoMarvel.mensaje(this, "NUEVA PARTIDA", "Faltan jugadores",
            "Necesitas al menos otro jugador registrado para iniciar una partida.");
        return;
    }

    JComboBox<String> oponentes = new JComboBox<String>();
    for (int i = 0; i < gestor.getCantidadJugadores(); i++) {
        Jugador otro = gestor.getJugador(i);
        if (otro != jugador) oponentes.addItem(otro.getUsername());
    }
    JRadioButton heroes = new JRadioButton("HÉROES", true);
    JRadioButton villanos = new JRadioButton("VILLANOS");
    heroes.setForeground(Color.WHITE);
    villanos.setForeground(Color.WHITE);
    heroes.setOpaque(false);
    villanos.setOpaque(false);
    ButtonGroup grupo = new ButtonGroup();
    grupo.add(heroes);
    grupo.add(villanos);

    JComboBox<ModoJuego> modos = new JComboBox<ModoJuego>(ModoJuego.values());
    modos.setSelectedItem(ModoJuego.CLASICO);
    estilosCombo(oponentes);
    estilosCombo(modos);

    DialogoMarvel d = new DialogoMarvel(this, "ESTABLECER PARTIDA",
        "Prepara el enfrentamiento", "Selecciona rival, bando y modalidad.", 680, 620);
    JPanel formulario = new JPanel();
    formulario.setOpaque(false);
    formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));

    formulario.add(DialogoMarvel.crearEtiqueta("JUGADOR CONTRARIO"));
    formulario.add(Box.createVerticalStrut(7));
    oponentes.setMaximumSize(new Dimension(Integer.MAX_VALUE, 43));
    formulario.add(oponentes);
    formulario.add(Box.createVerticalStrut(19));

    formulario.add(DialogoMarvel.crearEtiqueta("ELIGE TU BANDO"));
    formulario.add(Box.createVerticalStrut(7));
    JPanel equipos = new JPanel(new GridLayout(1, 2, 10, 0));
    equipos.setOpaque(false);
    equipos.add(panelOpcionBando(heroes, new Color(29, 98, 161)));
    equipos.add(panelOpcionBando(villanos, new Color(169, 35, 48)));
    formulario.add(equipos);
    formulario.add(Box.createVerticalStrut(19));

    formulario.add(DialogoMarvel.crearEtiqueta("MODALIDAD DE JUEGO"));
    formulario.add(Box.createVerticalStrut(7));
    modos.setMaximumSize(new Dimension(Integer.MAX_VALUE, 43));
    formulario.add(modos);
    formulario.add(Box.createVerticalStrut(8));
    JTextArea ayudaModos = new JTextArea(
        "TUTORIAL: muestra las identidades de todas las fichas.\n" +
        "CLÁSICO: mantiene ocultas las fichas rivales hasta que se revelen durante el combate.");
    ayudaModos.setEditable(false);
    ayudaModos.setLineWrap(true);
    ayudaModos.setWrapStyleWord(true);
    ayudaModos.setOpaque(false);
    ayudaModos.setForeground(new Color(225, 215, 208));
    ayudaModos.setFont(new Font("SansSerif", Font.PLAIN, 12));
    ayudaModos.setRows(2);
    ayudaModos.setBorder(BorderFactory.createEmptyBorder(3, 1, 3, 1));
    ayudaModos.setAlignmentX(Component.LEFT_ALIGNMENT);
    formulario.add(ayudaModos);
    d.getCuerpo().add(formulario, BorderLayout.CENTER);

    d.agregarBoton("VOLVER", new Color(65, 55, 55), e -> d.dispose());
    d.agregarBoton("COMENZAR", Estilo.ROJO, e -> {
        Jugador rival = gestor.buscarJugador((String) oponentes.getSelectedItem());
        if (rival == null) {
            DialogoMarvel.mensaje(d, "NUEVA PARTIDA", "Selecciona un rival",
                "No se encontró el jugador contrario.");
            return;
        }
        Jugador jugadorHeroes = heroes.isSelected() ? jugador : rival;
        Jugador jugadorVillanos = heroes.isSelected() ? rival : jugador;
        new VentanaJuego(gestor, jugadorHeroes, jugadorVillanos, jugador,
            (ModoJuego) modos.getSelectedItem()).setVisible(true);
        d.dispose();
        dispose();
    });
    d.setVisible(true);
}

private JPanel panelOpcionBando(JRadioButton radio, Color color) {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setOpaque(true);
    panel.setBackground(new Color(25, 20, 22));
    panel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(color, 2),
        BorderFactory.createEmptyBorder(10, 12, 10, 12)));
    radio.setFont(new Font("SansSerif", Font.BOLD, 14));
    panel.add(radio, BorderLayout.CENTER);
    return panel;
}


private void estilosCombo(JComboBox<?> combo) {
    final Color fondo = new Color(32, 25, 27);
    final Color texto = new Color(248, 242, 237);
    final Color borde = new Color(118, 74, 70);

    combo.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
        protected JButton createArrowButton() {
            JButton flecha = new JButton("▼");
            flecha.setFont(new Font("SansSerif", Font.BOLD, 9));
            flecha.setForeground(Estilo.DORADO);
            flecha.setBackground(new Color(116, 25, 39));
            flecha.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            flecha.setFocusPainted(false);
            return flecha;
        }
    });
    combo.setOpaque(true);
    combo.setBackground(fondo);
    combo.setForeground(texto);
    combo.setFont(new Font("SansSerif", Font.BOLD, 13));
    combo.setEditable(true);
    Component editorComponente = combo.getEditor().getEditorComponent();
    if (editorComponente instanceof JTextField) {
        JTextField editor = (JTextField) editorComponente;
        editor.setEditable(false);
        editor.setOpaque(true);
        editor.setBackground(fondo);
        editor.setForeground(texto);
        editor.setCaretColor(Estilo.DORADO);
        editor.setFont(new Font("SansSerif", Font.BOLD, 13));
        editor.setBorder(BorderFactory.createEmptyBorder(5, 9, 5, 7));
    }
    combo.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(borde, 1),
        BorderFactory.createEmptyBorder(5, 9, 5, 7)));
    combo.setRenderer(new DefaultListCellRenderer() {
        public Component getListCellRendererComponent(JList<?> lista, Object valor,
                int indice, boolean seleccionado, boolean foco) {
            JLabel etiqueta = (JLabel) super.getListCellRendererComponent(
                lista, valor, indice, seleccionado, foco);
            etiqueta.setOpaque(true);
            etiqueta.setFont(new Font("SansSerif", Font.BOLD, 13));
            etiqueta.setForeground(seleccionado ? Color.WHITE : texto);
            etiqueta.setBackground(seleccionado ? new Color(145, 28, 44) : fondo);
            etiqueta.setBorder(BorderFactory.createEmptyBorder(7, 9, 7, 9));
            return etiqueta;
        }
    });
}

private void cambiarPassword() {
    DialogoMarvel d = new DialogoMarvel(this, "SEGURIDAD DE CUENTA",
        "Cambiar contraseña", "Confirma tu clave actual para guardar una nueva.", 540, 465);
    JPasswordField actual = Estilo.password();
    JPasswordField nueva = Estilo.password();
    actual.setMaximumSize(new Dimension(Integer.MAX_VALUE, 43));
    nueva.setMaximumSize(new Dimension(Integer.MAX_VALUE, 43));

    JPanel formulario = new JPanel();
    formulario.setOpaque(false);
    formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));
    formulario.add(DialogoMarvel.crearEtiqueta("CONTRASEÑA ACTUAL"));
    formulario.add(Box.createVerticalStrut(7));
    formulario.add(actual);
    formulario.add(Box.createVerticalStrut(16));
    formulario.add(DialogoMarvel.crearEtiqueta("NUEVA CONTRASEÑA"));
    formulario.add(Box.createVerticalStrut(7));
    formulario.add(nueva);
    formulario.add(Box.createVerticalStrut(9));
    formulario.add(DialogoMarvel.crearMensaje(
        "La contraseña debe contener exactamente " + Usuario.LONGITUD_PASSWORD + " caracteres."));
    d.getCuerpo().add(formulario, BorderLayout.CENTER);

    d.agregarBoton("CANCELAR", new Color(65, 55, 55), e -> d.dispose());
    d.agregarBoton("GUARDAR", Estilo.ROJO, e -> {
        try {
            jugador.cambiarPassword(new String(actual.getPassword()), new String(nueva.getPassword()));
            d.dispose();
            DialogoMarvel.mensaje(this, "SEGURIDAD", "Contraseña actualizada",
                "Tu contraseña se cambió correctamente.");
        } catch (CredencialesInvalidasException ex) {
            DialogoMarvel.mensaje(d, "SEGURIDAD", "Verificación fallida", ex.getMessage());
            actual.setText("");
        } catch (PasswordInvalidoException ex) {
            DialogoMarvel.mensaje(d, "SEGURIDAD", "Contraseña inválida", ex.getMessage());
            nueva.setText("");
        }
    });
    d.setVisible(true);
}

    
private void eliminarCuenta() {
    DialogoMarvel d = new DialogoMarvel(this, "SEGURIDAD DE CUENTA",
        "Eliminar mi cuenta", "Esta acción no se puede deshacer.", 540, 420);
    JPasswordField clave = Estilo.password();
    clave.setMaximumSize(new Dimension(Integer.MAX_VALUE, 43));
    JPanel formulario = new JPanel();
    formulario.setOpaque(false);
    formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));
    formulario.add(DialogoMarvel.crearMensaje(
        "Se eliminará tu cuenta y todos los datos asociados. Introduce tu contraseña actual para confirmar."));
    formulario.add(Box.createVerticalStrut(18));
    formulario.add(DialogoMarvel.crearEtiqueta("CONTRASEÑA ACTUAL"));
    formulario.add(Box.createVerticalStrut(7));
    formulario.add(clave);
    d.getCuerpo().add(formulario, BorderLayout.CENTER);

    d.agregarBoton("CANCELAR", new Color(65, 55, 55), e -> d.dispose());
    d.agregarBoton("ELIMINAR CUENTA", Estilo.ROJO, e -> {
        if (!jugador.validarPassword(new String(clave.getPassword()))) {
            DialogoMarvel.mensaje(d, "SEGURIDAD", "Verificación fallida",
                "La contraseña no coincide. La cuenta permanece activa.");
            clave.setText("");
            return;
        }
        gestor.eliminarJugador(jugador);
        d.dispose();
        DialogoMarvel.mensaje(this, "CUENTA ELIMINADA", "Sesión finalizada",
            "Tu cuenta fue eliminada correctamente.");
        MenuInicial inicio = new MenuInicial(gestor);
        inicio.setVisible(true);
        dispose();
    });
    d.setVisible(true);
}

    
private void mostrarRanking() {
    List<Jugador> ranking = gestor.obtenerRanking();
    DialogoMarvel d = new DialogoMarvel(this, "UNIVERSO MARVEL",
        "Ranking de jugadores", "Los mejores estrategas de la arena.", 650, 490);

    JPanel lista = new JPanel();
    lista.setOpaque(false);
    lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));

    JPanel cabecera = new JPanel(new BorderLayout(10, 0));
    cabecera.setOpaque(true);
    cabecera.setBackground(new Color(133, 25, 39));
    cabecera.setBorder(BorderFactory.createEmptyBorder(10, 13, 10, 13));
    cabecera.setPreferredSize(new Dimension(500, 42));
    cabecera.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
    JLabel hPos = Estilo.etiqueta("POSICIÓN");
    JLabel hUsuario = Estilo.etiqueta("JUGADOR");
    JLabel hPuntos = Estilo.etiqueta("PUNTOS");
    hPos.setForeground(Color.WHITE);
    hUsuario.setForeground(Color.WHITE);
    hPuntos.setForeground(Color.WHITE);
    cabecera.add(hPos, BorderLayout.WEST);
    cabecera.add(hUsuario, BorderLayout.CENTER);
    cabecera.add(hPuntos, BorderLayout.EAST);
    lista.add(cabecera);
    lista.add(Box.createVerticalStrut(7));

    if (ranking.isEmpty()) {
        lista.add(DialogoMarvel.crearMensaje("Todavía no hay jugadores registrados."));
    } else {
        for (int i = 0; i < ranking.size(); i++) {
            final int posicion = i + 1;
            Jugador j = ranking.get(i);
            JPanel fila = new JPanel(new BorderLayout(12, 0));
            fila.setOpaque(true);
            fila.setBackground(i == 0 ? new Color(68, 48, 28) : new Color(33, 27, 29));
            fila.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(i == 0 ? Estilo.DORADO : new Color(77, 59, 59)),
                BorderFactory.createEmptyBorder(12, 13, 12, 13)));
            JLabel lugar = Estilo.titulo("#" + posicion, 17);
            lugar.setForeground(i == 0 ? Estilo.DORADO : new Color(216, 205, 198));
            JLabel nombre = Estilo.titulo(j.getUsername(), 14);
            JLabel puntos = Estilo.titulo(String.valueOf(j.getPuntos()), 17);
            puntos.setForeground(Estilo.DORADO);
            fila.add(lugar, BorderLayout.WEST);
            fila.add(nombre, BorderLayout.CENTER);
            fila.add(puntos, BorderLayout.EAST);
            fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
            lista.add(fila);
            lista.add(Box.createVerticalStrut(6));
        }
    }

    JScrollPane scroll = new JScrollPane(lista);
    scroll.setBorder(null);
    scroll.setOpaque(false);
    scroll.getViewport().setOpaque(false);
    d.getCuerpo().add(scroll, BorderLayout.CENTER);
    d.agregarBoton("CERRAR", Estilo.ROJO, e -> d.dispose());
    d.setVisible(true);
}

    
private void mostrarBatallas() {
    DialogoMarvel d = new DialogoMarvel(this, "UNIVERSO MARVEL",
        "Centro de batallas", "Resumen global del universo de jugadores.", 700, 575);

    JPanel metricas = new JPanel(new GridBagLayout());
    metricas.setOpaque(false);
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(6, 6, 6, 6);
    c.weightx = 1.0;
    c.fill = GridBagConstraints.HORIZONTAL;

    c.gridx = 0; c.gridy = 0; c.gridwidth = 1;
    metricas.add(tarjetaDato("JUGADORES ACTIVOS", String.valueOf(gestor.getCantidadJugadores())), c);
    c.gridx = 1; c.gridy = 0;
    metricas.add(tarjetaDato("JUGADORES HISTÓRICOS", String.valueOf(gestor.getUsuariosHistoricos())), c);

    c.gridx = 0; c.gridy = 1;
    metricas.add(tarjetaDato("PARTIDAS JUGADAS", String.valueOf(gestor.getCantidadPartidas())), c);
    c.gridx = 1; c.gridy = 1;
    metricas.add(tarjetaDato("VICTORIAS HÉROES", String.valueOf(gestor.contarVictorias(Bando.HEROES))), c);

    c.gridx = 0; c.gridy = 2; c.gridwidth = 2;
    c.weightx = 0;
    c.fill = GridBagConstraints.NONE;
    c.anchor = GridBagConstraints.CENTER;
    metricas.add(tarjetaDato("VICTORIAS VILLANOS", String.valueOf(gestor.contarVictorias(Bando.VILLANOS))), c);
    d.getCuerpo().add(metricas, BorderLayout.CENTER);
    d.agregarBoton("CERRAR", Estilo.ROJO, e -> d.dispose());
    d.setVisible(true);
}

private JPanel tarjetaDato(String titulo, String valor) {
    JPanel card = new JPanel(new BorderLayout(4, 7));
    card.setOpaque(true);
    card.setBackground(new Color(34, 27, 29));
    card.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(91, 65, 62), 1),
        BorderFactory.createEmptyBorder(12, 15, 10, 15)));

    JLabel label = Estilo.etiqueta(titulo);
    label.setFont(new Font("SansSerif", Font.BOLD, 10));
    label.setForeground(new Color(225, 190, 112));

    JLabel numero = Estilo.titulo(valor, 27);
    numero.setForeground(Estilo.DORADO);
    numero.setFont(new Font("SansSerif", Font.BOLD, 27));
    numero.setVerticalAlignment(SwingConstants.CENTER);
    numero.setPreferredSize(new Dimension(40, 36));

    card.add(label, BorderLayout.NORTH);
    card.add(numero, BorderLayout.CENTER);
    card.setPreferredSize(new Dimension(245, 92));
    return card;
}

    
private void cerrarSesion() {
    if (DialogoMarvel.confirmar(this, "SESIÓN DE JUGADOR",
        "¿Cerrar sesión?", "Vas a volver a la pantalla inicial. ¿Deseas continuar?",
        "CERRAR SESIÓN")) {
        MenuInicial inicio = new MenuInicial(gestor);
        inicio.setVisible(true);
        dispose();
    }
}
}