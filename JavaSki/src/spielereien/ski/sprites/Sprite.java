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

	public static BufferedImage noSprite;

	public static Map<Integer, BufferedImage> playerSkiing = new HashMap<Integer, BufferedImage>();
	public static Map<Integer, BufferedImage> playerAir = new HashMap<Integer, BufferedImage>();
	public static BufferedImage playerDown;
	public static BufferedImage playerSitting;
	public static BufferedImage playerAirDown;
	public static BufferedImage playerGliding;
	public static BufferedImage playerPushingL;
	public static BufferedImage playerPushingR;
	public static BufferedImage playerClimbingL;
	public static BufferedImage playerClimbingR;

	public static BufferedImage rock;
	public static BufferedImage tree;
	public static BufferedImage smallHill;
	public static BufferedImage largeHill;
	public static BufferedImage skiramp;
	public static BufferedImage bush;
	public static BufferedImage stump;
	public static BufferedImage deepSnow;

	public static BufferedImage slalomRedLeft;
	public static BufferedImage slalomRedRight;
	public static BufferedImage slalomBlueLeft;
	public static BufferedImage slalomBlueRight;
	public static BufferedImage slalomSuccess;
	public static BufferedImage slalomFail;

	public static BufferedImage liftMast;
	public static BufferedImage liftGondolaUp;
	public static BufferedImage liftGondolaDown;

	public static BufferedImage signSlalom;
	public static BufferedImage signStartLeft;
	public static BufferedImage signStartRight;
	public static BufferedImage signFinishLeft;
	public static BufferedImage signFinishRight;
	public static BufferedImage signCliff;

	public static BufferedImage line;

	public static BufferedImage liftBuilding;
	public static BufferedImage liftStationLower;
	public static BufferedImage liftStationUpper;
	public static BufferedImage liftStationUpperRoof;
	public static BufferedImage liftStationShadow;

	public static BufferedImage snowboarderRight;
	public static BufferedImage snowboarderLeft;
	public static BufferedImage snowboarderFlipLeft;
	public static BufferedImage snowboarderFlipCenter;
	public static BufferedImage snowboarderFlipRight;
	public static BufferedImage snowboarderCliff;

	public static BufferedImage lowerCliffEdge;
	public static BufferedImage upperCliffEdge;
	public static BufferedImage cliff;
	public static Map<Integer, BufferedImage> cliffs = new HashMap<Integer, BufferedImage>();

	public static void bufferImages() {

		try {
			String path = "src/spielereien/ski/sprites/";

			noSprite = ImageIO.read(new File(path + "noSprite.png"));

			for (int i = -4; i <= 4; i++) {
				playerSkiing.put(i, ImageIO.read(new File(path + "skiing" + i + ".png")));
			}
			for (int i = -1; i <= 1; i++) {
				playerAir.put(i, ImageIO.read(new File(path + "air" + i + ".png")));
			}
			playerAir.put(2, ImageIO.read(new File(path + "airBack.png")));
			playerAir.put(10, ImageIO.read(new File(path + "airFrontflip.png")));
			playerAir.put(-10, ImageIO.read(new File(path + "airBackflip.png")));
			playerPushingL = ImageIO.read(new File(path + "skiingPushingL.png"));
			playerPushingR = ImageIO.read(new File(path + "skiingPushingR.png"));
			playerClimbingL = ImageIO.read(new File(path + "climbingL.png"));
			playerClimbingR = ImageIO.read(new File(path + "climbingR.png"));

			playerSitting = ImageIO.read(new File(path + "sitting.png"));
			playerDown = ImageIO.read(new File(path + "down.png"));
			playerAirDown = ImageIO.read(new File(path + "airDown.png"));
			playerGliding = ImageIO.read(new File(path + "gliding.png"));

			rock = ImageIO.read(new File(path + "rock.png"));
			tree = ImageIO.read(new File(path + "tree.png"));
			smallHill = ImageIO.read(new File(path + "hillSmall.png"));
			largeHill = ImageIO.read(new File(path + "hillLarge.png"));
			skiramp = ImageIO.read(new File(path + "ramp.png"));
			bush = ImageIO.read(new File(path + "bush.png"));
			stump = ImageIO.read(new File(path + "stump.png"));
			deepSnow = ImageIO.read(new File(path + "deepSnow.png"));

			slalomRedLeft = ImageIO.read(new File(path + "slalomRedLeft.png"));
			slalomRedRight = ImageIO.read(new File(path + "slalomRedRight.png"));
			slalomBlueLeft = ImageIO.read(new File(path + "slalomBlueLeft.png"));
			slalomBlueRight = ImageIO.read(new File(path + "slalomBlueRight.png"));
			slalomSuccess = ImageIO.read(new File(path + "slalomSuccess.png"));
			slalomFail = ImageIO.read(new File(path + "slalomFail.png"));

			signSlalom = ImageIO.read(new File(path + "signSlalom.png"));
			signStartLeft = ImageIO.read(new File(path + "signSlalomStartLeft.png"));
			signStartRight = ImageIO.read(new File(path + "signSlalomStartRight.png"));
			signFinishLeft = ImageIO.read(new File(path + "signSlalomFinishLeft.png"));
			signFinishRight = ImageIO.read(new File(path + "signSlalomFinishRight.png"));
			signCliff = ImageIO.read(new File(path + "signCliff.png"));

			line = ImageIO.read(new File(path + "line.png"));

			liftBuilding = ImageIO.read(new File(path + "liftBuilding.png"));
			liftStationLower = ImageIO.read(new File(path + "liftStationLower.png"));
			liftStationUpper = ImageIO.read(new File(path + "liftStationUpper.png"));
			liftStationUpperRoof = ImageIO.read(new File(path + "liftStationUpperRoof.png"));
			liftStationShadow = ImageIO.read(new File(path + "liftStationShadow.png"));

			liftMast = ImageIO.read(new File(path + "liftMast.png"));
			liftGondolaUp = ImageIO.read(new File(path + "liftGondolaUp.png"));
			liftGondolaDown = ImageIO.read(new File(path + "liftGondolaDown.png"));

			snowboarderLeft = ImageIO.read(new File(path + "snowboarderLeft.png"));
			snowboarderRight = ImageIO.read(new File(path + "snowboarderRight.png"));
			snowboarderFlipLeft = ImageIO.read(new File(path + "snowboarderFlipLeft.png"));
			snowboarderFlipCenter = ImageIO.read(new File(path + "snowboarderFlipCenter.png"));
			snowboarderFlipRight = ImageIO.read(new File(path + "snowboarderFlipRight.png"));
			snowboarderCliff = ImageIO.read(new File(path + "snowboarderCliff.png"));

			lowerCliffEdge = ImageIO.read(new File(path + "lowerCliffEdge.png"));
			upperCliffEdge = ImageIO.read(new File(path + "upperCliffEdge.png"));
			cliff = ImageIO.read(new File(path + "cliff2.png"));
			for (int i = 0; i <= 3; i++) {
				cliffs.put(i, ImageIO.read(new File(path + "cliff" + i + ".png")));
			}

			System.out.println("images loaded");
		} catch (IOException e) {
			System.out.println("image loading exception");
			System.out.println(e.getMessage());
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
