package org.socialforce.app.impl.preset;

import org.socialforce.app.Scene;
import org.socialforce.app.SceneValue;
import org.socialforce.geom.Box;
import org.socialforce.geom.impl.Box2D;
import org.socialforce.geom.impl.Point2D;
import org.socialforce.model.SocialForceModel;

/**
 * Created by Whatever on 2016/9/16.
 */
public class SVSR_AgentGenerator extends SVSR_Base<SVSR_AgentGenerator.AgentGenerator> {

    /**
     * 用于描述一个刷怪笼
     * 参数为XYZ方向的agent间距，生成人的范围还有在何种模型下生成人。
     */
    protected class AgentGenerator{
        protected double X_distance,Y_distance,Z_distance;
        protected Box Area;
        protected SocialForceModel model;

        public AgentGenerator(double X_distance,double Y_distance,double Z_distance,Box Area){
            this.X_distance = X_distance;
            this.Y_distance = Y_distance;
            this.Z_distance = Z_distance;
            this.Area = Area;
        }

        public double getX_distance(){
            return X_distance;
        }
        public double getY_distance(){
            return Y_distance;
        }
        public double getZ_distance(){
            return Z_distance;
        }
        public Box getArea(){
            return Area;
        }
        public void setModel(SocialForceModel model){
            this.model = model;
        }
    }


    protected AgentGenerator agentGenerator;

    @Override
    public AgentGenerator getValue() {
        return null;
    }

    @Override
    public void setValue(AgentGenerator value) {

    }

    /**
     * 注意生成agent使用的是SocialForceModel里的createAgent方法
     * @param scene 要被更改的场景。
     */
    @Override
    public void apply(Scene scene) {
        if (agentGenerator.Area instanceof Box2D) {
            for (int i = 0; i < (agentGenerator.Area.getStartPoint().getX() - agentGenerator.Area.getEndPoint().getX()) / agentGenerator.X_distance; i++) {
                for (int j = 0; j < (agentGenerator.Area.getStartPoint().getY() - agentGenerator.Area.getEndPoint().getY()) / agentGenerator.Y_distance; j++) {
                    agentGenerator.model.createAgent().getShape().moveTo(new Point2D(agentGenerator.Area.getStartPoint().getX()+i*agentGenerator.X_distance,agentGenerator.Area.getStartPoint().getY()+j*agentGenerator.Y_distance)); }
            }
        }
        else throw new IllegalArgumentException("暂未实现非二维的生成区块");
    }

    @Override
    public int compareTo(SceneValue<AgentGenerator> o) {
        return o.getPriority() - this.getPriority();
    }

}