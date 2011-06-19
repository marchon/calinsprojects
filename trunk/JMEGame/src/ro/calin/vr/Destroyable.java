package ro.calin.vr;

import com.jme.scene.Spatial;


public abstract class Destroyable extends ModelNode {
	private int life;
	
	public Destroyable(String id, Spatial model, int life) {
		super(id, model);
		this.life = life;
	}

	public void hit(int damage) {
		life -= damage;
		if(life < 0) {
			destroy();
		}
	}
	public abstract void destroy();
}
