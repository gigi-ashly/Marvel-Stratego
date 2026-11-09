package modelo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RegistroBase {
    private String descripcion;
    private String fecha;

    public RegistroBase(String descripcion) {
        this.descripcion = descripcion;
        this.fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
    }
    
    public String getDescripcion() { 
        return descripcion; 
    }
    
    protected void setDescripcion(String descripcion) { 
        this.descripcion = descripcion; 
    }
    
    public String getFecha() { 
        return fecha; 
    }
}
