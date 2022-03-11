package spielereien.ski.obstacle;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import spielereien.ski.sprites.Sprite;

public abstract class Gondola extends Collideable {

	public boolean readyForPlayer;

	public static ArrayList<Gondola> everyGondola = new ArrayList<Gondola>();

	Gondola(int x, int y, BufferedImage sprite) {
		super(x, y, sprite);

		readyForPlayer = false;

		everyGondola.add(this);
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
		readyForPlayer = false;

		if (this instanceof GondolaUp) {
			y -= 3;
		} else {
			y += 3;
		}

		// gondola <-> station wrapping
		if (y < StationUpper.y + 20) {
			y = StationLower.y + 20;
			readyForPlayer = true;
		}
		if (y > StationLower.y + 20) {
			y = StationUpper.y + 20;
		}
	}

}
