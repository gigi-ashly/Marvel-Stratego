package logica;
import java.text.SimpleDateFormat;
import java.util.Date;
import modelo.Bando;
import modelo.Jugador;
import modelo.MotivoFin;

public final class GeneradorLogPartida {

    private GeneradorLogPartida() {
        
    }

    public static String crear(Jugador heroes, Jugador villanos, Bando ganador, MotivoFin motivo, Date fechaPartida) {
        Jugador ganadorJugador;
        Jugador perdedorJugador;
        Bando perdedor;

        if (ganador == Bando.HEROES) {
            ganadorJugador = heroes;
            perdedorJugador = villanos;
            perdedor = Bando.VILLANOS;
        } else {
            ganadorJugador = villanos;
            perdedorJugador = heroes;
            perdedor = Bando.HEROES;
        }

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String fecha = formato.format(fechaPartida);

        if (motivo == MotivoFin.SIN_MOVIMIENTOS) {
            return perdedorJugador.getUsername() + " usando " + perdedor + " ha perdido por no tener movimientos válidos disponibles ante " + ganadorJugador.getUsername() + " — " + fecha;
        }

        if (motivo == MotivoFin.RETIRO) {
            return ganadorJugador.getUsername() + " usando " + ganador + " ha ganado ya que " + perdedorJugador.getUsername() + " usando " + perdedor + " se ha retirado del juego — " + fecha;
        }

        String accion;
        if (ganador == Bando.HEROES) {
            accion = "SALVADO";
        } else {
            accion = "CAPTURADO";
        }

        return ganadorJugador.getUsername() + " usando " + ganador + " ha " + accion + " la TIERRA! Venciendo a " + perdedorJugador.getUsername() + " — " + fecha;
    }
}