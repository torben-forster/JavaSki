package spielereien.ski.obstacle;

import spielereien.ski.sprites.Sprite;

public class Stump extends Collideable {

	public Stump(int x, int y) {
		super(x, y, Sprite.stump);

		this.jumpable = true;
		this.jumpMult = 0.9;
	}

}
