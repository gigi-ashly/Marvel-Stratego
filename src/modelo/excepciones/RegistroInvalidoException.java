package modelo.excepciones;

public class RegistroInvalidoException extends Exception {
    public RegistroInvalidoException(String mensaje) { 
        super(mensaje); 
    }
}
