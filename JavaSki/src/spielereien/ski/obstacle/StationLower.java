package spielereien.ski.obstacle;

import java.awt.Color;
import java.awt.Graphics;

import spielereien.ski.sprites.Sprite;

public class StationLower extends Station {

	public static int yLower;

	public StationLower(int x, int y) {
		super(x, y, Sprite.liftStationLower);

		yLower = y;
	}

	@Override
	public void drawMe(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawLine(getDrawX() + sprite.getWidth() / 2 - 29, getDrawY() - 100, getDrawX() + sprite.getWidth() / 2 - 29,
				getDrawY() + 10);
		g.drawLine(getDrawX() + sprite.getWidth() / 2 + 29, getDrawY() - 100, getDrawX() + sprite.getWidth() / 2 + 29,
				getDrawY() + 10);

		super.drawMe(g);
	}

}
