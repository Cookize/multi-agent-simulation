package org.socialforce.model.impl;

import org.socialforce.geom.Affection;
import org.socialforce.geom.impl.*;
import org.socialforce.model.Agent;
import org.socialforce.model.ForceRegulation;
import org.socialforce.model.InteractiveEntity;

/**
 * Created by Whatever on 2016/10/23.
 */
public class WallForce implements ForceRegulation {
    /**
     * 判断源实体和目标实体之间是否有作用力。
     *
     * @param source
     * @param target
     * @return true 当源实体和目标实体之间是有作用力时返回真。
     */
    @Override
    public boolean hasForce(InteractiveEntity source, InteractiveEntity target) {
        if (source instanceof Wall && target instanceof Agent &&
                ((Agent) target).getView().hits(source.getPhysicalEntity().getBounds())){
            return true;
        }
        else
            return false;
    }


    /**
     * 获取源实体和目标实体之间的作用力。
     *
     * @param source
     * @param target
     * @return force
     */
    @Override
    public Affection getForce(InteractiveEntity source, InteractiveEntity target) {
        if (source.getPhysicalEntity().hits(target.getPhysicalEntity().getBounds())
                && ((Box2D)source.getPhysicalEntity()).intersect(target.getPhysicalEntity().getBounds()).getReferencePoint() != null){
            Point2D ForcePoint = (Point2D) ((Box2D)source.getPhysicalEntity()).intersect(target.getPhysicalEntity().getBounds()).getReferencePoint();
            double x = ForcePoint.getX() - target.getPhysicalEntity().getReferencePoint().getX();
            double y = ForcePoint.getY() - target.getPhysicalEntity().getReferencePoint().getY();
            Vector2D vector2D = new Vector2D(x,y);
            vector2D = (Vector2D) vector2D.getRefVector();
            vector2D.scale(-500);
            double[] force = new double[2];
            vector2D.get(force);
            return new Force2D(force[0],force[1]);
        }
        else return new Force2D(0,0);
    }
}
