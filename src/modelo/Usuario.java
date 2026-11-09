package modelo;

import modelo.excepciones.CredencialesInvalidasException;
import modelo.excepciones.PasswordInvalidoException;

public class Usuario {
    public static final int LONGITUD_PASSWORD = 5;

    private String username;
    private String password;

    public Usuario(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public String getUsername() { 
        return username; 
    }
    
    public boolean validarPassword(String clave) {
        return password != null && password.equals(clave);
    }
    public void cambiarPassword(String actual, String nueva) throws CredencialesInvalidasException, PasswordInvalidoException {
        if (!validarPassword(actual)){
            throw new CredencialesInvalidasException("El password actual no coincide.");
        }
        if (nueva == null || nueva.length() != LONGITUD_PASSWORD)
            throw new PasswordInvalidoException("El nuevo password debe tener exactamente " + LONGITUD_PASSWORD + " caracteres.");
        password = nueva;
    }
}