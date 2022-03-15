package spielereien.ski.obstacle;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import spielereien.ski.Shadow;

public abstract class Gondola extends Collideable {

	public boolean readyForPlayer;
	Shadow myShadow;

	public static ArrayList<Gondola> everyGondola = new ArrayList<Gondola>();

	Gondola(int x, int y, BufferedImage sprite) {
		super(x, y, sprite);

		readyForPlayer = false;

		myShadow = new Shadow();

		everyGondola.add(this);
	}

	@Override
	public int getMaskX(int height) {
		int mask = sprite.getWidth();
		if (height < 100) {
			mask = -10;
		}
		return mask;
	}

	public void step() {

		if (this instanceof GondolaUp) {
			y -= 3;
		} else {
			y += 3;
		}

		readyForPlayer = false;
		// gondola <-> station wrapping
		if (y < StationUpper.y + 20) {
			y = StationLower.y + 20;
			readyForPlayer = true;
		}
		if (y > StationLower.y + 20) {
			y = StationUpper.y + 20;
		}

		if (y >= StationMiddle.y + 15 && y <= StationMiddle.y + 25) {
			readyForPlayer = true;
		}

		if (y >= StationMiddle.y - 80 && y <= StationMiddle.y) {
			myShadow.enabled = false;
		}

		myShadow.x = x;
		myShadow.y = y;
	}

}
