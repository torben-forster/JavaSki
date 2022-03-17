package spielereien.ski;

import java.awt.Dimension;
import java.awt.Graphics;

public abstract class Drawable implements Comparable<Drawable> {

	public double x;
	public double y;

	protected static Dimension dimension;

	public static void updateDimension(Dimension dim) {
		dimension = dim;
	}

	public Drawable() {
		SkiPanel.drawables.add(this);
	}

	public abstract void drawMe(Graphics g);

	public abstract double getDrawHeight();

	public int getDrawX() {
		return (int) (x - SkiPanel.player.x + dimension.getSize().getWidth() * 0.5);
	}

	public int getDrawY() {
		return (int) (y - SkiPanel.player.y + dimension.getSize().getHeight() * 0.35);
	}

	@Override
	public int compareTo(Drawable drawable) {

		return (int) Math.signum((this.getDrawHeight() - drawable.getDrawHeight()));
	}

}