
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {
        JFrame f = new JFrame("SURVIVAL");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(800, 600);
        f.add(new Room());
        f.setVisible(true);
        f.setResizable(false);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage("res/aim.png").getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        Cursor cursor = toolkit.createCustomCursor(image , new Point(f.getX(), 
           f.getY()), "img");
        f.setCursor(cursor);

    }
}
