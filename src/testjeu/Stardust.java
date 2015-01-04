/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package testjeu;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Paul
 */
public class Stardust extends JFrame {
    GamePanel SDPanel;
    static final int PWIDTH=800;
    static final int PHEIGHT=600;
    public static final double FPS=60;
    
    public static boolean displayHitbox=false;
    
    public Stardust(){
        initComponents();
        
        
        //g2d.drawImage(img, AffineTransform.getRotateInstance(0.2, 0, 0),null);
        
        //initComponents();
    }
    
    public static void main(String[] args){
        
        Stardust game=new Stardust();
    }

    private void initComponents() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(PWIDTH+6,PHEIGHT+29);
        setResizable(false);
        getContentPane().add(new GamePanel());
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void initComponentsTest() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(new Dimension(PWIDTH,PHEIGHT));
        getContentPane().add(new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                Graphics2D g2d= (Graphics2D) g;
                Image img=null;
                try {        
                    img = ImageIO.read(new File("starship.gif"));
                    //System.out.println(g==null);
                    //g.fillOval(0, 0, 20, 30);
                    g2d.drawImage(img, AffineTransform.getRotateInstance(10, img.getWidth(null)/2, img.getHeight(null)/2),null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        
        
        setVisible(true);
    }
    
    
}
