package spielereien.ski.obstacle;

import java.awt.Graphics;

import spielereien.ski.Drawable;
import spielereien.ski.SkiPanel;
import spielereien.ski.sprites.Sprite;

public class Shadow extends Drawable {

	public boolean enabled;

	public Shadow() {
		this.enabled = true;
	}

	@Override
	public void drawMe(Graphics g) {

		if (enabled) {
			g.setColor(Sprite.SHADOW);
			g.fillOval(getDrawX(), getDrawY(), 20, 8);
		}
		enabled = true;

	}

	@Override
	public double getDrawHeight() {
		return y - 4;
	}

	@Override
	public int getDrawX() {
		return (int) (x - SkiPanel.player.x + dimension.getSize().getWidth() * 0.5 - 10);
	}

	@Override
	public int getDrawY() {
		return (int) (y - SkiPanel.player.y + dimension.getSize().getHeight() * 0.35 - 4);
	}
	
	

}
