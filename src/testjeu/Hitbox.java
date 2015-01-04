/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package testjeu;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
/**
 *
 * @author Paul
 */
public class Hitbox {

    
    public static final int ELLIPSE=0;
    public static final int RECT=1;
    
    public double cx;
    public double cy;
    public double r;
    private double[][] subBoxes=null;
    private double[][] sourceSubBoxes=null;
    
    public Hitbox(double centerX, double centerY, double radius, double[][] sB){
        cx=centerX;
        cy=centerY;
        r=radius;
        sourceSubBoxes=sB;
//        for(double[] sb:sourceSubBoxes){
//            for(int k=0;k<sb.length;k+=2){
//                sb[k]-=cx;
//                sb[k+1]-=cy;
//            }
//        }
        if(sB!=null){
            subBoxes=new double[sB.length][];
        
            for (int k=0;k<sourceSubBoxes.length;k++) {
                subBoxes[k]=new double[sourceSubBoxes[k].length];
                for (int l = 0; l < sourceSubBoxes[k].length; l++) {
                    subBoxes[k][l]=sourceSubBoxes[k][l];
                }
            }
        }
        //subBoxes=sourceSubBoxes.clone();
    }
    
    public boolean testCollision(Hitbox hb){
        
        if(Point2D.distance(cx, cy, hb.cx, hb.cy)<r+hb.r){
            if(subBoxes==null){
                if(hb.subBoxes==null) return true;
                for(double[] sb2 : hb.subBoxes){
                    for (int l = 0; l < sb2.length; l+=2) {
                        if(pointInCircle(sb2[l], sb2[l+1],cx, cy, r)) return true;
                    }
                }
            }else{
                for (double[] sb : subBoxes) {
                    for (int l = 0; l < sb.length; l+=2) {
                        if(pointInCircle(sb[l], sb[l+1],hb.cx, hb.cy, hb.r)){
                            if(hb.subBoxes==null)return true;
                            for(double[] sb2 : hb.subBoxes){
                                if(intersectPolygon(sb,sb2)) return true;
                            }
                        }
                    }
                }
            }
            
        }
        return false;
    }
    
    public static boolean pointInCircle(double x, double y,double centX,double centY,double rad){
        return (Point2D.distance(x, y, centX, centY)<rad);
    }
    
    public static boolean intersectPolygon(double[] a,double[] b){;
        
        for(int k=0;k<a.length;k+=2){
            if(isInPolygon(a[k],a[k+1],b)) return true;
        }
        return false;
    }
    
    public static boolean isInPolygon(double x,double y, double[] b){
        
        for(int k=0;k<b.length;k+=2){//parcours des points dans le sens trigo!
            if(Line2D.relativeCCW(x, y, b[k], b[k+1], b[(k+2)%b.length], b[(k+3)%b.length])==-1){
                return false;
            }
        }
        return true;
    }
    
    public void update(AffineTransform rot,Point2D.Double pos){
        cx=pos.x;
        cy=pos.y;
        if(subBoxes!=null){
            for(int k=0;k<subBoxes.length;k++){
            
                rot.transform(sourceSubBoxes[k].clone(), 0, subBoxes[k], 0, subBoxes[k].length/2);
                for(int l=0;l<subBoxes[k].length;l+=2){
                    subBoxes[k][l]+=cx;
                    subBoxes[k][l+1]+=cy;
                }
            //AffineTransform.getTranslateInstance(pos.x, pos.y).transform(subBoxes[k], 0, subBoxes[k], 0, subBoxes[k].length);
            }
        }
        
    }
    
    public void draw(Graphics2D g2d){
        g2d.drawArc((int)(cx-r), (int) (cy-r), 2*(int)r, 2*(int)r, 0, 360);
        if(subBoxes!=null){
            for (double[] sb : subBoxes) {
                for (int l = 0; l < sb.length; l=l+2) {
                    g2d.drawLine((int)sb[l], (int)sb[l+1],(int)sb[(l+2)%sb.length], (int)sb[(l+3)%sb.length]);
                }
            }
        }
        
        
    }
}
