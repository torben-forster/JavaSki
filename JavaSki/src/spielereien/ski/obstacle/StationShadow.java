package spielereien.ski.obstacle;

import spielereien.ski.sprites.Sprite;

class StationShadow extends Collideable {

	public StationShadow(int x, int y) {
		super(x, y, Sprite.liftStationShadow);

	}

	@Override
	public double getDrawHeight() {
		return y + sprite.getHeight() - spriteOriginY + 100;
	}


}
