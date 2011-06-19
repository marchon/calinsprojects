package ro.calin.vr;

import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.KeyInputAction;

public class FighterHandler extends InputHandler {
	private Fighter fighter;

	@Override
	public void update(float time) {
		if (!isEnabled())
			return;
		super.update(time);

		fighter.update(time);
	}

	public FighterHandler(Fighter fighter, String api) {
		this.fighter = fighter;
		setKeyBindings(api);
		setActions();
	}

	private void setKeyBindings(String api) {
		KeyBindingManager keyboard = KeyBindingManager.getKeyBindingManager();

		keyboard.set("up", KeyInput.KEY_W);
		keyboard.set("down", KeyInput.KEY_S);
		keyboard.set("right", KeyInput.KEY_D);
		keyboard.set("left", KeyInput.KEY_A);
		keyboard.set("inc", KeyInput.KEY_PGUP);
		keyboard.set("dec", KeyInput.KEY_PGDN);
		keyboard.set("fire", KeyInput.KEY_SPACE);
	}

	private void setActions() {
		addAction(new KeyInputAction() {
			@Override
			public void performAction(InputActionEvent evt) {
				fighter.moveUp(evt.getTime());
			}
		}, "up", true);

		addAction(new KeyInputAction() {
			@Override
			public void performAction(InputActionEvent evt) {
				fighter.moveDown(evt.getTime());
			}
		}, "down", true);
		
		addAction(new KeyInputAction() {
			@Override
			public void performAction(InputActionEvent evt) {
				fighter.moveRight(evt.getTime());
			}
		}, "right", true);
		
		addAction(new KeyInputAction() {
			@Override
			public void performAction(InputActionEvent evt) {
				fighter.moveLeft(evt.getTime());
			}
		}, "left", true);
		
		addAction(new KeyInputAction() {
			@Override
			public void performAction(InputActionEvent evt) {
				fighter.increaseSpeed(evt.getTime());
			}
		}, "inc", true);
		
		addAction(new KeyInputAction() {
			@Override
			public void performAction(InputActionEvent evt) {
				fighter.decreaseSpeed(evt.getTime());
			}
		}, "dec", true);
		
		addAction(new KeyInputAction() {
			@Override
			public void performAction(InputActionEvent evt) {
				fighter.fire(evt.getTime());
			}
		}, "fire", true);
	}
}
