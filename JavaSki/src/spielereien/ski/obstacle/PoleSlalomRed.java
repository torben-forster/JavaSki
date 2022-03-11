package spielereien.ski.obstacle;

import spielereien.ski.sprites.Sprite;

public class PoleSlalomRed extends PoleSlalom {

	public PoleSlalomRed(int x, int y, char f) {
		super(x, y, Sprite.slalomFail);
		this.facing = f;

		getSprite();

	}

	protected void getSprite() {
		switch (facing) {
		case 'l':
			sprite = Sprite.slalomRedLeft;
			break;
		case 'r':
			sprite = Sprite.slalomRedRight;
			break;
		}
	}

	/**
	 * creates 2 Slalom-Signs (red), linked together
	 * 
	 * @param x
	 * @param y
	 * @param row int, spacing between the two signs
	 */
	public static void spawnSlalomPair(int x, int y) {
		PoleSlalomRed sign1 = new PoleSlalomRed(x - PoleSlalom.SPACING / 2, y, 'r');
		PoleSlalomRed sign2 = new PoleSlalomRed(x + PoleSlalom.SPACING / 2, y, 'l');

		sign1.partner = sign2;
		sign2.partner = sign1;
	}

}
