package ro.calin.vr;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.app.BaseGame;
import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingSphere;
import com.jme.bounding.BoundingVolume;
import com.jme.image.Texture;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.light.DirectionalLight;
import com.jme.light.PointLight;
import com.jme.math.FastMath;
import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.system.JmeException;
import com.jme.util.TextureManager;
import com.jme.util.Timer;
import com.jmex.effects.particles.ParticleFactory;
import com.jmex.effects.particles.ParticleMesh;

public class SpaceGame extends BaseGame {
	private static final Logger logger = Logger.getLogger(SpaceGame.class
			.getName());

	private int width, height, depth, freq;
	private boolean fullscreen;
	private Node scene;

	private Camera cam;

	protected Timer timer;

	protected LightState lightState;

	// sky
	private SpaceBox spaceBox;
	// fighter
	private Fighter fighter;
	
	// enemies - make a sync list: you never know
	private List<EnemyShip> enemyShips = Collections
			.synchronizedList(new ArrayList<EnemyShip>());

	// user input
	private InputHandler userInput;
	
	// enemy input
	private InputHandler enemyInput;

	@Override
	protected void cleanup() {
		// TODO Auto-generated method stub

	}
	
	private void scale( Spatial model ) {
        if ( model != null ) {
            // scale model to maximum extent of 5.0
            model.updateGeometricState( 0, true );
            BoundingVolume worldBound = model.getWorldBound();
            if ( worldBound == null ) {
                model.setModelBound( new BoundingBox() );
                model.updateModelBound();
                model.updateGeometricState( 0, true );
                worldBound = model.getWorldBound();
            }
            if ( worldBound != null ) // check not still null (no geoms)
            {
                Vector3f center = worldBound.getCenter();
                BoundingBox boundingBox = new BoundingBox( center, 0, 0, 0 );
                boundingBox.mergeLocal( worldBound );
                Vector3f extent = boundingBox.getExtent( null );
                float maxExtent = Math.max( Math.max( extent.x, extent.y ), extent.z );
                if ( maxExtent != 0 ) {
                    model.setLocalScale( 5.0f / maxExtent );
                }
            }
        }
    }
	
	private ParticleMesh createParticles() {
		
		BlendState as1 = display.getRenderer().createBlendState();
		as1.setBlendEnabled(true);
		as1.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		as1.setDestinationFunction(BlendState.DestinationFunction.One);
		as1.setTestEnabled(true);
		as1.setTestFunction(BlendState.TestFunction.GreaterThan);
		as1.setEnabled(true);

		TextureState ts = display.getRenderer().createTextureState();
		ts.setTexture(TextureManager.loadTexture(SpaceGame.class
				.getClassLoader().getResource("res/textures/flaresmall.jpg"),
				Texture.MinificationFilter.Trilinear,
				Texture.MagnificationFilter.Bilinear));
		ts.setEnabled(true);

		ZBufferState zbuf = display.getRenderer().createZBufferState();
		zbuf.setWritable(false);
		zbuf.setEnabled(true);
		zbuf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);

		ParticleMesh pMesh;
		pMesh = ParticleFactory.buildParticles("particles", 500);
		pMesh.setEmissionDirection(new Vector3f(0, 0, 1));
		pMesh.setInitialVelocity(.01f);
		pMesh.setStartSize(0.5f);
		pMesh.setEndSize(0.5f);
		pMesh.setMinimumLifeTime(150f);
		pMesh.setMaximumLifeTime(200f);
		pMesh.setStartColor(new ColorRGBA(0.9f, 0.9f, 0.9f, 1));
		pMesh.setEndColor(new ColorRGBA(0, 0, 0.6f, 0));
		pMesh.setMaximumAngle(25 * FastMath.DEG_TO_RAD);
		pMesh.getParticleController().setControlFlow(false);
		pMesh.setParticlesInWorldCoords(true);
		pMesh.setRotateWithScene(true);
		pMesh.warmUp(60);

		pMesh.setRenderState(ts);
		pMesh.setRenderState(as1);
		pMesh.setRenderState(zbuf);
		pMesh.setModelBound(new BoundingSphere());
		pMesh.updateModelBound();

		return pMesh;
	}

	@Override
	protected void initGame() {
		spaceBox = new SpaceBox("space", 200, 200, 200, display, "/res/env/");
		scene.attachChild(spaceBox);
		scene.setCullHint(Spatial.CullHint.Never);

		ModelLoader.addModelTextureLocations("res/textures/");
		ModelLoader.addModelLocations("res/models/enemies/", "res/models/");
		try {
			//load player model
			Spatial fighterModel = ModelLoader.loadModel("fighter.3ds", new ModelProcessor() {
						@Override
						public void process(Spatial model) {
							scale(model);
							model.setLocalRotation(new Matrix3f(0, 0, -1, 0, 1,
									0, 1, 0, 0).multLocal(new Matrix3f(1, 0, 0,
									0, 0, 1, 0, -1, 0)));
						}
					});
			fighter = new Fighter("fighter", fighterModel, cam, createParticles(), createParticles());
			
			//load enemy models
			URL[] enemyModelUrls = Utils.getResInPackage("res.models.enemies", null);
			
			for (URL url : enemyModelUrls) {
				EnemyShip.addModel(ModelLoader.loadModel(url, new ModelProcessor() {
					@Override
					public void process(Spatial model) {
						//those have a large scale
						scale(model);
						model.setLocalRotation(new Matrix3f(1, 0, 0, 0, 0,
								1, 0, -1, 0));
					}
				}));
			}
			
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error loading model: " + e);
			System.exit(1);
		}

		fighter.setModelBound(new BoundingBox());
		fighter.updateModelBound();
		scene.attachChild(fighter);

		scene.updateGeometricState(0.0f, true);
		scene.updateRenderState();

		userInput = new FighterHandler(fighter, settings.getRenderer());
		enemyInput = new EnemyHandler(enemyShips, scene, fighter, settings.getRenderer());
		
	}

	@Override
	protected void initSystem() {
		// store the properties information
		width = settings.getWidth();
		height = settings.getHeight();
		depth = settings.getDepth();
		freq = settings.getFrequency();
		fullscreen = settings.isFullscreen();

		try {
			display = DisplaySystem.getDisplaySystem(settings.getRenderer());
			display.createWindow(width, height, depth, freq, fullscreen);
			cam = display.getRenderer().createCamera(width, height);
		} catch (JmeException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// set the background to black
		display.getRenderer().setBackgroundColor(ColorRGBA.black);

		scene = new Node("Root node");

		// ---- LIGHTS
		/** Set up a basic, default light. */
		PointLight light = new PointLight();
		light.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
		light.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
		light.setLocation(new Vector3f(0, 20, -10));
		light.setEnabled(true);
		
		DirectionalLight dr = new DirectionalLight();
        dr.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
        dr.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
        dr.setSpecular(new ColorRGBA(1.0f, 0.0f, 0.0f, 1.0f));
        dr.setDirection(new Vector3f(50, 50, -50));
        dr.setEnabled(true);

		/** Attach the light to a lightState and the lightState to rootNode. */
		lightState = display.getRenderer().createLightState();
		lightState.setEnabled(true);
		lightState.attach(light);
		lightState.attach(dr);
		scene.setRenderState(lightState);
		
		/**
		 * Create a ZBuffer to display pixels closest to the camera above
		 * farther ones.
		 */
		ZBufferState buf = display.getRenderer().createZBufferState();
		buf.setEnabled(true);
		buf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);

		scene.setRenderState(buf);

		// ---- CAMERA
		cam.setFrustumPerspective(45.0f, (float) width / (float) height, 1,
				1000);
		Vector3f loc = new Vector3f(0.0f, 5.0f, 20.0f);
		Vector3f left = new Vector3f(-1.0f, 0.0f, 0.0f);
		Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
		Vector3f dir = new Vector3f(0.0f, 0f, -1.0f);

		// Move our camera to a correct place and orientation.
		cam.setFrame(loc, left, up, dir);
		/** Signal that we've changed our camera's location/frustum. */
		cam.update();

		// ---- ACTION :D
		/** Get a high resolution timer for FPS updates. */
		timer = Timer.getTimer();

		display.getRenderer().setCamera(cam);

		KeyBindingManager.getKeyBindingManager().set("exit",
				KeyInput.KEY_ESCAPE);

	}

	@Override
	protected void reinit() {
		display.recreateWindow(width, height, depth, freq, fullscreen);
	}

	@Override
	protected void render(float arg0) {
		// Clear the screen
		display.getRenderer().clearBuffers();

		// draw next frame
		display.getRenderer().draw(scene);
	}

	@Override
	protected void update(float tpf) {
		// update time, must be called every frame
		timer.update();

		tpf = timer.getTimePerFrame();

		userInput.update(tpf);
		enemyInput.update(tpf);
		
		// handle keys
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("exit")) {
			finished = true;
		}

		// center skybox
		// this is not needed if the camera isn't moved
		// spaceBox.getLocalTranslation().set(cam.getLocation().x,
		// cam.getLocation().y,
		// cam.getLocation().z);
		// spaceBox.updateGeometricState(0, true);

		// update the scene
		scene.updateGeometricState(tpf, true);
	}

}
