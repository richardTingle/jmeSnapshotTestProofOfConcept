package org.jmonkeyengine.simple;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.app.state.BaseAppState;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import org.jmonkeyengine.TestDriver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

public class SimpleBlueCube{

    @Test
    public void simpleBlueCube(TestInfo testInfo){

        AppState simpleBlueCube = new BaseAppState(){
            @Override
            protected void initialize(Application app){
                Box b = new Box(1, 1, 1); // create cube shape
                Geometry geom = new Geometry("Box", b);  // create cube geometry from the shape
                Material mat = new Material(app.getAssetManager(),
                        "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
                mat.setColor("Color", ColorRGBA.Blue);   // set color of material to blue
                geom.setMaterial(mat);                   // set the cube's material
                ((SimpleApplication)app).getRootNode().attachChild(geom);
            }

            @Override protected void cleanup(Application app){}

            @Override protected void onEnable(){}

            @Override protected void onDisable(){}
        };
        String fullyQualifiedTestName = testInfo.getTestClass().get().getName() + "." + testInfo.getTestMethod().get().getName();

        TestDriver.bootAppForTest(new AppSettings(true),fullyQualifiedTestName,simpleBlueCube);
    }
}
