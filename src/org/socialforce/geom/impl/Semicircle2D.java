package org.socialforce.geom.impl;

import org.socialforce.drawer.Drawer;
import org.socialforce.geom.*;

/**
 * Created by sunjh1999 on 2016/11/12.
 */
public class Semicircle2D extends Shape2D implements PhysicalEntity  {
    protected Drawer drawer;
    protected Rectangle2D bounding_box;
    protected Circle2D bounding_circle;
    protected Point center;
    protected double radius,angle;

    public Semicircle2D(Point center, double radius, double angle){
        this.radius = radius;
        this.center = center;
        this.angle = angle;
        bounding_circle = new Circle2D(this.center,radius);
        bounding_box = new Rectangle2D(new Point2D(center.getX()-0.5*radius*Math.sin(angle),center.getY()+0.5*radius*Math.cos(angle)),2*radius,radius,angle);
    }
    @Override
    public void setDrawer(Drawer drawer) {
        this.drawer = drawer;
    }

    @Override
    public Drawer getDrawer() {
        return this.drawer;
    }

    @Override
    public int dimension() {
        return 2;
    }

    @Override
    public boolean contains(Point point) {
        return bounding_box.contains(point) && bounding_circle.contains(point);
    }

    /**
     * 距离相等时返回距离半圆的距离
     * @param point 将被检查的点.
     * @return
     */
    @Override
    public double getDistance(Point point) {
        if(bounding_box.getDistance(point) > bounding_circle.getDistance(point))
            return bounding_box.getDistance(point);
        else return bounding_circle.getDistance(point);
    }

    public Vector getDirection(Point point){
        if(bounding_box.getDistance(point) > bounding_circle.getDistance(point))
            return bounding_box.getDirection(point);
        else return bounding_circle.getDirection(point);
    }

    @Override
    public Point getReferencePoint() {
        return center;
    }

    @Override
    public Box getBounds() {
        return bounding_box.getBounds().intersect(bounding_circle.getBounds());
    }

    @Override
    public boolean hits(Box hitbox) {
        return bounding_box.hits(hitbox) && bounding_circle.hits(hitbox);
    }

    @Override
    public void moveTo(Point location) {
         //并不建议在实际应用中用此方法 最好新建一个半圆形区域
        center = location.clone();
        bounding_box.moveTo(new Point2D(center.getX()-0.5*radius*Math.sin(angle),center.getY()+0.5*radius*Math.cos(angle)));
    }

    public void spin(double angle){
        this.angle = this.angle +angle;
    }

    public double getAngle(){
        return angle;
    }

    @Override
    public PhysicalEntity clone() {
        return new Semicircle2D(center, radius, angle);
    }

    @Override
    public PhysicalEntity expandBy(double extent) {
        radius += extent;
        return this;
    }

    @Override
    public double getArea() {
        return Math.PI*radius*radius/2;
    }
}
