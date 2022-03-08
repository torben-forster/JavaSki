package spielereien.ski.obstacle;

import spielereien.ski.sprites.Sprite;

public class Ramp extends Collideable {

	public Ramp(int x, int y) {
		super(x, y, Sprite.skiramp);

		this.jumpable = true;
		this.ramp = true;
		this.jumpMult = 1.35;
	}
}
