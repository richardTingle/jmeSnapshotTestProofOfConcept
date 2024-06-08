package org.jmonkeyengine.screenshottests.testframework;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.math.ColorRGBA;

import java.util.logging.Level;
import java.util.logging.Logger;

public class App extends SimpleApplication {

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    public App(AppState... initialStates){
        super(initialStates);
    }

    @Override
    public void simpleInitApp(){
        getViewPort().setBackgroundColor(ColorRGBA.Black);
        setTimer(new VideoRecorderAppState.IsoTimer(60));
    }

    @Override
    public void handleError(String errMsg, Throwable t){
        LOGGER.log(Level.SEVERE, "an exception was thrown", t);
        t.printStackTrace();
        super.handleError(errMsg, t);
    }
}