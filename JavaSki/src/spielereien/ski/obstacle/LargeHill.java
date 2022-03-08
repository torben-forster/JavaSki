package spielereien.ski.obstacle;

import spielereien.ski.sprites.Sprite;

public  class LargeHill extends Collideable {

	public LargeHill(int x, int y) {
		super(x, y, Sprite.largeHill);

		this.jumpable = true;
		this.ramp = false;
		this.jumpMult = 1.15;
	}
}
