package modelo;

public class RegistroPartida extends RegistroBase {
    private Bando ganador;

    public RegistroPartida(Bando ganador, String log) {
        super(log);
        this.ganador = ganador;
    }
    
    public Bando getGanador() { 
        return ganador; 
    }
    
    public String getLog() { 
        return getDescripcion(); 
    }

    public void anonimizarUsuario(String username) {
        if (username == null || username.isEmpty()){
            return;
        }
        String texto = getDescripcion();
        texto = texto.replaceAll("(?i)(?<![\\p{L}\\p{N}_])" + java.util.regex.Pattern.quote(username) + "(?![\\p{L}\\p{N}_])", "[USUARIO ELIMINADO]");
        setDescripcion(texto);
    }
}
