package spielereien.ski;

import java.awt.Graphics;

import spielereien.ski.obstacle.Collideable;

public abstract class Drawable implements Comparable<Drawable> {

	public abstract void drawMe(Graphics g);

	public abstract double getDrawHeight();

	public abstract int getDrawX();

	public abstract int getDrawY();

	@Override
	public int compareTo(Drawable drawable) {

		return (int) Math.signum((this.getDrawHeight() - drawable.getDrawHeight()));
	}

}