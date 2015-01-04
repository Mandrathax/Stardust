/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package testjeu;

import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Paul
 */
public class Background {
    
    public static final byte PATH_STATIONNARY=0;
    public static final byte PATH_CIRCLE=1;
    public static final byte PATH_ELLIPSE=2;
    public static final byte PATH_ROTATION=3;
    
    public byte path; //chemin décrit par l'image d'arrière plan
    private AffineTransform position;
    double anchorX;//coordonnées selon X et Y du centre de rotation
    double anchorY;
    public int pathRadius; // dans le cas d'une ellipse, rayon selon l'axe X
    public double excentricity; //quand le path est une ellipse, rapport rayonY/rayonX
    
    public Image img=null;
    

    public int period; //période de rotation en secondes
    
    
    public Background(String imgURL,int period){
        try {
            img = ImageIO.read(new File(imgURL));
        } catch (IOException ex) {
            System.out.println("Error :"+ ex+"couldn't load background image");
        }
        this.period=period;
        
    }
    
    public void initiateStationnary(int sx, int sy){
        anchorX=sx;
        anchorY=sy;
        position=AffineTransform.getTranslateInstance(-anchorX, -anchorY);
        path=PATH_STATIONNARY;
    }
    
    public void initiateCircle(int pCX, int pCY, int pR){
        anchorX=pCX;
        anchorY=pCY;
        pathRadius=pR;
        position=AffineTransform.getTranslateInstance(pathRadius-anchorX,-anchorY);
        path=PATH_CIRCLE;
    }
    
    public void initiateRotation(int pCX, int pCY, int sx, int sy){
        anchorX=pCX;
        anchorY=pCY;
        position=AffineTransform.getTranslateInstance(-sx, -sy);
        path=PATH_ROTATION;
    }
    
    public void print(Graphics2D g2d){
        //Graphics2D
        g2d.drawImage(img,position, null);
    }
    

    public void calculatePath() {
        switch(path){
            default:
                path=PATH_STATIONNARY;
                position=new AffineTransform();
            case PATH_STATIONNARY:
                break;
            case PATH_CIRCLE:
                double a=Math.cos(Math.PI/(period*Stardust.FPS/2));
                double b=Math.sin(Math.PI/(period*Stardust.FPS/2));
                double sx=position.getTranslateX();
                double sy=position.getTranslateY();
                position.translate((sx+anchorX)*a+(sy+anchorY)*b-anchorX-sx, -(sx+anchorX)*b+(sy+anchorY)*a-anchorY-sy);
                break;
            case PATH_ROTATION:
                position.rotate(Math.PI/(period*Stardust.FPS/2), anchorX, anchorY);
                break;
        }
    }
}
