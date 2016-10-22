package org.socialforce.app;

import org.socialforce.app.impl.preset.SVSR_AgentGenerator;
import org.socialforce.app.impl.preset.SVSR_Exit;
import org.socialforce.app.impl.preset.SVSR_SafetyRegion;
import org.socialforce.app.impl.preset.SquareRoomLoader;
import org.socialforce.geom.impl.Box2D;
import org.socialforce.geom.impl.Point2D;
import org.socialforce.model.Agent;
import org.socialforce.model.PathFinder;
import org.socialforce.model.SocialForceModel;
import org.socialforce.model.impl.SafetyRegion;
import org.socialforce.model.impl.StraightPath;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by Whatever on 2016/10/22.
 */
public class SimpleApplication implements SocialForceApplication {
    public SimpleApplication() {
        singleScene = loader.readScene();
        singleScene.setApplication(this);
    }

    /**
     * start the application immediately.
     */
    @Override
    public void start() {
        SVSR_Exit exit = new SVSR_Exit();
        SVSR_AgentGenerator agentGenerator = new SVSR_AgentGenerator(3,3,1,new Box2D(3,3,10,10));
        SVSR_SafetyRegion safetyRegion = new SVSR_SafetyRegion();
        exit.setValue((new Box2D[]{new Box2D(-1,5,4,6),new Box2D(10,-1,6,4),new Box2D(10,14,6,4),new Box2D(24,6,4,6)}));
        exit.apply(singleScene);
        agentGenerator.apply(singleScene);
        safetyRegion.setValue(new SafetyRegion(new Box2D(-1,5,-4,2)));
        safetyRegion.apply(singleScene);
        safetyRegion.setValue(new SafetyRegion(new Box2D(10,-1,2,-4)));
        safetyRegion.apply(singleScene);
        safetyRegion.setValue(new SafetyRegion(new Box2D(10,14,2,-4)));
        safetyRegion.apply(singleScene);
        safetyRegion.setValue(new SafetyRegion(new Box2D(24,10,-4,2)));
        safetyRegion.apply(singleScene);
        Agent agent;
        Point2D goal,temp;
        for (Iterator iter = singleScene.getAllAgents().iterator(); iter.hasNext();){
            //给所有agent设置path
            agent = (Agent)iter.next();
            goal = new Point2D(-1,5);
            temp = new Point2D(10,-1);
            if (temp.distanceTo(agent.getShape().getReferencePoint())<goal.distanceTo(agent.getShape().getReferencePoint())){
                goal = temp;
            }
            temp = new Point2D(10,14);
            if (temp.distanceTo(agent.getShape().getReferencePoint())<goal.distanceTo(agent.getShape().getReferencePoint())){
                goal = temp;
            }
            temp = new Point2D(24,10);
            if (temp.distanceTo(agent.getShape().getReferencePoint())<goal.distanceTo(agent.getShape().getReferencePoint())){
                goal = temp;
            }
            agent.setPath(new StraightPath(agent.getShape().getReferencePoint(),goal));
        }

        while (!singleScene.getAllAgents().isEmpty()) {
        singleScene.stepNext();}
    }


    protected SceneLoader loader = new SquareRoomLoader();

    protected SocialForceModel model;
    /**
     * get the social force model the application is using.
     *
     * @return the model.
     */
    @Override
    public SocialForceModel getModel() {
        return model;
    }

    /**
     * set the social force model for the application.
     *
     * @param model the model to be set.
     */
    @Override
    public void setModel(SocialForceModel model) {
        this.model = model;
    }


    protected Scene singleScene = new SimpleScene(new Box2D(-50,-50,100,100));
    /**
     * get all the scenes the applicaion is simulating.
     *
     * @return all scenes to simulate.
     */
    @Override
    public Iterable<Scene> getAllScenes() {
        return Stream.of(singleScene)::iterator;
    }



    protected ApplicationListener listener;
    /**
     * get the application listener for the application.
     *
     * @return the application listener.
     */
    @Override
    public ApplicationListener getApplicationListener() {
        return listener;
    }

    /**
     * set a listener for application events.
     *
     * @param listener the listener to be set.
     */
    @Override
    public void setApplicationListener(ApplicationListener listener) {
        this.listener = listener;
    }

    @Override
    public Scene findScene(ValueSet set) {
        return singleScene;
    }

    protected  List<PathFinder> pathFinder;
    @Override
    public List<PathFinder> getAllPathFinders() {
        return pathFinder;
    }
}
