package org.socialforce.model.impl;

import org.socialforce.container.Pool;
import org.socialforce.geom.*;
import org.socialforce.geom.impl.*;
import org.socialforce.model.*;

import java.util.LinkedList;
import java.util.List;

/**定义SimpleSocialForceModel类，其实现了接口SocialForceModel的方法
 * Created by Ledenel on 2016/8/17.
 */
public class SimpleForceModel implements Model {
    double TIME_PER_STEP = 0.008;
    double EXPECTED_SPEED = 1.5;
    double EXPECTED_PALSTANCE = 0;
    double REACT_TIME = 0.5;

    long psyT = 0, bodyT = 0, flT = 0;
    int psyN = 0, bodyN = 0, flN = 0;

    protected List<ForceRegulation> regulations;

    public SimpleForceModel() {
        regulations = new LinkedList<>();
        regulations.add(new PsychologicalForceRegulation(Blockable.class, Agent.class, this));
        regulations.add(new BodyForce(Blockable.class, Agent.class, this));
        regulations.add(new DoorForce(Agent.class, Door.class, this));
        regulations.add(new GravityRegulation(Star_Planet.class, Star_Planet.class, this));
        regulations.add(new SpinBodyForceRegulation(this));
        regulations.add(new SpinPsyForceRegulation(this));
    }

    public SimpleForceModel(double timePerStep){
        this();
        TIME_PER_STEP = timePerStep;
    }

    /**
     * 计算源实体对目标实体的作用力。
     *
     * @param source 产生作用力的实体。
     * @param target 受力的作用的实体。
     * @return the force. 返回力的大小，其单位是牛。
     */
    @Override
    public Force interactionForce(InteractiveEntity source, InteractiveEntity target) {
        Force force = this.zeroForce();
        for (ForceRegulation regulation : regulations) {
            if (regulation.hasForce(source, target)) {
                Affection temp = regulation.getForce(source, target);
                if (temp instanceof Force){
                force.add((Vector) temp);}
            }
        }
        return force;
    }

    @Override
    public Moment interactionMoment(InteractiveEntity source, InteractiveEntity target) {
        Moment moment = this.zeroMoment();
        for (ForceRegulation regulation : regulations) {
            if (regulation.hasForce(source,target)) {
                Affection temp = regulation.getForce(source, target);
                if (temp instanceof Moment) {
                    moment.add((Moment) temp);
                }
            }
            }
        return moment;
    }

    @Override
    public Vector zeroVector() {
        return new Vector2D(0,0);
    }

    @Override
    public Velocity zeroVelocity() {
        return new Velocity2D(0,0);
    }

    @Override
    public Force zeroForce() {
        return new Force2D(0,0);
    }

    @Override
    public Moment zeroMoment(){return new Moment2D(0);}

    @Override
    public double getTimePerStep() {
        return TIME_PER_STEP;
    }

    public double getExpectedSpeed() {
        return EXPECTED_SPEED;
    }

    public void setExpectedSpeed(double Expect) {
        EXPECTED_SPEED = Expect;
    }

    /**
     * 生成模型的场力--即驱动力。
     *
     * @param sources  获有作用力的实体们。
     * @return the force. 返回力的大小，其单位是牛。
     */
    public void fieldForce(Pool sources) {
        for(Object source : sources){
            if (source instanceof Star_Planet || !(source instanceof Agent)) {
                return;
            }
            Agent agent = (Agent) source;
            Velocity expected = this.zeroVelocity();
            Force force = this.zeroForce();
            Point current = agent.getPhysicalEntity().getReferencePoint(), goal = agent.getPath().nextStep(current);
            expected.sub(current);
            expected.add(goal);
            expected.scale(EXPECTED_SPEED / expected.length());
            force.add(expected);
            force.sub(agent.getVelocity());
            force.scale(agent.getMass() / REACT_TIME);
            agent.push(force);

            if(agent.getPhysicalEntity() instanceof Ellipse2D){
                Moment moment;
                Palstance p = agent.getPalstance().clone(), expectP = new Palstance2D(EXPECTED_PALSTANCE);
                p.scale(-1);
                expectP.add(p);
                moment = new Moment2D((expectP.getOmega() * agent.getIntetia()) / REACT_TIME);
                agent.rotate(moment);
            }
        }
    }

    private <T extends InteractiveEntity> T reg(T entity) {
        entity.setModel(this);
        return entity;
    }

    public Model clone(){
        return new SimpleForceModel();
    }
}