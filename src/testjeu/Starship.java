/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package testjeu;


import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

/**
 *
 * @author Paul
 */
public class Starship implements Entity {

    
    int id=0;
    
//    private BufferedImage spriteSheet=null;
    private Sprite sprite=null;
    
    public double delay=10;
    
    private static final int STATUS_ENGINE_DOWN=0;
    private static final int STATUS_NORMAL=1;
    private static final int STATUS_BOOST=2;
    private byte inertia=0;
    private int status=0;
//    private int statusLoop=1;
    
    private static final byte ROTATE_STATUS_NONE=0;
    private static final byte ROTATE_STATUS_LEFT=1;
    private static final byte ROTATE_STATUS_RIGHT=2;
    private byte rotateStatus=0;
    private double rotateInertia=0.0;
    
    public boolean isFiring=false;
    public boolean fireSide;
//    public double fireLoop=1;
    private double coolDown=0.1;//time between shots
    private int realCoolDown=0;//frames between shots
    public boolean readyToFire=false;
    
    public AffineTransform rot;
    private AffineTransform speed;
    public Double pos=new Double(0.0,0.0);
    
    public Hitbox hitbox;
    
//        double deltaX1=10;
//        double deltaY1=10;
//        double deltaX2=10;
//        double deltaY2=10;
    
    public Starship(String tilesetURL,int sX, int sY){
        sprite=new Sprite(tilesetURL,sX,sY,delay);
        double[][] hb={{25-sprite.x,24-sprite.y,38-sprite.x,24-sprite.y,63-sprite.x,44-sprite.y,63-sprite.x,49-sprite.y,51-sprite.x,55-sprite.y,12-sprite.x,55-sprite.y,0-sprite.x,49-sprite.y,0-sprite.x,44-sprite.y},{26-sprite.x,24-sprite.y,26-sprite.x,10-sprite.y,30-sprite.x,0-sprite.y,33-sprite.x,0-sprite.y,37-sprite.x,10-sprite.y,37-sprite.x,24-sprite.y}};
        hitbox=new Hitbox(sprite.x,sprite.y,40,hb);
//        spriteX=sX;
//        spriteY=sY;
//        try{
//            spriteSheet= ImageIO.read(new File(tilesetURL));
//        }catch(IOException e){
//            System.out.println("Error : "+e+"couldn't load sprites");
//        }
    }
    
    @Override
    public void spawn(){
        pos.x=(Stardust.PWIDTH)/2;
        pos.y=(Stardust.PHEIGHT)/2;
        rot=new AffineTransform();
        speed = new AffineTransform();
        status=STATUS_ENGINE_DOWN;
    }
    
    @Override
    public void print(Graphics2D g2d) {
        AffineTransform t=new AffineTransform();
        //t.concatenate(AffineTransform.getTranslateInstance(-spriteX/2,-spriteY/2));
        
//        t.translate(spriteX/2,spriteY/2);
//        t.preConcatenate(new AffineTransform(rot));
//        t.translate(-spriteX/2,-spriteY/2);
        //System.out.println(rot.getTranslateX());
        t.concatenate(AffineTransform.getTranslateInstance(pos.x-sprite.x,pos.y-sprite.y));
        t.rotate(rot.getScaleX(),rot.getShearY(),sprite.x,sprite.y);
        //t.rotate(angle,spriteX/2,spriteY/2);
        
        
        g2d.drawLine((int)pos.x, (int)pos.y, (int)pos.x, (int)pos.y);
        g2d.drawString("x="+(int)pos.x+" ; y="+(int)pos.y, Stardust.PWIDTH-100, Stardust.PHEIGHT-5);
        g2d.drawImage(sprite.img, t, null);
        if(Stardust.displayHitbox)hitbox.draw(g2d);
    }

    @Override
    public void updateStatus() {
        if(isFiring) updateFireStatus();
        
        setSprite();
        setPosition();
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
    
    private void setSprite(){
        sprite.setDelay(status==STATUS_ENGINE_DOWN?10:4);
        switch (status){
            case STATUS_ENGINE_DOWN:
                
                sprite.setImgLoop(2,1,5);
                if(readyToFire){
                    sprite.concatenate(sprite.getImg(fireSide?2:1, 3));
                }
                    //sprite=sprsprite.setSprite(0,0);iteSheet.getSubimage(0, 0, spriteX, spriteY);
                
                break;
            case STATUS_NORMAL:
                sprite.setImgLoop(2,1,5);
                if(readyToFire){
                    sprite.concatenate(sprite.getImg(fireSide?2:1, 3));
                }
                break;
            case STATUS_BOOST:
                sprite.setImgLoop(2,2,5);
                if(readyToFire){
                    sprite.concatenate(sprite.getImg(fireSide?2:1, 3));
                }
                break;
            default:
                sprite.setImg(1,1);
                //sprite=spriteSheet.getSubimage(0, 0, spriteX, spriteY);
                status=STATUS_ENGINE_DOWN;
                
        }
        
    }
    
    private void setPosition(){
        //pos=AffineTransform.getTranslateInstance(pos.x,pos.y);
        
        speed.setToIdentity();
        
        switch(rotateStatus){
            case ROTATE_STATUS_NONE:
                if (rotateInertia>0){
                    rot.rotate(-rotateInertia/20*2*Math.PI/(Stardust.FPS));
                    //angle-=rotateInertia/20*2*Math.PI/(Stardust.FPS);
                    rotateInertia--;
                }else if(rotateInertia<0){
                    rot.rotate(-rotateInertia/20*2*Math.PI/(Stardust.FPS));
                    //angle-=rotateInertia/20*2*Math.PI/(Stardust.FPS);
                    rotateInertia++;
                }
                break;
            case ROTATE_STATUS_LEFT:
                if (rotateInertia<20){
                    rot.rotate(-rotateInertia/20*2*Math.PI/(Stardust.FPS));
                    //angle-=rotateInertia/20*2*Math.PI/(Stardust.FPS);
                    rotateInertia++;
                }else{
                    rot.rotate(-2*Math.PI/(Stardust.FPS));
                    //angle-=2*Math.PI/(Stardust.FPS);
                }
                break;
            case ROTATE_STATUS_RIGHT:
                if (rotateInertia>(-20)){
                    rot.rotate(-rotateInertia/20*2*Math.PI/(Stardust.FPS));
                    //angle-=rotateInertia/20*2*Math.PI/(Stardust.FPS);
                    rotateInertia--;
                }else{
                    rot.rotate(2*Math.PI/(Stardust.FPS));
                    //angle+=2*Math.PI/(Stardust.FPS);
                }
                
                break;
            default:
                rotateStatus=ROTATE_STATUS_NONE;
        }
        
        switch(status){
            case STATUS_NORMAL:
                if(inertia<20){
                    speed.translate(0,-60*inertia/(4*Stardust.FPS));
                    
                    //pos.y-=60*inertia/(4*Stardust.FPS);
                    inertia++;
                }else if(inertia>20){
                    speed.translate(0,-60*inertia/(4*Stardust.FPS));
                    //pos.y-=60*inertia/(4*Stardust.FPS);
                    inertia--;
                }else{
                    speed.translate(0,-300/(Stardust.FPS));
                    //pos.y-=300/Stardust.FPS;
                }

                break;
            case STATUS_BOOST:
                if(inertia<40){
                    speed.translate(0,-60*inertia/(4*Stardust.FPS));
                    //pos.y-=60*inertia/(4*Stardust.FPS);
                    inertia++;
                }else{
                    speed.translate(0,-600/(Stardust.FPS));
                    //pos.y-=600/Stardust.FPS;
                }
                break;
            case STATUS_ENGINE_DOWN:
                if(inertia>0){
                    speed.translate(0,-60*inertia/(4*Stardust.FPS));
                    //pos.y-=60*inertia/(4*Stardust.FPS);
                    inertia--;
                }
            break;
        }
        
        speed.preConcatenate(rot);
        pos.x+=speed.getTranslateX();
        pos.y+=speed.getTranslateY();
        
//        deltaX1=10-pos.x;
//        deltaY1=10-pos.y;
//        deltaX2=Stardust.PWIDTH-pos.x-10;
//        deltaY2=Stardust.PWIDTH-pos.y-10;
        
    }
    
    public void startEngine(){
        status=STATUS_NORMAL;
    }
    public void stopEngine(){
        status=STATUS_ENGINE_DOWN;
    }
    public void startBoost(){
        if (status==STATUS_NORMAL){
            status=STATUS_BOOST;
        }
    }
    public void stopBoost(){
        if(status==STATUS_BOOST){
            status=STATUS_NORMAL;
        }
    }
    public void rotateRight(){
        rotateStatus=ROTATE_STATUS_RIGHT;
    }
    public void rotateLeft(){
        rotateStatus=ROTATE_STATUS_LEFT;
    }
    public void stopRotation(){
        rotateStatus=ROTATE_STATUS_NONE;
    }
    public void fire(){
        isFiring=true;
        
    }
    public void stopFire(){
        isFiring=false;
        
        readyToFire=false;
        realCoolDown=0;
    }
    
    @Override
    public boolean isOut(){
        boolean ret=false;
        if (pos.x<10){
            pos.x=10;
            ret=true;
        } else if (pos.x>Stardust.PWIDTH-10){
            pos.x=Stardust.PWIDTH-10;
            ret=true;
        }
        if (pos.y<10){
            pos.y=10;
            ret=true;
        } else if (pos.y>Stardust.PHEIGHT-10){
            pos.y=Stardust.PHEIGHT-10;
            ret=true;
        }
        return ret;
    }
    
    private void updateFireStatus(){
        if(realCoolDown==0){
            fireSide=!fireSide;
            readyToFire=true;
            realCoolDown=(int) (coolDown*Stardust.FPS);
        }else{
            realCoolDown--;
            readyToFire=false;
        }
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
