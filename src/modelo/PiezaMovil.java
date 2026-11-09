package modelo;

import modelo.interfaces.Combatiente;

public class PiezaMovil extends Pieza {
    public static final int RANGO_AGENTE = 1;
    public static final int RANGO_MISTICO = 2;
    public static final int RANGO_SCOUT = 3;
    public static final int RANGO_MAXIMO = 10;

    public PiezaMovil(Bando bando, int rango, String nombre, String imagenKey) {
        super(bando, TipoPieza.NORMAL, rango, nombre, imagenKey);
    }

    public boolean puedeMoverse() { 
        return true; 
    }

    public boolean mueveVariasCasillas() { 
        return getRango() == RANGO_MISTICO;
    }

    public String getNombreVisible() { 
        return getNombre() + " (Rango " + getRango() + ")";
    }

    public ResultadoCombate recibirAtaque(Pieza atacante) {
        Combatiente combatiente = atacante;
        int poder = combatiente.atacar();
        if (poder == RANGO_AGENTE && getRango() == RANGO_MAXIMO) {
            return ResultadoCombate.GANA;
        }
        if (poder > getRango()){
            return ResultadoCombate.GANA;
        }
        if (poder < getRango()){
            return ResultadoCombate.PIERDE;
        }
        return ResultadoCombate.EMPATE;
    }
}
