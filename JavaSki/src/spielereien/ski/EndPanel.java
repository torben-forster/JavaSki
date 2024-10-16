package spielereien.ski;

import java.awt.Color;
import java.awt.Graphics;

public class EndPanel extends Drawable {

	@Override
	public void drawMe(Graphics g) {

		int rectWidth = (int) (dimension.getWidth() / 8);
		int rectHeight = (int) (dimension.getHeight() / 8);

		g.setColor(Color.WHITE);
		g.fillRect((int) (dimension.getWidth() - rectWidth) / 2, (int) (dimension.getHeight() - rectHeight) / 2,
				rectWidth, rectHeight);
		g.setColor(Color.BLACK);
		g.drawRect((int) (dimension.getWidth() - rectWidth) / 2, (int) (dimension.getHeight() - rectHeight) / 2,
				rectWidth, rectHeight);

		g.drawString("Thanks for playing!", (int) (dimension.getWidth() / 2 - 3 * 20),
				(int) (dimension.getHeight() / 2));

	}

	@Override
	public double getDrawHeight() {

		return SkiPanel.player.y + 1000;
	}

	@Override
	public int getDrawX() {
		return 0;
	}

	@Override
	public int getDrawY() {
		return 0;
	}

}
