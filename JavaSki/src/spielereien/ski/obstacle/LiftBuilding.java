package spielereien.ski.obstacle;

import spielereien.ski.sprites.Sprite;

public class LiftBuilding extends Solid {

	public LiftBuilding(int x, int y) {
		super(x, y, Sprite.liftBuilding);

		this.spriteOriginY = 64;
		this.maskY = sprite.getHeight() - spriteOriginY;
	}

}
