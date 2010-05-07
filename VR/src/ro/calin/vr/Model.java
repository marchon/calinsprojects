package ro.calin.vr;

import com.jme.scene.Node;
import com.jme.scene.Spatial;

public abstract class Model extends Node {
	private Spatial model;
	
	public Model(String id, Spatial model) {
        super(id);
        setModel(model);
    }

	public void setModel(Spatial aModel) {
		this.detachChild(this.model);
        this.model = aModel;
        this.attachChild(this.model);
	}
}
