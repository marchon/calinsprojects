package ro.calin.vr;

import com.jme.app.BaseGame;
import com.jme.app.AbstractGame.ConfigShowMode;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BaseGame game = SpaceGame.getGame();
		game.setConfigShowMode(ConfigShowMode.AlwaysShow);
		game.start();
	}

}
