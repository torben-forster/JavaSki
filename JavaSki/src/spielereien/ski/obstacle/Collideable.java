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

		Sprite.drawSprite(g, drawX, drawY, sprite);

	}

	public static boolean onScreen(int x, int y, int margin) {

		int width = (int) dimension.getWidth();
		int height = (int) dimension.getHeight();

		if (x < 0 - margin || x > width + margin || y < 0 - margin || y > height + margin) {
			return false;
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
