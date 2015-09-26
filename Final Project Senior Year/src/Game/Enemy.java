/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;


public class Enemy {
    
private double x,y;
private int speed, r;
private double dx,dy,rad;
private int health, type, rank;
private boolean ready, dead;
private Color color1;

    public Enemy(int type, int rank){
        
        
        this.type = type;
        this.rank = rank;
        
        switch (type){
            default:
                color1 = Color.BLUE;
                speed = 2;
                r = 10;
                health = 1;
                break;
            case 2:
                switch (rank){
                    
                }
                break;
        }
        
        x = Math.random() * Panel.WIDTH / 2 + Panel.WIDTH / 4;
        y = -r;
        
        double angle = Math.random() * 140 + 20;
        rad = Math.toRadians(angle);
        
        dx = Math.cos(rad) * speed;
        dy = Math.sin(rad) * speed;
        
        ready = false;
        dead = false;
        
        
    }
    
    public double getX(){ return x;}
    public double getY(){ return y;}
    public double getR(){ return r;}
    public boolean isDead(){ return dead; }
    
    public void hit(){
        health --;
        if (health <= 0)
            dead = true;
    }
    
    public void update(){
        x += dx;
        y += dy;
        if (!ready){
            if (x > r && x < Panel.WIDTH - r && y > r && y < Panel.HEIGHT - r)
                ready = true;
        }
        
        if (x < r && dx < 0) dx = -dx;
        if (y < r && dy < 0) dy = -dy;
        if (x > Panel.WIDTH - r && dx > 0) dx = -dx;
        if (y > Panel.HEIGHT - r && dy > 0) dy = -dy;
    }
    
    public void draw(Graphics2D g){
        g.setColor(color1);
        g.fillOval((int) (x-r), (int) (y-r), r*2, r*2);
        g.setStroke(new BasicStroke(3));
        g.setColor(color1.darker());
        g.drawOval((int) (x-r), (int) (y-r), r*2, r*2);
        g.setStroke(new BasicStroke(1));

    }
}
