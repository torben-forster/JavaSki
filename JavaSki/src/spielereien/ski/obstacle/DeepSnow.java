package spielereien.ski.obstacle;

import spielereien.ski.sprites.Sprite;

public class DeepSnow extends Collideable {

	public DeepSnow(int x, int y) {
		super(x, y, Sprite.deepSnow);

		this.maskX = sprite.getWidth() / 2;
		this.maskY = sprite.getHeight() / 2;
		this.maskZ = 5;

		this.spriteOriginY = sprite.getHeight() / 2;

	}

	@Override
	public double getDrawHeight() {
		return y - 100;
	}

}
