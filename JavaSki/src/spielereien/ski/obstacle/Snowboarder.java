package spielereien.ski.obstacle;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import spielereien.ski.Player;
import spielereien.ski.Shadow;
import spielereien.ski.SkiPanel;
import spielereien.ski.sprites.Sprite;

public class Snowboarder extends Collideable {

	public static ArrayList<Snowboarder> allSnowboarders = new ArrayList<Snowboarder>();

	int heading;
	int sinceLastTurn;
	int flipTimer;

	double z;
	double speedZ;

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
		if (Math.random() * 40 + 20 < sinceLastTurn) {
			turn();
		}

		sinceLastTurn++;

		x += 5 * heading;
		y += 10;

		if (z > 0) {
			speedZ += Player.GRAVITY;
			z += speedZ;
		}
		if (z < 0) {
			speedZ = 0;
			z = 0;
		}

		if (!onScreen(getDrawX(), getDrawY(), Math.max((int) dimension.getHeight(), (int) dimension.getWidth()))) {

			double randomX = Math.random() * dimension.getWidth() * 0.5 - dimension.getWidth() * 0.25;
			double randomY = Math.random() * dimension.getHeight() * 0.25 - dimension.getWidth() * 0.75;

			x = SkiPanel.player.x + randomX;

			if (y < SkiPanel.player.y) {
				y = SkiPanel.player.y - randomY;
			} else {
				y = SkiPanel.player.y + randomY;
			}

		}

		myShadow.x = x;
		myShadow.y = y;
	}

	private void turn() {
		if (z > 0) {
			return;
		}
		heading = -heading;
		sinceLastTurn = 0;

	}

	public void handleCollisions(List<Collideable> collideables) {

		for (Collideable c : collideables) {
			if (c.equals(this)) {
				continue;
			}

			double dX = Math.abs(x - c.x);
			double dY = Math.abs(y - c.y);

			if (dX < 5 + c.getMaskX((int) z) && dY < 5 + c.maskY) {
				flip();
				if (c.ramp) {
					speedZ += c.jumpMult;
				}
			}
		}
	}

	public void flip() {
		turn();
		flipTimer = 0;
		speedZ = 4;
		z = 0.1;
	}

	@Override
	public void drawMe(Graphics g) {

		getSprite();

		if (flipTimer != -1) {
			flipTimer++;
		}
		if (flipTimer > 15) {
			flipTimer = -1;
		}

		myShadow.enabled = false;
		if (z > 0) {
			myShadow.enabled = true;
		}

		super.drawMe(g);
	}

	private void getSprite() {
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

			switch (heading) {
			case 1:
				sprite = Sprite.snowboarderRight;
				if (flipTimer > 5) {
					sprite = Sprite.snowboarderFlipCenter;
				}
				if (flipTimer > 10) {
					sprite = Sprite.snowboarderFlipLeft;
				}
				break;
			case -1:
				sprite = Sprite.snowboarderLeft;
				if (flipTimer > 5) {
					sprite = Sprite.snowboarderFlipCenter;
				}
				if (flipTimer > 10) {
					sprite = Sprite.snowboarderFlipRight;
				}
				break;
			}

		}
	}

	@Override
	public int getDrawY() {
		return (int) (y - SkiPanel.player.y + dimension.getSize().getHeight() * 0.35 - z - spriteOriginY);
	}

}
