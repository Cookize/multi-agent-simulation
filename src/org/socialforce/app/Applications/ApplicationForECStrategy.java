package org.socialforce.app.Applications;

import org.socialforce.app.*;
import org.socialforce.app.impl.SimpleInterpreter;
import org.socialforce.geom.DistanceShape;
import org.socialforce.scene.*;
import org.socialforce.scene.impl.*;
import org.socialforce.geom.impl.Box2D;
import org.socialforce.geom.impl.Circle2D;
import org.socialforce.geom.impl.Point2D;
import org.socialforce.strategy.DynamicStrategy;
import org.socialforce.strategy.PathFinder;
import org.socialforce.strategy.impl.*;

import java.io.File;
import java.util.Iterator;

import static org.socialforce.scene.SceneLoader.genParameter;

/**
 * Created by Whatever on 2016/12/15.
 */
public class ApplicationForECStrategy extends SimpleApplication implements SocialForceApplication{
    DistanceShape template;

    public ApplicationForECStrategy(){
    }

    /**
     * start the application immediately.
     * TODO start和setUpstrategy重构，将strategy独立于scene
     */
    @Override
    public void start() {
        System.out.println("Application starts!!");
        for (Iterator<Scene> iterator = scenes.iterator(); iterator.hasNext();){
            Scene scene = iterator.next();
            int iteration = 0;
            PathFinder pathFinder = new AStarPathFinder(scene, template);
            strategy = new ECStrategy(scene, pathFinder);
            //strategy = new DynamicLifeBeltStrategy(scene, pathFinder);
            //strategy = new LifeBeltStrategy(scene, pathFinder);
            //strategy = new NearestGoalStrategy(scene, pathFinder);
            strategy.pathDecision();
            long span = System.currentTimeMillis();
            while (scene.getAllAgents().size() > 5) {
                this.StepNext(scene);
                iteration += 1;
                if(iteration % 500 ==0 && strategy instanceof DynamicStrategy){
                    ((DynamicStrategy) strategy).dynamicDecision();
                }
            }
        }
    }

    @Override
    public void setUpScenes(){
        template = new Circle2D(new Point2D(0,0),0.486/2);
        File file = new File("/Users/sunjh1999/IdeaProjects/SocialForceSimulation/test/org/socialforce/app/impl/test.s");
        Interpreter interpreter = new SimpleInterpreter();
        interpreter.loadFile(file);
        SceneLoader loader = interpreter.setLoader();
        ParameterPool parameters = new SimpleParameterPool();
        parameters.addLast(genParameter(new SV_RandomAgentGenerator(405,new Box2D(4,4 ,27.5,15.5),template)));
        parameters.addLast(genParameter((new SV_SafetyRegion(new Box2D(24,2,4,1)))));
        parameters.addLast(genParameter(new SV_SafetyRegion(new Box2D(33,12,1,4))));
        parameters.addLast(genParameter(new SV_SafetyRegion(new Box2D(2,8,1,4))));
        parameters.addLast(genParameter(new SV_SafetyRegion(new Box2D(12,21,4,1))));
        parameters.addLast(genParameter(new SV_Exit(new Box2D[]{new Box2D(new Point2D(25,2), new Point2D(26.75,5)),
                                             new Box2D(new Point2D(31,13.5), new Point2D(34,14.5)),
                                             new Box2D(new Point2D(2,9.5), new Point2D(5,10.25)),
                                             new Box2D(new Point2D(13,19), new Point2D(14.5,22))})));
        loader.readParameterSet(parameters);
        scenes = loader.readScene(this);
    }

}
