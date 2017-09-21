package org.socialforce.model.impl;


import org.socialforce.geom.impl.*;
import org.socialforce.model.Agent;
import org.socialforce.model.Influential;

/**
 * Created by Administrator on 2017/9/15 0015.
 */
public class DoorTurn extends Exit implements Influential {

    protected Box2D door;
    protected Vector2D viewPoint;  //门内侧的朝向

    public DoorTurn(Box2D door,Vector2D viewPoint) {
        super(door);
        this.door = door;
        this.viewPoint = viewPoint;
    }

    @Override
    public Semicircle2D getView() {
        Vector2D viewBasic = new Vector2D(0,-1);
        double angle = Vector2D.getRotateAngle(viewPoint,viewBasic);
        Point2D center = new Point2D();
        if(Math.abs(angle)<1.0e-7) {
            center.moveTo((((Box2D)this.getPhysicalEntity()).getXmin()+((Box2D)this.getPhysicalEntity()).getXmax())/2,((Box2D)this.getPhysicalEntity()).getYmin());
            return new Semicircle2D(center,((Vector2D)((Box2D)this.getPhysicalEntity()).getSize()).getX()/2,angle);
        }
        else if(Math.abs(angle-(Math.PI)/2)<1.0e-7){
            center.moveTo(((Box2D)this.getPhysicalEntity()).getXmax(),(((Box2D)this.getPhysicalEntity()).getYmin()+((Box2D)this.getPhysicalEntity()).getYmax())/2);
            return new Semicircle2D(center,((Vector2D)((Box2D)this.getPhysicalEntity()).getSize()).getY()/2,angle);
        }
        else if(Math.abs(angle-Math.PI)<1.0e-7){
            center.moveTo((((Box2D)this.getPhysicalEntity()).getXmin()+((Box2D)this.getPhysicalEntity()).getXmax())/2,((Box2D)this.getPhysicalEntity()).getYmax());
            return new Semicircle2D(center,((Vector2D)((Box2D)this.getPhysicalEntity()).getSize()).getX()/2,angle);
        }
        else{
            center.moveTo(((Box2D)this.getPhysicalEntity()).getXmin(),(((Box2D)this.getPhysicalEntity()).getYmin()+((Box2D)this.getPhysicalEntity()).getYmax())/2);
            return new Semicircle2D(center,((Vector2D)((Box2D)this.getPhysicalEntity()).getSize()).getY()/2,angle);
      }
    }

    @Override
    public void affect(Agent target) {
        target.rotate(model.interactionMoment(this,target));
    }

    @Override
    public DoorTurn clone(){ return new DoorTurn((Box2D)door.clone(),viewPoint.clone()); }

    public Vector2D getViewDirection(){
        return viewPoint;
    }
}