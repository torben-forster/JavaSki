package spielereien.ski.obstacle;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import spielereien.ski.Shadow;
import spielereien.ski.sprites.Sprite;

public class Snowboarder extends Collideable {

	public static ArrayList<Snowboarder> allSnowboarders = new ArrayList<Snowboarder>();

	int heading;
	int sinceLastTurn;
	int flipTimer;

	int z;

	Shadow myShadow;

	public Snowboarder(int x, int y) {
		super(x, y, Sprite.snowboarderRight);
		this.heading = 1;
		this.sinceLastTurn = 0;
		this.flipTimer = -1;

		this.myShadow = new Shadow();

		allSnowboarders.add(this);
	}

	public void step() {
		if (Math.random() * 10 + 20 < sinceLastTurn) {
			turn();
		}

		sinceLastTurn++;

		x += 4 * heading;
		y += 8;

		myShadow.x = x;
		myShadow.y = y;
	}

	private void turn() {
		heading = -heading;
		sinceLastTurn = 0;
		flipTimer = 0;
	}

	public void handleCollisions(List<Collideable> collideables) {

		for (Collideable c : collideables) {
			if (c.equals(this)) {
				continue;
			}

			double dX = Math.abs(x - c.x);
			double dY = Math.abs(y - c.y);

			if (dX < 5 + c.getMaskX(0) && dY < 5 + c.maskY) {
				turn();
			}
		}
	}

	@Override
	public void drawMe(Graphics g) {
		if (flipTimer == -1) {
			switch (heading) {
			case 1:
				sprite = Sprite.snowboarderRight;
				break;
			case -1:
				sprite = Sprite.snowboarderLeft;
				break;
			}
		} else {
			flipTimer++;

			sprite = Sprite.snowboarderFlipLeft;
			if (flipTimer > 5) {
				sprite = Sprite.snowboarderFlipCenter;
			}
			if (flipTimer > 10) {
				sprite = Sprite.snowboarderFlipRight;
			}
		}
		if (flipTimer > 15) {
			flipTimer = -1;
		}

		myShadow.enabled = false;
		if (z > 0) {
			System.out.println("snowboarder ain air");
			myShadow.enabled = true;
			// TODO seems to be flickering
		}

		super.drawMe(g);
	}

}
