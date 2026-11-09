package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Jugador extends Usuario {
    private int puntos;
    private int partidasHeroes;
    private int partidasVillanos;
    private int victorias;
    private int derrotas;
    private List<String> historial;

    public Jugador(String username, String password) {
        super(username, password);

        puntos = 0;
        partidasHeroes = 0;
        partidasVillanos = 0;
        victorias = 0;
        derrotas = 0;
        historial = new ArrayList<String>();
    }

    public int getPuntos() {
        return puntos;
    }

    public int getPartidasHeroes() {
        return partidasHeroes;
    }

    public int getPartidasVillanos() {
        return partidasVillanos;
    }

    public int getVictorias() {
        return victorias;
    }

    public int getDerrotas() {
        return derrotas;
    }

    public List<String> getHistorial() {
        return Collections.unmodifiableList(historial);
    }

    public void anonimizarReferencia(String username) {
        if (username == null || username.isEmpty()) {
            return;
        }

        for (int i = 0; i < historial.size(); i++) {
            String texto = historial.get(i);
            texto = texto.replaceAll("(?i)(?<![\\p{L}\\p{N}_])" + java.util.regex.Pattern.quote(username) + "(?![\\p{L}\\p{N}_])", "[USUARIO ELIMINADO]");
            historial.set(i, texto);
        }
    }

    public void registrarPartida(Bando bando, boolean gano, String log, int puntosGanados) {
        if (bando == Bando.HEROES) {
            partidasHeroes++;
        } else {
            partidasVillanos++;
        }
        if (gano) {
            victorias++;
            puntos += puntosGanados;
        } else {
            derrotas++;
        }
        historial.add(0, log);
    }
}