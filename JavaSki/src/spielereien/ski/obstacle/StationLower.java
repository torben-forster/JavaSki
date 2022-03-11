package spielereien.ski.obstacle;

import java.awt.Color;
import java.awt.Graphics;

import spielereien.ski.sprites.Sprite;

public class StationLower extends Station {

	public static int x;
	public static int y;

	public StationLower(int x, int y) {
		super(x, y, Sprite.liftStationLower);

		StationLower.x = x;
		StationLower.y = y;
	}

	@Override
	public void drawMe(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawLine(getDrawX() + sprite.getWidth() / 2 - 29, getDrawY() - 100, getDrawX() + sprite.getWidth() / 2 - 29,
				getDrawY() + 10);
		g.drawLine(getDrawX() + sprite.getWidth() / 2 + 29, getDrawY() - 100, getDrawX() + sprite.getWidth() / 2 + 29,
				getDrawY() + 10);

		// g.drawRect(getDrawX(), getDrawY() + 140, 250, 100);

		super.drawMe(g);

		// g.drawString("x: " + x + " y: " + y, getDrawX(), getDrawY());
	}

}
