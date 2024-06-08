package org.jmonkeyengine.screenshottests.effects;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import org.jmonkeyengine.screenshottests.testframework.ExtentReportExtension;
import org.jmonkeyengine.screenshottests.testframework.ScreenshotTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ExtentReportExtension.class)
public class TestExplosionEffect{

    /**
     * This test's particle effects (using an explosion)
     */
    @Test
    public void testExplosionEffect(TestInfo testInfo){
        new ScreenshotTest(new BaseAppState(){
            private ParticleEmitter flame, flash, spark, roundspark, smoketrail, debris,
                    shockwave;

            private float time = 0;
            private int state = 0;

            final private Node explosionEffect = new Node("explosionFX");

            @Override
            protected void initialize(Application app){
                createFlame();
                createFlash();
                createSpark();
                createRoundSpark();
                createSmokeTrail();
                createDebris();
                createShockwave();
                explosionEffect.setLocalScale(0.5f);
                app.getRenderManager().preloadScene(explosionEffect);

                Camera camera = app.getCamera();
                camera.setLocation(new Vector3f(0, 3.5135868f, 10));
                camera.setRotation(new Quaternion(1.5714673E-4f, 0.98696727f, -0.16091813f, 9.6381607E-4f));
                Node rootNode = ((SimpleApplication)app).getRootNode();
                rootNode.attachChild(explosionEffect);
            }

            @Override
            protected void cleanup(Application app){}

            @Override
            protected void onEnable(){}

            @Override
            protected void onDisable(){}

            private void createFlame(){
                flame = new ParticleEmitter("Flame", ParticleMesh.Type.Point, 32);
                flame.setSelectRandomImage(true);
                flame.setStartColor(new ColorRGBA(1f, 0.4f, 0.05f, 1f ));
                flame.setEndColor(new ColorRGBA(.4f, .22f, .12f, 0f));
                flame.setStartSize(1.3f);
                flame.setEndSize(2f);
                flame.setShape(new EmitterSphereShape(Vector3f.ZERO, 1f));
                flame.setParticlesPerSec(0);
                flame.setGravity(0, -5, 0);
                flame.setLowLife(.4f);
                flame.setHighLife(.5f);
                flame.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 7, 0));
                flame.getParticleInfluencer().setVelocityVariation(1f);
                flame.setImagesX(2);
                flame.setImagesY(2);

                AssetManager assetManager = getApplication().getAssetManager();
                Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
                mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
                mat.setBoolean("PointSprite", true);
                flame.setMaterial(mat);
                explosionEffect.attachChild(flame);
            }

            private void createFlash(){
                AssetManager assetManager = getApplication().getAssetManager();
                flash = new ParticleEmitter("Flash", ParticleMesh.Type.Point, 24 );
                flash.setSelectRandomImage(true);
                flash.setStartColor(new ColorRGBA(1f, 0.8f, 0.36f, 1f ));
                flash.setEndColor(new ColorRGBA(1f, 0.8f, 0.36f, 0f));
                flash.setStartSize(.1f);
                flash.setEndSize(3.0f);
                flash.setShape(new EmitterSphereShape(Vector3f.ZERO, .05f));
                flash.setParticlesPerSec(0);
                flash.setGravity(0, 0, 0);
                flash.setLowLife(.2f);
                flash.setHighLife(.2f);
                flash.getParticleInfluencer()
                        .setInitialVelocity(new Vector3f(0, 5f, 0));
                flash.getParticleInfluencer().setVelocityVariation(1);
                flash.setImagesX(2);
                flash.setImagesY(2);
                Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
                mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flash.png"));
                mat.setBoolean("PointSprite", true);
                flash.setMaterial(mat);
                explosionEffect.attachChild(flash);
            }

            private void createRoundSpark(){
                AssetManager assetManager = getApplication().getAssetManager();
                roundspark = new ParticleEmitter("RoundSpark", ParticleMesh.Type.Point, 20 );
                roundspark.setStartColor(new ColorRGBA(1f, 0.29f, 0.34f, (float) (1.0 )));
                roundspark.setEndColor(new ColorRGBA(0, 0, 0, 0.5f ));
                roundspark.setStartSize(1.2f);
                roundspark.setEndSize(1.8f);
                roundspark.setShape(new EmitterSphereShape(Vector3f.ZERO, 2f));
                roundspark.setParticlesPerSec(0);
                roundspark.setGravity(0, -.5f, 0);
                roundspark.setLowLife(1.8f);
                roundspark.setHighLife(2f);
                roundspark.getParticleInfluencer()
                        .setInitialVelocity(new Vector3f(0, 3, 0));
                roundspark.getParticleInfluencer().setVelocityVariation(.5f);
                roundspark.setImagesX(1);
                roundspark.setImagesY(1);
                Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
                mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/roundspark.png"));
                mat.setBoolean("PointSprite", true);
                roundspark.setMaterial(mat);
                explosionEffect.attachChild(roundspark);
            }

            private void createSpark(){
                AssetManager assetManager = getApplication().getAssetManager();
                spark = new ParticleEmitter("Spark", ParticleMesh.Type.Triangle, 30 );
                spark.setStartColor(new ColorRGBA(1f, 0.8f, 0.36f, 1.0f));
                spark.setEndColor(new ColorRGBA(1f, 0.8f, 0.36f, 0f));
                spark.setStartSize(.5f);
                spark.setEndSize(.5f);
                spark.setFacingVelocity(true);
                spark.setParticlesPerSec(0);
                spark.setGravity(0, 5, 0);
                spark.setLowLife(1.1f);
                spark.setHighLife(1.5f);
                spark.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 20, 0));
                spark.getParticleInfluencer().setVelocityVariation(1);
                spark.setImagesX(1);
                spark.setImagesY(1);
                Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
                mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/spark.png"));
                spark.setMaterial(mat);
                explosionEffect.attachChild(spark);
            }

            private void createSmokeTrail(){
                AssetManager assetManager = getApplication().getAssetManager();
                smoketrail = new ParticleEmitter("SmokeTrail", ParticleMesh.Type.Triangle, 22 );
                smoketrail.setStartColor(new ColorRGBA(1f, 0.8f, 0.36f, 1.0f ));
                smoketrail.setEndColor(new ColorRGBA(1f, 0.8f, 0.36f, 0f));
                smoketrail.setStartSize(.2f);
                smoketrail.setEndSize(1f);

                smoketrail.setFacingVelocity(true);
                smoketrail.setParticlesPerSec(0);
                smoketrail.setGravity(0, 1, 0);
                smoketrail.setLowLife(.4f);
                smoketrail.setHighLife(.5f);
                smoketrail.getParticleInfluencer()
                        .setInitialVelocity(new Vector3f(0, 12, 0));
                smoketrail.getParticleInfluencer().setVelocityVariation(1);
                smoketrail.setImagesX(1);
                smoketrail.setImagesY(3);
                Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
                mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/smoketrail.png"));
                smoketrail.setMaterial(mat);
                explosionEffect.attachChild(smoketrail);
            }

            private void createDebris(){
                AssetManager assetManager = getApplication().getAssetManager();
                debris = new ParticleEmitter("Debris", ParticleMesh.Type.Triangle, 15 );
                debris.setSelectRandomImage(true);
                debris.setRandomAngle(true);
                debris.setRotateSpeed(FastMath.TWO_PI * 4);
                debris.setStartColor(new ColorRGBA(1f, 0.59f, 0.28f, 1.0f ));
                debris.setEndColor(new ColorRGBA(.5f, 0.5f, 0.5f, 0f));
                debris.setStartSize(.2f);
                debris.setEndSize(.2f);

//        debris.setShape(new EmitterSphereShape(Vector3f.ZERO, .05f));
                debris.setParticlesPerSec(0);
                debris.setGravity(0, 12f, 0);
                debris.setLowLife(1.4f);
                debris.setHighLife(1.5f);
                debris.getParticleInfluencer()
                        .setInitialVelocity(new Vector3f(0, 15, 0));
                debris.getParticleInfluencer().setVelocityVariation(.60f);
                debris.setImagesX(3);
                debris.setImagesY(3);
                Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
                mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/Debris.png"));
                debris.setMaterial(mat);
                explosionEffect.attachChild(debris);
            }

            private void createShockwave(){
                AssetManager assetManager = getApplication().getAssetManager();
                shockwave = new ParticleEmitter("Shockwave", ParticleMesh.Type.Triangle, 1);
//        shockwave.setRandomAngle(true);
                shockwave.setFaceNormal(Vector3f.UNIT_Y);
                shockwave.setStartColor(new ColorRGBA(.48f, 0.17f, 0.01f, .8f));
                shockwave.setEndColor(new ColorRGBA(.48f, 0.17f, 0.01f, 0f));

                shockwave.setStartSize(0f);
                shockwave.setEndSize(7f);

                shockwave.setParticlesPerSec(0);
                shockwave.setGravity(0, 0, 0);
                shockwave.setLowLife(0.5f);
                shockwave.setHighLife(0.5f);
                shockwave.getParticleInfluencer()
                        .setInitialVelocity(new Vector3f(0, 0, 0));
                shockwave.getParticleInfluencer().setVelocityVariation(0f);
                shockwave.setImagesX(1);
                shockwave.setImagesY(1);
                Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
                mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/shockwave.png"));
                shockwave.setMaterial(mat);
                explosionEffect.attachChild(shockwave);
            }
            @Override
            public void update(float tpf){
                time += tpf;
                if (state == 0){
                    flash.emitAllParticles();
                    spark.emitAllParticles();
                    smoketrail.emitAllParticles();
                    debris.emitAllParticles();
                    shockwave.emitAllParticles();
                    state++;
                }
                if (state == 1){
                    flame.emitAllParticles();
                    roundspark.emitAllParticles();
                    state++;
                }
            }

        }).setFramesToTakeScreenshotsOn(2,15)
          .run();
    }

}
