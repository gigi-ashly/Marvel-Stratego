package vista;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import modelo.*;
import logica.*;
import modelo.excepciones.*;

public class VentanaJuego extends JFrame {
    private GestorJugadores gestor;
    private Jugador jugadorHeroes;
    private Jugador jugadorVillanos;
    private Jugador jugadorLogueado;
    private ModoJuego modo;
    private Tablero tablero;
    private TableroVisual tableroVisual;
    private JLabel turnoLabel;
    private JLabel seleccionLabel;
    private JLabel seleccionImagen;
    private JLabel seleccionNombre;
    private JLabel seleccionRango;
    private JLabel seleccionBando;
    private JTextArea historialArea;
    private JPanel vencidasHeroes;
    private JPanel vencidasVillanos;
    private int filaSeleccionada = -1;
    private int columnaSeleccionada = -1;
    private boolean resultadoGuardado = false;
    private Map<String, ImageIcon> cacheIconos = new HashMap<String, ImageIcon>();

    public VentanaJuego(GestorJugadores gestor, Jugador heroes, Jugador villanos, Jugador logueado, ModoJuego modo) {
        this.gestor = gestor;
        this.jugadorHeroes = heroes;
        this.jugadorVillanos = villanos;
        this.jugadorLogueado = logueado;
        this.modo = modo;
        this.tablero = new Tablero();

        setTitle("Marvel Stratego | Batalla | Modo " + modo.name());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1450, 930);
        setMinimumSize(new Dimension(1050, 680));
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        construirInterfaz();
        actualizarInterfaz();
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                confirmarCierreVentana();
            }
        });
    }

    private void construirInterfaz() {
        JPanel fondo = Estilo.fondo();
        fondo.setLayout(new BorderLayout(12,12));
        fondo.setBorder(new EmptyBorder(14,16,14,16));

        JPanel top = new JPanel(new BorderLayout(12,0));
        top.setOpaque(false);
        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos,BoxLayout.Y_AXIS));
        JLabel titulo = Estilo.titulo("MARVEL STRATEGO",22);
        titulo.setForeground(Estilo.DORADO);
        turnoLabel = Estilo.titulo("TURNO: HÉROES",15);
        turnoLabel.setOpaque(true);
        turnoLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        turnoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        seleccionLabel = Estilo.texto("FICHA SELECCIONADA: ninguna");
        seleccionLabel.setFont(new Font("SansSerif",Font.BOLD,11));
        seleccionLabel.setForeground(Estilo.DORADO);
        textos.add(titulo);
        textos.add(Box.createVerticalStrut(6));
        textos.add(turnoLabel);
        textos.add(Box.createVerticalStrut(5));
        textos.add(seleccionLabel);
        top.add(textos,BorderLayout.WEST);

        JPanel controles = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));
        controles.setOpaque(false);
        controles.add(leyendaEquipo("HÉROES", new Color(48,143,215)));
        controles.add(leyendaEquipo("VILLANOS", new Color(196,48,59)));
        JButton retiro = Estilo.boton("RETIRO",Estilo.ROJO);
        retiro.addActionListener(e -> confirmarRetiro());
        controles.add(retiro);
        top.add(controles,BorderLayout.EAST);
        fondo.add(top,BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(12,0));
        centro.setOpaque(false);
        JPanel contenedorTablero = new JPanel(new GridBagLayout());
        contenedorTablero.setOpaque(false);
        tableroVisual = new TableroVisual();
        GridBagConstraints tableroConstraints = new GridBagConstraints();
        tableroConstraints.gridx = 0;
        tableroConstraints.gridy = 0;
        tableroConstraints.weightx = 1.0;
        tableroConstraints.weighty = 1.0;
        tableroConstraints.fill = GridBagConstraints.BOTH;
        contenedorTablero.add(tableroVisual, tableroConstraints);
        centro.add(contenedorTablero,BorderLayout.CENTER);

        JPanel lateral = new JPanel(new GridLayout(4,1,0,9));
        lateral.setOpaque(false);
        lateral.setPreferredSize(new Dimension(285,0));

        JPanel fichaCard = crearPanelFichaSeleccionada();
        lateral.add(fichaCard);

        JPanel logCard = Estilo.tarjeta();
        logCard.setLayout(new BorderLayout(5,5));
        JLabel logTitle = Estilo.titulo("HISTORIAL DE BATALLA",14);
        logCard.add(logTitle,BorderLayout.NORTH);
        historialArea = new JTextArea();
        historialArea.setEditable(false);
        historialArea.setLineWrap(true);
        historialArea.setWrapStyleWord(true);
        historialArea.setBackground(new Color(17,13,15));
        historialArea.setForeground(Estilo.TEXTO);
        historialArea.setFont(new Font("SansSerif",Font.PLAIN,12));
        logCard.add(new JScrollPane(historialArea),BorderLayout.CENTER);
        lateral.add(logCard);

        JPanel heroesCard = crearPanelVencidas("HÉROES VENCIDOS",true);
        JPanel villainsCard = crearPanelVencidas("VILLANOS VENCIDOS",false);
        lateral.add(heroesCard);
        lateral.add(villainsCard);
        centro.add(lateral,BorderLayout.EAST);
        fondo.add(centro,BorderLayout.CENTER);

        JLabel ayuda = new JLabel("<html><b>Selecciona tu ficha y después el destino.</b> Borde AZUL = Héroes; borde ROJO = Villanos; borde DORADO = selección. " +
            "Rango 10 es el máximo; rango 1 vence al 10 y es la única pieza que captura la Tierra; rango 3 destruye bombas.</html>");
        ayuda.setForeground(Estilo.SUAVE);
        ayuda.setFont(new Font("SansSerif",Font.PLAIN,12));
        fondo.add(ayuda,BorderLayout.SOUTH);
        setContentPane(fondo);
    }

    private JPanel crearPanelVencidas(String titulo, boolean heroes) {
        JPanel card = Estilo.tarjeta();
        card.setLayout(new BorderLayout(4,4));
        card.add(Estilo.titulo(titulo,13),BorderLayout.NORTH);
        JPanel contenido = new JPanel(new FlowLayout(FlowLayout.LEFT,4,4));
        contenido.setOpaque(false);
        if (heroes) vencidasHeroes = contenido; else vencidasVillanos = contenido;
        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setBorder(null);
        scroll.setBackground(new Color(17,13,15));
        scroll.getViewport().setBackground(new Color(17,13,15));
        card.add(scroll,BorderLayout.CENTER);
        return card;
    }


    private JLabel leyendaEquipo(String texto, Color color) {
        JLabel l = new JLabel("  " + texto + "  ");
        l.setOpaque(true);
        l.setBackground(new Color(28, 22, 24));
        l.setForeground(Color.WHITE);
        l.setFont(new Font("SansSerif", Font.BOLD, 11));
        l.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(6, 5, 6, 5)));
        return l;
    }


    private JPanel crearPanelFichaSeleccionada() {
        JPanel card = Estilo.tarjeta();
        card.setLayout(new BorderLayout(10, 6));
        card.add(Estilo.titulo("FICHA SELECCIONADA", 13), BorderLayout.NORTH);

        seleccionImagen = new JLabel();
        seleccionImagen.setHorizontalAlignment(SwingConstants.CENTER);
        seleccionImagen.setPreferredSize(new Dimension(88, 88));
        JPanel imagenPanel = new JPanel(new BorderLayout());
        imagenPanel.setOpaque(false);
        imagenPanel.add(seleccionImagen, BorderLayout.CENTER);
        card.add(imagenPanel, BorderLayout.WEST);

        JPanel detalles = new JPanel();
        detalles.setOpaque(false);
        detalles.setLayout(new BoxLayout(detalles, BoxLayout.Y_AXIS));
        seleccionNombre = Estilo.titulo("Ninguna ficha", 14);
        seleccionNombre.setForeground(Color.WHITE);
        seleccionRango = Estilo.texto("Selecciona una pieza");
        seleccionRango.setForeground(Estilo.DORADO);
        seleccionRango.setFont(new Font("SansSerif", Font.BOLD, 12));
        seleccionBando = Estilo.texto("—");
        detalles.add(Box.createVerticalGlue());
        detalles.add(seleccionNombre);
        detalles.add(Box.createVerticalStrut(5));
        detalles.add(seleccionRango);
        detalles.add(Box.createVerticalStrut(5));
        detalles.add(seleccionBando);
        detalles.add(Box.createVerticalGlue());
        card.add(detalles, BorderLayout.CENTER);
        return card;
    }

    private void actualizarFichaSeleccionada() {
        if (seleccionImagen == null) return;
        if (filaSeleccionada < 0) {
            seleccionImagen.setIcon(null);
            seleccionNombre.setText("Ninguna ficha");
            seleccionRango.setText("Selecciona una pieza");
            seleccionBando.setText("—");
            return;
        }
        Pieza pieza = tablero.getPieza(filaSeleccionada, columnaSeleccionada);
        if (pieza == null) return;
        boolean visible = modo == ModoJuego.TUTORIAL
            || pieza.getBando() == tablero.getTurno() || pieza.isRevelada();
        seleccionImagen.setIcon(icono(visible ? pieza.getImagenKey() : "reverso", 78));
        seleccionNombre.setText(visible ? pieza.getNombre() : "Identidad oculta");
        if (visible) {
            if (pieza.getTipo() == TipoPieza.TIERRA) seleccionRango.setText("TIERRA");
            else if (pieza.getTipo() == TipoPieza.BOMBA) seleccionRango.setText("BOMBA");
            else seleccionRango.setText("RANGO " + pieza.getRango());
        } else {
            seleccionRango.setText("Rango desconocido");
        }
        boolean heroes = pieza.getBando() == Bando.HEROES;
        seleccionBando.setText(heroes ? "HÉROES" : "VILLANOS");
        seleccionBando.setForeground(heroes
            ? new Color(105, 183, 238) : new Color(238, 112, 122));
    }

    private void actualizarInterfaz() {
        String nombreTurno = tablero.getTurno() == Bando.HEROES ?
            jugadorHeroes.getUsername() : jugadorVillanos.getUsername();
        Color colorTurno = tablero.getTurno() == Bando.HEROES
            ? new Color(29, 91, 145) : new Color(137, 29, 43);
        turnoLabel.setText("TURNO " + tablero.getTurno() + "  •  " + nombreTurno);
        turnoLabel.setBackground(colorTurno);
        turnoLabel.setForeground(Color.WHITE);
        turnoLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(colorTurno.brighter(), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        if (filaSeleccionada >= 0) {
            Pieza seleccionada = tablero.getPieza(filaSeleccionada, columnaSeleccionada);
            if (seleccionada != null) {
                seleccionLabel.setText("SELECCIÓN: " + seleccionada.getNombreVisible()
                    + "  •  " + (seleccionada.getBando() == Bando.HEROES ? "HÉROE" : "VILLANO"));
            }
        } else {
            seleccionLabel.setText("FICHA SELECCIONADA: ninguna");
        }
        actualizarFichaSeleccionada();
        tableroVisual.repaint();
        historialArea.setText("");
        List<String> logs = tablero.getHistorial();
        for (int i = Math.max(0,logs.size()-35); i < logs.size(); i++)
            historialArea.append("• " + logs.get(i) + "\n");
        actualizarVencidas();
    }

    private void actualizarVencidas() {
        vencidasHeroes.removeAll();
        vencidasVillanos.removeAll();
        List<Pieza> piezas = tablero.getVencidas();
        for (int i=0; i<piezas.size(); i++) {
            Pieza p = piezas.get(i);
            JLabel chip = new JLabel(icono(p.getImagenKey(),52));
            chip.setToolTipText(p.getNombreVisible());
            if (p.getBando() == Bando.HEROES) vencidasHeroes.add(chip);
            else vencidasVillanos.add(chip);
        }
        vencidasHeroes.revalidate(); vencidasHeroes.repaint();
        vencidasVillanos.revalidate(); vencidasVillanos.repaint();
    }

    private ImageIcon icono(String key, int size) {
        String cacheKey = key + "_" + size;
        if (cacheIconos.containsKey(cacheKey)) return cacheIconos.get(cacheKey);
        String path = key.equals("reverso") ? "/recursos/fichas/reverso.png" :
            "/recursos/fichas/" + key + ".png";
        try {
            java.net.URL url = getClass().getResource(path);
            if (url == null) return null;
            ImageIcon original = new ImageIcon(url);
            Image scaled = original.getImage().getScaledInstance(size,size,Image.SCALE_SMOOTH);
            ImageIcon resultado = new ImageIcon(scaled);
            cacheIconos.put(cacheKey,resultado);
            return resultado;
        } catch (Exception ex) {
            return null;
        }
    }

    private void clickCasilla(int f, int c) {
        if (tablero.isTerminado()) return;
        Pieza p = tablero.getPieza(f,c);
        if (filaSeleccionada < 0) {
            if (p == null || p.getBando() != tablero.getTurno()) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            filaSeleccionada = f;
            columnaSeleccionada = c;
            actualizarInterfaz();
            return;
        }
        if (p != null && p.getBando() == tablero.getTurno()) {
            filaSeleccionada = f;
            columnaSeleccionada = c;
            actualizarInterfaz();
            return;
        }

        try {
            tablero.mover(filaSeleccionada,columnaSeleccionada,f,c);
            filaSeleccionada = -1;
            columnaSeleccionada = -1;
            actualizarInterfaz();
            if (tablero.isTerminado()) finalizarPartida();
        } catch (MovimientoInvalidoException ex) {
            DialogoMarvel.mensaje(this,"MOVIMIENTO","Movimiento no válido",ex.getMessage());
            filaSeleccionada = -1; columnaSeleccionada = -1; actualizarInterfaz();
        } catch (AtaqueInvalidoException ex) {
            DialogoMarvel.mensaje(this,"COMBATE","Ataque no válido",ex.getMessage());
            filaSeleccionada = -1; columnaSeleccionada = -1; actualizarInterfaz();
        }
    }


    private void confirmarRetiro() {
        if (confirmarAbandono()) {
            tablero.retirar(tablero.getTurno());
            finalizarPartida();
        }
    }

    private void confirmarCierreVentana() {
        if (tablero.isTerminado()) {
            dispose();
            new MenuPrincipal(gestor, jugadorLogueado).setVisible(true);
            return;
        }
        if (confirmarAbandono()) {
            tablero.retirar(tablero.getTurno());
            finalizarPartida();
        }
    }

    private boolean confirmarAbandono() {
        String actual = tablero.getTurno() == Bando.HEROES
            ? jugadorHeroes.getUsername() : jugadorVillanos.getUsername();
        return DialogoMarvel.confirmar(this, "RENDICIÓN",
            "¿Retirarse de la batalla?",
            actual + ", si abandonas ahora el oponente gana y recibe 3 puntos.",
            "CONFIRMAR RETIRO");
    }

    private void finalizarPartida() {
        if (resultadoGuardado) return;
        resultadoGuardado = true;
        Bando ganador = tablero.getGanador();
        Jugador ganadorJugador = ganador == Bando.HEROES ? jugadorHeroes : jugadorVillanos;
        Jugador perdedorJugador = ganador == Bando.HEROES ? jugadorVillanos : jugadorHeroes;
        String log = GeneradorLogPartida.crear(jugadorHeroes, jugadorVillanos, ganador,
            tablero.getMotivoFinal(), new java.util.Date());
        gestor.registrarResultado(jugadorHeroes, jugadorVillanos, ganador,
            tablero.getMotivoFinal(), log);
        mostrarResultado(ganador, ganadorJugador, perdedorJugador, log);
    }


    private void mostrarResultado(Bando ganador, Jugador ganadorJugador,
                                  Jugador perdedorJugador, String log) {
        DialogoMarvel d = new DialogoMarvel(this, "FIN DE LA PARTIDA",
            "¡Victoria!", "La batalla por la Tierra ha llegado a su fin.", 730, 575);

        JPanel contenido = new JPanel(new BorderLayout(0, 15));
        contenido.setOpaque(false);

        JLabel cinta = new JLabel(ganador == Bando.HEROES
            ? "HÉROES · PROTECTORES DE LA TIERRA"
            : "VILLANOS · DOMINIO DEL UNIVERSO", SwingConstants.CENTER);
        cinta.setOpaque(true);
        cinta.setBackground(ganador == Bando.HEROES
            ? new Color(25, 83, 132) : new Color(133, 27, 43));
        cinta.setForeground(Color.WHITE);
        cinta.setFont(new Font("SansSerif", Font.BOLD, 14));
        cinta.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
        contenido.add(cinta, BorderLayout.NORTH);

        JPanel centro = new JPanel();
        centro.setOpaque(false);
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));

        JLabel ganadorNombre = Estilo.titulo(ganadorJugador.getUsername().toUpperCase(), 30);
        ganadorNombre.setForeground(Estilo.DORADO);
        ganadorNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel subtitulo = Estilo.titulo("HA GANADO LA PARTIDA", 16);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        int puntosOtorgados = tablero.getMotivoFinal() == MotivoFin.RETIRO
            ? GestorJugadores.PUNTOS_RETIRO : GestorJugadores.PUNTOS_VICTORIA;
        JLabel puntos = Estilo.texto("+" + puntosOtorgados + " PUNTOS PARA " + ganadorJugador.getUsername().toUpperCase());
        puntos.setForeground(Estilo.DORADO);
        puntos.setFont(new Font("SansSerif", Font.BOLD, 13));
        puntos.setAlignmentX(Component.CENTER_ALIGNMENT);

        centro.add(Box.createVerticalStrut(6));
        centro.add(ganadorNombre);
        centro.add(Box.createVerticalStrut(4));
        centro.add(subtitulo);
        centro.add(Box.createVerticalStrut(7));
        centro.add(puntos);
        centro.add(Box.createVerticalStrut(17));

        JPanel enfrentamiento = new JPanel(new GridLayout(1, 2, 12, 0));
        enfrentamiento.setOpaque(false);
        enfrentamiento.add(panelResultadoJugador("GANADOR", ganadorJugador, ganador));
        enfrentamiento.add(panelResultadoJugador("OPONENTE", perdedorJugador,
            ganador == Bando.HEROES ? Bando.VILLANOS : Bando.HEROES));
        enfrentamiento.setMaximumSize(new Dimension(Integer.MAX_VALUE, 112));
        centro.add(enfrentamiento);
        centro.add(Box.createVerticalStrut(16));

        contenido.add(centro, BorderLayout.CENTER);

        JPanel detallePanel = new JPanel(new BorderLayout(0, 5));
        detallePanel.setOpaque(true);
        detallePanel.setBackground(new Color(28, 20, 22));
        detallePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(105, 64, 65)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        JLabel detalleTitulo = Estilo.etiqueta("DETALLE DE LA PARTIDA");
        detalleTitulo.setFont(new Font("SansSerif", Font.BOLD, 10));
        detallePanel.add(detalleTitulo, BorderLayout.NORTH);

        JTextArea detalle = new JTextArea(log);
        detalle.setEditable(false);
        detalle.setLineWrap(true);
        detalle.setWrapStyleWord(true);
        detalle.setRows(2);
        detalle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        detalle.setForeground(new Color(230, 219, 211));
        detalle.setBackground(new Color(28, 20, 22));
        detalle.setBorder(BorderFactory.createEmptyBorder(2, 1, 2, 1));
        detallePanel.add(detalle, BorderLayout.CENTER);
        detallePanel.setPreferredSize(new Dimension(620, 76));
        contenido.add(detallePanel, BorderLayout.SOUTH);

        d.getCuerpo().add(contenido, BorderLayout.CENTER);
        d.agregarBoton("VOLVER AL MENÚ", Estilo.ROJO, e -> {
            MenuPrincipal menu = new MenuPrincipal(gestor, jugadorLogueado);
            menu.setVisible(true);
            d.dispose();
            dispose();
        });
        d.setVisible(true);
    }

    private JPanel panelResultadoJugador(String titulo, Jugador jugador, Bando bando) {
        JPanel panel = Estilo.tarjetaSuave();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel rol = Estilo.etiqueta(titulo);
        rol.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel nombre = Estilo.titulo(jugador.getUsername(), 16);
        nombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel equipo = Estilo.texto(bando == Bando.HEROES ? "HÉROES" : "VILLANOS");
        equipo.setForeground(bando == Bando.HEROES
            ? new Color(105, 183, 238) : new Color(238, 112, 122));
        equipo.setFont(new Font("SansSerif", Font.BOLD, 12));
        equipo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(rol);
        panel.add(Box.createVerticalStrut(9));
        panel.add(nombre);
        panel.add(Box.createVerticalStrut(5));
        panel.add(equipo);
        return panel;
    }


    private class TableroVisual extends JPanel {
        private JButton[][] celdas = new JButton[10][10];
        private BufferedImage imagenFondo;
        private int tableroX;
        private int tableroY;
        private int tableroLado;

        TableroVisual() {
            setLayout(null);
            Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
            int lado = Math.min(780, Math.min(pantalla.width - 350, pantalla.height - 150));
            lado = Math.max(540, lado);
            setPreferredSize(new Dimension(lado,lado));
            setMinimumSize(new Dimension(500,500));
            try {
                imagenFondo = ImageIO.read(getClass().getResource("/recursos/tablero_cuadricula.png"));
            } catch (Exception ex) {
                imagenFondo = null;
            }
            setOpaque(false);
            for (int f=0; f<10; f++) for (int c=0; c<10; c++) {
                final int ff=f, cc=c;
                JButton b = new JButton();
                b.setOpaque(false);
                b.setContentAreaFilled(false);
                b.setBorderPainted(false);
                b.setFocusPainted(false);
                b.setMargin(new Insets(0,0,0,0));
                b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                b.addActionListener(e -> clickCasilla(ff,cc));
                celdas[f][c] = b;
                add(b);
            }
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2=(Graphics2D)g.create();
            tableroLado = Math.min(getWidth(), getHeight());
            tableroX = (getWidth() - tableroLado) / 2;
            tableroY = (getHeight() - tableroLado) / 2;
            if (imagenFondo != null) {
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2.drawImage(imagenFondo,tableroX,tableroY,tableroLado,tableroLado,this);
            } else {
                g2.setColor(new Color(24,20,21));
                g2.fillRect(tableroX,tableroY,tableroLado,tableroLado);
            }
            if (filaSeleccionada >= 0) {
                Rectangle r=celdas[filaSeleccionada][columnaSeleccionada].getBounds();
                g2.setColor(new Color(255,204,65,75));
                g2.fillRoundRect(r.x+3,r.y+3,r.width-6,r.height-6,14,14);
            }
            g2.dispose();
        }

        public void doLayout() {
            tableroLado = Math.min(getWidth(), getHeight());
            tableroX = (getWidth() - tableroLado) / 2;
            tableroY = (getHeight() - tableroLado) / 2;
            int cw = tableroLado / 10;
            int ch = tableroLado / 10;
            for (int f=0; f<10; f++) for (int c=0; c<10; c++) {
                int x = tableroX + c*cw;
                int y = tableroY + f*ch;
                int w = (c==9) ? tableroX + tableroLado - x : cw;
                int h = (f==9) ? tableroY + tableroLado - y : ch;
                celdas[f][c].setBounds(x,y,w,h);
            }
            pintarFichas();
        }

        private void pintarFichas() {
            for (int f=0; f<10; f++) for (int c=0; c<10; c++) {
                JButton b=celdas[f][c];
                Pieza p=tablero.getPieza(f,c);
                b.setIcon(null);
                b.setToolTipText(null);
                b.setBorderPainted(false);
                b.setBorder(null);
                if (p != null) {
                    boolean propia=p.getBando()==tablero.getTurno();
                    boolean visible=modo==ModoJuego.TUTORIAL || propia || p.isRevelada();
                    String key=visible?p.getImagenKey():"reverso";
                    int base=Math.max(38,Math.min(70,Math.min(b.getWidth(),b.getHeight())-5));
                    boolean seleccionada=(filaSeleccionada==f && columnaSeleccionada==c);
                    b.setIcon(icono(key,seleccionada?Math.min(base+8,70):base));
                    b.setToolTipText(visible
                        ? "<html><b>" + p.getNombre() + "</b><br>"
                            + (p.getBando() == Bando.HEROES ? "HÉROES" : "VILLANOS")
                            + "<br>" + (p.getRango() == 0 ? p.getTipo().name() : "RANGO " + p.getRango())
                            + "</html>"
                        : "Identidad oculta · " + (p.getBando() == Bando.HEROES ? "HÉROES" : "VILLANOS"));
                }
            }
        }

        protected void paintChildren(Graphics g) {
            pintarFichas();
            super.paintChildren(g);
            Graphics2D equipos = (Graphics2D) g.create();
            equipos.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            for (int f = 0; f < 10; f++) {
                for (int c = 0; c < 10; c++) {
                    Pieza p = tablero.getPieza(f, c);
                    if (p == null) continue;
                    Rectangle r = celdas[f][c].getBounds();
                    Color color = p.getBando() == Bando.HEROES
                        ? new Color(39, 139, 214) : new Color(204, 48, 62);
                    equipos.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 220));
                    equipos.setStroke(new BasicStroke(2.2f));
                    equipos.drawRoundRect(r.x + 2, r.y + 2, r.width - 5, r.height - 5, 8, 8);
                    equipos.setColor(color);
                    equipos.fillRoundRect(r.x + r.width - 18, r.y + 3, 14, 13, 5, 5);
                    equipos.setColor(Color.WHITE);
                    equipos.setFont(new Font("SansSerif", Font.BOLD, 9));
                    equipos.drawString(p.getBando() == Bando.HEROES ? "H" : "V",
                        r.x + r.width - 14, r.y + 13);
                }
            }
            equipos.dispose();
            if (filaSeleccionada>=0) {
                JButton b=celdas[filaSeleccionada][columnaSeleccionada];
                Rectangle r=b.getBounds();
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255,205,65,115));
                g2.setStroke(new BasicStroke(8f));
                g2.drawRoundRect(r.x+5,r.y+5,r.width-10,r.height-10,15,15);
                g2.setColor(new Color(255,241,177));
                g2.setStroke(new BasicStroke(3.5f));
                g2.drawRoundRect(r.x+5,r.y+5,r.width-10,r.height-10,15,15);
                g2.dispose();
            }
        }
    }

}
