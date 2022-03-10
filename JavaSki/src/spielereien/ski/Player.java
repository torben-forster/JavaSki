package spielereien.ski;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;

import java.awt.image.BufferedImage;

import spielereien.ski.obstacle.Collideable;
import spielereien.ski.obstacle.DeepSnow;
import spielereien.ski.obstacle.PoleSlalom;
import spielereien.ski.obstacle.PoleSlalomLeft;
import spielereien.ski.obstacle.Solid;
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

	final static int FRONTFLIP = 10;
	final static int BACKFLIP = -10;

	static final double GRAVITY = -0.5;

	static Dimension dimension;

	int totalScore;
	int currentScore;

	int climbingTimer;
	int graceTimer;
	int knockoutTimer;
	int currentScoreTimer;

	int heading; // 0 is down, negative is left, positive is right
	int airHeading;
	double speed;

	BufferedImage currentSprite;

	int state; // eg. standing, crashed, etc

	public Player(int x, int y) {

		// coordinates relative to the window, x and y always centered in window
		this.x = x;
		this.y = y;
		this.z = 0;

		this.speedX = 0;
		this.speedY = 0;
		this.speedZ = 0;

		this.turbo = 1;

		this.heading = 0;
		this.airHeading = 0;

		this.climbingTimer = 0;
		this.graceTimer = 5;
		this.knockoutTimer = -1;
		this.currentScoreTimer = -1;

		this.totalScore = 0;
		this.currentScore = 0;

		this.state = SITTING;
		this.currentSprite = Sprite.playerSitting;
	}

	public void step() {

		handleTimers();

		if (state == SKIING && onGround()) {

			speed += Math.cos(Math.PI * heading * 0.125) * 0.25 * turbo;
			speed -= speed * speed * 0.00125;

			if ((heading == 4 || heading == -4)) {
				// stop early when going horizontal
				speed -= speed * speed * 0.015;
			}

		} else if (state == SLOW) {
			turnDownwards();

			speed += Math.cos(Math.PI * heading * 0.125) * 0.5;
			// more friction
			speed -= speed * speed * 0.05;

			state = SKIING;
		}

		if (!onGround()) {

			speedZ += GRAVITY;
			z += speedZ;

			if (z < 0) {
				land();
			}
		}

		if (speed < 0) {
			speed = 0;
		}

		if (state == DOWN && onGround()) {
			speed = 0;
		}

		speedX = Math.sin(Math.PI * heading * 0.125) * speed;
		speedY = Math.cos(Math.PI * heading * 0.125) * speed;

		if (state == DOWN) {
			climbingTimer = 0;
		}
		if (climbingTimer > 0) {
			speedY = -1.5;
		}

		x += speedX;
		y += speedY;
	}

	public void wrap() {
		// create illusion of endless map
		if (SkiPanel.player.x < 2000) {
			SkiPanel.player.x += 5000;
		} else if (SkiPanel.player.x > 8000) {
			SkiPanel.player.x -= 5000;
		}
	}

	private void handleTimers() {
		climbingTimer--;
		graceTimer--;

		if (knockoutTimer > 0 && onGround()) {
			knockoutTimer--;
		}

		if (knockoutTimer == 0 && state == SKIING) {
			// just stood up
			knockoutTimer = -1;
		}

		if (knockoutTimer == 0) {
			graceTimer = 0;
			state = SITTING;
		}

		Popup.advanceAllTimers();

	}

	public void handleSlalom(List<PoleSlalom> slalomSigns) {
		for (PoleSlalom sign : slalomSigns) {
			if (sign.alreadyPassed) {
				continue;
			}
			if (Math.abs(x - sign.x) <= 500 && y - sign.y >= 0) {
				if (sign instanceof PoleSlalomLeft) {
					if (sign.x > x) {
						sign.success();
					} else {
						sign.fail();
					}
				} else {
					if (sign.x < x) {
						sign.success();
					} else {
						sign.fail();
					}

				}
			}
		}
	}

	public void handleCollisions(List<Collideable> collideables) {
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
			if (Math.abs(heading) != 4 && state != DOWN && state != SITTING) {
				state = SLOW;
			}
			if (speedZ < 0) {
				accident();
			}
		} else {
			accident();
		}

		if (coll instanceof Solid) {
			speed = 0;
			x += Math.signum(x - coll.x);
			y += Math.signum(y - coll.y);
		}

	}

	private void accident() {
		if (graceTimer > 0 || state == DOWN) {
			return;
		}
		currentScore = currentScore / 2;
		currentScore -= 10;
		currentScoreTimer = 20;

		state = DOWN;
		knockoutTimer = 40;
	}

	public void handleScores() {

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
					climbingTimer = 6;
				}

				turnUpwards();
			} else if (state == SITTING) {
				state = SKIING;
				if (heading >= 0) {
					heading = 4;
					climbingTimer = 6;
				} else {
					heading = -4;
					climbingTimer = 6;
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
					speed += 0.5;
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
					speed += 0.5;
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
		if (inputBlocked()) {
			return;
		}

		if (onGround() && state == SITTING) {
			state = SKIING;

		} else if (onGround()) {
			state = SKIING;
			jump();
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
	 * @param mult double, multiplier
	 */
	private void kickedJump(double mult) {
		heading = 0;
		airHeading = 0;

		speed += 0.5;
		speedZ = speed * mult * 1;

		int comboScore = (int) (currentScore * 0.25);
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
		speedZ = 0;

		if (airHeading <= 1 && airHeading >= -1) {
			heading = airHeading;
			if (heading != 0) {
				currentScore -= 5;
			}

		} else {
			accident();
		}

		currentScoreTimer = 20;

	}

	private boolean onGround() {
		if (z == 0) {
			return true;
		} else {
			return false;
		}
	}

	private boolean inputBlocked() {
		if (knockoutTimer > 0 || state == SLOW) {
			return true;
		} else {
			return false;
		}
	}

	public void updateDimension(Dimension dim) {
		dimension = dim;
	}

	@Override
	public void drawMe(Graphics g) {
		int drawX = getDrawX();
		int drawY = getDrawY();

		drawShadow(g, drawX, drawY);

		drawPlayerSprite(g, drawX, drawY);

		drawCurrentScore(g, drawX, drawY);

		g.setColor(Color.BLACK);

		g.drawString("Score: " + Integer.toString(totalScore), 10, 15);
		g.drawString("Speed: " + Integer.toString((int) speed), 10, 30);

		if (state == DOWN) {
			g.drawString("OUCH", drawX - 20, drawY - 15);
		}

		Popup.drawAllPopups(g);

		/*
		 * g.setColor(Color.RED); g.drawRect(drawX, drawY, 2, 2);
		 */
	}

	public void drawShadow(Graphics g, int drawX, int drawY) {
		if (!onGround()) {
			g.setColor(Sprite.SHADOW);
			g.fillOval(drawX - 10, (int) (drawY + z - 3), 20, 6);
		}
	}

	public void drawPlayerSprite(Graphics g, int drawX, int drawY) {
		switch (state) {
		case SLOW:
			if (heading == 0) {
				currentSprite = Sprite.playerAir.get(0);
				break;
			}
		case SKIING:
			if (onGround()) {
				currentSprite = Sprite.playerSkiing.get(heading);
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
		case SITTING:
			currentSprite = Sprite.playerSitting;
			break;
		case GONDOLA:
			currentSprite = Sprite.playerAir.get(2);
			break;
		}

		Sprite.drawPlayerSprite(g, drawX, drawY, currentSprite);
	}

	public void drawCurrentScore(Graphics g, int drawX, int drawY) {
		if (currentScore != 0) {
			String score;

			if (currentScore > 0) {
				g.setColor(Color.BLACK);
				score = "+" + Integer.toString(currentScore);
			} else {
				g.setColor(Color.RED);
				score = Integer.toString(currentScore);
			}
			g.drawString(score, drawX - 7 * (score.length() / 2), drawY - 35);
		}
	}

	@Override
	public double getDrawHeight() {
		return y + 0.1;
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
