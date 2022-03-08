package spielereien.ski.obstacle;

import spielereien.ski.sprites.Sprite;

public class Station extends Solid {

	public Station(int x, int y) {
		super(x, y, Sprite.station);

		this.spriteOriginY = 65;
		this.maskY = sprite.getHeight() - spriteOriginY;
	}

}
