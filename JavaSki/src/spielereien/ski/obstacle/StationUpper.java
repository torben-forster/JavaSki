package spielereien.ski.obstacle;

import java.awt.Color;
import java.awt.Graphics;

import spielereien.ski.sprites.Sprite;

public class StationUpper extends Station {

	public static int x;
	public static int y;

	public StationUpper(int x, int y) {
		super(x, y, Sprite.liftStationUpper);

		new StationUpperShadow(x, y + sprite.getHeight() - spriteOriginY);

		StationUpper.x = x;
		StationUpper.y = y;
	}

	@Override
	public void drawMe(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawLine(getDrawX() + sprite.getWidth() / 2 - 29, getDrawY() + 10, getDrawX() + sprite.getWidth() / 2 - 29,
				getDrawY() + 100);
		g.drawLine(getDrawX() + sprite.getWidth() / 2 + 29, getDrawY() + 10, getDrawX() + sprite.getWidth() / 2 + 29,
				getDrawY() + 100);

		super.drawMe(g);
	}

	private class StationUpperShadow extends Collideable {

		public StationUpperShadow(int x, int y) {
			super(x, y, Sprite.liftStationShadow);

		}

		@Override
		public double getDrawHeight() {
			return y + sprite.getHeight() - spriteOriginY + 100;
		}

	}
}
