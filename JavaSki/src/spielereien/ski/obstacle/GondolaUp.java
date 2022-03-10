package spielereien.ski.obstacle;

import java.util.ArrayList;

import spielereien.ski.sprites.Sprite;

public class GondolaUp extends Gondola {
	
	public static ArrayList<GondolaUp> everyGondolaUp = new ArrayList<GondolaUp>();

	public GondolaUp(int x, int y) {
		super(x, y, Sprite.liftGondolaUp);

		everyGondolaUp.add(this);
	}

}
