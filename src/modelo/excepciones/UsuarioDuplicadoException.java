package modelo.excepciones;

public class UsuarioDuplicadoException extends RegistroInvalidoException {
    public UsuarioDuplicadoException(String mensaje) { 
        super(mensaje); 
    }
}
