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

	public abstract int getDrawX();

	public abstract int getDrawY();

	@Override
	public int compareTo(Drawable drawable) {

		return (int) Math.signum((this.getDrawHeight() - drawable.getDrawHeight()));
	}

}