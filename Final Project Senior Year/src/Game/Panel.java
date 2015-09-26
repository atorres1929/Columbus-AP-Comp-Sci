

package Game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;

public class Panel extends JPanel implements Runnable, KeyListener{
    
    private Thread thread;
    private boolean running;
    private BufferedImage image;
    private Graphics2D g;
    private final int SCALE = 2;
    public static final int WIDTH = 400;
    public static final int HEIGHT = 400;
    private ScrollingBackground background;
    private double averageFPS;
    private final int FPS = 30;
    private int fpsCount = 0;
    private static Player player;
    protected static ArrayList<Bullet> bullets;
    protected static ArrayList<Enemy> enemys;
    private long waveStartTimer, waveStartTimerDiff;
    private int waveNumber;
    private int waveStartDelay = 2000;
    private boolean waveStart;
    public Panel(){
        super();
        setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
        setFocusable(true);
        requestFocus();
    }
    
    public void addNotify(){
        super.addNotify();
        if (thread == null)
            thread = new Thread(this);
        thread.start();
        addKeyListener(this);
        
    }
    
    public void run() {
        running = true;
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        g = (Graphics2D) image.getGraphics();
        
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        background = new ScrollingBackground("/FireBackground.jpg", 3);
        long start, now, elapsedTime;
        long targetTime = 1000000000/FPS;
        long waitTime=0;
        long totalTime = 0;
        bullets = new ArrayList<>();
        enemys = new ArrayList<>();
        
        waveStartTimer = 0;
        waveStartTimerDiff = 0;
        waveStart = true;
        waveNumber = 0;
        
        player = new Player();
        while(running){           
            start = System.nanoTime();
            gameUpdate();
            gameRender();
            gameDraw();
            
            now = System.nanoTime();
            elapsedTime = now-start;
            waitTime = targetTime - elapsedTime;
            try{
                Thread.sleep(waitTime/1000000);
            }catch(Exception e){}
            fpsCount++;
            totalTime += System.nanoTime() - start;
            if (totalTime > 1000000000){
                System.out.println("FPS: "+fpsCount);
                fpsCount = 0;
                totalTime = 0;
            }
            
        }
        
    }

    private void gameUpdate() {
        background.update();
        addWave();
        checkEndOfWave();
        player.update();
        updateBullets();
        updateEnemies();
        checkEnemyBulletCollision();
        checkDeadEnemies();
    }
    
    private void gameRender(){  
        
        //panel render
//       g.setColor(Color.WHITE);
//       g.fillRect(0, 0, WIDTH, HEIGHT);
//       g.setColor(Color.BLACK);
//       g.drawString("Dumb", 10, 10);
        
       background.draw(g);
       g.setColor(Color.BLACK);
       g.setFont(new Font("Arial", Font.PLAIN, 12));
       g.drawString("FPS: "+ FPS, 0, 10);
       g.drawString("Wave: "+ waveNumber, 0, 20);
       
//       player draw
       player.draw(g);
       drawPlayerLives(g);
       
//       bullet draw
       for (Bullet x: bullets){
           x.draw(g);
       }
       
//       enemy draw
       for (Enemy e: enemys){
           e.draw(g);
       }
       
       drawWaveNumber(g);
        
    }
    
    private void gameDraw(){
        Graphics2D g2 = (Graphics2D) this.getGraphics();
        g2.drawImage(image, 0,0, WIDTH*SCALE, HEIGHT*SCALE, null);
        g2.dispose();
    }
    
    private void checkPlayerEnemyCollision(){
        if (!player.isRecovering()){
            int px = player.getX();
            int py = player.getY();
            int pr = player.getR();
            
            for (int i = 0; i < enemys.size(); i++){
                Enemy e = enemys.get(i);
                double ex = e.getX();
                double ey = e.getY();
                double er = e.getR();
                
                double dx = px - ex;
            }
        }
            
    }
    private void checkEndOfWave(){
        if (waveStart && enemys.isEmpty()){
            createNewEnemys();
        }
    }
    
    private void addWave(){
        if (waveStartTimer == 0 && enemys.isEmpty()){
            waveNumber++;
            waveStart = false;
            waveStartTimer = System.nanoTime();
        }
        else{
            waveStartTimerDiff = (System.nanoTime() - waveStartTimer) / 1000000;
            if (waveStartTimerDiff > waveStartDelay){
                waveStart = true;
                waveStartTimer = 0;
                waveStartTimerDiff = 0;
            }
        }
    }
    private void drawWaveNumber(Graphics2D g){
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        String s = ">>  WAVE "+waveNumber+"  <<";
        int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
        int alpha = (int) (255 * Math.sin(Math.PI * waveStartTimerDiff / waveStartDelay));
        if (alpha > 255) alpha = 255;
        g.setColor(new Color(255,255,255, alpha));
        g.drawString(s, WIDTH / 2 - length / 2, HEIGHT / 2);
    }
    private void updateBullets(){
        int n = 0;
        while (n < bullets.size()){
            Bullet x = bullets.get(n);
            boolean remove = x.update();
            if (remove){
                bullets.remove(x);
            }
            else{
                n++;
            }
        }
    }
    private void updateEnemies(){
        for (int i = 0; i < enemys.size(); i++){
            enemys.get(i).update();
        }
    }
    private void checkDeadEnemies(){
        for (int i = 0; i < enemys.size(); i++){
            if (enemys.get(i).isDead()){
                enemys.remove(i);
                i--;
            }
        }
    }
    
    private void checkEnemyBulletCollision(){
        for (int i = 0; i < bullets.size(); i++){
            double bx = bullets.get(i).getX();
            double by = bullets.get(i).getY();
            double br = bullets.get(i).getR();
            for(Enemy e: enemys){
                double ex = e.getX();
                double ey = e.getY();
                double er = e.getR();
                
                double dx = bx - ex;
                double dy = by - ey;
                double dr = br - er;
                
                double dist = Math.sqrt(dx*dx + dy*dy);
                if (dist < br+er){
                    e.hit();
                    bullets.remove(bullets.get(i));
                    i--;
                    break;
                }
            }
        }
    }
    
    private void createNewEnemys(){
        enemys.clear();
        
        switch (waveNumber){
            default:
                for (int i = 0; i < waveNumber*5; i++){
                    enemys.add(new Enemy(1,1));
                }
                break;
        }
    
    }
    
    private void drawPlayerLives(Graphics2D g){
        for (int i = 0; i < player.getLives(); i++){
            g.setColor(player.color1);
            g.fillOval(5 + (25*i), 5, player.getR() *2, player.getR()*2);
            g.setStroke(new BasicStroke(3));
            g.setColor(player.color1.darker());
            g.drawOval(5 + (25*i), 5, player.getR() *2, player.getR()*2);
        }
    }
    
    @Override
    public void keyPressed(KeyEvent ke) {
        int key = ke.getKeyCode();
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A){
            player.left = true;
        }
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D){
            player.right = true;
        }
        if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S){
            player.down = true;
        }
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W){
            player.up = true;
        }
        if (key == KeyEvent.VK_SPACE){
            player.setFiring(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        int key = ke.getKeyCode();
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A){
            player.left = false;
        }
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D){
            player.right = false;
        }
        if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S){
            player.down = false;
        }
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W){
            player.up = false;
        }
        if (ke.getKeyCode() == KeyEvent.VK_SPACE){
            player.setFiring(false);
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        
    }
    
}
