package spielereien.ski.obstacle;

import java.awt.Color;
import java.awt.Graphics;

import spielereien.ski.sprites.Sprite;

public class LiftMast extends Collideable {

	public LiftMast(int x, int y) {
		super(x, y, Sprite.liftMast);

	}

	@Override
	public int getMaskX(int height) {
		// variable width to account for the geometry of the mast
		int mask = 16;

		if (height > 100) {
			mask = sprite.getWidth();
		}

		return mask;
	}

	public void drawCables(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawLine(getDrawX() + sprite.getWidth() / 2 - 29, getDrawY() - 500, getDrawX() + sprite.getWidth() / 2 - 29,
				getDrawY() + 500);
		g.drawLine(getDrawX() + sprite.getWidth() / 2 + 29, getDrawY() - 500, getDrawX() + sprite.getWidth() / 2 + 29,
				getDrawY() + 500);
	}

}
