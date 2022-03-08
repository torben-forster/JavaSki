package spielereien.ski.obstacle;

import spielereien.ski.sprites.Sprite;

public class Rock extends Collideable {

	public Rock(int x, int y) {
		super(x, y, Sprite.rock);

		this.jumpable = true;
		this.jumpMult = 0.9;
	}

}