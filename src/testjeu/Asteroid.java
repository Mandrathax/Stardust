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
public class Asteroid implements Entity {
    int id;
    
    private Sprite sprite;
    
    int size;
    
    private Point2D.Double pos;
    private AffineTransform selfRot;
    private AffineTransform rot;
    private double speed;
    private double selfRotSpeed;//in turn per seconds
    
    public Hitbox hitbox;
    
    public Asteroid(String spriteURL, int size){
        sprite=new Sprite(spriteURL,40,40,0);
        sprite.setImg((9-size)%9+2, 1);
        //rot=AffineTransform.getRotateInstance(Math.random()*Math.PI);
        pos=new Point2D.Double(0,0);
        selfRotSpeed=Math.random()/2;
        selfRot=new AffineTransform();
        speed=Math.random()*3;
        hitbox=new Hitbox(sprite.x,sprite.y,size+10,null);
        hitbox.update(rot, pos);
    }
    
    @Override
    public void print(Graphics2D g2d) {
        AffineTransform t=AffineTransform.getTranslateInstance(pos.x-sprite.x,pos.y-sprite.y);
        t.rotate(selfRot.getScaleX(), selfRot.getShearY(), sprite.x, sprite.y);
        g2d.drawImage(sprite.img, t, null);
        if(Stardust.displayHitbox)hitbox.draw(g2d);;
    }

    @Override
    public void updateStatus() {
        selfRot.rotate(selfRotSpeed*2*Math.PI/Stardust.FPS);
        rot.translate(0, -speed);
        pos.x+=rot.getTranslateX();
        pos.y+=rot.getTranslateY();
        rot.translate(0, +speed);
        hitbox.update(rot, pos);
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
        double a=Math.random();
        double b=Math.PI/2*Math.random();
        int side=(int) (4*a);
        a=10*a-((int)(10*a));
        switch(side){
            case 0:
                pos.x=-sprite.x;
                pos.y=10+(Stardust.PHEIGHT-20)*a;
                rot=AffineTransform.getRotateInstance(b-3*Math.PI/4);
                break;
            case 1:
                pos.y=-sprite.y;
                pos.x=10+(Stardust.PWIDTH-20)*a;
                rot=AffineTransform.getRotateInstance(b-Math.PI/4);
                break;
            case 2:
                pos.x=Stardust.PWIDTH+sprite.x;
                pos.y=10+(Stardust.PHEIGHT-20)*a;
                rot=AffineTransform.getRotateInstance(b+Math.PI/4);
                break;
            case 3:
                pos.y=Stardust.PHEIGHT+sprite.y;
                pos.x=10+(Stardust.PWIDTH-20)*a;
                rot=AffineTransform.getRotateInstance(b+3*Math.PI/4);
                break;
        }
    }

    @Override
    public boolean isOut() {
        boolean ret=false;
        if (pos.x<-10){
            pos.x=Stardust.PWIDTH+10;
            ret=true;
        } else if (pos.x>Stardust.PWIDTH+10){
            pos.x=-10;
            ret=true;
        }
        if (pos.y<-10){
            pos.y=Stardust.PHEIGHT+10;
            ret=true;
        } else if (pos.y>Stardust.PHEIGHT+10){
            pos.y=-10;
            ret=true;
        }
        return ret;
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
