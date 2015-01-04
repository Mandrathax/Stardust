/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package testjeu;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Paul
 */
public class Sprite {
    private BufferedImage sheet;
    public BufferedImage img;
    public double x=0;//coordonnée selon x du centre de l'image
    public double y=0;
    private int gridX=1;
    private int gridY=1;
    
    public double delay=0;
    private int loopCount;
    
    public Sprite(String url,double sizeX,double sizeY,double delay){
        x=sizeX/2;
        y=sizeY/2;
        try{
            sheet= ImageIO.read(new File(url));
            gridX=(int) (sheet.getWidth()/sizeX);
            gridY=(int) (sheet.getHeight()/sizeY);
        }catch(IOException e){
            System.out.println("Error : "+e+"couldn't load spritesheet "+ url);
        }
        this.delay=delay;
        setImg(1, 2);
    }
    
    public Sprite(String url){
        try{
            img= ImageIO.read(new File(url));
            x= img.getWidth(null)/2;
            y=img.getHeight(null)/2;
        }catch(IOException e){
            System.out.println("Error : "+e+"couldn't load sprite "+ url);
        }
        
    }
//    public Sprite(Image img){
//        img.clone();
//        
//    }
    
    public void setImgLoop(int a, int b, int loop){
        //int loopCount=0;
        //img.getGraphics().clearRect(0, 0, img.getWidth(null), img.getHeight(null));
        img=deepCopy(sheet.getSubimage(0, 0, (int)(2*x), (int)(2*y)));
//        if(delayCount>=delay){
//            loopCount=(loopCount+1)%loop;
//            delayCount=0;
//        }else{
            loopCount=(loopCount+1)%((int)(loop*delay));
//        }
        img.getGraphics().drawImage(sheet.getSubimage((int)(2*x)*((a+(int)(loopCount/delay)-1)%gridX), (int)(2*y)*((b-1)%gridY), (int)(2*x), (int)(2*y)),0,0,null);
        
    
    }
    public void setImg(int a, int b){
        img=deepCopy(sheet.getSubimage(0, 0, (int)(2*x), (int)(2*y)));
        img.getGraphics().drawImage(sheet.getSubimage((int)(2*x)*((a-1)%gridX), (int)(2*y)*((b-1)%gridY), (int)(2*x), (int)(2*y)),0,0,null);
    }
    
    public BufferedImage getImg(int a, int b){
        return sheet.getSubimage((int)(2*x)*((a-1)%gridX), (int)(2*y)*((b-1)%gridY), (int)(2*x), (int)(2*y));
    }
    
    public void setDelay(double d){
        delay=d;
    }
    
    public void concatenate(Image image){
        img.getGraphics().drawImage(image, 0, 0, null);
        
    }
    public static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
         
    }

    double getDelay() {
        return delay;
    }
}
