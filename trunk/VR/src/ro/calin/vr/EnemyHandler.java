package ro.calin.vr;

import java.util.Iterator;
import java.util.List;

import com.jme.bounding.BoundingBox;
import com.jme.input.InputHandler;
import com.jme.scene.Node;

public class EnemyHandler extends InputHandler {
	private List<EnemyShip> enemyShips;
	private Node scene;
	private Fighter target;
	
	public EnemyHandler(List<EnemyShip> enemyShips, Node scene, Fighter target, String api) {
		super();
		this.enemyShips = enemyShips;
		this.scene = scene;
		this.target = target;
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
