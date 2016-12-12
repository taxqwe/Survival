
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

public class Zombie {

    int hp;
    int x;
    int y;

    Image img = new ImageIcon("res/zombie.gif").getImage();

    public Zombie(int hp, int x, int y) {
        this.hp = hp;
        this.x = x;
        this.y = y;
    }

    public void move() {
        x -= 3;
    }
    
    public Rectangle getRect(){
        return new Rectangle(x,y + (190*3)/4, 121, 190/4);
    }
    
    public Rectangle checkHit(){
        return new Rectangle(x,y, 121, 190);
    }
    
    public int getX() {
     
        return x;
    }
}
