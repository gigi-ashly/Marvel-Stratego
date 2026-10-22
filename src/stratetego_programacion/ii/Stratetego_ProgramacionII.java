
package stratetego_programacion.ii;

import javax.swing.SwingUtilities;
import vista.MenuInicial;

public class Stratetego_ProgramacionII {

    public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
            MenuInicial ventana = new MenuInicial();
            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);
        });

    }
    
}
