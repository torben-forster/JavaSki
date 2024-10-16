package spielereien.ski.obstacle;

import java.awt.image.BufferedImage;

import spielereien.ski.sprites.Sprite;

public class Sign extends Collideable {

	public Sign(int x, int y, BufferedImage sprite) {
		super(x, y, sprite);

		maskY = 2;
	}

	public static void newSignSlalom(int x, int y) {
		new Sign(x, y, Sprite.signSlalom);
	}

	public static Sign newSignStartLeft(int x, int y) {
		return new Sign(x, y, Sprite.signStartLeft);
	}

	public static Sign newSignStartRight(int x, int y) {
		return new Sign(x, y, Sprite.signStartRight);
	}

	public static Sign newSignFinishLeft(int x, int y) {
		return new Sign(x, y, Sprite.signFinishLeft);
	}

	public static Sign newSignFinishRight(int x, int y) {
		return new Sign(x, y, Sprite.signFinishRight);
	}

	public static void newSignCliff(int x, int y) {
		new Sign(x, y, Sprite.signCliff);
	}
}
