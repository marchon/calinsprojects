package ro.calin.vr;

import java.util.logging.Logger;

import com.jme.image.Texture;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Skybox;
import com.jme.scene.state.CullState;
import com.jme.scene.state.MaterialState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;

public class SpaceBox extends Skybox {
	private static final Logger logger = Logger.getLogger(SpaceBox.class
			.getName());
	private DisplaySystem display;
	private String texDir;

	public SpaceBox(String name, float xExtent, float yExtent, float zExtent,
			DisplaySystem display, String texDir) {
		super(name, xExtent, zExtent, zExtent);

		this.display = display;
		this.texDir = texDir;
		setup();
	}

	private void setup() {
		try {
			ResourceLocatorTool.addResourceLocator(
					ResourceLocatorTool.TYPE_TEXTURE,
					new SimpleResourceLocator(getClass().getResource(texDir)));
		} catch (Exception e) {
			logger.warning("Unable to access texture directory.");
			e.printStackTrace();
		}

		this.setTexture(Skybox.Face.North, TextureManager.loadTexture(
				"north.jpg", Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear));
		this.setTexture(Skybox.Face.West, TextureManager.loadTexture(
				"west.jpg", Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear));
		this.setTexture(Skybox.Face.East, TextureManager.loadTexture(
				"east.jpg", Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear));
		this.setTexture(Skybox.Face.Up, TextureManager.loadTexture("up.jpg",
				Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear));
		this.setTexture(Skybox.Face.Down, TextureManager.loadTexture(
				"down.jpg", Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear));
		
		//this is the one visible
		this.setTexture(Skybox.Face.South, TextureManager.loadTexture(
				"south.jpg", Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear));

		this.preloadTextures();

		CullState cullState = display.getRenderer().createCullState();
		cullState.setCullFace(CullState.Face.None);
		cullState.setEnabled(true);
		this.setRenderState(cullState);

		this.updateRenderState();
	}
}
