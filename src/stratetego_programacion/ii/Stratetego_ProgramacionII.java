package stratetego_programacion.ii;

import java.awt.Color;
import java.awt.Font;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import logica.GestorJugadores;
import vista.Estilo;
import vista.MenuInicial;

public class Stratetego_ProgramacionII {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ex) {
                }
                
                Font negrita = new Font("SansSerif", Font.BOLD, 13);
                UIManager.put("OptionPane.background", Estilo.PANEL);
                UIManager.put("Panel.background", Estilo.PANEL);
                UIManager.put("OptionPane.messageForeground", Color.WHITE);
                UIManager.put("Button.background", new Color(232, 228, 226));
                UIManager.put("Button.foreground", new Color(30, 25, 26));
                UIManager.put("Button.font", negrita);
                UIManager.put("OptionPane.buttonFont", negrita);
                UIManager.put("TextField.background", Estilo.PANEL_CLARO);
                UIManager.put("TextField.foreground", Color.WHITE);
                UIManager.put("PasswordField.background", Estilo.PANEL_CLARO);
                UIManager.put("PasswordField.foreground", Color.WHITE);
                UIManager.put("ToolTip.background", new Color(30, 23, 25));
                UIManager.put("ToolTip.foreground", Color.WHITE);
                UIManager.put("ToolTip.font", new Font("SansSerif", Font.BOLD, 12));
                ToolTipManager.sharedInstance().setInitialDelay(300);
                MenuInicial inicio = new MenuInicial(new GestorJugadores());
                inicio.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
                inicio.setVisible(true);
            }
        });
    }
}
