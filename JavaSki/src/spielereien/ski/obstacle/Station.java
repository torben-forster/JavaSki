package spielereien.ski.obstacle;

import java.awt.image.BufferedImage;
import spielereien.ski.obstacle.LiftBuilding;

public abstract class Station extends Solid {

	public Station(int x, int y, BufferedImage sprite) {
		super(x, y, sprite);

		this.spriteOriginY = 104;
		this.maskY = sprite.getHeight() - spriteOriginY;

		new LiftBuilding(x + sprite.getWidth() - 6, y + 10);
	}

}
