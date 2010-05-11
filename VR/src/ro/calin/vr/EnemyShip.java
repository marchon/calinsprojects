package ro.calin.vr;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.SharedNode;
import com.jme.scene.Spatial;
import com.jmex.effects.particles.ParticleMesh;

/**
 * @author Calin
 *
 */
public class EnemyShip extends Destroyable {
	private static final Logger logger = Logger.getLogger(EnemyShip.class
			.getName());

	public static final float SPEED = 20f;

	private static ArrayList<Spatial> enemyModels = new ArrayList<Spatial>();

	public static void addModel(Spatial model) {
		enemyModels.add(model);
	}

	private static int id = 0;

	public static EnemyShip createRandomEnemy(Fighter target) {
		if (enemyModels.size() == 0)
			return null;

		int index = (int) (Math.random() * enemyModels.size());
		EnemyShip ship = new EnemyShip("enemy" + id, new SharedNode("shared" + id++,
				(Node) enemyModels.get(index)), target, 200);
		
		ship.setModelBound(new BoundingBox());
		ship.updateModelBound();
		//uh, for texture
		//ship.updateRenderState();
		
		return ship;
	}

	private Fighter target;
	private Vector3f pos;
	private float speed = SPEED;

	public EnemyShip(String id, Spatial model, Fighter target, int life) {
		super(id, model, life);
		this.target = target;

		pos = new Vector3f(target.getPos().x, target.getPos().y, target
				.getPos().z - 450f);
	}

	public Vector3f getPos() {
		return pos;
	}

	public boolean isInCameraRange() {
		return pos.z < target.getCam().getLocation().z;

		// TODO: why not working?
		// return target.getCam().contains(this.getWorldBound()) !=
		// FrustumIntersect.Outside;
	}

	public void update(float time) {
		pos.z += time * (speed + target.getSpeed());

		this.setLocalTranslation(pos);
	}

	private boolean alive = true;
	@Override
	public void destroy() {
		alive = false;
		
		ParticleMesh exp = ExplosionFactory.getExplosion();
		exp.setOriginOffset(pos);
		exp.forceRespawn();
		this.getParent().attachChild(exp);
		
		SoundServer.get().playSound("explosion", pos);
	}

	public boolean isAlive() {
		return alive;
	}	
}
