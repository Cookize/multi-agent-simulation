package org.socialforce.model;

import org.socialforce.container.Pool;
import org.socialforce.scene.Scene;
import org.socialforce.geom.Point;
import org.socialforce.geom.Shape;

/**
 *表示场景中所有可以进行交互的实体。
 *
 * @author Ledenel
 *         Created by Ledenel on 2016/7/28.
 * @see Scene
 * @see Pool
 * @see Agent
 */
public interface InteractiveEntity {
    Scene getScene();

    void setScene(Scene scene);

    /**
     * 获取实体的名称
     * @return name 名称
     */
    String getName();

    /**
     * 设置实体的名称
     * @param name
     */
    void setName(String name);
    /**
     * 当前this所影响的实体
     * 例如，墙会影响agent(反作用，反推)
     * @param affectedEntity 被影响的实体
     * @see Agent
     * @see SocialForceModel
     */
    void affect(InteractiveEntity affectedEntity);

    /**
     * 获取一个实体的形状
     * 如线，矩形，圆等。
     *
     * @return 实体的形状.
     */
    Shape getShape();

    /**
     * 获取实体的质量。
     * 通常质量位于形状的参考点上（或者是位于质心上）TODO
     * @return the mass.
     */
    double getMass();

    /**
     * 获取社会模型中使用的实体模型
     *
     * @return model 模型
     */
    SocialForceModel getModel();

    /**
     * 设置社会力模型。
     *
     * @param model 模型
     */
    void setModel(SocialForceModel model);

    /**
     * 将该实体放置在一个特殊的点上。
     * TODO the shape will {@code moveTo} that point.
     * @param point 目标点。
     */
    void placeOn(Point point);

    InteractiveEntity standardclone();

}

