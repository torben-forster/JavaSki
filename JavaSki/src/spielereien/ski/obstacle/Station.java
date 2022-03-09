package spielereien.ski.obstacle;

import spielereien.ski.sprites.Sprite;

public class Station extends Solid {

	public Station(int x, int y) {
		super(x, y, Sprite.liftStation);

		this.spriteOriginY = 104;
		this.maskY = sprite.getHeight() - spriteOriginY;
	}

}
