package modelo.excepciones;

public class PasswordInvalidoException extends RegistroInvalidoException {
    public PasswordInvalidoException(String mensaje) {
        super(mensaje);
    }
}

