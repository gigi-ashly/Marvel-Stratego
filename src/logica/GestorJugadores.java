package logica;

import java.util.ArrayList;
import java.util.List;
import modelo.Bando;
import modelo.Jugador;
import modelo.RegistroPartida;
import modelo.RegistroAcceso;
import modelo.MotivoFin;
import modelo.excepciones.CredencialesInvalidasException;
import modelo.excepciones.PasswordInvalidoException;
import modelo.excepciones.RegistroInvalidoException;
import modelo.excepciones.UsuarioDuplicadoException;

public class GestorJugadores {

    public static final int PUNTOS_VICTORIA = 5;
    public static final int PUNTOS_RETIRO = 3;

    private Jugador[] jugadores;
    private int cantidadJugadores;
    private int usuariosHistoricos;

    private List<RegistroPartida> partidas;
    private List<RegistroAcceso> accesos;

    public GestorJugadores() {
        jugadores = new Jugador[100];
        cantidadJugadores = 0;
        usuariosHistoricos = 0;

        partidas = new ArrayList<RegistroPartida>();
        accesos = new ArrayList<RegistroAcceso>();
    }

    public Jugador buscarJugador(String username) {
        if (username == null) {
            return null;
        }
        for (int i = 0; i < cantidadJugadores; i++) {
            if (jugadores[i].getUsername().equalsIgnoreCase(username.trim())) {
                return jugadores[i];
            }
        }
        return null;
    }

    public void registrarJugadorValidado(String username, String password) throws RegistroInvalidoException {
        if (username == null || username.trim().isEmpty()) {
            throw new RegistroInvalidoException("Escribe un nombre de usuario.");
        }
        if (password == null || password.length() != 5) {
            throw new PasswordInvalidoException("La contraseña debe tener exactamente 5 caracteres.");
        }
        if (buscarJugador(username) != null) {
            throw new UsuarioDuplicadoException("Ese nombre de usuario ya está registrado.");
        }
        if (cantidadJugadores >= jugadores.length) {
            throw new RegistroInvalidoException("Se alcanzó el límite de jugadores.");
        }
        jugadores[cantidadJugadores] = new Jugador(username.trim(), password);
        cantidadJugadores++;
        usuariosHistoricos++;
    }

    public Jugador iniciarSesion(String username, String password) throws CredencialesInvalidasException {
        Jugador jugador = buscarJugador(username);
        if (jugador == null || !jugador.validarPassword(password)) {
            throw new CredencialesInvalidasException("El usuario o la contraseña son incorrectos.");
        }
        accesos.add(new RegistroAcceso(jugador.getUsername()));
        return jugador;
    }

    public boolean eliminarJugador(Jugador jugador) {
        if (jugador == null) {
            return false;
        }
        String username = jugador.getUsername();
        for (int i = 0; i < cantidadJugadores; i++) {
            if (jugadores[i] == jugador) {
                for (int j = i; j < cantidadJugadores - 1; j++) {
                    jugadores[j] = jugadores[j + 1];
                }
                cantidadJugadores--;
                jugadores[cantidadJugadores] = null;
                for (int j = accesos.size() - 1; j >= 0; j--) {
                    if (accesos.get(j).getUsername().equalsIgnoreCase(username)) {
                        accesos.remove(j);
                    }
                }

                for (int j = 0; j < partidas.size(); j++) {
                    partidas.get(j).anonimizarUsuario(username);
                }

                for (int j = 0; j < cantidadJugadores; j++) {
                    jugadores[j].anonimizarReferencia(username);
                }
                return true;
            }
        }
        return false;
    }

    public void registrarResultado(Jugador heroes, Jugador villanos, Bando ganador, MotivoFin motivo, String log) {
        int puntosGanados;

        if (motivo == MotivoFin.RETIRO) {
            puntosGanados = PUNTOS_RETIRO;
        } else {
            puntosGanados = PUNTOS_VICTORIA;
        }
        heroes.registrarPartida(Bando.HEROES, ganador == Bando.HEROES, log, puntosGanados);
        villanos.registrarPartida(Bando.VILLANOS, ganador == Bando.VILLANOS, log, puntosGanados);
        partidas.add(new RegistroPartida(ganador, log));
    }

    public int getCantidadJugadores() {
        return cantidadJugadores;
    }

    public int getUsuariosHistoricos() {
        return usuariosHistoricos;
    }

    public int getCantidadPartidas() {
        return partidas.size();
    }

    public int contarVictorias(Bando bando) {
        return contarVictoriasRecursivo(0, bando);
    }

    private int contarVictoriasRecursivo(int indice, Bando bando) {
        if (indice >= partidas.size()) {
            return 0;
        }
        int actual = 0;
        if (partidas.get(indice).getGanador() == bando) {
            actual = 1;
        }
        return actual + contarVictoriasRecursivo(indice + 1, bando);
    }

    public List<Jugador> obtenerRanking() {
        List<Jugador> ordenados = new ArrayList<Jugador>();
        for (int i = 0; i < cantidadJugadores; i++) {
            insertarRankingRecursivo(ordenados, jugadores[i], 0);
        }
        return ordenados;
    }
}