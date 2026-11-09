package modelo;

public class RegistroAcceso extends RegistroBase {
    private String username;

    public RegistroAcceso(String username) {
        super("Inicio de sesión correcto: " + username);
        this.username = username;
    }
    public String getUsername() { 
        return username; 
    }
}

