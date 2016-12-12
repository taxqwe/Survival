
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.*;
import javax.imageio.IIOImage;

public class Hands {
    Image img = new ImageIcon("res/Hands.png").getImage().getScaledInstance(365/3, 571/3, Image.SCALE_SMOOTH);
    
    int x;
    int y;
    boolean fire = false;
    
    Hands()
    {
        
    }
    
}
