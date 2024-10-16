package spielereien.ski;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JLayeredPane;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import spielereien.ski.obstacle.*;
import spielereien.ski.sprites.Sprite;

public class SkiPanel extends JLayeredPane implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final static long SEED = System.currentTimeMillis();

	boolean gameOver = false;

	Timer timer;

	int heldTicker;

	final int windowWidth = 1000;
	final int windowHeight = 1000;

	public final static int LASTMAST = 16;

	Sign slalomStartLeft;
	Sign slalomStartRight;
	Sign slalomFinishLeft;
	Sign slalomFinishRight;

	static Dimension windowDimension;

	Map<String, String> heldKeys = new HashMap<String, String>();
	Map<String, String> pressedKeys = new HashMap<String, String>();

	static List<Drawable> drawables = new LinkedList<Drawable>();

	public static List<Collideable> collideables = new LinkedList<Collideable>();
	// List<Collideable> collideablesToRemove = new LinkedList<Collideable>();

	boolean paused;

	public static Player player;

	static long drawtime;
	static long sortStart;
	static long sortEnd;
	static long collisiontime;
	static long prevCollisiontime;

	public SkiPanel() {

		// CONSTRUCTOR

		setOpaque(true);

		timer = new Timer(20, this);

		heldTicker = 10;

		setBackground(Color.WHITE);

		initializeKeyMaps();
		drawables.clear();
		collideables.clear();

		Sprite.bufferImages();

		windowDimension = getPreferredSize();
		Collideable.updateDimension(windowDimension);

		player = new Player(5000, 500);

		spawnObstacles();

		spawnLift();

		spawnSlalomCourse();

		spawnUpperCliff();
		spawnLowerCliff();

		Collections.sort(drawables);

		paused = false;
		reset();

		/*
		 * start the timer all the way at the end otherwise some multhithreading or some
		 * such makes the program step before the list is filled/sorted ->
		 * ConcurrentModificationException
		 */
		timer.start();

	}

	public void actionPerformed(ActionEvent e) {
		// Invoked when the Timer fires

		for (String keyHeld : heldKeys.keySet()) {

			if (heldTicker <= 0) {
				heldKeyInputs(keyHeld);
				heldTicker = 3;
			}

			if (keyHeld.equals("G")) {
				player.inputHeldG();
			}
		}

		for (String keyPressed : pressedKeys.keySet()) {
			pressedKeyInputs(keyPressed);
			heldTicker = 5;
		}

		heldTicker--;

		if (!paused) {

			stepSimulation();
		}

		heldKeys.putAll(pressedKeys);
		pressedKeys.clear();

	}

	private void heldKeyInputs(String keyHeld) {
		if (!paused) {
			if (keyHeld.equals("UP")) {
				player.inputUp();
			} else if (keyHeld.equals("DOWN")) {
				player.inputDown();
			} else if (keyHeld.equals("LEFT")) {
				player.inputLeft();
			} else if (keyHeld.equals("RIGHT")) {
				player.inputRight();
			}
		}
	}

	private void pressedKeyInputs(String keyPressed) {
		if (!paused) {
			if (keyPressed.equals("UP")) {
				player.inputUp();
			} else if (keyPressed.equals("DOWN")) {
				player.inputDown();
			} else if (keyPressed.equals("LEFT")) {
				player.inputLeft();
			} else if (keyPressed.equals("RIGHT")) {
				player.inputRight();
			}

			else if (keyPressed.equals("SPACE")) {
				player.inputSpace();
			}

			if (keyPressed.equals("DELETE")) {
				reset();
			} else if (keyPressed.equals("ENTER")) {
				player.x = 5300;
				player.y = 16000;
			} else if (keyPressed.equals("F")) {
				if (player.turbo < 2) {
					player.turbo = 3;
				} else {
					player.turbo = 1;
				}
			} else if (keyPressed.equals("G")) {
				player.inputG();
			}
		}
		if (keyPressed.equals("P")) {
			paused = !paused;
			getGraphics().drawString("PAUSED - PRESS 'P' TO CONTINUE", (int) (windowDimension.getWidth() / 2 - 30 * 5),
					50);
		}
	}

	private void reset() {
		player.x = StationUpper.x + Sprite.liftStationUpper.getWidth() / 2 + Sprite.liftBuilding.getWidth() - 24;
		player.y = StationUpper.y + Sprite.liftStationUpper.getHeight() / 2 - 24;
		player.z = 0;
		player.speed = 0;
		player.heading = 0;
		player.currentScoreTimer = 0;
		player.state = 2;

		Drawable.updateDimension(windowDimension);

		gameOver = false;
	}

	private void initializeKeyMaps() {

		addKeyMap("UP");
		addKeyMap("DOWN");
		addKeyMap("LEFT");
		addKeyMap("RIGHT");

		addKeyMap("SPACE");

		addKeyMap("F");
		addKeyMap("P");
		addKeyMap("G");

		addKeyMap("ENTER");
		addKeyMap("DELETE");

	}

	private void addKeyMap(String keyString) {
		InputMap inputMap = this.getInputMap();
		ActionMap actionMap = this.getActionMap();

		String keyPressed = "pressed " + keyString;
		String keyReleased = "released " + keyString;

		inputMap.put(KeyStroke.getKeyStroke(keyPressed), keyPressed);
		inputMap.put(KeyStroke.getKeyStroke(keyReleased), keyReleased);

		actionMap.put(keyPressed, new KeyAction(keyPressed));
		actionMap.put(keyReleased, new KeyAction(keyReleased));
	}

	public Dimension getPreferredSize() {
		return new Dimension(windowWidth, windowHeight);
	}

	private void stepSimulation() {

		Dimension previousDim = windowDimension;
		windowDimension = this.getSize();

		if (windowDimension.getWidth() != previousDim.getWidth()
				|| windowDimension.getHeight() != previousDim.getHeight()) {
			Drawable.updateDimension(windowDimension);
		}

		player.step();
		player.wrap();

		for (Snowboarder s : Snowboarder.allSnowboarders) {
			s.step();
		}

		for (Gondola g : Gondola.everyGondola) {
			g.step();
		}

		// prevCollisiontime = System.currentTimeMillis();
		player.handleCollisions(collideables);
		for (Snowboarder s : Snowboarder.allSnowboarders) {
			s.handleCollisions(collideables);
		}
		// collisiontime = System.currentTimeMillis();

		player.handleSlalom(PoleSlalom.allSlalomSigns);

		player.handleScores();

		repaint();
	}

	/**
	 * clears a rectangle, specified by the parameters, of all randomly generated
	 * obstacles in {@link SkiPanel.collideables}; Station buildings, chair lifts
	 * and lift masts are not removed
	 * 
	 * @param x  int, x-coordinate of upper-left corner
	 * @param y  int, y-coordinate of upper-left corner
	 * @param x2 int, x-coordinate of lower-right corner
	 * @param y2 int, y-coordinate of lower-right corner
	 */
	private void clearArea(int x, int y, int x2, int y2) {
		ArrayList<Collideable> collideablesToRemove = new ArrayList<Collideable>();
		for (Collideable c : collideables) {
			if (c instanceof Gondola || c instanceof LiftMast || c instanceof Solid) {
				continue;
			}
			if (c.x >= x && c.x <= x2 && c.y >= y && c.y <= y2) {
				collideablesToRemove.add(c);
			}
		}
		collideables.removeAll(collideablesToRemove);
		drawables.removeAll(collideablesToRemove);
	}

	private void spawnLift() {
		System.out.println("generating lift");

		clearArea(4900, -700, 5100, 15600);

		int x = 5000;
		int xGondolaUp = x + 28;
		int xGondolaDown = x - 30;

		int mastZaehler = LASTMAST;
		for (int y = 0; y <= 15000; y += 1000) {

			new LiftMast(x, y, mastZaehler--);
			new GondolaUp(xGondolaUp, y + 500);
			new GondolaUp(xGondolaUp, y);

			new GondolaDown(xGondolaDown, y + 500);
			new GondolaDown(xGondolaDown, y);
		}
		clearArea(x - 100, -700, x + 200, -500);
		new StationUpper(x, -600);
		clearArea(x - 100, 8400, x + 200, 8600);
		new StationMiddle(x, 8500);
		clearArea(x - 100, 15380, x + 200, 15580);
		new StationLower(x, 15480);

		System.out.println("lift generated");
	}

	private void spawnSlalomCourse() {
		// player starts at x = 5000, y = -600
		System.out.println("generating slalom with SEED: " + SEED);

		int x = 4300;
		int y = 250;

		int randomizerX = 100; // random offset in either direction

		clearArea(x - 200, y - 500, x + 200, y - 200);

		Sign.newSignSlalom(x, y - 450);

		slalomStartLeft = Sign.newSignStartLeft(x + 75, y - 250);
		slalomStartRight = Sign.newSignStartRight(x - 75, y - 250);
		new StartLine(x, y - 249);

		for (; y < 8000; y += 250) {

			int spawnX = (int) (x + myAlgorithm(y) * randomizerX * 2 - randomizerX);
			clearArea(spawnX - 100, y - 100, spawnX + 100, y + 100);

			if (y % 500 == 0) {
				PoleSlalomBlue.spawnSlalomPair(spawnX, y);
			} else {
				PoleSlalomRed.spawnSlalomPair(spawnX, y);
			}

		}

		clearArea(x - 200, y - 200, x + 200, y - 200);

		slalomFinishLeft = Sign.newSignFinishLeft(x + 75, y);
		slalomFinishRight = Sign.newSignFinishRight(x - 75, y);
		new FinishLine(x, y + 1);

		System.out.println("slalom generated");
	}

	private double myAlgorithm(int x) {
		return Math.sin(x * x * SEED);
	}

	private void spawnUpperCliff() {
		int y = -1100;

		for (int x = 0; x < 10000; x += 64) {
			new CliffUpperEdge(x, y, true);

		}

		for (int i = 0; i < 24; i++) {
			y -= 64;
			for (int x = 0; x < 10000; x += 64) {
				new CliffUpperEdge(x, y);
			}
		}
	}

	private void spawnLowerCliff() {
		int y = 16100;

		for (int x = 0; x < 10000; x += 64) {
			new CliffLowerEdge(x, y, true);

		}
		for (int x = 0; x < 10000; x += 500) {
			Sign.newSignCliff(x, y - 80);
		}

		for (int i = 0; i < 24; i++) {
			y += 64;
			for (int x = 0; x < 10000; x += 64) {
				new CliffLowerEdge(x, y);
			}
		}
	}

	private void spawnObstacles() {

		System.out.println("generating obstacles");

		new Snowboarder(4000, 0);

		/*
		 * creates obstacles in two rows at the same height, with player's wrap() that
		 * creates the illusion of an endless world (horizontally at least)
		 */

		int row = 5000;

		for (int x = 0; x < row; x += 10) {
			for (int y = -1000; y < 16000; y += 10) {

				if (Math.random() < 0.00125) {

					double rnd = Math.random();
					if (rnd < 0.125) {
						new Rock((int) (x), (int) (y));
						new Rock((int) (x + row), (int) (y));

					} else if (rnd < 0.25) {
						new Tree((int) (x), (int) (y));
						new Tree((int) (x + row), (int) (y));

					} else if (rnd < 0.375) {
						new Stump((int) (x), (int) (y));
						new Stump((int) (x + row), (int) (y));

					} else if (rnd < 0.5) {
						new LargeHill((int) (x), (int) (y));
						new LargeHill((int) (x + row), (int) (y));

					} else if (rnd < 0.625) {
						new Bush((int) (x), (int) (y));
						new Bush((int) (x + row), (int) (y));

					} else if (rnd < 0.75) {
						new DeepSnow((int) (x), (int) (y));
						new DeepSnow((int) (x + row), (int) (y));

					} else if (rnd < 0.95) {
						new SmallHill((int) (x), (int) (y));
						new SmallHill((int) (x + row), (int) (y));
					}

					else {
						new Ramp((int) (x), (int) (y));
						new Ramp((int) (x + row), (int) (y));

					}
				}

			}
		}

		System.out.println("obstacles generated");

	}

	protected void paintComponent(Graphics g) {
		// is called by repaint();

		super.paintComponent(g);

		// g.drawString("sorttime: " + (sortEnd - sortStart), 5, 60);

		Collections.sort(drawables);

		drawtime = System.currentTimeMillis();

		drawSlalomMarkers(g);

		for (Drawable d : drawables) {
			d.drawMe(g);
		}

		// drawDebug(g);
	}

	private void drawSlalomMarkers(Graphics g) {

		PoleSlalom lastPsR = null;

		int width = Sprite.slalomBlueLeft.getWidth();
		int height = Sprite.slalomBlueLeft.getHeight();

		for (PoleSlalom ps : PoleSlalom.allSlalomSigns) {

			if (ps.facing == 'r') {
				if (lastPsR == null) {
					drawFirstMarkers(g, width, height, ps);

					lastPsR = ps;
					continue;
				}

				drawMarkers(g, lastPsR, width, height, ps);

				lastPsR = ps;
			}

		}

		drawLastMarkers(g, lastPsR, width, height);
	}

	private void drawFirstMarkers(Graphics g, int width, int height, PoleSlalom ps) {
		// markers form first pole to start
		g.setColor(Sprite.MARKING);

		for (int i = -2; i < 2; i++) {
			g.drawLine(ps.getDrawX() + width + i, ps.getDrawY() + height,
					slalomStartRight.getDrawX() + Sprite.signStartRight.getWidth() + i - 4,
					slalomStartRight.getDrawY() + Sprite.signStartRight.getHeight() + 4);
			g.drawLine(ps.getDrawX() + i + PoleSlalom.SPACING, ps.getDrawY() + height,
					slalomStartRight.getDrawX() + i + 150 + 4,
					slalomStartRight.getDrawY() + Sprite.signStartRight.getHeight() + 4);
		}

		g.setColor(Color.WHITE);
		g.fillRect(ps.getDrawX() - 400 + PoleSlalom.SPACING / 2, ps.getDrawY() + height - 200, 800 + PoleSlalom.SPACING,
				150);
	}

	private void drawMarkers(Graphics g, PoleSlalom lastPsR, int width, int height, PoleSlalom ps) {
		// markers in between
		g.setColor(Sprite.MARKING);

		for (int i = -2; i < 2; i++) {

			g.drawLine(ps.getDrawX() + width + i, ps.getDrawY() + height, lastPsR.getDrawX() + width + i,
					lastPsR.getDrawY() + height);
			g.drawLine(ps.getDrawX() + i + PoleSlalom.SPACING, ps.getDrawY() + height,
					lastPsR.getDrawX() + +i + PoleSlalom.SPACING, lastPsR.getDrawY() + height);

		}

		g.setColor(Color.WHITE);
		g.fillRect(ps.getDrawX() - 400 + PoleSlalom.SPACING / 2, ps.getDrawY() + height - 200, 800 + PoleSlalom.SPACING,
				150);
	}

	private void drawLastMarkers(Graphics g, PoleSlalom lastPsR, int width, int height) {
		// markers form last pole to finish
		g.setColor(Sprite.MARKING);
		for (int i = -2; i < 2; i++) {

			g.drawLine(lastPsR.getDrawX() + width + i, lastPsR.getDrawY() + height,
					slalomFinishRight.getDrawX() + Sprite.signFinishRight.getWidth() + i - 4,
					slalomFinishRight.getDrawY() + Sprite.signFinishRight.getHeight() - 1);

			g.drawLine(lastPsR.getDrawX() + i + PoleSlalom.SPACING, lastPsR.getDrawY() + height,
					slalomFinishRight.getDrawX() + i + 150 + 4,
					slalomFinishRight.getDrawY() + Sprite.signFinishRight.getHeight() - 1);

		}

		g.setColor(Color.WHITE);
		g.fillRect(lastPsR.getDrawX() - 400 + PoleSlalom.SPACING / 2, lastPsR.getDrawY() + height + 50,
				800 + PoleSlalom.SPACING, 150);
	}

	public void drawDebug(Graphics g) {
		g.setColor(Color.BLACK);

		g.drawString("objects in collideables: " + collideables.size(), 10, 45);
		g.drawString("Player x: " + (int) player.x + " y: " + (int) player.y, 10, 60);

		g.drawString("drawtime: " + (System.currentTimeMillis() - drawtime), 10, 75);
		g.drawString("collisiontime: " + (collisiontime - prevCollisiontime), 10, 90);

		g.drawString("Skiptimer: " + player.skipTimer, 10, 105);
	}

	private class KeyAction extends AbstractAction implements ActionListener {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		String action;

		KeyAction(String _action) {
			action = _action;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// action is sth like "pressed SPACE"
			int spaceIndex = action.indexOf(" ");
			String actionName = action.substring(0, spaceIndex);
			String keyName = action.substring(spaceIndex + 1);

			if (actionName.equals("pressed") && !heldKeys.containsValue(keyName)) {
				pressedKeys.put(keyName, keyName);
			} else if (actionName.equals("released")) {
				heldKeys.remove(keyName);
				pressedKeys.remove(keyName);
			}

		}

	}

}
