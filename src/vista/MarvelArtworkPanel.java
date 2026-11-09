package vista;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class MarvelArtworkPanel extends JPanel {
    private BufferedImage imagen;
    private boolean degradadoDerecha;
    private boolean degradadoAbajo;

    public MarvelArtworkPanel(boolean degradadoDerecha, boolean degradadoAbajo) {
        this.degradadoDerecha = degradadoDerecha;
        this.degradadoAbajo = degradadoAbajo;
        setOpaque(false);
        try {
            imagen = ImageIO.read(getClass().getResource("/recursos/marvel_banner.jpg"));
        } catch (Exception ex) {
            imagen = null;
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        int w = getWidth();
        int h = getHeight();
        g2.setColor(new Color(12, 10, 12));
        g2.fillRect(0, 0, w, h);

        int imageW = 0;
        int imageH = 0;
        if (imagen != null && w > 0 && h > 0) {
            if (degradadoDerecha) {
                imageH = h;
                imageW = (int) (imagen.getWidth() * (h / (double) imagen.getHeight()));
                g2.drawImage(imagen, 0, 0, imageW, h, this);
            } else {
                imageW = w;
                imageH = (int) (imagen.getHeight() * (w / (double) imagen.getWidth()));
                g2.drawImage(imagen, 0, 0, w, imageH, this);
            }
            g2.setColor(new Color(8, 7, 9, 42));
            if (degradadoDerecha) {
                g2.fillRect(0, 0, imageW, h);
            } else {
                g2.fillRect(0, 0, w, imageH);
            }
        }

        if (degradadoDerecha) {
            int inicio = Math.max(0, imageW * 54 / 100);
            g2.setPaint(new GradientPaint(inicio, 0, new Color(11, 9, 11, 25),
                Math.max(inicio + 1, w), 0, new Color(11, 9, 11, 255)));
            g2.fillRect(inicio, 0, w - inicio, h);
            g2.setPaint(new GradientPaint(Math.max(0, imageW - 80), 0,
                new Color(153, 20, 37, 20), w, 0, new Color(153, 20, 37, 48)));
            g2.fillRect(Math.max(0, imageW - 80), 0, w - Math.max(0, imageW - 80), h);
        }

        if (degradadoAbajo) {
            int inicio = Math.min(h, imageH * 52 / 100);
            g2.setPaint(new GradientPaint(0, inicio, new Color(12, 10, 12, 10),
                0, h, new Color(12, 10, 12, 252)));
            g2.fillRect(0, inicio, w, h - inicio);
        }

        g2.setColor(new Color(190, 28, 45, 200));
        g2.fillRect(0, 0, w, 5);
        g2.setColor(new Color(239, 190, 79, 145));
        g2.fillRect(0, 5, w, 1);

        g2.setColor(new Color(213, 39, 54, 29));
        for (int y = 16; y < h; y += 17) {
            for (int x = Math.max(15, w - 170); x < w - 12; x += 17) {
                g2.fillOval(x, y, 3, 3);
            }
        }
        g2.dispose();
    }
}
