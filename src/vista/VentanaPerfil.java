package vista;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import modelo.Jugador;

/* Datos del jugador (sin password) e historial de partidas, del más reciente al más viejo. */
public class VentanaPerfil extends JDialog {

    public VentanaPerfil(JFrame padre, Jugador jugador) {
        super(padre, "Mi perfil | " + jugador.getUsername(), true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(640, 600);
        setMinimumSize(new Dimension(560, 520));
        setLocationRelativeTo(padre);

        MarvelArtworkPanel fondo = new MarvelArtworkPanel(false, true);
        fondo.setLayout(new BorderLayout(0, 14));
        fondo.setBorder(new EmptyBorder(20, 24, 20, 24));

        JPanel datos = Estilo.tarjeta();
        datos.setBackground(new Color(19, 15, 17));
        datos.setLayout(new GridLayout(0, 2, 10, 8));
        JLabel titulo = Estilo.titulo(jugador.getUsername().toUpperCase(), 22);
        titulo.setForeground(Estilo.DORADO);
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setOpaque(false);
        cabecera.add(titulo, BorderLayout.NORTH);
        cabecera.add(Box.createVerticalStrut(10), BorderLayout.CENTER);
        agregarDato(datos, "PUNTOS", String.valueOf(jugador.getPuntos()));
        agregarDato(datos, "VICTORIAS", String.valueOf(jugador.getVictorias()));
        agregarDato(datos, "PARTIDAS CON HÉROES", String.valueOf(jugador.getPartidasHeroes()));
        agregarDato(datos, "DERROTAS", String.valueOf(jugador.getDerrotas()));
        agregarDato(datos, "PARTIDAS CON VILLANOS", String.valueOf(jugador.getPartidasVillanos()));
        agregarDato(datos, "PARTIDAS JUGADAS", String.valueOf(jugador.getPartidasHeroes() + jugador.getPartidasVillanos()));

        JPanel arriba = new JPanel(new BorderLayout(0, 10));
        arriba.setOpaque(false);
        arriba.add(cabecera, BorderLayout.NORTH);
        arriba.add(datos, BorderLayout.CENTER);
        fondo.add(arriba, BorderLayout.NORTH);

        JPanel historial = Estilo.tarjeta();
        historial.setLayout(new BorderLayout(0, 8));
        historial.add(Estilo.titulo("LOG DE MIS ÚLTIMOS JUEGOS", 14), BorderLayout.NORTH);
        JTextArea area = new JTextArea(textoHistorial(jugador.getHistorial()));
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBackground(new Color(17, 13, 15));
        area.setForeground(Estilo.TEXTO);
        area.setFont(new Font("SansSerif", Font.PLAIN, 12));
        area.setBorder(new EmptyBorder(8, 8, 8, 8));
        area.setCaretPosition(0);
        historial.add(new JScrollPane(area), BorderLayout.CENTER);
        fondo.add(historial, BorderLayout.CENTER);

        JButton cerrar = Estilo.boton("CERRAR", new Color(65, 55, 55));
        cerrar.addActionListener(e -> dispose());
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pie.setOpaque(false);
        pie.add(cerrar);
        fondo.add(pie, BorderLayout.SOUTH);
        setContentPane(fondo);
    }

    private void agregarDato(JPanel panel, String etiqueta, String valor) {
        JLabel texto = Estilo.texto(etiqueta + ":  " + valor);
        texto.setFont(new Font("SansSerif", Font.BOLD, 13));
        texto.setForeground(Estilo.TEXTO);
        panel.add(texto);
    }

    private String textoHistorial(List<String> lista) {
        if (lista.isEmpty()) return "Todavía no tienes partidas registradas.";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lista.size(); i++)
            sb.append(i + 1).append(". ").append(lista.get(i)).append("\n\n");
        return sb.toString();
    }
}