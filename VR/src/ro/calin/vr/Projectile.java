package ro.calin.vr;

import com.jme.bounding.BoundingSphere;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Controller;
import com.jme.scene.shape.Sphere;

public class Projectile extends Sphere {
	private static int numBullets = 0;

	private Vector3f dir;
	private Vector3f pos;

	public Projectile(Vector3f position, Vector3f direction,
			ColorRGBA color) {
		super("bullet" + numBullets, 8, 8, 0.01f);
		
		setModelBound(new BoundingSphere());
		updateModelBound();

		this.dir = direction;
		this.pos = new Vector3f(position);
		setLocalTranslation(pos);

		updateGeometricState(0, true);

		addController(new Mover());
		
		TrailManager.get().createTrail(this, color);
	}

	private class Mover extends Controller {
		float speed = 500;

		/** Seconds it will last before going away */
		float lifeTime = 3;

		@Override
		public void update(float time) {
			lifeTime -= time;

			if (lifeTime < 0) {
				TrailManager.get().removeTrail(Projectile.this);
				Projectile.this.removeFromParent();
				Projectile.this.removeController(this);
				return;
			}

			Vector3f len = dir.mult(time * speed);
			setLocalTranslation(pos.addLocal(len));
		}
	}
}
