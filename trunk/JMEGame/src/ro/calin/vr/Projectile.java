package ro.calin.vr;

import java.util.List;

import com.jme.bounding.BoundingSphere;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Controller;
import com.jme.scene.shape.Sphere;
import com.jmex.effects.particles.ParticleMesh;

public class Projectile extends Sphere {
	private static int numBullets = 0;

	private Vector3f dir;
	private Vector3f pos;
	private List<? extends Destroyable> potentialTargets;
	private int damage;

	public Projectile(Vector3f position, Vector3f direction,
			ColorRGBA color, List<? extends Destroyable> potentialTargets, int damage) {
		super("bullet" + numBullets, 8, 8, 0.01f);
		
		setModelBound(new BoundingSphere());
		updateModelBound();

		this.dir = direction;
		this.pos = new Vector3f(position);
		this.potentialTargets = potentialTargets;
		this.damage = damage;
		setLocalTranslation(pos);

		updateGeometricState(0, true);

		addController(new Mover());
		
		TrailManager.get().createTrail(this, color);
	}

	private class Mover extends Controller {
		float speed = 500;

		/** Seconds it will last before going away */
		float lifeTime = 3;
		
		private void removeBullet() {
			TrailManager.get().removeTrail(Projectile.this);
			Projectile.this.removeFromParent();
			Projectile.this.removeController(this);
		}
		
		private void explodeBullet() {
			//explosion
			ParticleMesh exp = ExplosionFactory.getSmallExplosion();
			exp.setOriginOffset(pos);
			exp.forceRespawn();
			Projectile.this.getParent().attachChild(exp);
			
			
			removeBullet();
		}

		@Override
		public void update(float time) {
			lifeTime -= time;

			if (lifeTime < 0) {
				removeBullet();
				return;
			}

			Vector3f len = dir.mult(time * speed);
			setLocalTranslation(pos.addLocal(len));
			
			for (Destroyable target : potentialTargets) {
				if(Projectile.this.getWorldBound().intersects(target.getWorldBound())) {
					target.hit(damage);
					explodeBullet();
					SoundServer.get().playSound("hit", pos);
					return;
				}
			}
		}
	}
}
