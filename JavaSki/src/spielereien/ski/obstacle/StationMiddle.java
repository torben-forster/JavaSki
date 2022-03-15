package spielereien.ski.obstacle;

import java.awt.Color;
import java.awt.Graphics;

import spielereien.ski.sprites.Sprite;

public class StationMiddle extends Station {

	public static int x;
	public static int y;
	
	public static int exitX;
	public static int exitY;

	public StationMiddle(int x, int y) {
		super(x, y, Sprite.liftStationUpper);

		new StationShadow(x, y + sprite.getHeight() - spriteOriginY);

		StationMiddle.x = x;
		StationMiddle.y = y;
		
		StationMiddle.exitX = x + Sprite.liftStationUpper.getWidth() / 2 + Sprite.liftBuilding.getWidth() - 24;
		StationMiddle.exitY = y + Sprite.liftStationUpper.getHeight() / 2 - 24;
	}

	@Override
	public void drawMe(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawLine(getDrawX() + sprite.getWidth() / 2 - 29, getDrawY() - 510, getDrawX() + sprite.getWidth() / 2 - 29,
				getDrawY() + 10);
		g.drawLine(getDrawX() + sprite.getWidth() / 2 + 29, getDrawY() - 510, getDrawX() + sprite.getWidth() / 2 + 29,
				getDrawY() + 10);

		super.drawMe(g);
	}

}
