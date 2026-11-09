package modelo;

import modelo.interfaces.Combatiente;
import modelo.interfaces.Movible;

public abstract class Pieza implements Movible, Combatiente {
    private Bando bando;
    private TipoPieza tipo;
    private int rango;
    private String nombre;
    private String imagenKey;
    private boolean revelada;
    private int fila;
    private int columna;

    public Pieza(Bando bando, TipoPieza tipo, int rango, String nombre, String imagenKey) {
        this.bando = bando;
        this.tipo = tipo;
        this.rango = rango;
        this.nombre = nombre;
        this.imagenKey = imagenKey;
        this.revelada = false;
        this.fila = -1;
        this.columna = -1;
    }
    
    public Bando getBando() { 
        return bando; 
    }
    
    public TipoPieza getTipo() { 
        return tipo; 
    }
    public int getRango() {
        return rango; 
    }
    
    public String getNombre() {
        return nombre; 
    }
    
    public String getImagenKey() { 
        return imagenKey; 
    }
    
    public boolean isRevelada() { 
        return revelada; 
    }
    
    public void revelar() { 
        revelada = true; 
    }
    
    public void ocultar() { 
        revelada = false; 
    }
    
    public int getFila() {
        return fila; 
    }
    
    public int getColumna() { 
        return columna; 
    }
    
    public void setPosicion(int f, int c) { 
        fila = f; 
        columna = c; 
    }
    
    public int atacar() { 
        return rango; 
    }
    
    public boolean mueveVariasCasillas() { 
        return false; 
    }

    public abstract String getNombreVisible();

    public abstract ResultadoCombate recibirAtaque(Pieza atacante);
}

