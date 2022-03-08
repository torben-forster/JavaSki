package spielereien.ski.obstacle;

import spielereien.ski.sprites.Sprite;

public class PoleSlalomLeft extends PoleSlalom {

	public PoleSlalomLeft(int x, int y) {
		super(x, y, Sprite.slalomLeft);
	}

	/**
	 * creates 2 Slalom-Signs (facing left), linked together for
	 * {@link Player#wrap()}-purposes
	 * 
	 * @param x
	 * @param y
	 * @param row int, spacing between the two signs
	 */
	public static void spawnSlalomPair(int x, int y, int row) {
		PoleSlalomLeft sign1 = new PoleSlalomLeft(x, y);
		PoleSlalomLeft sign2 = new PoleSlalomLeft(x + row, y);

		sign1.partner = sign2;
		sign2.partner = sign1;
	}

}
