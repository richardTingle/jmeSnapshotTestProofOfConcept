package org.jmonkeyengine;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.app.state.BaseAppState;
import com.jme3.app.state.ScreenshotAppState;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * The test driver allows for controlled interaction between the thread running the application and the thread
 * running the test
 */
public class TestDriver extends BaseAppState{

    private static final Executor executor = Executors.newSingleThreadExecutor( (r) -> {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        return thread;
    });

    int tick = 0;

    ScreenshotAppState screenshotAppState;

    private final Object waitLock = new Object();

    public TestDriver(ScreenshotAppState screenshotAppState){
        this.screenshotAppState = screenshotAppState;
    }

    @Override
    public void update(float tpf){
        super.update(tpf);

        if(tick == 1){
            screenshotAppState.takeScreenshot();
        }
        if(tick == 3){
            getApplication().stop(true);
            synchronized (waitLock) {
                waitLock.notify(); // Release the wait
            }
        }

        tick++;
    }

    @Override protected void initialize(Application app){}

    @Override protected void cleanup(Application app){}

    @Override protected void onEnable(){}

    @Override protected void onDisable(){}

    /**
     * Boots up the application on a separate thread (blocks this thread) and then does the following:
     * - allows 1 update tick to occur, requests a screenshot (during that tick)
     * - on the next tick compares the screenshot to the expected screenshot (if any). Fails the test if they are different
     */
    public static void bootAppForTest(AppSettings appSettings, AppState... initialStates){

        Path imageTempDir;

        try{
            imageTempDir = Files.createTempDirectory("jmeSnapshotTest");
        } catch(IOException e){
            throw new RuntimeException(e);
        }

        ScreenshotAppState screenshotAppState = new ScreenshotAppState(imageTempDir.toString() + "/");

        List<AppState> states = new ArrayList<>(Arrays.asList(initialStates));
        TestDriver testDriver = new TestDriver(screenshotAppState);
        states.add(screenshotAppState);
        states.add(testDriver);

        SimpleApplication app = new App(states.toArray(new AppState[0]));
        app.setSettings(appSettings);
        app.setShowSettings(false);

        executor.execute(() -> app.start(JmeContext.Type.Display));

        synchronized (testDriver.waitLock) {
            try {
                testDriver.waitLock.wait(10000); // Wait for the screenshot to be taken and application to stop
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }

        //search the imageTempDir
        List<Path> imageFiles = new ArrayList<>();
        try{
            Files.list(imageTempDir).forEach(imageFiles::add);
        } catch(IOException e){
            throw new RuntimeException(e);
        }

        if(imageFiles.isEmpty()){
            fail("No screenshot found in the temporary directory.");
        }
        if(imageFiles.size() > 1){
            fail("More than one screenshot found in the temporary directory.");
        }

        Path generatedImage = imageFiles.get(0);
        Path expectedImage = Path.of("src/test/resources/App1.png");

        try {
            BufferedImage img1 = ImageIO.read(generatedImage.toFile());
            BufferedImage img2 = ImageIO.read(expectedImage.toFile());

            if (compareImages(img1, img2)) {
                System.out.println("Images are identical.");
            } else {
                fail("Images are different.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading images", e);
        }
    }
    private static boolean compareImages(BufferedImage img1, BufferedImage img2) {
        if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()) {
            return false;
        }

        for (int y = 0; y < img1.getHeight(); y++) {
            for (int x = 0; x < img1.getWidth(); x++) {
                if (img1.getRGB(x, y) != img2.getRGB(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }

}
