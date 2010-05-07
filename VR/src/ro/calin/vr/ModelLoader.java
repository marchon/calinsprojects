package ro.calin.vr;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.jme.image.Texture;
import com.jme.math.Matrix3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.model.converters.FormatConverter;
import com.jmex.model.converters.MaxToJme;

public class ModelLoader {
	private ModelLoader() {
	}

	/**
	 * @param url
	 * @return
	 * @throws IOException
	 * 
	 * TODO: load jme format by default, convert depending on file extension
	 */
	private static ByteArrayInputStream getLocalFormatInput(URL url)
			throws IOException {
		FormatConverter converter = new MaxToJme();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		InputStream input = url.openStream();
		converter.convert(input, baos);
		input.close();

		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

		return bais;
	}
	
	public static Spatial loadModel(String modelPath, ModelProcessor processor) throws IOException {
		return loadModel(modelPath, null, null, processor);
	}

	public static Spatial loadModel(String modelPath, String texPath,
			DisplaySystem display, ModelProcessor processor) throws IOException {
		Spatial model;

		URL modelUrl = ModelLoader.class.getClassLoader()
				.getResource(modelPath);
		if(modelUrl == null)
			modelUrl = new File(modelPath).toURI().toURL();

		if(modelUrl == null)
			throw new IOException("Could not find model path.");
		
		model = (Node) BinaryImporter.getInstance().load(
				getLocalFormatInput(modelUrl));
		
		//TODO: externalize this
		
		if(processor != null)
			processor.process(model);
		
		// load texture
		//TODO: figure out how to apply texture to complex model
		if (texPath != null && display != null) {
			URL texUrl = ModelLoader.class.getClassLoader()
					.getResource(texPath);
			TextureState ts = display.getRenderer().createTextureState();
			Texture t = TextureManager.loadTexture(texUrl,
					Texture.MinificationFilter.BilinearNearestMipMap,
					Texture.MagnificationFilter.Bilinear);
			ts.setTexture(t);
			model.setRenderState(ts);
			model.updateRenderState();
		}

		return model;
	}
}
