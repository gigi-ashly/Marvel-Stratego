package modelo;

import modelo.interfaces.Combatiente;

public class PiezaFija extends Pieza {
    public PiezaFija(Bando bando, TipoPieza tipo, String nombre, String imagenKey) {
        super(bando, tipo, 0, nombre, imagenKey);
    }

    public boolean puedeMoverse() {
        return false; 
    }

    public String getNombreVisible() { 
        return getNombre(); 
    }

    public ResultadoCombate recibirAtaque(Pieza atacante) {
        Combatiente combatiente = atacante;
        if (getTipo() == TipoPieza.TIERRA && atacante.getRango() != PiezaMovil.RANGO_AGENTE){
            return ResultadoCombate.PIERDE;
        }
        if (getTipo() == TipoPieza.BOMBA && combatiente.atacar() != PiezaMovil.RANGO_SCOUT){
            return ResultadoCombate.PIERDE;
        }
        return ResultadoCombate.GANA;
    }
}
