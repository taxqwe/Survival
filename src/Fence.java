
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;


public class Fence {

    Image img = new ImageIcon("res/fence.png").getImage();
    int hp;
    int x;
    int y;
    boolean destroyed = false;
    
    public Fence(int x, int y, int hp, boolean destroyed){
        this.x = x;
        this.y = y;
        this.hp = hp;
        this.destroyed = destroyed;
    }
    
    public Rectangle getRect(){
        return new Rectangle(x, y, 43, 160);
    }
}
