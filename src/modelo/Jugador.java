
package modelo;


public class Jugador {
    
    private String username;
    private String password;
    private int puntos;
    private int partidasHeroes;
    private int partidasVillanos;
    
    public Jugador(String username, String password){
        this.username = username;
        this.password = password;
        this.puntos = 0;
        this.partidasHeroes = 0;
        this.partidasVillanos = 0;
    }
}
