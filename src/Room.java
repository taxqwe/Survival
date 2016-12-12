
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.scene.Cursor.cursor;
import javax.imageio.ImageIO;

public class Room extends JPanel implements ActionListener, Runnable {

    Point location = MouseInfo.getPointerInfo().getLocation();
    int mousex;
    int mousey;

    int state = 1; // состояние экрана игры: 1 - старт ,2 - игра,3,4

    Image newGame; // кратинка для меню игры
    Image quit; // кратинка для меню игры
    Rectangle btn1 = new Rectangle(250, 300, 358, 60); // область для отслеживания нажатий на кнопки в меню игры, start game
    Rectangle btn2 = new Rectangle(350, 370, 117, 58); // область для отслеживания нажатий на кнопки в меню игры, quit

    boolean pause = false;

    double Cos;

    Timer mainTimer = new Timer(15, this);

    Image bg = new ImageIcon("res/Background.png").getImage();
    Player p = new Player();
    Hands hands = new Hands();

    Fence fence1 = new Fence(180, 120, 5, false);
    Fence fence2 = new Fence(180, 280, 5, false);
    Fence fence3 = new Fence(180, 440, 5, false);

    int killed = 0;

    Thread zombieSpawner = new Thread(this);
    public static ArrayList<Zombie> zombies = new ArrayList<Zombie>();

    public Room() {
        mainTimer.start();
        zombieSpawner.start();
        addKeyListener(new MyKeyAdapter());
        addMouseListener(new MyMouseListener());
        addMouseMotionListener(new MyMouseMotionListener());
        setFocusable(true);
    }

    @Override
    public void paint(Graphics g) {
        g = (Graphics2D) g;
        g.drawImage(bg, 0, 0, null);                            // отрисовка фона

        if (state == 1) {
            if (!btn1.contains(mousex, mousey)) {
                newGame = new ImageIcon("res/start_game_small.png").getImage();
            } else {
                newGame = new ImageIcon("res/start_game_choised.png").getImage();
            }

            if (!btn2.contains(mousex, mousey)) {
                quit = new ImageIcon("res/quit_btn.png").getImage();
            } else {
                quit = new ImageIcon("res/quit_btn_choised.png").getImage();
            }

            g.drawImage(newGame, 250, 300, null);
            g.drawImage(quit, 350, 370, this);

        }
        if (state == 2) {
            g.drawImage(p.img, p.x, p.y, null);                     // отрисовка игрока

            // отрисовка заборов
            if (!fence1.destroyed) {
                g.drawImage(fence1.img, fence1.x, fence1.y, null);
            }
            if (!fence2.destroyed) {
                g.drawImage(fence2.img, fence2.x, fence2.y, null);
            }
            if (!fence3.destroyed) {
                g.drawImage(fence3.img, fence3.x, fence3.y, null);
            }
            ///

            //g.drawLine(p.x + 40, p.y + 40, mousex, mousey);  // Отрисовать линию до курсора
            //// ОТРИСОВКА РУК
            BufferedImage bihands = LoadImage("res/Hands.png");
            AffineTransform at = AffineTransform.getTranslateInstance(p.x + 35, p.y + 30);
            at.scale(0.33, 0.33);
            at.rotate(Cos, 16, 45);
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(bihands, at, null);
            g.setColor(Color.WHITE);
            Font font = new Font("Arial", Font.PLAIN, 20);
            g.setFont(font);
            g.drawString("Ammo: " + p.ammo, 500, 30);
            g.drawString("Killed: " + killed, 700, 30);

            g.setColor(Color.red);
            
            Iterator<Zombie> i = zombies.iterator();
            while (i.hasNext()) {
                Zombie e = i.next();
                g.drawRect(e.x,e.y, 121, 190);
                if (e.x < -100) {
                    i.remove();
                } else {
                    e.move();
                    g.drawImage(e.img, e.x, e.y, null);
                }
            }
            g.drawRect(p.x, p.y + (190*3)/4, 80, 190/4);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        p.move();
        repaint();
        Cos = GetCos();
        zombieTouchFence();
        zombieTouchPlayer();
    }
    
    private void zombieTouchPlayer(){
        Iterator<Zombie> i = zombies.iterator();
        while (i.hasNext()) {
            Zombie e = i.next();
            if ((e.getRect().intersects((new Rectangle (p.x, p.y + (190*3)/4, 80, 190/4)))) || (e.x <= -50)){
                JOptionPane.showMessageDialog(null, "You loose, points earned: " + killed);
                System.exit(0);
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            Random rnd = new Random();
            try {
                Thread.sleep(rnd.nextInt(2000));
            } catch (InterruptedException e) {

            }
            if ((!pause) && (state == 2)) {
                zombies.add(new Zombie(rnd.nextInt(5) + 3, 800, rnd.nextInt(380)));
            }
            //zombies.add(new Zombie(rnd.nextInt(5), 800, 102));
        }
    }

    private class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            p.keyPressed(e);
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                state = 1;
            }
            if ((e.getKeyCode() == KeyEvent.VK_P) && (state == 2)) {
                if (pause == false) {
                    pause = true;
                    mainTimer.stop();

                } else {
                    pause = false;
                    mainTimer.start();
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            p.keyReleased(e);
        }
    }

    private class MyMouseMotionListener implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            location = MouseInfo.getPointerInfo().getLocation();
            mousex = (int) location.getX();
            mousey = (int) location.getY() - 23;

            //System.out.println(mousex + "||" + mousey);
        }

    }

    private class MyMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            //JOptionPane.showMessageDialog(null, "lol");
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (state == 2) {
                if (p.ammo != 0) {
                    p.ammo--;
                    hands.fire = true;
                }

                Iterator<Zombie> i = zombies.iterator();
                while (i.hasNext()) {
                    Zombie z = i.next();
                    //if (z.getRect().intersects(mousex,mousey,mousex,mousey)){
                    if (z.getRect().contains(mousex, mousey)) {
                        if (p.ammo != 0) {
                            z.hp--;
                        }
                        if (z.hp <= 0) {
                            i.remove();
                            killed++;
                        }
                    }
                }
            }
            if (state == 1) {
                if (btn1.contains(mousex, mousey)) {
                    state = 2;
                    Room.zombies.clear();
                }
                if (btn2.contains(mousex, mousey)) {
                    System.exit(0);
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            //JOptionPane.showMessageDialog(null, "mouserelesed");
            hands.fire = false;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            //JOptionPane.showMessageDialog(null, "mouseentered");
        }

        @Override
        public void mouseExited(MouseEvent e) {
            //JOptionPane.showMessageDialog(null, "mouseexited");
        }
    }

    private void zombieTouchFence() {
        Iterator<Zombie> i = zombies.iterator();
        while (i.hasNext()) {
            Zombie e = i.next();

            if (e.x <= 228) {                           //Если зомби достигает забора
                if ((0 <= e.y) && (e.y <= 102)) {       // Координаты забора 
                    if (fence1.hp > 0) {                // Если забор не сломан...
                        fence1.hp--;                    //... то отнимаем единицу здоровья...
                        i.remove();                     //... и убиваем зомби
                        if (fence1.hp == 0) {           // Если  здоровье достигает нуля...
                            fence1.destroyed = true;    // ... то забор сломан.
                        }
                    }

                } else if ((102 < e.y) && (e.y <= 261)) {
                    if (fence2.hp > 0) {
                        fence2.hp--;
                        i.remove();
                        if (fence2.hp == 0) {
                            fence2.destroyed = true;
                        }
                    }
                } else if ((261 < e.y) && (e.y <= 380)) {
                    if (fence3.hp > 0) {
                        fence3.hp--;
                        i.remove();
                        if (fence3.hp == 0) {
                            fence3.destroyed = true;
                        }
                    }
                }
            }
        }
    }

    BufferedImage LoadImage(String FileName) {
        BufferedImage img = null;

        try {
            img = ImageIO.read(new File(FileName));
        } catch (IOException e) {

        }
        return img;
    }

    private double GetCos() {
        int M; // прилежащий катет
        int L; // противолежащий катет

        double CosA;

        M = (mousex - p.x) / 10; // длина прилежащего катета
        L = (mousey - p.y - 25) / 10; // длина противолежащего катета

        CosA = (L / (Math.sqrt(Math.pow(M, 2) + Math.pow(L, 2))));
        //System.out.println(M + "/ SQRT(" + M + "^2 + " + L + "^2");
        return CosA;
    }

}
