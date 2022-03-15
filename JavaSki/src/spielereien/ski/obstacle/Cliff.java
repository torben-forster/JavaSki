package spielereien.ski.obstacle;

import spielereien.ski.sprites.Sprite;

public class Cliff extends Collideable {

	public Cliff(int x, int y, boolean top) {
		super(x, y, top ? Sprite.cliffEdge : Sprite.cliff);

		this.maskX = sprite.getWidth() / 2;
		this.maskY = sprite.getHeight() / 2;
		this.maskZ = 1000;

		this.spriteOriginY = sprite.getHeight() / 2;

	}

	public Cliff(int x, int y) {
		this(x, y, false);
	}

	@Override
	public double getDrawHeight() {
		return y - dimension.getHeight();
	}

}
