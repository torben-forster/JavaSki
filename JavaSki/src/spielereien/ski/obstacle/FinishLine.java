package spielereien.ski.obstacle;

import spielereien.ski.sprites.Sprite;

public class FinishLine extends Collideable {

	public FinishLine(int x, int y) {
		super(x, y, Sprite.line);

		this.maskZ = 100;
	}

}
