/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package testjeu;

//import java.awt.geom.AffineTransform;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;


/**
 *
 * @author Paul
 */
public interface Entity {
    void print(Graphics2D g2d);
    void spawn();
    void updateStatus();
    AffineTransform getRot();
    Point2D.Double getPos();
    int getID();
    void setID(int id);
    boolean isOut();
}
