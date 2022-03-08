package spielereien.ski.obstacle;

import spielereien.ski.sprites.Sprite;

public class PoleSlalomRight extends PoleSlalom {

	public PoleSlalomRight(int x, int y) {
		super(x, y, Sprite.slalomRight);
	}

	/**
	 * creates 2 Slalom-Signs (facing right), linked together for
	 * {@link Player#wrap()}-purposes
	 * 
	 * @param x
	 * @param y
	 * @param row int, spacing between the two signs
	 */
	public static void spawnSlalomPair(int x, int y, int row) {
		PoleSlalomRight sign1 = new PoleSlalomRight(x, y);
		PoleSlalomRight sign2 = new PoleSlalomRight(x + row, y);

		sign1.partner = sign2;
		sign2.partner = sign1;
	}

}
