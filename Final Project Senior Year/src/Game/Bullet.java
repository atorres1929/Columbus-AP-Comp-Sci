

package Game;

import java.awt.Color;
import java.awt.Graphics2D;

public class Bullet {
    
    private double x,y;
    private int r;
    private double dx, dy, rad, speed;
    private Color color1;
    
    public Bullet(double angle, int x, int y){
        this.y = y;
        this.x = x;
        r = 5;
        speed = 15;
        rad = Math.toRadians(angle);
        dx = Math.cos(rad)*speed;
        dy = Math.sin(rad)*speed;
        
        
        color1 = Color.YELLOW;
        
    }
    
    public double getX(){ return x;}
    public double getY(){ return y;}
    public double getR(){ return r;}
    
    public boolean update(){
        x += dx;
        y += dy;
        if (x < -r || x > Panel.WIDTH 
                || y < -r || y > Panel.HEIGHT)
            return true;
        
        return false;
    }  
    
    public void draw(Graphics2D g){
        g.setColor(color1);
        g.fillOval((int)(x-r), (int)(y-r), 2*r,2*r);
        g.setColor(Color.BLACK);
    }

}
