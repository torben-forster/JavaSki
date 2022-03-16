package spielereien.ski;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import java.awt.image.BufferedImage;

import spielereien.ski.obstacle.CliffLowerEdge;
import spielereien.ski.obstacle.Collideable;
import spielereien.ski.obstacle.DeepSnow;
import spielereien.ski.obstacle.FinishLine;
import spielereien.ski.obstacle.GondolaUp;
import spielereien.ski.obstacle.PoleSlalom;
import spielereien.ski.obstacle.Snowboarder;
import spielereien.ski.obstacle.Solid;
import spielereien.ski.obstacle.StartLine;
import spielereien.ski.obstacle.StationLower;
import spielereien.ski.obstacle.StationMiddle;
import spielereien.ski.obstacle.StationUpper;
import spielereien.ski.sprites.Sprite;

public class Player extends Drawable {
	public double x;
	public double y;
	double z;

	double speedX;
	double speedY;
	double speedZ;

	double turbo;

	final static int SKIING = 0;
	final static int DOWN = 1;
	final static int SITTING = 2;
	final static int SLOW = 3;
	final static int GONDOLA = 4;
	final static int WAITING = 5;
	public final static int GAMEOVER = 6;

	final static int LEFT = -1;
	final static int RIGHT = 1;

	final static int FRONTFLIP = 10;
	final static int BACKFLIP = -10;

	boolean inSlalom;
	public double slalomTimer;

	public static final double GRAVITY = -0.5;

	int totalScore;
	int currentScore;

	int climbingTimer;
	int graceTimer;
	int knockoutTimer;
	int currentScoreTimer;
	int skipTimer;
	int pushingTimer;

	boolean heldG;

	int heading; // 0 is down, negative is left, positive is right
	int airHeading;
	double speed;

	int pushing;

	GondolaUp myGondola;
	Shadow myShadow;

	BufferedImage currentSprite;

	public int state; // eg. standing, crashed, etc

	public Player(int x, int y) {

		// coordinates relative to the window, x and y always centered in window
		this.x = x;
		this.y = y;
		this.z = 0;

		this.speedX = 0;
		this.speedY = 0;
		this.speedZ = 0;

		this.pushing = 0;

		this.turbo = 1;

		this.heading = 0;
		this.airHeading = 0;

		this.climbingTimer = 0;
		this.graceTimer = 5;
		this.knockoutTimer = -1;
		this.currentScoreTimer = -1;
		this.skipTimer = 0;
		this.pushingTimer = -1;

		this.heldG = false;

		this.totalScore = 0;
		this.currentScore = 0;

		this.inSlalom = false;
		this.slalomTimer = 0;

		this.state = SITTING;
		this.currentSprite = Sprite.playerSitting;

		this.myShadow = new Shadow();
	}

	private void stop() {
		speed = 0;
		speedZ = 0;
		heading = 0;
		climbingTimer = 0;
	}

	public void step() {

		handleTimers();

		switch (state) {
		case SKIING:
			if (onGround()) {
				speed += Math.cos(Math.PI * heading * 0.125) * 0.25 * turbo;
				speed -= speed * speed * 0.00125;

				if ((heading == 4 || heading == -4)) {
					// stop early when going horizontal
					speed -= speed * speed * 0.015;
				}
			}
			break;
		case DOWN:
			if (onGround()) {
				stop();
			}
			break;
		case SITTING:
			break;
		case SLOW:
			turnDownwards();
			speed += Math.cos(Math.PI * heading * 0.125) * 0.5;
			// more friction
			speed -= speed * speed * 0.05;
			state = SKIING;
			break;
		case GONDOLA:
			stop();
			x = myGondola.x + 8;
			y = myGondola.y - 1;
			z = 81;

			if (y < StationUpper.y + 25) {
				leaveGondola();
			}
			break;
		case WAITING:
			stop();
			GondolaUp currentClosest = findClosestGondolaUp();
			if (currentClosest.readyForPlayer) {
				enterGondola(currentClosest);
			}
			break;
		case GAMEOVER:
			heading = 0;

			speedZ -= speed;
			speed = 0;

			speedZ += GRAVITY;
			speedZ += speedZ * speedZ * 0.01;
			z += speedZ;
			break;
		}

		if (speed < 0) {
			speed = 0;
		}
		if (!onGround() && state != GAMEOVER) {
			speedZ += GRAVITY;
			z += speedZ;

			if (z < 0) {
				land();
			}
		}

		speedX = Math.sin(Math.PI * heading * 0.125) * speed;
		speedY = Math.cos(Math.PI * heading * 0.125) * speed;

		if (climbingTimer > 0) {
			speedY = -1.5 * turbo;
		}

		x += speedX;
		y += speedY;

		myShadow.x = x;
		myShadow.y = y;
	}

	private boolean atStation() {
		if (state == GONDOLA || !onGround()) {
			return false;
		}
		int stationX = StationLower.x - Sprite.liftStationLower.getWidth() / 2;
		int stationY = StationLower.y;

		if (x > stationX && x < stationX + 250) {

			if (y > stationY && y < stationY + 150) {
				return true;
			}
		}

		stationX = StationMiddle.x - Sprite.liftStationLower.getWidth() / 2;
		stationY = StationMiddle.y;

		if (x > stationX && x < stationX + 250) {

			if (y > stationY && y < stationY + 150) {
				return true;
			}
		}
		return false;

	}

	public void wrap() {
		// create illusion of endless map
		if (SkiPanel.player.x < 2000) {
			SkiPanel.player.x += 5000;
			for (Snowboarder s : Snowboarder.allSnowboarders) {
				s.x += 5000;
			}
		} else if (SkiPanel.player.x > 8000) {
			SkiPanel.player.x -= 5000;
			for (Snowboarder s : Snowboarder.allSnowboarders) {
				s.x -= 5000;
			}
		}
	}

	private void handleTimers() {

		graceTimer--;

		if (climbingTimer >= 0) {
			climbingTimer--;
		}

		if (knockoutTimer > 0 && onGround()) {
			knockoutTimer--;
		}

		if (knockoutTimer == 0 && (state == SKIING || state == WAITING)) {
			// just stood up
			knockoutTimer = -1;
		}

		if (knockoutTimer == 0) {
			graceTimer = 0;
			state = SITTING;
		}

		if (skipTimer > 30) {
			leaveGondola();
		}

		if (heldG) {
			skipTimer += 1;
		} else {
			skipTimer = 0;
		}
		heldG = false;

		if (pushingTimer > 0) {
			pushingTimer--;
		}
		if (Math.abs(heading) != 4) {
			pushingTimer = 0;
		}
		if (pushingTimer == 0) {
			pushing = 0;
			pushingTimer = -1;
		}

		if (inSlalom) {
			slalomTimer += 0.03;
		}

		Popup.advanceAllTimers();

	}

	public void handleSlalom(List<PoleSlalom> slalomSigns) {
		if (!inSlalom) {
			return;
		}
		for (PoleSlalom sign : slalomSigns) {
			if (sign.alreadyPassed) {
				continue;
			}

			if (y > sign.y) {
				double dX = x - sign.x;

				if (sign.facing == 'l') {
					if (dX < 0 && dX > -PoleSlalom.SPACING) {
						sign.success();
					} else {
						sign.fail();

					}
				} else {
					if (dX > 0 && dX < PoleSlalom.SPACING) {
						sign.success();
					} else {
						sign.fail();

					}

				}
			}
		}
	}

	public void handleCollisions(List<Collideable> collideables) {
		if (state == GAMEOVER) {
			return;
		}

		for (Collideable c : collideables) {

			double dX = Math.abs(x - c.x);
			double dY = Math.abs(y - c.y);

			if (dX < 5 + c.getMaskX((int) z) && dY < 5 + c.maskY && (z < 2 + c.maskZ)) {
				if (c.enabled) {
					collisionWith(c);
				}
			} else if (!c.enabled) {
				c.enabled = true;
			}
		}
	}

	private void collisionWith(Collideable coll) {
		if (!(coll instanceof Solid || coll instanceof DeepSnow)) {
			coll.enabled = false;
		}

		if (coll.jumpable && !onGround() && Math.abs(airHeading) <= 1) {
			kickedJump(coll.jumpMult);

		} else if (coll.ramp) {
			kickedJump(coll.jumpMult);

		} else if (coll instanceof DeepSnow) {
			if (Math.abs(heading) != 4 && state == SKIING) {
				state = SLOW;
			}
			if (speedZ < 0) {
				accident();
			}
		} else if (coll instanceof StartLine) {
			inSlalom = true;
			slalomTimer = 0;

			PoleSlalom.resetAll();

		} else if (coll instanceof FinishLine) {
			inSlalom = false;

		} else if (coll instanceof CliffLowerEdge) {
			state = GAMEOVER;
			currentScoreTimer = 0;
			new EndPanel();

		} else {
			accident();
		}

		if (inSlalom && coll instanceof PoleSlalom) {
			((PoleSlalom) coll).fail();
		}

		if (coll instanceof Solid) {

			speed = 0;
			x += Math.signum(x - coll.x);
			y += Math.signum(y - coll.y);
		}

		if (coll instanceof Snowboarder)

		{
			((Snowboarder) coll).flip();
		}
	}

	private void accident() {
		if (graceTimer > 0 || state == DOWN || state == GONDOLA) {
			return;
		}

		currentScore = currentScore / 2;
		currentScore -= 10;
		currentScoreTimer = 20;

		state = DOWN;
		knockoutTimer = 40;
	}

	public void handleScores() {
		if (inSlalom) {
			currentScore = 0;
			currentScoreTimer = 0;
		}

		if (currentScoreTimer == 0) {
			totalScore += currentScore;
			currentScore = 0;
		}

		if (currentScoreTimer >= 0 && onGround()) {
			currentScoreTimer--;
		}

	}

	public void inputUp() {
		if (inputBlocked()) {
			return;
		}

		if (onGround()) {
			if (state == SKIING) {

				if ((heading == -4 || heading == 4) && speed <= 2) {
					speed = 0;
					climbingTimer = 4;
				}

				turnUpwards();
			} else if (state == SITTING) {
				state = SKIING;
				climbingTimer = 3;
				if (heading >= 0) {
					heading = 4;

				} else {
					heading = -4;

				}
			}
		} else {
			// air
			currentScore += 3;

			airHeading = airHeading / 10;
			airHeading--;
			airHeading = airHeading * 10;
			if (airHeading < BACKFLIP) {
				airHeading = FRONTFLIP;
			}
		}
	}

	private void turnUpwards() {
		if (heading < 0 && heading > -4) {
			heading--;
		} else if (heading > 0 && heading < 4) {
			heading++;
		}
	}

	public void inputDown() {
		if (inputBlocked()) {
			return;
		}

		if (onGround()) {
			if (state == SKIING) {

				turnDownwards();

			} else if (state == SITTING) {
				state = SKIING;
				heading = 0;
			}
		} else {
			// air
			currentScore += 3;

			airHeading = airHeading / 10;
			airHeading++;
			airHeading = airHeading * 10;
			if (airHeading > FRONTFLIP) {
				airHeading = BACKFLIP;
			}
		}
	}

	private void turnDownwards() {
		if (heading < 0) {
			heading++;
		} else if (heading > 0) {
			heading--;
		}
	}

	public void inputLeft() {
		if (inputBlocked()) {
			return;
		}

		if (onGround()) {
			if (state == SKIING) {

				if (heading == -4) {
					speed += 0.75 * turbo;
					if (pushingTimer <= 0) {
						pushing = LEFT;
						pushingTimer = 10;
					}
				} else if (heading > -4) {
					heading--;
				}
			} else if (state == SITTING) {
				state = SKIING;
				heading = -4;

			}
		} else {
			currentScore += 2;

			airHeading = airHeading % 10;
			airHeading--;
			if (airHeading < -1) {
				airHeading = 2;
			}
		}

	}

	public void inputRight() {
		if (inputBlocked()) {
			return;
		}

		if (onGround()) {
			if (state == SKIING) {

				if (heading == 4) {
					speed += 0.75 * turbo;
					if (pushingTimer <= 0) {
						pushing = RIGHT;
						pushingTimer = 10;
					}
				} else if (heading < 4) {
					heading++;
				}
			} else if (state == SITTING) {
				state = SKIING;
				heading = 4;
			}
		} else {
			currentScore += 2;

			airHeading = airHeading % 10;
			airHeading++;
			if (airHeading > 2) {
				airHeading = -1;
			}
		}
	}

	public void inputSpace() {
		if (inputBlocked() && state != GONDOLA) {
			return;
		}

		if (onGround() && state == SITTING) {
			state = SKIING;

		} else if (onGround()) {
			state = SKIING;
			jump();
		} else if (state == GONDOLA) {
			state = SKIING;
			jump();
			airHeading = 1;
		}
	}

	public void inputG() {
		if (inputBlocked() && state != WAITING) {
			return;
		}
		System.out.println("input g detected");

		if (state == WAITING) {
			state = SITTING;
		} else if (atStation()) {
			state = WAITING;
		}

	}

	public void inputHeldG() {
		if (state == GONDOLA) {
			heldG = true;
		}
	}

	/**
	 * jumping by pressing SPACE
	 */
	private void jump() {
		heading = 0;
		airHeading = 0;

		speed += 0.5;
		speedZ = 4.5;

		currentScore += 1;
		currentScoreTimer = 20;

		z += 0.1;
	}

	/**
	 * jumping over an obstacle (i.e. ramp), can generate COMBO points
	 * 
	 * @param mult double, multiplier for upwards speed, usually determined by the
	 *             obstacle
	 */
	private void kickedJump(double mult) {
		heading = 0;
		airHeading = 0;

		speed += 0.5;
		speedZ = speed * mult;

		int comboScore = (int) (currentScore * 0.5);
		if (comboScore != 0) {
			new Popup("COMBO +" + Integer.toString(comboScore), Sprite.DARK_GREEN);
			currentScore += comboScore;
		}

		currentScore += speedZ;
		currentScoreTimer = 20;

		z += 0.1;

	}

	private void land() {
		z = 0;

		speed += Math.sqrt(Math.abs(speedZ));
		speedZ = 0;

		if (airHeading <= 1 && airHeading >= -1) {
			heading = airHeading;
			if (heading != 0) {
				currentScore -= 5;
			}

		} else {
			accident();
		}

		currentScoreTimer = 30;

	}

	public void enterGondolaDebug() {
		enterGondola(findClosestGondolaUp());
	}

	public void enterGondola(GondolaUp gondola) {
		myGondola = gondola;
		state = GONDOLA;
		currentScoreTimer = 0;
		x = myGondola.x;
		y = myGondola.y;
		z = 80;
		speed = 0;
	}

	public void leaveGondola() {
		myGondola = null;
		state = SITTING;

		x = StationUpper.exitX;
		y = StationUpper.exitY;
		z = 0;
		speed = 0;
	}

	private GondolaUp findClosestGondolaUp() {
		GondolaUp currentClosest = null;
		for (GondolaUp gondola : GondolaUp.everyGondolaUp) {

			if (currentClosest == null) {
				currentClosest = gondola;
				continue;
			}

			if (euclideanDistance(x, y, gondola.x, gondola.y) < euclideanDistance(x, y, currentClosest.x,
					currentClosest.y)) {
				currentClosest = gondola;
			}

		}
		return currentClosest;
	}

	private double euclideanDistance(double x1, double y1, double x2, double y2) {
		double dX = x1 - x2;
		double dY = y1 - y2;
		return (Math.sqrt(dX * dX + dY * dY));
	}

	private boolean onGround() {
		if (z == 0) {
			return true;
		} else {
			return false;
		}
	}

	private boolean inputBlocked() {
		if (knockoutTimer > 0 || state == SLOW || state == GONDOLA || state == WAITING) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void drawMe(Graphics g) {
		int drawX = getDrawX();
		int drawY = getDrawY();

		g.setColor(Color.BLACK);
		// g.drawString("x: " + x + " y: " + y, drawX, drawY);

		if (onGround() || state == GONDOLA || state == GAMEOVER) {
			myShadow.enabled = false;
		} else {
			myShadow.enabled = true;
		}

		drawPlayerSprite(g, drawX, drawY);

		drawCurrentScore(g, drawX, drawY);

		g.setColor(Color.BLACK);

		drawSkipPrompt(g, drawX, drawY);
		drawStationPrompt(g, drawX, drawY);

		g.drawString("Score: " + Integer.toString(totalScore), 10, 15);
		g.drawString("Speed: " + Integer.toString((int) speed), 10, 30);

		if (slalomTimer > 0) {
			g.drawString("Time: " + String.format("%1.2f s", slalomTimer), 10, 45);
		}

		if (state == DOWN) {
			g.drawString("OUCH", drawX - 20, drawY - 15);
		}

		Popup.drawAllPopups(g);

		/*
		 * g.setColor(Color.RED); g.drawRect(drawX, drawY, 2, 2);
		 */
	}

	private void drawSkipPrompt(Graphics g, int drawX, int drawY) {
		if (state == GONDOLA) {
			g.drawString("hold 'G' to skip to the top", drawX + 20, drawY - 10);

			if (skipTimer > 0) {
				g.setColor(Sprite.SHADOW);
				g.drawLine(drawX + 20, drawY - 9, drawX + 20 + 26 * 5, drawY - 9);
				g.setColor(Color.BLACK);
				g.drawLine(drawX + 20, drawY - 9, (int) (drawX + 20 + (26 * 5 * Math.min(skipTimer, 30.0) / 30)),
						drawY - 9);
			}
		}
	}

	private void drawStationPrompt(Graphics g, int drawX, int drawY) {
		if (atStation()) {
			if (state != WAITING) {
				g.drawString("press 'G' to wait for the next chairlift", drawX + 20, drawY - 10);
			} else {
				g.drawString("press 'G' to stop waiting for the chairlift", drawX + 20, drawY - 10);
			}
		}
	}

	private void drawPlayerSprite(Graphics g, int drawX, int drawY) {
		switch (state) {
		case SLOW:
			if (heading == 0) {
				currentSprite = Sprite.playerAir.get(0);
				break;
			}
		case SKIING:
			if (onGround()) {
				switch (pushing) {
				case 0:
					if (climbingTimer >= 2) {
						if (heading == 4) {
							currentSprite = Sprite.playerClimbingR;
						} else {
							currentSprite = Sprite.playerClimbingL;
						}
					} else {
						currentSprite = Sprite.playerSkiing.get(heading);
					}
					break;
				case LEFT:
					currentSprite = Sprite.playerPushingL;
					break;
				case RIGHT:
					currentSprite = Sprite.playerPushingR;
					break;
				}

			} else {
				currentSprite = Sprite.playerAir.get(airHeading);
			}
			break;
		case DOWN:
			if (onGround()) {
				currentSprite = Sprite.playerDown;
			} else {
				currentSprite = Sprite.playerAirDown;
			}
			break;
		case WAITING:
		case SITTING:
			currentSprite = Sprite.playerSitting;
			break;
		case GONDOLA:
			currentSprite = Sprite.playerAir.get(2);
			break;
		case GAMEOVER:
			currentSprite = Sprite.playerGliding;
			break;
		}

		Sprite.drawPlayerSprite(g, drawX, drawY, currentSprite);
	}

	private void drawCurrentScore(Graphics g, int drawX, int drawY) {
		if (currentScore != 0) {
			String score;

			if (currentScore > 0) {
				g.setColor(Color.BLACK);
				score = "+" + Integer.toString(currentScore);
			} else {
				g.setColor(Color.RED);
				score = Integer.toString(currentScore);
			}
			g.drawString(score, drawX - 3 * score.length(), drawY - 35);
		}
	}

	@Override
	public double getDrawHeight() {
		return y - 5 + 0.1;
	}

	@Override
	public int getDrawX() {
		return (int) (dimension.getSize().getWidth() * 0.5);
	}

	@Override
	public int getDrawY() {
		return (int) (dimension.getSize().getHeight() * 0.35 - z);
	}
}
