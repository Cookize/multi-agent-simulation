package org.socialforce.entity.impl;

import org.socialforce.entity.*;

/**
 * Created by Ledenel on 2016/8/14.
 */
public class Gate extends Entity implements InteractiveEntity {
    public Gate(Shape shape) {
        super(shape);
    }

    /**
     * affect the target entity with this.
     * for example, walls can affect an agent (push them).
     *
     * @param affectedEntity the entity to be affected.
     * @see Agent
     * @see SocialForceModel
     */
    @Override
    public void affect(InteractiveEntity affectedEntity) {
        if(affectedEntity instanceof Agent) {
            Agent agent = (Agent)affectedEntity;
            agent.push(model.calcualte(this,agent));
            if(shape.contains(agent.getShape().getReferencePoint())) {
                //agent exited.
                if(listener != null) {
                    listener.onAgentEscape(null,agent); //FIXME: add valid scene.
                }
            }
        }
    }

    public ApplicationListener getListener() {
        return listener;
    }

    public void setListener(ApplicationListener listener) {
        this.listener = listener;
    }

    ApplicationListener listener;

    /**
     * get the total mass of the entity.
     * usually the mass is all on the shape's reference point.
     *
     * @return the mass.
     */
    @Override
    public double getMass() {
        return Double.POSITIVE_INFINITY;
    }
}
