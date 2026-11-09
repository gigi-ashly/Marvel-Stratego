package logica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import modelo.*;
import modelo.excepciones.AtaqueInvalidoException;
import modelo.excepciones.MovimientoInvalidoException;
import modelo.interfaces.Movible;

public class Tablero {
    private Pieza[][] celdas = new Pieza[10][10];
    private Bando turno = Bando.HEROES;
    private List<Pieza> vencidas = new ArrayList<Pieza>();
    private List<String> historial = new ArrayList<String>();
    private Random random = new Random();
    private boolean terminado;
    private Bando ganador;
    private MotivoFin motivoFinal;

    public Tablero() {
        colocarEjercito(Bando.VILLANOS);
        colocarEjercito(Bando.HEROES);
    }

    private void colocarEjercito(Bando bando) {
        boolean heroes = false;
        if (bando == Bando.HEROES) {
            heroes = true;
        }
        int filaFondo;
        int filaFondo2;
        int filaMedia;
        int filaFrente;
        if (heroes) {
            filaFondo = 9;
            filaFondo2 = 8;
            filaMedia = 7;
            filaFrente = 6;
        } else {
            filaFondo = 0;
            filaFondo2 = 1;
            filaMedia = 2;
            filaFrente = 3;
        }

        List<Pieza> piezas = ConfiguracionEjercitos.crearPiezas(bando, random);

        int colTierra = 1 + random.nextInt(8);

        colocar(extraerTipo(piezas, TipoPieza.TIERRA), filaFondo, colTierra);
        colocar(extraerTipo(piezas, TipoPieza.BOMBA), filaFondo, colTierra - 1);
        colocar(extraerTipo(piezas, TipoPieza.BOMBA), filaFondo, colTierra + 1);
        colocar(extraerTipo(piezas, TipoPieza.BOMBA), filaFondo2, colTierra);

        Pieza bomba;

        while ((bomba = extraerTipo(piezas, TipoPieza.BOMBA)) != null) {
            int filaMin = Math.min(filaFondo, filaFondo2);
            int filaMax = Math.max(filaFondo, filaFondo2);
            colocarAleatoria(bomba, filaMin, filaMax);
        }

        Pieza mistico;

        while ((mistico = extraerRango(piezas, PiezaMovil.RANGO_MISTICO)) != null) {
            int filaMin = Math.min(filaMedia, filaFrente);
            int filaMax = Math.max(filaMedia, filaFrente);
            colocarAleatoria(mistico, filaMin, filaMax);
        }

        rellenarFila(piezas, filaFrente);
        rellenarFila(piezas, filaMedia);
        rellenarFila(piezas, filaFondo2);
        rellenarFila(piezas, filaFondo);
    }

    private void rellenarFila(List<Pieza> piezas, int fila) {
        for (int c = 0; c < 10 && !piezas.isEmpty(); c++) {
            if (celdas[fila][c] == null) {
                colocar(piezas.remove(0), fila, c);
            }
        }
    }

    private Pieza extraerTipo(List<Pieza> piezas, TipoPieza tipo) {
        for (int i = 0; i < piezas.size(); i++) {
            if (piezas.get(i).getTipo() == tipo) {
                return piezas.remove(i);
            }
        }
        return null;
    }

    private Pieza extraerRango(List<Pieza> piezas, int rango) {
        for (int i = 0; i < piezas.size(); i++) {
            if (piezas.get(i).getRango() == rango) {
                return piezas.remove(i);
            }
        }
        return null;
    }

    private void colocarAleatoria(Pieza pieza, int filaMin, int filaMax) {
        List<int[]> libres = new ArrayList<int[]>();
        for (int f = filaMin; f <= filaMax; f++) {
            for (int c = 0; c < 10; c++) {
                if (celdas[f][c] == null) {
                    libres.add(new int[]{f, c});
                }
            }
        }

        if (libres.isEmpty()) {
            return;
        }
        int[] destino = libres.get(random.nextInt(libres.size()));
        colocar(pieza, destino[0], destino[1]);
    }

    private void colocar(Pieza pieza, int f, int c) {
        if (pieza == null) {
            return;
        }
        celdas[f][c] = pieza;
        pieza.setPosicion(f, c);
    }

    public Pieza getPieza(int f, int c) {
        if (dentro(f, c)) {
            return celdas[f][c];
        }
        return null;
    }

    public Bando getTurno() {
        return turno;
    }

    public boolean isTerminado() {
        return terminado;
    }

    public Bando getGanador() {
        return ganador;
    }

    public MotivoFin getMotivoFinal() {
        return motivoFinal;
    }

    public List<String> getHistorial() {
        return Collections.unmodifiableList(historial);
    }

    public List<Pieza> getVencidas() {
        return Collections.unmodifiableList(vencidas);
    }

    public boolean esZonaProhibida(int f, int c) {
        if ((f == 4 || f == 5) && (c == 2 || c == 3 || c == 6 || c == 7)) {
            return true;
        }
        return false;
    }

    private boolean dentro(int f, int c) {
        if (f >= 0 && f < 10 && c >= 0 && c < 10) {
            return true;
        }
        return false;
    }

    public void mover(int f1, int c1, int f2, int c2) throws MovimientoInvalidoException, AtaqueInvalidoException {
        if (terminado) {
            throw new MovimientoInvalidoException("La partida ya terminó.");
        }
        if (!dentro(f1, c1) || !dentro(f2, c2)) {
            throw new MovimientoInvalidoException("Casilla fuera del tablero.");
        }
        Pieza origen = celdas[f1][c1];
        Pieza destino = celdas[f2][c2];

        if (origen == null || origen.getBando() != turno) {
            throw new MovimientoInvalidoException("Selecciona una pieza de tu turno.");
        }

        Movible movible = origen;

        if (!movible.puedeMoverse()) {
            throw new MovimientoInvalidoException("La Tierra y las bombas no se mueven.");
        }

        if (esZonaProhibida(f2, c2)) {
            throw new MovimientoInvalidoException("No puedes entrar en una zona prohibida.");
        }

        int df = Math.abs(f2 - f1);
        int dc = Math.abs(c2 - c1);

        if (df + dc == 0 || (df != 0 && dc != 0)) {
            throw new MovimientoInvalidoException("El movimiento debe ser recto y ortogonal.");
        }

        int distancia = df + dc;

        if (destino != null && destino.getBando() == turno) {
            throw new MovimientoInvalidoException("No puedes ocupar una casilla propia.");
        }

        if (destino != null && distancia != 1) {
            throw new AtaqueInvalidoException("Solo puedes atacar una pieza adyacente.");
        }

        if (destino != null && destino.getTipo() == TipoPieza.TIERRA && origen.getRango() != PiezaMovil.RANGO_AGENTE) {
            throw new AtaqueInvalidoException("Solo una pieza de rango 1 puede capturar la Tierra.");
        }

        if (!origen.mueveVariasCasillas() && distancia != 1) {
            throw new MovimientoInvalidoException("Solo el rango 2 puede avanzar varias casillas.");
        }

        int pasoF = Integer.compare(f2, f1);
        int pasoC = Integer.compare(c2, c1);

        if (!caminoLibreRecursivo(f1 + pasoF, c1 + pasoC, f2, c2, pasoF, pasoC)) {
            throw new MovimientoInvalidoException("El camino está bloqueado o atraviesa una zona prohibida.");
        }

        if (destino == null) {
            celdas[f2][c2] = origen;
            celdas[f1][c1] = null;

            origen.setPosicion(f2, c2);

            if (origen.mueveVariasCasillas() && distancia > 1) {
                origen.revelar();
            }

            String identidad;

            if (origen.isRevelada()) {
                identidad = origen.getNombreVisible();
            } else {
                identidad = "una pieza";
            }
            historial.add(turno + " movió " + identidad + ".");
        } else {
            combatir(origen, destino, f1, c1, f2, c2);
        }

        if (!terminado) {
            comprobarVictoriaPorPiezas();
        }

        if (!terminado) {
            Bando siguiente;

            if (turno == Bando.HEROES) {
                siguiente = Bando.VILLANOS;
            } else {
                siguiente = Bando.HEROES;
            }

            if (!buscarMovimientoRecursivo(siguiente, 0)) {
                finalizar(turno, MotivoFin.SIN_MOVIMIENTOS);
                historial.add(siguiente + " no tiene movimientos válidos. Gana " + ganador + ".");
            } else {
                turno = siguiente;
            }
        }
    }

    private boolean caminoLibreRecursivo(int f, int c, int destinoF, int destinoC, int pasoF, int pasoC) {
        if (f == destinoF && c == destinoC) {
            return true;
        }

        if (!dentro(f, c) || celdas[f][c] != null || esZonaProhibida(f, c)) {
            return false;
        }

        return caminoLibreRecursivo(f + pasoF, c + pasoC, destinoF, destinoC, pasoF, pasoC);
    }

    private void combatir(Pieza atacante, Pieza defensor, int f1, int c1, int f2, int c2) {
        atacante.revelar();
        defensor.revelar();

        String texto = atacante.getNombreVisible() + " atacó a " + defensor.getNombreVisible() + ": ";

        ResultadoCombate resultado = defensor.recibirAtaque(atacante);

        if (resultado == ResultadoCombate.GANA) {
            eliminar(defensor);
            moverAtacante(atacante, f1, c1, f2, c2);
            if (defensor.getTipo() == TipoPieza.TIERRA) {
                finalizar(atacante.getBando(), MotivoFin.CAPTURA_TIERRA);
                historial.add(texto + "capturó la TIERRA. ¡Victoria!");
            } else if (defensor.getTipo() == TipoPieza.BOMBA) {
                historial.add(texto + "el rango 3 destruyó la bomba.");
            } else if (atacante.getRango() == PiezaMovil.RANGO_AGENTE) {
                historial.add(texto + "el rango 1 derrotó al rango 10.");
            } else {
                historial.add(texto + "ganó el atacante.");
            }
        } else if (resultado == ResultadoCombate.PIERDE) {
            eliminar(atacante);
            if (defensor.getTipo() == TipoPieza.BOMBA) {
                historial.add(texto + "la bomba destruyó al atacante.");
            } else {
                historial.add(texto + "ganó el defensor.");
            }
        } else if (resultado == ResultadoCombate.EMPATE) {
            eliminar(atacante);
            eliminar(defensor);
            historial.add(texto + "empate: ambas fichas fueron eliminadas.");
        }
    }

    private void moverAtacante(Pieza p, int f1, int c1, int f2, int c2) {
        celdas[f2][c2] = p;
        celdas[f1][c1] = null;
        p.setPosicion(f2, c2);
    }

    private void eliminar(Pieza p) {
        if (p.getFila() >= 0 && p.getColumna() >= 0) {
            celdas[p.getFila()][p.getColumna()] = null;
        }
        vencidas.add(p);
        p.setPosicion(-1, -1);
    }

    private void finalizar(Bando bandoGanador, MotivoFin motivo) {
        ganador = bandoGanador;
        motivoFinal = motivo;
        terminado = true;
    }

    private void comprobarVictoriaPorPiezas() {
        boolean heroes = false;
        boolean villanos = false;
        for (int f = 0; f < 10; f++) {
            for (int c = 0; c < 10; c++) {
                Pieza p = celdas[f][c];
                if (p != null && p.puedeMoverse()) {
                    if (p.getBando() == Bando.HEROES) {
                        heroes = true;
                    } else {
                        villanos = true;
                    }
                }
            }
        }

        if (heroes && villanos) {
            return;
        }

        Bando bandoGanador;

        if (heroes) {
            bandoGanador = Bando.HEROES;
        } else if (villanos) {
            bandoGanador = Bando.VILLANOS;
        } else {
            bandoGanador = turno;
        }

        finalizar(bandoGanador, MotivoFin.SIN_PIEZAS);

        historial.add(bandoGanador + " eliminó todas las piezas móviles del rival.");
    }

    private boolean buscarMovimientoRecursivo(Bando bando, int indice) {
        if (indice >= 100) {
            return false;
        }
       
        Pieza p = celdas[indice / 10][indice % 10];

        if (p != null && p.getBando() == bando
                && p.puedeMoverse() && tieneMovimiento(p)) {
            return true;
        }
        return buscarMovimientoRecursivo(bando, indice + 1);
    }

    private boolean tieneMovimiento(Pieza p) {
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int i = 0; i < dirs.length; i++) {
            int pasoF = dirs[i][0];
            int pasoC = dirs[i][1];
            int f = p.getFila() + pasoF;
            int c = p.getColumna() + pasoC;
            if (!dentro(f, c) || esZonaProhibida(f, c)) {
                continue;
            }
            Pieza destino = celdas[f][c];
            if (destino == null) {
                return true;
            }

            if (destino.getBando() != p.getBando()) {
                if (destino.getTipo() != TipoPieza.TIERRA || p.getRango() == PiezaMovil.RANGO_AGENTE) {
                    return true;
                }
            }
        }
        return false;
    }

    public void retirar(Bando bandoRetirado) {
        if (terminado) {
            return;
        }
        Bando bandoGanador;

        if (bandoRetirado == Bando.HEROES) {
            bandoGanador = Bando.VILLANOS;
        } else {
            bandoGanador = Bando.HEROES;
        }
        finalizar(bandoGanador, MotivoFin.RETIRO);
        historial.add(bandoRetirado + " se retiró. Gana " + bandoGanador + ".");
    }
}