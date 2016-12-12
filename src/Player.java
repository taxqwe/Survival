
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Player {

    Image stand = new ImageIcon("res/Player.png").getImage().getScaledInstance(365 / 3, 571 / 3, Image.SCALE_SMOOTH);
    Image step = new ImageIcon("res/PlayerStep.png").getImage().getScaledInstance(144, 208, Image.SCALE_SMOOTH);
    Image animated = new ImageIcon("res/output_J1cI8F.gif").getImage();

    Image img = stand;

    int x = 0;
    int y = 0;
    int ammo = 12;

    int dx = 0;
    int dy = 0;

    int maxX = 105;
    int minX = 0;
    int maxY = 380;
    int minY = 0;

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_W:
                dy = -3;
                img = animated;
                break;
            case KeyEvent.VK_S:
                dy = 3;
                img = animated;
                break;
            case KeyEvent.VK_D:
                dx = 3;
                img = animated;
                break;
            case KeyEvent.VK_A:
                dx = -3;
                img = animated;
                break;
            case KeyEvent.VK_SPACE:
                JOptionPane.showMessageDialog(null, x + "|" + y);
            default:
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_W:
                dy = 0;
                img = stand;
                break;
            case KeyEvent.VK_S:
                dy = 0;
                img = stand;
                break;
            case KeyEvent.VK_D:
                dx = 0;
                img = stand;
                break;
            case KeyEvent.VK_A:
                dx = 0;
                img = stand;
                break;
            case KeyEvent.VK_R:
                ammo = 12;
            default:
                break;
        }
    }

    public void move() {
        y += dy;
        if (y <= minY) {
            y = minY;
        }
        if (y >= maxY) {
            y = maxY;
        }

        x += dx;
        if (x >= maxX) {
            x = maxX;
        }
        if (x <= minX) {
            x = minX;
        }

    }
}
