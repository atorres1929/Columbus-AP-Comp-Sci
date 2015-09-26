

package Game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Player {
    
    public int x,y,z,r;
    protected int dx, dy, speed;
    protected int lives;
    public boolean left, right, up, down, firing;
    protected Color color1, color2;
    
    private boolean recovering;
    private long recoveryTimer;
    
    private long firingTimer, firingDelay;
    
    public Player(){
        x = Panel.WIDTH/2;
        y = Panel.HEIGHT/2;
        r = 10;
        dx = 0; dy = 0;
        speed = 10; lives = 3;
        color1 = Color.GREEN;
        color2 = Color.RED;
        firing = false;
        firingTimer = System.nanoTime();
        firingDelay = 200;
        recovering = false;
        recoveryTimer = 0;
    }
    
    public int getX(){ return x;}
    public int getY(){ return y;}
    public int getR(){ return r;}
    public int getLives(){return lives;}
    public boolean isRecovering(){return recovering;}
    
    public void looseLife(){
        lives--;
        recovering = true;
        recoveryTimer = System.nanoTime();
    }
    
    public void update(){
        if (left){
            dx = -speed;
        }
        if (right){
            dx = speed;
        }
        if (up){
            dy = -speed;
        }
        if (down){
            dy = speed;
        }
        x += dx;
        y += dy;
        if (x < r)
            x = r;
        if (y < r)
            y = r;
        if (x > Panel.WIDTH - r)
            x = Panel.WIDTH - r;
        if (y > Panel.HEIGHT - r)
            y = Panel.HEIGHT - r;
        dx = 0;
        dy = 0;
        if (firing){
            long elapsed = (System.nanoTime()-firingTimer)/1000000;
            if (elapsed > firingDelay){
                Panel.bullets.add(new Bullet(270, x, y));
                firingTimer = System.nanoTime();
            }
        }
        
        long elapsed = (System.nanoTime() - recoveryTimer) / 1000000;
        if (elapsed > 2000){
            recovering = false;
            recoveryTimer = 0;
        }
    }
    public void draw(Graphics2D g){
        
        if (recovering){
            g.setColor(color2);
            g.fillOval(x-r, y-r, 2*r, 2*r);
            g.setStroke(new BasicStroke(3));
            g.setColor(color2.darker());
            g.drawOval(x-r, y-r, 2*r, 2*r);
        }
        else{
            g.setColor(color1);
            g.fillOval(x-r, y-r, 2*r, 2*r);
            g.setStroke(new BasicStroke(3));
            g.setColor(color1.darker());
            g.drawOval(x-r, y-r, 2*r, 2*r);
        }
    }
    public void setFiring(boolean firing){
        this.firing = firing;
    }

}
