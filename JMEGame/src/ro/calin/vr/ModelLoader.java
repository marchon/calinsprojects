package ro.calin.vr;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.util.export.binary.BinaryImporter;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;
import com.jmex.model.converters.FormatConverter;
import com.jmex.model.converters.MaxToJme;

public class ModelLoader {
	public static void addModelTextureLocations(String... paths) {
		for (String path : paths) {
			addLocation(path, ResourceLocatorTool.TYPE_TEXTURE);
		}
	}

	public static void addModelLocations(String... paths) {
		for (String path : paths) {
			addLocation(path, ResourceLocatorTool.TYPE_MODEL);
		}
	}

	private static void addLocation(String path, String type) {
		try {
			ResourceLocatorTool.addResourceLocator(type,
					new SimpleResourceLocator(ModelLoader.class
							.getClassLoader().getResource(path)));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	private ModelLoader() {
	}

	/**
	 * @param url
	 * @return
	 * @throws IOException
	 * 
	 *             TODO: load jme format by default, convert depending on file
	 *             extension
	 */
	private static ByteArrayInputStream getLocalFormatInput(URL url)
			throws IOException {
		FormatConverter converter = new MaxToJme();
		//converter.setProperty("mtllib", url);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		InputStream input = url.openStream();
		converter.convert(input, baos);
		input.close();

		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

		return bais;
	}

	public static Spatial loadModel(String model, ModelProcessor processor)
			throws IOException {
		// assume name, try find in declared paths
		URL modelURL = ResourceLocatorTool.locateResource(
				ResourceLocatorTool.TYPE_MODEL, model);
		// assume loader path
		if (modelURL == null)
			modelURL = ModelLoader.class.getClassLoader().getResource(model);
		// assume os path
		if (modelURL == null)
			modelURL = new File(model).toURI().toURL();

		if (modelURL == null)
			throw new IOException("Could not find model path.");

		return loadModel(modelURL, processor);
	}

	public static Spatial loadModel(URL modelURL, ModelProcessor processor)
			throws IOException {
		Spatial model;

		model = (Node) BinaryImporter.getInstance().load(
				getLocalFormatInput(modelURL));

		if (processor != null)
			processor.process(model);

		return model;
	}
}
