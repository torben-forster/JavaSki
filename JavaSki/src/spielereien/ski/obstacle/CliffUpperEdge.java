package spielereien.ski.obstacle;

import java.awt.image.BufferedImage;

import spielereien.ski.sprites.Sprite;

public class CliffUpperEdge extends Solid {

	boolean top;

	public CliffUpperEdge(int x, int y, boolean top) {
		super(x, y, top ? Sprite.upperCliffEdge : randomCliffSprite());

		this.maskX = sprite.getWidth() / 2;
		this.maskY = sprite.getHeight() / 2;
		this.maskZ = 1000;

		this.top = top;

		this.spriteOriginY = sprite.getHeight() / 2;

	}

	private static BufferedImage randomCliffSprite() {
		double rnd = Math.random() * 10;

		return Sprite.cliff;// return Sprite.cliffs.get((int) (rnd % Sprite.cliffs.size()));

	}

	public CliffUpperEdge(int x, int y) {
		this(x, y, false);
	}

	@Override
	public double getDrawHeight() {

		return y - dimension.getHeight();

	}

}
