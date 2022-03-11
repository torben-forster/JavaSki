package spielereien.ski.obstacle;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import spielereien.ski.SkiPanel;
import spielereien.ski.sprites.Sprite;
import spielereien.ski.Drawable;

public class Collideable extends Drawable {
	/*
	 * TODO- types I can think of: tree, stump, ramp, stone, small snow-hill, large
	 * snow-hill, bush, deep snow, lift, snowboarder, beginner, dog
	 */



	public int maskX;
	public int maskY;
	public int maskZ;

	public boolean enabled;

	protected BufferedImage sprite;
	int spriteOriginX;
	int spriteOriginY;


	public boolean jumpable;// whether you jump off of the obstacle when landing on it
	public boolean ramp;
	public double jumpMult; // jump Multiplier if jumped

	public Collideable(int x, int y, BufferedImage sprite) {
		super();
		
		this.x = x;
		this.y = y;

		this.sprite = sprite;

		this.maskX = sprite.getWidth() / 2;
		this.maskY = 5;
		this.maskZ = sprite.getHeight();

		this.spriteOriginX = sprite.getWidth() / 2;
		this.spriteOriginY = sprite.getHeight();

		this.jumpable = false;
		this.ramp = false;

		this.enabled = true;

		SkiPanel.collideables.add(this);
	}



	public int getDrawX() {
		return (int) (x - SkiPanel.player.x + dimension.getSize().getWidth() * 0.5 - spriteOriginX);
	}

	public int getDrawY() {
		return (int) (y - SkiPanel.player.y + dimension.getSize().getHeight() * 0.35 - spriteOriginY);
	}

	@Override
	public void drawMe(Graphics g) {
		int drawX = getDrawX() + 1;
		int drawY = getDrawY() + 3;

		if (Collideable.onScreen(drawX, drawY)) {
			/*
			 * g.setColor(SHADOW); g.fillOval(drawX - 10, drawY - 5, 20, 9);
			 */

			Sprite.drawSprite(g, drawX, drawY, sprite);

			/*
			 * g.setColor(Color.BLUE); g.fillRect((int) x - maskX, (int) y - maskY, maskX *
			 * 2, maskY * 2);
			 */

		}
	}

	private static boolean onScreen(int x, int y) {
		/*
		 * turns out, drawing wasn't the problem, my sorting algorithm is! so this isn't
		 * strictly necessary, i don't even know if it's beneficial after all
		 */

		int offset = 100;
		int width = (int) dimension.getWidth();
		int height = (int) dimension.getHeight();

		if (x < 0 - offset || x > width + offset || y < 0 - offset || y > height + offset) {
			return true;
		} else {
			return true;
		}
	}

	@Override
	public double getDrawHeight() {
		return y + sprite.getHeight() - spriteOriginY;
	}

	public int getMaskX(int height) {

		return maskX;
	}
}
