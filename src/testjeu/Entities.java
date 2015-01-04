/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package testjeu;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Paul
 */
public class Entities {
    public static final int STARSHIP=0;
    public static final int ASTEROIDS=1;
    public static final int LASERS=2;
    public static final int EXPLOSIONS=3;
    
    public Sprite laserSprites;
    
    private int difficulty;
    private long spawnTimer;
    
    EntityList[] list;
    
    public Entities(int diff,String lS){
        list= new EntityList[4];
        list[STARSHIP]=new EntityList(new Starship("starshipTileset.png",64,80),null);
        list[ASTEROIDS]=null;
        list[LASERS]=null;
        list[EXPLOSIONS]=null;
        
        laserSprites=new Sprite(lS);
        
        difficulty=diff;
        spawnTimer=System.currentTimeMillis();
    }
    
    public void update(){
        for (EntityList list1 : list) {
            for (EntityList c = list1; c!=null; c=c.tl) {
                c.hd.updateStatus();
            }
        }
        if(((Starship)list[STARSHIP].hd).readyToFire){
            list[LASERS]=EntityList.add(new Laser(((Starship)list[STARSHIP].hd).pos,((Starship)list[STARSHIP].hd).rot,((Starship)list[STARSHIP].hd).fireSide,laserSprites), list[LASERS]);
        }
        
        if(System.currentTimeMillis()-spawnTimer>=10000/3) {
            list[ASTEROIDS]=EntityList.add(new Asteroid("asteroids.png",(int)(Math.random()*9+1)),list[ASTEROIDS]);
            list[ASTEROIDS].hd.spawn();
            spawnTimer=System.currentTimeMillis();
        }
        EntityList buff=null;
        for(EntityList c=list[EXPLOSIONS];c!=null;c=c.tl){
            if(((Explosion)c.hd).finished){
                if(buff==null) {
                    list[EXPLOSIONS]=list[EXPLOSIONS].tl;
                }else{
                    buff.tl=c.tl;
                }
                
            }
            buff=c;
            
        }
        
        checkBorders();
        checkHits();
    }
    
    public void print(Graphics2D g2d){
        for (EntityList list1 : list) {
            for (EntityList c = list1; c!=null; c=c.tl) {
                c.hd.print(g2d);
            }
        }
    }
    
    public void checkBorders(){
        list[STARSHIP].hd.isOut();
        for(EntityList c=list[ASTEROIDS];c!=null;c=c.tl){
            c.hd.isOut();
        }
        EntityList buff=null;
        for(EntityList c=list[LASERS];c!=null;c=c.tl){
            if(c.hd.isOut()) {
                if(buff==null){
                    list[LASERS]=list[LASERS].tl;
                    //list[LASERS].tl=list[LASERS].tl.tl;
                }else{
                    buff.tl=c.tl;
                }
             
            }
            buff=c;
        }
    }
    public void checkHits(){
        for(EntityList c=list[ASTEROIDS];c!=null;c=c.tl){
            if(((Starship)list[STARSHIP].hd).hitbox.testCollision(((Asteroid)c.hd).hitbox)){
                GamePanel.gameOver=true;
                list[EXPLOSIONS]=EntityList.add(new Explosion("explosion.png",((Starship)list[STARSHIP].hd).getRot(),((Starship)list[STARSHIP].hd).getPos()),list[EXPLOSIONS]);
                list[EXPLOSIONS].hd.spawn();
                ((Starship)list[STARSHIP].hd).stopEngine();
                ((Starship)list[STARSHIP].hd).stopRotation();
                ((Starship)list[STARSHIP].hd).stopFire();
            }
            
            
            
        }
        
        for(EntityList c=list[LASERS];c!=null;c=c.tl){
            for(EntityList d=list[ASTEROIDS];d!=null;d=d.tl){
                if(((Laser)c.hd).hitbox.testCollision(((Asteroid)d.hd).hitbox)){
                    System.out.println("touché!");
                    list[EXPLOSIONS]=EntityList.add(new Explosion("explosion.png",((Asteroid)d.hd).getRot(),((Asteroid)d.hd).getPos()),list[EXPLOSIONS]);
                    list[EXPLOSIONS].hd.spawn();
                    list[ASTEROIDS]=EntityList.remove(list[ASTEROIDS], d.hd);
                    list[LASERS]=EntityList.remove(list[LASERS], c.hd);
                }
            }
        }
    }
    
}
