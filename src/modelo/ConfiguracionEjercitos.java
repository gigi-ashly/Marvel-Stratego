package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class ConfiguracionEjercitos {
    private static final String[] HEROES = {"Mr. Fantastic", "Captain America", "Professor X", "Nick Fury", "Spider-Man", "Wolverine", "Namor", "Daredevil",
        "Silver Surfer", "Hulk", "Iron Man", "Thor", "Human Torch", "Cyclops", "Invisible Woman", "Ghost Rider",
        "Punisher", "Blade", "Thing", "Emma Frost", "She-Hulk", "Giant Man", "Beast", "Colossus",
        "Gambit", "Spider-Girl", "Iceman", "Storm", "Phoenix", "Dr. Strange", "Elektra", "Nightcrawler",
        "Black Widow", "Nova Blast", "Nova Blast", "Nova Blast", "Nova Blast", "Nova Blast", "Nova Blast", "Planet Earth"
    };

    private static final int[] HERO_RANKS = {
        10, 9, 8, 8, 7, 7, 7, 6,
        6, 6, 6, 5, 5, 5, 5, 4,
        4, 4, 4, 3, 3, 3, 3, 3,
        2, 2, 2, 2, 2, 2, 2, 2,
        1, 0, 0, 0, 0, 0, 0, 0
    };

    private static final String[] VILLAINS = {
        "Dr. Doom", "Galactus", "Kingpin", "Magneto", "Apocalypse", "Green Goblin", "Venom", "Bullseye",
        "Omega Red", "Onslaught", "Red Skull", "Mystique", "Mysterio", "Dr. Octopus", "Deadpool", "Abomination",
        "Thanos", "Black Cat", "Sabretooth", "Juggernaut", "Rhino", "Carnage", "Mole Man", "Lizard",
        "Mr. Sinister", "Sentinel 1", "Ultron", "Sandman", "Leader", "Viper", "Sentinel 2", "Electro",
        "Black Widow", "Pumpkin Bomb", "Pumpkin Bomb", "Pumpkin Bomb", "Pumpkin Bomb", "Pumpkin Bomb", "Pumpkin Bomb", "Planet Earth"
    };

    private static final int[] VILLAIN_RANKS = {
        10, 9, 8, 8, 7, 7, 7, 6,
        6, 6, 6, 5, 5, 5, 5, 4,
        4, 4, 4, 3, 3, 3, 3, 3,
        2, 2, 2, 2, 2, 2, 2, 2,
        1, 0, 0, 0, 0, 0, 0, 0
    };

    private ConfiguracionEjercitos() { 
        
    }

public static List<Pieza> crearPiezas(Bando bando, Random random) {
    String[] nombres;
    int[] rangos;
    String prefijo;

    if (bando == Bando.HEROES) {
        nombres = HEROES;
        rangos = HERO_RANKS;
        prefijo = "h";
    } else {
        nombres = VILLAINS;
        rangos = VILLAIN_RANKS;
        prefijo = "v";
    }

    List<Pieza> piezas = new ArrayList<Pieza>();

    for (int i = 0; i < nombres.length; i++) {
        String imagen = prefijo + i;
        if (rangos[i] > 0) {
            piezas.add(new PiezaMovil(bando, rangos[i], nombres[i], imagen));
        } else {
            TipoPieza tipo;
            if ("Planet Earth".equals(nombres[i])) {
                tipo = TipoPieza.TIERRA;
            } else {
                tipo = TipoPieza.BOMBA;
            }
            piezas.add(new PiezaFija(bando, tipo, nombres[i], imagen));
        }
    }
    Collections.shuffle(piezas, random);
    return piezas;
}
}
