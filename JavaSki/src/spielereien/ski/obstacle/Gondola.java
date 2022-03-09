package spielereien.ski.obstacle;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import spielereien.ski.sprites.Sprite;

public abstract class Gondola extends Collideable {

	public static ArrayList<Gondola> allGondolas = new ArrayList<Gondola>();

	Gondola(int x, int y, BufferedImage sprite) {
		super(x, y, sprite);

		allGondolas.add(this);

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

	public void step() {
		if (this instanceof GondolaUp) {
			y -= 1;
		} else {
			y += 1;
		}

		// gondola <-> station wrapping hardcoded values
		if (y < -560) {
			y = 15440;
		}
		if (y > 15440) {
			y = -560;
		}
	}

}
