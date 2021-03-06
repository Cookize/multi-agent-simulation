package org.socialforce.drawer.impl;

import org.junit.Test;
import org.socialforce.geom.impl.Box2D;
import org.socialforce.test.util.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.*;

/**
 * Created by Ledenel on 2016/8/10.
 */
public class SolidBox2DDrawerTest extends AwtMathDrawerTest {
    SolidBox2DDrawer drawer;

    @Test
    public void renderBoxShape() throws Exception {
        drawer.draw(new Box2D(0, 0, 3, 4));
        ImageUtils.assertImageSimilar(
                ImageIO.read(SolidBox2DDrawerTest.class.getResourceAsStream("SolidBox2DDrawer_base.png")),
                image);

    }

    @Test
    public void renderBoxOverflow() throws Exception {
        drawer.draw(new Box2D(-10, -10, 8, 9));
        ImageUtils.assertImageSimilar(
                ImageIO.read(SolidBox2DDrawerTest.class.getResourceAsStream("SolidBox2DDrawer_base_overflow.png")),
                image);
    }


    @Override
    protected void drawerInit(Graphics2D gra) {
        drawer = new SolidBox2DDrawer(gra);
        drawer.setColor(0xFFFF0000);
    }
}