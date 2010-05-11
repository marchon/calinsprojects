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
import com.jme.image.Image;
import com.jme.image.Texture;
import com.jme.input.FirstPersonHandler;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.light.LightNode;
import com.jme.light.PointLight;
import com.jme.math.FastMath;
import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.Text;
import com.jme.scene.Spatial.LightCombineMode;
import com.jme.scene.Spatial.TextureCombineMode;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.system.JmeException;
import com.jme.util.TextureManager;
import com.jme.util.Timer;
import com.jme.util.geom.Debugger;
import com.jmex.audio.AudioSystem;
import com.jmex.effects.LensFlare;
import com.jmex.effects.LensFlareFactory;
import com.jmex.effects.particles.ParticleFactory;
import com.jmex.effects.particles.ParticleMesh;

public class SpaceGame extends BaseGame {
	private static final Logger logger = Logger.getLogger(SpaceGame.class
			.getName());

	private static SpaceGame game;

	public static SpaceGame getGame() {
		if (game == null) {
			game = new SpaceGame();
		}

		return game;
	}

	private SpaceGame() {
	}

	private int width, height, depth, freq;
	private boolean fullscreen;
	private Node scene;

	public Node getScene() {
		return scene;
	}

	private Camera cam;

	public Camera getCam() {
		return cam;
	}

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

	private void scale(Spatial model) {
		if (model != null) {
			// scale model to maximum extent of 5.0
			model.updateGeometricState(0, true);
			BoundingVolume worldBound = model.getWorldBound();
			if (worldBound == null) {
				model.setModelBound(new BoundingBox());
				model.updateModelBound();
				model.updateGeometricState(0, true);
				worldBound = model.getWorldBound();
			}
			if (worldBound != null) // check not still null (no geoms)
			{
				Vector3f center = worldBound.getCenter();
				BoundingBox boundingBox = new BoundingBox(center, 0, 0, 0);
				boundingBox.mergeLocal(worldBound);
				Vector3f extent = boundingBox.getExtent(null);
				float maxExtent = Math.max(Math.max(extent.x, extent.y),
						extent.z);
				if (maxExtent != 0) {
					model.setLocalScale(5.0f / maxExtent);
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

	private LightNode createStar() {
		LightNode lightNode;
		LensFlare flare;

		PointLight dr = new PointLight();
		dr.setEnabled(true);
		dr.setDiffuse(ColorRGBA.white.clone());
		dr.setAmbient(ColorRGBA.gray.clone());
		dr.setLocation(new Vector3f(0f, 0f, 0f));

		lightState.attach(dr);
		lightState.setTwoSidedLighting(true);

		lightNode = new LightNode("light");
		lightNode.setLight(dr);

		Sphere lightBox = new Sphere("sun", 30, 30, .01f);
		lightBox.setModelBound(new BoundingBox());
		lightBox.updateModelBound();
		lightNode.attachChild(lightBox);

		// clear the lights from this lightbox so the lightbox itself doesn't
		// get affected by light:
		lightBox.setLightCombineMode(LightCombineMode.Off);

		// Setup the lensflare textures.
		TextureState[] tex = new TextureState[4];
		tex[0] = display.getRenderer().createTextureState();
		tex[0].setTexture(TextureManager.loadTexture(LensFlare.class
				.getClassLoader().getResource("res/textures/flare/flare1.png"),
				Texture.MinificationFilter.Trilinear,
				Texture.MagnificationFilter.Bilinear, Image.Format.RGBA8, 0.0f,
				true));
		tex[0].setEnabled(true);

		tex[1] = display.getRenderer().createTextureState();
		tex[1].setTexture(TextureManager.loadTexture(LensFlare.class
				.getClassLoader().getResource("res/textures/flare/flare2.png"),
				Texture.MinificationFilter.Trilinear,
				Texture.MagnificationFilter.Bilinear));
		tex[1].setEnabled(true);

		tex[2] = display.getRenderer().createTextureState();
		tex[2].setTexture(TextureManager.loadTexture(LensFlare.class
				.getClassLoader().getResource("res/textures/flare/flare3.png"),
				Texture.MinificationFilter.Trilinear,
				Texture.MagnificationFilter.Bilinear));
		tex[2].setEnabled(true);

		tex[3] = display.getRenderer().createTextureState();
		tex[3].setTexture(TextureManager.loadTexture(LensFlare.class
				.getClassLoader().getResource("res/textures/flare/flare4.png"),
				Texture.MinificationFilter.Trilinear,
				Texture.MagnificationFilter.Bilinear));
		tex[3].setEnabled(true);

		flare = LensFlareFactory.createBasicLensFlare("flare", tex);
		lightNode.attachChild(flare);

		return lightNode;
	}

	@Override
	protected void initGame() {
		spaceBox = new SpaceBox("space", 200, 200, 200, display, "/res/env/");
		scene.attachChild(spaceBox);
		scene.setCullHint(Spatial.CullHint.Never);

		ModelLoader.addModelTextureLocations("res/textures/");
		ModelLoader.addModelLocations("res/models/enemies/", "res/models/");

		// the sun/star
		LightNode lightNode = createStar();
		lightNode.setLocalTranslation(new Vector3f(15f, 30f, -50f));
		scene.attachChild(lightNode);

		TrailManager.create(scene);
		ExplosionFactory.warmup();
		
		SoundServer.get().initSound(cam);
		SoundServer.get().loadSound("fire", getClass().getResource("/res/sound/laser.ogg"), false);
		SoundServer.get().loadSound("hit", getClass().getResource("/res/sound/hit.ogg"), false);
		SoundServer.get().loadSound("explosion", getClass().getResource("/res/sound/explosion.wav"), false);
		SoundServer.get().loadSound("jet", getClass().getResource("/res/sound/jet.wav"), true);
		
		try {
			// load player model
			Spatial fighterModel = ModelLoader.loadModel("fighter.3ds",
					new ModelProcessor() {
						@Override
						public void process(Spatial model) {
							scale(model);
							model.setLocalRotation(new Matrix3f(0, 0, -1, 0, 1,
									0, 1, 0, 0).multLocal(new Matrix3f(1, 0, 0,
									0, 0, 1, 0, -1, 0)));
						}
					});
			fighter = new Fighter("fighter", fighterModel, cam,
					createParticles(), createParticles(), 500);

			// load enemy models
			URL[] enemyModelUrls = Utils.getResInPackage("res.models.enemies",
					null);

			for (URL url : enemyModelUrls) {
				EnemyShip.addModel(ModelLoader.loadModel(url,
						new ModelProcessor() {
							@Override
							public void process(Spatial model) {
								// those have a large scale
								scale(model);
								model.setLocalRotation(new Matrix3f(1, 0, 0, 0,
										0, 1, 0, -1, 0));
							}
						}));
			}

		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error loading model: " + e);
			System.exit(1);
		}

		fighter.setModelBound(new BoundingBox());
		fighter.updateModelBound();
		//SoundServer.get().playSound("jet", fighter.getWorldTranslation(), 0.15f);
		
		scene.attachChild(fighter);

		scene.updateGeometricState(0.0f, true);
		scene.updateRenderState();

		userInput = new FighterHandler(fighter, settings.getRenderer());
		enemyInput = EnemyHandler.createEnemyHandler(enemyShips, scene,
				fighter, settings.getRenderer());

		// prepare debug stuff
		fps = Text.createDefaultTextLabel("FPS label", "");
		fps.setCullHint(Spatial.CullHint.Never);
		fps.setTextureCombineMode(TextureCombineMode.Replace);

		BlendState as1 = display.getRenderer().createBlendState();
		as1.setBlendEnabled(true);
		as1.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		as1.setDestinationFunction(BlendState.DestinationFunction.One);
		as1.setTestEnabled(true);
		as1.setTestFunction(BlendState.TestFunction.GreaterThan);
		as1.setEnabled(true);
		TextureState font = display.getRenderer().createTextureState();
		/** The texture is loaded from fontLocation */
		font.setTexture(TextureManager.loadTexture(SpaceGame.class
				.getClassLoader().getResource("com/jme/app/defaultfont.tga"),
				Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear));
		font.setEnabled(true);

		fps.setRenderState(font);
		fps.setRenderState(as1);
		fps.setCullHint(Spatial.CullHint.Never);
		fps.updateGeometricState(0.0f, true);
		fps.updateRenderState();

		KeyBindingManager.getKeyBindingManager()
				.set("debug", KeyInput.KEY_BACK);
		KeyBindingManager.getKeyBindingManager().set("bounds",
				KeyInput.KEY_EQUALS);
		KeyBindingManager.getKeyBindingManager().set("pause",
				KeyInput.KEY_MINUS);
	}

	private void prepareCamera(Camera cam) {
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

			// debug camera
			dCam = display.getRenderer().createCamera(width, height);
		} catch (JmeException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// set the background to black
		display.getRenderer().setBackgroundColor(ColorRGBA.black.clone());

		scene = new Node("Root node");

		// ---- LIGHTS
		/** Set up a basic, default light. */
		PointLight light = new PointLight();
		light.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
		light.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
		light.setLocation(new Vector3f(0, 20, -10));
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
		prepareCamera(cam);

		// ---- ACTION :D
		/** Get a high resolution timer for FPS updates. */
		timer = Timer.getTimer();

		display.getRenderer().setCamera(cam);

		KeyBindingManager.getKeyBindingManager().set("exit",
				KeyInput.KEY_ESCAPE);

		// this is for debug
		prepareCamera(dCam);
		dInputHandler = new FirstPersonHandler(dCam, .25f, .005f);
		dInputHandler.getKeyboardLookHandler().setActionSpeed(10f);
		dInputHandler.getMouseLookHandler().setActionSpeed(1f);

		// overwrite those, I use the in the game
		KeyBindingManager keyboard = KeyBindingManager.getKeyBindingManager();

		keyboard.set("forward", KeyInput.KEY_UP);
		keyboard.set("backward", KeyInput.KEY_DOWN);
		keyboard.set("strafeLeft", KeyInput.KEY_LEFT);
		keyboard.set("strafeRight", KeyInput.KEY_RIGHT);
		keyboard.set("lookUp", KeyInput.KEY_NUMPAD8);
		keyboard.set("lookDown", KeyInput.KEY_NUMPAD2);
		keyboard.set("turnRight", KeyInput.KEY_NUMPAD6);
		keyboard.set("turnLeft", KeyInput.KEY_NUMPAD4);
		keyboard.set("elevateUp", KeyInput.KEY_NUMPAD7);
		keyboard.set("elevateDown", KeyInput.KEY_NUMPAD1);
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

		if (debug) {
			if (showBounds)
				Debugger.drawBounds(scene, display.getRenderer());
			/** Draw the fps node to show the fancy information at the bottom. */
			display.getRenderer().draw(fps);

		}
	}

	@Override
	protected void update(float tpf) {
		// update time, must be called every frame
		timer.update();

		tpf = timer.getTimePerFrame();

		if (!debug || !pause) {
			userInput.update(tpf);
			enemyInput.update(tpf);
			TrailManager.get().update(tpf);
			//AudioSystem.getSystem().update();
		}

		// handle keys
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("exit",
				false)) {
			finished = true;
		} else if (KeyBindingManager.getKeyBindingManager().isValidCommand(
				"debug")) {
			debug = !debug;

			if (debug) {
				display.getRenderer().setCamera(dCam);
			} else {
				display.getRenderer().setCamera(cam);
			}
		}

		if (debug) {
			if (KeyBindingManager.getKeyBindingManager().isValidCommand(
					"bounds", false)) {
				showBounds = !showBounds;
			} else if (KeyBindingManager.getKeyBindingManager().isValidCommand(
					"pause", false)) {
				pause = !pause;
			}
			dInputHandler.update(tpf);
			fps.print("FPS: " + (int) timer.getFrameRate());
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

	private boolean debug = false;
	private boolean showBounds = false;
	private boolean pause = false;
	private Camera dCam;
	private FirstPersonHandler dInputHandler;
	/** Displays all the lovely information at the bottom. */
	protected Text fps;

}
