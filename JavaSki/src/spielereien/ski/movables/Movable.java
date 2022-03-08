package spielereien.ski.movables;

import java.awt.image.BufferedImage;

import spielereien.ski.obstacle.Collideable;

public abstract class Movable extends Collideable{
//TODO better name
	
	public Movable(int x, int y, BufferedImage sprite) {
		super(x, y, sprite);
	}
	
	@Override
	public double getDrawHeight() {
		return y;
	}
	
	public void step() {
	}
	

}
