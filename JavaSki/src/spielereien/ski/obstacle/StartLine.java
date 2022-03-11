package spielereien.ski.obstacle;

import java.awt.Graphics;

import spielereien.ski.sprites.Sprite;

public class StartLine extends Collideable {

	public StartLine(int x, int y) {
		super(x, y, Sprite.line);

		this.maskZ = 100;
	}

}
