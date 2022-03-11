package spielereien.ski.obstacle;

import spielereien.ski.sprites.Sprite;

public class PoleSlalomBlue extends PoleSlalom {

	public PoleSlalomBlue(int x, int y, char f) {
		super(x, y, Sprite.slalomFail);
		this.facing = f;

		getSprite();
	}
	
	protected void getSprite() {
		switch (facing) {
		case 'l':
			sprite = Sprite.slalomBlueLeft;
			break;
		case 'r':
			sprite = Sprite.slalomBlueRight;
			break;
		}
	}


	/**
	 * creates 2 Slalom-Signs (blue), linked together
	 * 
	 * @param x
	 * @param y
	 * @param row int, spacing between the two signs
	 */
	public static void spawnSlalomPair(int x, int y) {
		PoleSlalomBlue sign1 = new PoleSlalomBlue(x - PoleSlalom.SPACING / 2, y, 'r');
		PoleSlalomBlue sign2 = new PoleSlalomBlue(x + PoleSlalom.SPACING / 2, y, 'l');

		sign1.partner = sign2;
		sign2.partner = sign1;
	}

}
