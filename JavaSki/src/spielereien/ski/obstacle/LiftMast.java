package spielereien.ski.obstacle;

import java.awt.Color;
import java.awt.Graphics;

import spielereien.ski.SkiPanel;
import spielereien.ski.sprites.Sprite;

public class LiftMast extends Collideable {

	int nr;

	public LiftMast(int x, int y, int nr) {
		super(x, y, Sprite.liftMast);
		this.nr = nr;
	}

	public LiftMast(int x, int y) {
		this(x, y, -1);

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

	@Override
	public void drawMe(Graphics g) {
		if (nr == SkiPanel.LASTMAST) {
			drawCables(g, 490);
			
		} else if (nr == 7) {
			drawCables(g, 440);
			
		} else {
			drawCables(g, 990);
		}

		super.drawMe(g);

		if (nr != -1) {
			g.setColor(Color.WHITE);
			String nummer = Integer.toString(nr);
			g.drawString(nummer, getDrawX() + sprite.getWidth() / 2 - 3 * nummer.length(), getDrawY() + 90);
		}

	}

	private void drawCables(Graphics g, int length) {
		g.setColor(Color.BLACK);
		g.drawLine(getDrawX() + sprite.getWidth() / 2 - 29, getDrawY() - length,
				getDrawX() + sprite.getWidth() / 2 - 29, getDrawY() + 5);
		g.drawLine(getDrawX() + sprite.getWidth() / 2 + 29, getDrawY() - length,
				getDrawX() + sprite.getWidth() / 2 + 29, getDrawY() + 5);
	}

}
