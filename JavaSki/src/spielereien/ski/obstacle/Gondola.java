package spielereien.ski.obstacle;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import spielereien.ski.sprites.Sprite;

public abstract class Gondola extends Collideable {

	Gondola(int x, int y, BufferedImage sprite) {
		super(x, y, sprite);

		this.maskZ = 124;
	}

	@Override
	public int getMaskX(int height) {
		int mask = sprite.getWidth();
		if (height < 100) {
			mask = -10;
		}
		return mask;
	}

	@Override
	public void drawMe(Graphics g) {
		super.drawMe(g);

		g.setColor(Sprite.SHADOW);
		g.fillOval(getDrawX() + sprite.getWidth() / 2 - 10, getDrawY() + sprite.getHeight(), 20, 8);
	}

}
