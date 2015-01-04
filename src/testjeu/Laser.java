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
    
public class Laser implements Entity {
    
    int id;
    
    private Starship origin;
    private Sprite sprite;
    
    private Point2D.Double pos;
    private AffineTransform rot;
    private double speed=20;
    
    Hitbox hitbox;
    
    
    //private AffineTransform position;
    
    public Laser(Point2D.Double p, AffineTransform r,boolean side, Sprite sprite){
        //origin=o;
        //position=AffineTransform.getRotateInstance(o.rot.getScaleX(),o.rot.getShearY(),sprite.getWidth(null)/2,sprite.getHeight(null)/2);
        //position=AffineTransform.getTranslateInstance(Stardust.PWIDTH/2,Stardust.PHEIGHT/2);
        pos=(Point2D.Double) p.clone();
        rot=(AffineTransform) r.clone();
        if (side){
            rot.translate(-28,-15-sprite.y);
            pos.x+=rot.getTranslateX();
            pos.y+=rot.getTranslateY();
            rot.translate(28,15+sprite.y);
            //position.concatenate(AffineTransform.getTranslateInstance(-27,-10));
        }else{
            rot.translate(27,-15-sprite.y);
            pos.x+=rot.getTranslateX();
            pos.y+=rot.getTranslateY();
            rot.translate(-27,15+sprite.y);
            //position.concatenate(AffineTransform.getTranslateInstance(28,-10));
        }
        
        //position.translate(o.pos.x+o.spriteX/2,o.pos.y+o.spriteY/2);
        this.sprite=sprite;
        double[][] sb={{-sprite.x,-sprite.y,sprite.x,-sprite.y,sprite.x,sprite.y,-sprite.x,sprite.y}};
        hitbox=new Hitbox(sprite.x,sprite.y,sprite.y,sb);
        hitbox.update(rot, pos);
    }
    @Override
    public void print(Graphics2D g2d) {
        //Double source=(Double)position.transform(new Double(-1,-11),null);
        AffineTransform t=AffineTransform.getTranslateInstance(pos.x-sprite.x,pos.y-sprite.y);
        t.rotate(rot.getScaleX(), rot.getShearY(), sprite.x, sprite.y);
        g2d.drawImage(sprite.img, t, null);
        if(Stardust.displayHitbox)hitbox.draw(g2d);
    }

    @Override
    public void updateStatus() {
        rot.translate(0, -speed);
        pos.x+=rot.getTranslateX();
        pos.y+=rot.getTranslateY();
        rot.translate(0, speed);
        speed=speed+(speed<=12?0:-1);
        hitbox.update(rot, pos);
        //position.concatenate(AffineTransform.getTranslateInstance(0,-12));
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
    public void spawn() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public boolean isOut(){
        return (pos.x<-10)||(pos.x>Stardust.PWIDTH+10)||(pos.y<-10)||(pos.y>Stardust.PHEIGHT+10);
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
