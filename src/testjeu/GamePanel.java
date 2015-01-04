/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package testjeu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author Paul
 */
public class GamePanel extends JPanel implements Runnable {
    //private static final int PWIDTH=800;
    //private static final int PHEIGHT=600;
    
    private Thread animator;
    private volatile boolean running = false;
    
    public static volatile boolean gameOver = false;
    private volatile boolean isPaused = false;
    
    
    private boolean displayFPS=true;
    private double actualFPS=60.0;
    private static final int MAX_FRAME_SKIPS=2;
    
    
    
    private Graphics dbg;
    private Image dbImage = null;
    
    private int period=1000/(int)Stardust.FPS;
    
    private Background bg;
    private Entities ent;
    
    //private double theta=0.0;
    
    
    
    public GamePanel(){
        setBackground(Color.white);
        setPreferredSize(new Dimension(Stardust.PWIDTH, Stardust.PHEIGHT));
        
        setFocusable(true);
        requestFocus();
        readyForTermination();
        newGame();
        
        
        //bg.initiateRotation(1400,800,800,200);
        
        addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e)
                {testPress(e.getKeyCode());}
            
        });
        addKeyListener(new KeyAdapter(){
            @Override
            public void keyReleased(KeyEvent e)
                {testReleased(e.getKeyCode());}
        });
    }
    
    @Override
    public void addNotify(){
        super.addNotify();
        startGame();
    }
    
    private void startGame() {
        if (animator==null|| !running){
            animator = new Thread(this);
            animator.start();
        }
    }
    
    public void stopGame(){
        running=false;
    }
    
    @Override
    public void run() {
        long beforeTime, timeDiff, sleepTime;
        long excess=0;
        int skips=0;
        
        beforeTime=System.currentTimeMillis();
        
        running=true;
        while(running){
            gameUpdate();
            gameRender();
            paintScreen();
            
            timeDiff =System.currentTimeMillis()-beforeTime;
            sleepTime = period - timeDiff;
            excess-=sleepTime;
            if(displayFPS)actualFPS=1000/(timeDiff+(sleepTime<=0?0:sleepTime));
            
            if (sleepTime<=0)
                sleepTime=5;
            try{
                Thread.sleep(sleepTime);
            }catch(InterruptedException e){}
            
            beforeTime=System.currentTimeMillis();
            
            while((excess>period)&&(skips<MAX_FRAME_SKIPS)){
                excess-=period;
                skips++;
                gameUpdate();
            }
            //System.out.println("1");
        }
        System.exit(0);
    }

    private void gameUpdate() {
        if(!isPaused){
        //code du jeu
            //theta=theta+Math.PI/30;
            bg.calculatePath();
            ent.update();
        }
    }
    
    private void gameRender() {
        if(dbImage==null){
            dbImage = createImage(Stardust.PWIDTH, Stardust.PHEIGHT);
            if(dbImage==null){
                System.out.println("dbImage is null");
                return;
            }
            else
            dbg= dbImage.getGraphics();
        }
        dbg.setColor(Color.white);
        dbg.fillRect(0, 0, Stardust.PWIDTH, Stardust.PHEIGHT);
        
        //code de l'animation
        
        Graphics2D dbg2d = (Graphics2D) dbg;
        bg.print(dbg2d);
        ent.print(dbg2d);
        if(displayFPS) dbg.drawString(actualFPS+"fps",0,Stardust.PHEIGHT);
        if(isPaused) pausedMessage(dbg);
        if(gameOver) gameOverMessage(dbg);
    }

    private void gameOverMessage(Graphics g) {
        int x=180;
        int y=145;
        g.drawString("Game Over",x,y);
    }
    
    private void pausedMessage(Graphics g){
        
    }

    private void readyForTermination() {
        addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                int keyCode=e.getKeyCode();
                if((keyCode==KeyEvent.VK_ESCAPE)||(keyCode==KeyEvent.VK_CANCEL)||(keyCode==KeyEvent.VK_END)||((keyCode==KeyEvent.VK_X)&&e.isControlDown())){
                    running=false;
                }else if(keyCode==KeyEvent.VK_ENTER){
                    isPaused=!isPaused;
                }
            }
        });
    }
    private void testPress(int keyCode){
        if(!gameOver){
            //code de l'interaction
            if((keyCode==KeyEvent.VK_Z)||(keyCode==KeyEvent.VK_UP)){
                ((Starship)ent.list[Entities.STARSHIP].hd).startEngine();
            }
            if((keyCode==KeyEvent.VK_Q)||(keyCode==KeyEvent.VK_LEFT)){
                ((Starship)ent.list[Entities.STARSHIP].hd).rotateLeft();
            }else if((keyCode==KeyEvent.VK_D)||(keyCode==KeyEvent.VK_RIGHT)){
                ((Starship)ent.list[Entities.STARSHIP].hd).rotateRight();
            }
            if((keyCode==KeyEvent.VK_SHIFT)){
                ((Starship)ent.list[Entities.STARSHIP].hd).startBoost();
            }
            if((keyCode==KeyEvent.VK_SPACE)){
                ((Starship)ent.list[Entities.STARSHIP].hd).fire();
                
            }
            if((keyCode==KeyEvent.VK_F1)){
                System.out.println(((Starship)ent.list[Entities.STARSHIP].hd).isFiring);
            }else if((keyCode==KeyEvent.VK_F2)){
                System.out.println(((Starship)ent.list[Entities.STARSHIP].hd).readyToFire);
            }else if((keyCode==KeyEvent.VK_F3)){
                ent.list[Entities.ASTEROIDS]=EntityList.add(new Asteroid("asteroids.png",7), ent.list[Entities.ASTEROIDS]);
                ent.list[Entities.ASTEROIDS].hd.spawn();
            }else if((keyCode==KeyEvent.VK_F4)){
                ent.list[Entities.ASTEROIDS]=EntityList.add(new Asteroid("asteroids.png",6), ent.list[Entities.ASTEROIDS]);
                ent.list[Entities.ASTEROIDS].hd.spawn();
            }else if((keyCode==KeyEvent.VK_F5)){
                Stardust.displayHitbox=!Stardust.displayHitbox;
            }
            
            
        }else if (isPaused&&!gameOver){
            
        }
    }
    
    private void testReleased(int keyCode){
        
            //code de l'interaction
            if((keyCode==KeyEvent.VK_Z)||(keyCode==KeyEvent.VK_UP)){
                ((Starship)ent.list[Entities.STARSHIP].hd).stopEngine();
            }
            if((keyCode==KeyEvent.VK_Q)||(keyCode==KeyEvent.VK_LEFT)||(keyCode==KeyEvent.VK_D)||(keyCode==KeyEvent.VK_RIGHT)){
                ((Starship)ent.list[Entities.STARSHIP].hd).stopRotation();
            }
            if(keyCode==KeyEvent.VK_SHIFT){
                ((Starship)ent.list[Entities.STARSHIP].hd).stopBoost();
            }
            if(keyCode==KeyEvent.VK_SPACE){
                ((Starship)ent.list[Entities.STARSHIP].hd).stopFire();
            }
            
        
    }

    private void paintScreen() {
        Graphics g;
        try{
            g=this.getGraphics();
            if((g !=null)&&(dbImage!=null)) g.drawImage(dbImage, 0, 0, null);
            Toolkit.getDefaultToolkit().sync();
            g.dispose();
        }catch(Exception e){
            System.out.println("Graphics context error"+e);
        }
//        System.out.println(((Starship)ent.list[Entities.STARSHIP].hd).readyToFire);
//        System.out.println(((Starship)ent.list[Entities.STARSHIP].hd).fireSide?1:0);
    }
    
    private void newGame(){
        ent=new Entities(1,"laserSprite1.png");
        bg= new Background("Tarantula-Nebula.png",60);
        bg.initiateRotation(1800,800,1200, 500);
        ent.list[Entities.STARSHIP].hd.spawn();
    }
}
