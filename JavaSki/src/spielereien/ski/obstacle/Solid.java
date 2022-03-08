package spielereien.ski.obstacle;

import java.awt.image.BufferedImage;

/**
 * can't be moved through by player, even after colliding with it
 * 
 * @author forster_torben
 *
 */
public class Solid extends Collideable {

	public Solid(int x, int y, BufferedImage sprite) {
		super(x, y, sprite);
	}

}
