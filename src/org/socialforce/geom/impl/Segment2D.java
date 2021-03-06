package org.socialforce.geom.impl;

import org.socialforce.geom.*;
import org.socialforce.drawer.Drawer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**这是一个二维的线段
 *
 * 有两个点定义.
 * Created by Whatever on 2016/8/10.
 */
public class Segment2D extends Shape2D implements PhysicalEntity {
    //protected Point2D a, b;
    //protected double k1, b1, startX, endX;
    protected double x1, x2, y1, y2;
    protected Drawer drawer;

    @Override
    public Drawer getDrawer() {
        return drawer;
    }

    @Override
    public void setDrawer(Drawer drawer) {
        this.drawer = drawer;
    }

    public Segment2D() {
    }

    public Segment2D(Point2D a, Point2D b) {
        if (a.equals(b)) {
            throw new IllegalArgumentException("a and b can not be the same point");
        }
        //this.a = a;
        //this.b = b;
        if(a.getX() < b.getX()) {
            x1 = a.getX();
            x2 = b.getX();
            y1 = a.getY();
            y2 = b.getY();
        }
        if(a.getX() > b.getX()) {
            x1 = b.getX();
            x2 = a.getX();
            y1 = b.getY();
            y2 = a.getY();
        }

       /* if (x1 != x2) {
            k1 = (y1 - y2) / (x1 - x2);
            b1 = y1 - k1 * x1;
            startX = Math.min(x1, x2);
            endX = Math.max(x1, x2);
        }*/
    }

    public Segment2D(double k1, double b1, double startX, double endX) {
        if (startX == endX) {
            throw new IllegalArgumentException("a and b can not be the same point");
        }
        /*this.k1 = k1;
        this.b1 = b1;
        this.startX = startX;
        this.endX = endX;*/
        x1 = startX;
        x2 = endX;
        y1 = k1 * startX + b1;
        y2 = k1 * endX + b1;
       /* a = new Point2D(startX, k1 * startX + b1);
        b = new Point2D(endX, k1 * endX + b1);*/
    }

    @Override
    public int dimension() {
        return 2;
    }

    @Override
    public boolean contains(Point point) {
        double x = point.getX();
        double y = point.getY();
        double dx1 = x - x1;
        double dx2 = x - x2;
        double dy1 = y - y1;
        double dy2 = y - y2;
        double dx12 = x1 - x2;
        double dy12 = y1 - y2;
        dx1 *= dx1;
        dx2 *= dx2;
        dy1 *= dy1;
        dy2 *= dy2;
        dx12 *= dx12;
        dy12 *= dy12;
        double delta = dx12 + dy12 - dx1 - dy1 - dx2 - dy2;
        return delta >= 0 && Math.abs(4 * (dx1 + dy1) * (dx2 + dy2) - delta * delta) < 1e-15;
    }

    @Override
    public double getDistance(Point point) {
        double x = point.getX(), y = point.getY();
        //equation: (x-x1)(y2-y1) - (x2-x1)(y-y1) = 0
        double dy12 = y2 - y1;
        double dx12 = x2 - x1;
        double dx1 = x - x1;
        double dy1 = y - y1;
        double dot = dx1*dx12+dy1*dy12;
        double len_sq = dx12*dx12+dy12*dy12;
        double scale = dot / len_sq;
        double tx,ty;
        if(scale < 0) {
            tx = x1;
            ty = y1;
        } else if(scale > 1) {
            tx = x2;
            ty = y2;
        } else {
            tx = x1 + scale * dx12;
            ty = y1+scale*dy12;
        }
        tx -= x;
        ty -= y;
        return new Vector2D(-tx,-ty).length();
    }

    public Vector getDirection(Point point){
        double x = point.getX(), y = point.getY();
        //equation: (x-x1)(y2-y1) - (x2-x1)(y-y1) = 0
        double dy12 = y2 - y1;
        double dx12 = x2 - x1;
        double dx1 = x - x1;
        double dy1 = y - y1;
        double dot = dx1*dx12+dy1*dy12;
        double len_sq = dx12*dx12+dy12*dy12;
        double scale = dot / len_sq;
        double tx,ty;
        if(scale < 0) {
            tx = x1;
            ty = y1;
        } else if(scale > 1) {
            tx = x2;
            ty = y2;
        } else {
            tx = x1 + scale * dx12;
            ty = y1+scale*dy12;
        }
        tx -= x;
        ty -= y;
        return new Vector2D(-tx,-ty).getRefVector();
    }

    //  2016/8/19 refactored with x1,y1,x2,y2.;
    @Override
    public Point getReferencePoint() {
        return new Point2D((x1 + x2) / 2, (y1 + y2) / 2);
    }

    @Override
    public Box getBounds() {
        return new Box2D(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
    }

    @Override
    public boolean hits(Box hitbox) {
        boolean flag = false;
        Box2D covBox;
        double xmin,xmax,ymin,ymax;
        if (getBounds().hits(hitbox)){
            covBox = (Box2D) getBounds().intersect(hitbox);
            xmin = covBox.getXmin();
            xmax = covBox.getXmax();
            ymin = covBox.getYmin();
            ymax = covBox.getYmax();
            if (xmin != xmax){
                if (ymin != ymax){
                    if (intersect(new Segment2D(new Point2D(xmin,ymin),new Point2D(xmin,ymax)))){return true;}
                    if (intersect(new Segment2D(new Point2D(xmin,ymax),new Point2D(xmax,ymax)))){return true;}
                    if (intersect(new Segment2D(new Point2D(xmax,ymax),new Point2D(xmax,ymin)))){return true;}
                    if (intersect(new Segment2D(new Point2D(xmin,ymin),new Point2D(xmax,ymin)))){return true;}
                }
                else return intersect(new Segment2D(new Point2D(xmin,ymin),new Point2D(xmax,ymin)));
            }
            else if (ymin != ymax){
                return intersect(new Segment2D(new Point2D(xmin,ymin),new Point2D(xmax,ymax)));
            }
            else return contains(new Point2D(xmin,ymin));
        }
        return false;
    }

    @Override
    public void moveTo(Point location) {
        double movedX, movedY;
        movedX = getReferencePoint().getX() - location.getX();
        movedY = getReferencePoint().getY() - location.getY();
        x1 -= movedX;
        y1 -= movedY;
        x2 -= movedX;
        y2 -= movedY;
        /*a = new Point2D(x1 - movedX, y1 - movedY);
        b = new Point2D(x2 - movedX, y2 - movedY);*/
/*        if (x1 != x2) {
            k1 = (y1 - y2) / (x1 - x2);
            b1 = y1 - k1 * x1;
            startX = Math.min(x1, x2);
            endX = Math.max(x1, x2);
        }*/
    }

    /**
     * * 创建并返回这条线段的副本.
     * “复制”的精确含义可能取决于这条线的类.
     * @return 线的副本
     */
    @Override
    public Segment2D clone() {
        Segment2D cloned = new Segment2D();
        cloned.x1 = x1;
        cloned.x2 = x2;
        cloned.y1 = y1;
        cloned.y2 = y2;
        //Segment2D lineClone = new Segment2D(a, b);
        return cloned;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Segment2D) {
            Segment2D tg = (Segment2D) obj;
            if (Math.abs(x1 - tg.x1) < 1e-15 && Math.abs(y1 - tg.y1) < 1e-15) {
                return Math.abs(x2 - tg.x2) < 1e-15 && Math.abs(y2 - tg.y2) < 1e-15;
            } else {
                return Math.abs(x1 - tg.x2) < 1e-15 && Math.abs(y1 - tg.y2) < 1e-15 && Math.abs(x2 - tg.x1) < 1e-15 && Math.abs(y2 - tg.y1) < 1e-15;
            }
/*if (getReferencePoint().equals(testLine.getReferencePoint()) &&
                    a.distanceTo(testLine.getReferencePoint()) == a.distanceTo(getReferencePoint())
                    && testLine.getDistance(a) == 0) {
                return true;
            } else {
                return false;
            }*/
        }
        return false;
    }
/*
    public double distanceToSegment(Point2D point) {
        double distance, temp;
        distance = getDistance(point);
        temp = a.distanceTo(point);
        if (b.distanceTo(point) < a.distanceTo(point)) {
            temp = b.distanceTo(point);
        }
        if (b.distanceTo(point) * b.distanceTo(point) - a.distanceTo(point) * a.distanceTo(point) + a.distanceTo(b) * a.distanceTo(b) < 0 ||
                -b.distanceTo(point) * b.distanceTo(point) + a.distanceTo(point) * a.distanceTo(point) + a.distanceTo(b) * a.distanceTo(b) < 0) {
            distance = temp;
        }
        return distance;
    }

    public void setParallelX(double Y, double startX, double endX) {
        this.k1 = 0;
        this.b1 = Y;
        this.startX = startX;
        this.endX = endX;
        this.a = new Point2D(startX, Y);
        this.b = new Point2D(endX, Y);
    }

    public void setParallelY(double X, double startY, double endY) {
        this.a = new Point2D(X, startY);
        this.b = new Point2D(X, endY);
        //this.k1 = 10000;
    }
*/
    /*这个方法从来没有被调用过，目前来说不会引发任何的问题，
    主要是怕在set的时候如果k是无穷，只设置ab，没设置k1b1，导致之后直接调用k1b1时出现问题。
    但是目前的方法里暂时没有这种情况出现。
    protected void quiteConvert(Segment2D line){
            if (k1 < 9999) {
            //do nothing
            } else {
                throw new IllegalArgumentException("the slope of line is null or too big, please ues point A and B");
            }
            
        }*/

    public Point2D[] getExtrimePoint(){
        return new Point2D[]{new Point2D(x1,y1),new Point2D(x2,y2)};
    }


    public boolean intersect(Segment2D line){
        double px3 = line.getExtrimePoint()[0].getX();
        double px4 = line.getExtrimePoint()[1].getX();
        double py3 = line.getExtrimePoint()[0].getY();
        double py4 = line.getExtrimePoint()[1].getY();
        boolean flag = false;
        double d = (x2-x1)*(py4-py3) - (y2-y1)*(px4-px3);
        if(d!=0){
            double r = ((y1-py3)*(px4-px3)-(x1-px3)*(py4-py3))/d;
            double s = ((y1-py3)*(x2-x1)-(x1-px3)*(y2-y1))/d;
            if((r>=0) && (r <= 1) && (s >=0) && (s<=1))
            {
                flag = true;
            }
        }
        else {
            if((x1 == x2)&&(px3 != px4)){
                if((x1 >= px3)&&(x1 <= px4))
                    flag = true;
            }
            else if((x1 != x2)&&(px3 == px4)){
                if((px3 >= x1)&&(px3 <= x2))
                    flag = true;
            }
            else{
                if(this.contains(line.getExtrimePoint()[0])||this.contains(line.getExtrimePoint()[1])||line.contains(getExtrimePoint()[0])||line.contains(getExtrimePoint()[1]))
                    flag = true;
            }
        }

        return flag;
    }

    /**
     * 以一个点为圆心旋转整个线段
     * @param center
     * @param angle
     */
    public void spin(Point2D center,double angle){
        Vector2D v1,v2;
        v1 = new Vector2D(x1-center.getX(),y1 - center.getY());
        v2 = new Vector2D(x2-center.getX(),y2 - center.getY());
        v1.rotate(angle);
        v2.rotate(angle);
        x1 = center.getX()+v1.values[0];
        x2 = center.getX()+v2.values[0];
        y1 = center.getX()+v1.values[1];
        y2 = center.getX()+v2.values[1];
    }

    public Rectangle2D flatten(double width){
        return new Rectangle2D((Point2D) getReferencePoint(),getLenth(),width, getAngle());
    }

    public double getLenth(){
        return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
    }

    public double getAngle(){
        if(x1 == x2)
            return Math.PI/2;
        return Math.atan2((y2-y1),(x2-x1));
    }

    public double getK(){
        if(Math.abs(x1-x2) < 1.0e-7)
            return Double.POSITIVE_INFINITY;
        return (y2-y1)/(x2-x1);
    }

    public double getB(){
        return (x1*y2-x2*y1)/(x1-x2);
    }

    @Override
    public PhysicalEntity expandBy(double extent) {
        double angel = this.getAngle();
        x1 -= extent*Math.cos(angel);
        x2 += extent*Math.cos(angel);
        y1 -= extent*Math.sin(angel);
        y2 += extent*Math.sin(angel);
        return this;
    }

    @Override
    public double getArea() {
        return 0;
    }

    public Segment2D[] remove(List<Segment2D> segments){
        /*double[] segmentLeftX = new double[]{};
        HashMap map = new HashMap();
        for(int i = 0; i < segments.length; i++){
            segmentLeftX[i] = (segments[i].getExtrimePoint())[0].getX();
            map.put(segmentLeftX[i],segments[i]);
        }
        Arrays.sort(segmentLeftX);
        Segment2D[] segmentSort = new Segment2D[]{};
        for(int i = 0; i < segmentLeftX.length; i++){
            segmentSort[i] = (Segment2D) map.get(segmentLeftX[i]);
        }*/

        //排序
        double[] leftX = new double[segments.size()];
        double[] tempX = new double[segments.size()];
        double[] rightX = new double[segments.size()];
        HashMap map = new HashMap();
        int i;
        for(i = 0; i < segments.size(); i++){
            leftX[i] = (segments.get(i)).getExtrimePoint()[0].getX();
            tempX[i] = (segments.get(i)).getExtrimePoint()[1].getX();
            map.put(leftX[i],tempX[i]);
        }
        Arrays.sort(leftX);
        for(i = 0; i < leftX.length; i++){
            rightX[i] = (double) map.get(leftX[i]);
        }
        //融合
        int count = 0;
        double[][] blockLine = new double[2][leftX.length];
        blockLine[0][0] = leftX[0];
        blockLine[1][0] = rightX[0];
        for(i = 0; i < (leftX.length-1); i++){
            if(blockLine[1][count] < leftX[i+1]){
                //blockLine[1][count] = rightX[i];
                count++;
                blockLine[0][count] = leftX[i+1];
                blockLine[1][count] = rightX[i+1];
            }
            else{
                if(blockLine[1][count] < rightX[i+1])
                    blockLine[1][count] = rightX[i+1];
                else
                    blockLine[1][count] = rightX[i];
            }
        }
        //去除
        List<Double> pointX = new ArrayList<>();
        pointX.add(this.getExtrimePoint()[0].getX());
        for(i = 0; i < count+1; i++){
            for(int j = 0; j < 2; j++){
                pointX.add(blockLine[j][i]);
            }
        }
        if(blockLine[0][0] <= this.getExtrimePoint()[0].getX()){
            pointX.remove(this.getExtrimePoint()[0].getX());
            pointX.remove(blockLine[0][0]);
        }
        if(blockLine[1][count] >= this.getExtrimePoint()[1].getX())
            pointX.remove(blockLine[1][count]);
        else
            pointX.add(this.getExtrimePoint()[1].getX());
        Segment2D[] restLine = new Segment2D[pointX.size()/2];
        for(i = 0; i < pointX.size()/2; i++){
            restLine[i] = new Segment2D(new Point2D(pointX.get(2*i),pointX.get(2*i)*getK()+getB()),new Point2D(pointX.get(2*i+1),pointX.get(2*i+1)*getK()+getB()));
        }
        return restLine;
    }


}
