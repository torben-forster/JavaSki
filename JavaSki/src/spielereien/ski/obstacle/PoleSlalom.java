package spielereien.ski.obstacle;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import spielereien.ski.Popup;
import spielereien.ski.SkiPanel;
import spielereien.ski.sprites.Sprite;

public abstract class PoleSlalom extends Collideable {

	public static ArrayList<PoleSlalom> allSlalomSigns = new ArrayList<PoleSlalom>();

	public boolean alreadyPassed;
	public char facing;

	public static final int SPACING = 120;

	PoleSlalom partner;

	public PoleSlalom(int x, int y, BufferedImage sprite) {
		super(x, y, sprite);

		alreadyPassed = false;
		allSlalomSigns.add(this);
		maskY = 1;
		maskX = 9; // sprite is 13
	}

	public static void resetAll() {
		for (PoleSlalom p : allSlalomSigns) {
			p.reset();
		}
	}

	public void reset() {
		getSprite();
		alreadyPassed = false;
	};

	protected abstract void getSprite();

	public void fail() {
		if (alreadyPassed) {
			return;
		}
		alreadyPassed = true;
		sprite = Sprite.slalomFail;

		new Popup("TIME +5", Color.RED);
		SkiPanel.player.slalomTimer += 5;

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
