package spielereien.ski.obstacle;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import spielereien.ski.sprites.Sprite;

public abstract class PoleSlalom extends Collideable {

	public static ArrayList<PoleSlalom> allSlalomSigns = new ArrayList<PoleSlalom>();

	public boolean alreadyPassed;

	PoleSlalom partner;

	public PoleSlalom(int x, int y, BufferedImage sprite) {
		super(x, y, sprite);

		alreadyPassed = false;
		allSlalomSigns.add(this);
		maskX = 2;
	}

	public void fail() {
		alreadyPassed = true;
		sprite = Sprite.slalomFail;
		failPartner();
	}

	private void failPartner() {
		partner.alreadyPassed = true;
		partner.sprite = Sprite.slalomFail;
	}

	public void success() {
		alreadyPassed = true;
		sprite = Sprite.slalomSuccess;
		successPartner();
	}

	private void successPartner() {
		partner.alreadyPassed = true;
		partner.sprite = Sprite.slalomSuccess;
	}

}
