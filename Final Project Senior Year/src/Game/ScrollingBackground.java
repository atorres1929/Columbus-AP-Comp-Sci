

package Game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class ScrollingBackground {

    private double yOffset, speed;
    private BufferedImage img;
    private int imgHeight;
    public ScrollingBackground(String path, double speed){
        this.speed = speed;
        yOffset = 0;
        
        try{
            img = ImageIO.read(getClass().getResource(path));
            imgHeight = img.getHeight();
        }catch(Exception e){
            System.err.println("Error loading scrolling background");
        }
    }
    public void update(){
        yOffset += speed;
        if ((int) Math.round(yOffset) >= Panel.HEIGHT)
            yOffset = 0;
    }
    public void draw(Graphics2D g){
        int roundedOffset = (int) Math.round(yOffset);
        g.drawImage(img, 0, roundedOffset, Panel.WIDTH, Panel.HEIGHT, null);
        g.drawImage(img, 0, roundedOffset - Panel.HEIGHT, Panel.WIDTH, Panel.HEIGHT, null);
    }
}
