package spielereien.ski.obstacle;

import spielereien.ski.sprites.Sprite;

public class SmallHill extends Collideable {

	public SmallHill(int x, int y) {
		super(x, y, Sprite.smallHill);

		this.jumpable = true;
		this.ramp = true;
		this.jumpMult = 0.65;
	}
}
