package ro.calin.vr;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.jme.bounding.BoundingBox;
import com.jme.input.InputHandler;
import com.jme.scene.Node;

public class EnemyHandler extends InputHandler {
	private static Logger log = Logger.getLogger(EnemyHandler.class.getName());
	private static EnemyHandler instance;
	public static EnemyHandler createEnemyHandler(List<EnemyShip> enemyShips, Node scene, Fighter target, String api){
		if(instance == null) {
			instance = new EnemyHandler(enemyShips, scene, target, api);
		}
		
		return instance;
	}
	public static EnemyHandler get() {
		if (instance == null) {
			log.severe("TrailManager not yet initialized");
		}
		return instance;
	}
	
	
	private List<EnemyShip> enemyShips;
	private Node scene;
	private Fighter target;
	
	private EnemyHandler(List<EnemyShip> enemyShips, Node scene, Fighter target, String api) {
		super();
		this.enemyShips = enemyShips;
		this.scene = scene;
		this.target = target;
	}
	
	public List<EnemyShip> getEnemyShips() {
		return enemyShips;
	}
	
	private void addAnEnemy() {
		EnemyShip enemyShip = EnemyShip.createRandomEnemy(target);
		
		scene.attachChild(enemyShip);
		//for the textures, uh
		scene.updateRenderState();
		
		enemyShips.add(enemyShip);
	}
	
	
	private long last = 0;
	@Override
	public void update(float time) {
		super.update(time);
		
		long now = System.currentTimeMillis();
		if(now - last > 2000) {
			addAnEnemy();
			last = now;
		}
		
		for (Iterator<EnemyShip> iterator = enemyShips.iterator(); iterator.hasNext();) {
			EnemyShip enemyShip = iterator.next();

			if(!enemyShip.isInCameraRange() || !enemyShip.isAlive()) {
				scene.detachChild(enemyShip);
				iterator.remove();
			} else {
				enemyShip.update(time);
			}
		}
	}
}
