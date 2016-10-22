package org.socialforce.app.gui;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import org.socialforce.app.ApplicationListener;
import org.socialforce.app.Scene;
import org.socialforce.app.SocialForceApplication;
import org.socialforce.model.Agent;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Ledenel on 2016/8/23.
 */
public class SimulationPanelMain implements ApplicationListener {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException e) {
            //  e.printStackTrace();
        } catch (InstantiationException e) {
            //  e.printStackTrace();
        } catch (IllegalAccessException e) {
            //  e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            //  e.printStackTrace();
        } finally {
            //UIManager.look
        }
        try {
            JFrame frame = new JFrame("SimulationPanelMain");
            frame.setContentPane(new SimulationPanelMain().root);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        } catch (HeadlessException e) {
            System.out.println("GUI Not Supported on this machine.");
        }

    }

    private JPanel root;
    private JTextField currentScnenTextField;
    private JButton importFromFileButton;
    private JComboBox agentPathFindingComboBox;
    private JButton runButton;
    private JButton pauseButton;
    private JButton stopButton;
    private JPanel sceneContainer;
    private JPanel scene1;
    private JPanel scene2;
    private JPanel scene3;
    private JPanel scene4;
    private JLabel timeUsedLabel;
    private JTextArea logTextArea;
    private JTextField timePerStepTextField;
    private JLabel fpsLabel;

    private void createUIComponents() {
        // TODO: place custom component creation code here
        scene1 = new SceneShower("Scene 1").getRoot();
        scene2 = new SceneShower("Scene 2").getRoot();
        scene3 = new SceneShower("Scene 3").getRoot();
        scene4 = new SceneShower("Scene 4").getRoot();
    }

    public SocialForceApplication getApplication() {
        return application;
    }

    public void setApplication(SocialForceApplication application) {
        this.application = application;
    }

    SocialForceApplication application;

    /**
     * triggered while a agent is escaped.
     *
     * @param scene       the scene where the agent in.
     * @param escapeAgent the escaped agent.
     */
    @Override
    public void onAgentEscape(Scene scene, Agent escapeAgent) {

    }

    /**
     * triggered while the application is start.
     */
    @Override
    public void onStart() {

    }

    /**
     * triggered while the application is stop.
     *
     * @param terminated whether the application is forced to shut down.
     */
    @Override
    public void onStop(boolean terminated) {

    }

    /**
     * triggered while a scene is step-forwarded.
     *
     * @param scene the scene steped-forwarded.
     */
    @Override
    public void onStep(Scene scene) {
        
    }
}
