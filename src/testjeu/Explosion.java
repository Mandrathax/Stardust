/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package testjeu;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 *
 * @author Paul
 */
public class Explosion implements Entity{
    int id;
    
    private Sprite sprite;
    
    private AffineTransform rot;
    private Point2D.Double pos;
    private double size;
    
    private int counter;
    public boolean finished;
    
    public Explosion(String spriteURL,AffineTransform r,Point2D.Double p){
        sprite=new Sprite(spriteURL,40,40,0.5);
        //sprite.setDelay(3);
        pos=p;
        rot=r;
        finished=false;
        size=2;
        counter=1;
    }

    @Override
    public void print(Graphics2D g2d) {
        AffineTransform t=AffineTransform.getTranslateInstance(pos.x-size*sprite.x,pos.y-size*sprite.y);
        t.rotate(rot.getScaleX(), rot.getShearY(), size*sprite.x, size*sprite.y);
        t.scale(size, size);
        g2d.drawImage(sprite.img, t, null);
    }

    @Override
    public void spawn() {
        
    }

    @Override
    public void updateStatus() {
        if(counter>47*sprite.delay){
            finished=true;
        }else{
            sprite.setImgLoop(2, 1, 48);
            sprite.setDelay(sprite.getDelay()+0.01);
            counter++;
        }
        
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void setID(int id) {
        this.id=id;
    }

    @Override
    public boolean isOut() {
        return false;
    }
    
    @Override
    public AffineTransform getRot() {
        return rot;
    }

    @Override
    public Point2D.Double getPos() {
        return pos;
    }
}
