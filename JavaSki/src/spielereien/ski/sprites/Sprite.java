package spielereien.ski.sprites;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class Sprite {

	public final static Color DARK_GREEN = new Color(50, 100, 50);
	public final static Color BROWN = new Color(185, 122, 87);
	public final static Color SHADOW = new Color(0, 0, 0, 50);

	public static Map<Integer, BufferedImage> playerSkiing = new HashMap<Integer, BufferedImage>();
	public static Map<Integer, BufferedImage> playerAir = new HashMap<Integer, BufferedImage>();
	public static BufferedImage playerDown;
	public static BufferedImage playerSitting;
	public static BufferedImage playerAirDown;

	public static BufferedImage rock;
	public static BufferedImage tree;
	public static BufferedImage smallHill;
	public static BufferedImage largeHill;
	public static BufferedImage skiramp;
	public static BufferedImage bush;
	public static BufferedImage stump;
	public static BufferedImage deepSnow;

	public static BufferedImage slalomLeft;
	public static BufferedImage slalomRight;
	public static BufferedImage slalomSuccess;
	public static BufferedImage slalomFail;

	public static BufferedImage liftMast;
	public static BufferedImage liftGondolaUp;
	public static BufferedImage liftGondolaDown;

	public static BufferedImage signSlalom;

	public static BufferedImage station;

	public static void bufferImages() {

		try {
			String path = "src/spielereien/ski/sprites/";
			for (int i = -4; i <= 4; i++) {
				playerSkiing.put(i, ImageIO.read(new File(path + "skiing" + i + ".png")));
			}
			for (int i = -1; i <= 1; i++) {
				playerAir.put(i, ImageIO.read(new File(path + "air" + i + ".png")));
			}
			playerAir.put(2, ImageIO.read(new File(path + "airBack.png")));
			playerAir.put(10, ImageIO.read(new File(path + "airFrontflip.png")));
			playerAir.put(-10, ImageIO.read(new File(path + "airBackflip.png")));

			playerSitting = ImageIO.read(new File(path + "sitting.png"));
			playerDown = ImageIO.read(new File(path + "down.png"));
			playerAirDown = ImageIO.read(new File(path + "airDown.png"));

			rock = ImageIO.read(new File(path + "rock.png"));
			tree = ImageIO.read(new File(path + "tree.png"));
			smallHill = ImageIO.read(new File(path + "hillSmall.png"));
			largeHill = ImageIO.read(new File(path + "hillLarge.png"));
			skiramp = ImageIO.read(new File(path + "ramp.png"));
			bush = ImageIO.read(new File(path + "bush.png"));
			stump = ImageIO.read(new File(path + "stump.png"));
			deepSnow = ImageIO.read(new File(path + "deepSnow.png"));

			slalomLeft = ImageIO.read(new File(path + "slalomLeft.png"));
			slalomRight = ImageIO.read(new File(path + "slalomRight.png"));
			slalomSuccess = ImageIO.read(new File(path + "slalomSuccess.png"));
			slalomFail = ImageIO.read(new File(path + "slalomFail.png"));

			signSlalom = ImageIO.read(new File(path + "signSlalom.png"));

			station = ImageIO.read(new File(path + "station.png"));

			liftMast = ImageIO.read(new File(path + "liftMast.png"));
			liftGondolaUp = ImageIO.read(new File(path + "liftGondolaUp.png"));
			liftGondolaDown = ImageIO.read(new File(path + "liftGondolaDown.png"));

			System.out.println("images loaded");
		} catch (IOException e) {

			System.out.println(e);
		}

	}

	public static void drawPlayerSprite(Graphics g, int x, int y, BufferedImage sprite) {

		g.drawImage(sprite, x - sprite.getWidth() / 2, y - sprite.getHeight(), null);

	}

	public static void drawSprite(Graphics g, int x, int y, BufferedImage sprite) {
		// "origin" of sprite is bottom-center

		g.drawImage(sprite, x, y, null);

	}

}
