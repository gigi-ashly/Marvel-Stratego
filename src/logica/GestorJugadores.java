
package logica;
import modelo.Jugador;
/**
 *
 * @author gigiw
 */
public class GestorJugadores {
    private Jugador[] jugadores;
    private int cantidadJugadores;
    
    public GestorJugadores(){
        jugadores = new Jugador [100];
        cantidadJugadores = 0;
    }
    
    public Jugador buscarJugador(String username) {
        for (int i = 0; i < cantidadJugadores; i++) {
            if (jugadores[i].getUsername().equalsIgnoreCase(username)) {
                return jugadores[i];
            }
        }
        return null;
    }
}
