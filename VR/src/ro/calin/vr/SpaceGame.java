package ro.calin.vr;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.app.BaseGame;
import com.jme.bounding.BoundingBox;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.light.PointLight;
import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.state.LightState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.system.JmeException;
import com.jme.util.Timer;

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

	// user input
	private InputHandler input;

	@Override
	protected void cleanup() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initGame() {
		 spaceBox = new SpaceBox("space", 200, 200, 200, display,
		 "/res/env/");
		 scene.attachChild(spaceBox);
		 scene.setCullHint(Spatial.CullHint.Never);

		try {
			Spatial fighterModel = ModelLoader.loadModel("res/fighter.3ds",
					new ModelProcessor() {
						@Override
						public void process(Spatial model) {
							model.setLocalScale(.25f);
							model.setLocalRotation(new Matrix3f(0, 0, -1, 0, 1,
									0, 1, 0, 0).multLocal(new Matrix3f(1, 0, 0,
									0, 0, 1, 0, -1, 0)));
						}
					});
			fighter = new Fighter("fighter", fighterModel, cam);
			
//			Spatial enemyModel = ModelLoader.loadModel("res/enemy2.3ds", new ModelProcessor() {
//				
//				@Override
//				public void process(Spatial model) {
//					model.setLocalScale(.01f);
//				}
//			});
//			EnemyShip enemyShip = new EnemyShip("ship", enemyModel);
//			enemyShip.setLocalTranslation(new Vector3f(0, 0, -20));
//			enemyShip.setModelBound(new BoundingBox());
//			enemyShip.updateModelBound();
//			scene.attachChild(enemyModel);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error loading model: " + e);
			System.exit(1);
		}

		fighter.setModelBound(new BoundingBox());
		fighter.updateModelBound();
		scene.attachChild(fighter);

		scene.updateGeometricState(0.0f, true);
		scene.updateRenderState();

		input = new FighterHandler(fighter, settings.getRenderer());
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
		light.setLocation(new Vector3f(100, 100, 100));
		light.setEnabled(true);

		/** Attach the light to a lightState and the lightState to rootNode. */
		lightState = display.getRenderer().createLightState();
		lightState.setEnabled(true);
		lightState.attach(light);
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

		input.update(tpf);

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
