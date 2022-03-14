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

import spielereien.ski.obstacle.Collideable;
import spielereien.ski.obstacle.Bush;
import spielereien.ski.obstacle.DeepSnow;
import spielereien.ski.obstacle.FinishLine;
import spielereien.ski.obstacle.Gondola;
import spielereien.ski.obstacle.GondolaDown;
import spielereien.ski.obstacle.GondolaUp;
import spielereien.ski.obstacle.LargeHill;
import spielereien.ski.obstacle.LiftMast;
import spielereien.ski.obstacle.Ramp;
import spielereien.ski.obstacle.Rock;
import spielereien.ski.obstacle.Sign;
import spielereien.ski.obstacle.PoleSlalom;
import spielereien.ski.obstacle.PoleSlalomRed;
import spielereien.ski.obstacle.PoleSlalomBlue;
import spielereien.ski.obstacle.SmallHill;
import spielereien.ski.obstacle.StartLine;
import spielereien.ski.obstacle.StationLower;
import spielereien.ski.obstacle.StationMiddle;
import spielereien.ski.obstacle.StationUpper;
import spielereien.ski.obstacle.Stump;
import spielereien.ski.obstacle.Tree;
import spielereien.ski.sprites.Sprite;

public class SkiPanel extends JLayeredPane implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	boolean gameOver = false;

	Timer timer;

	int heldTicker;

	final int windowWidth = 1000;
	final int windowHeight = 1000;

	public final static int LASTMAST = 16;

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
				player.y = 8000;
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

		for (Gondola g : Gondola.everyGondola) {
			g.step();
		}

		prevCollisiontime = System.currentTimeMillis();
		player.handleCollisions(collideables);
		collisiontime = System.currentTimeMillis();

		player.handleSlalom(PoleSlalom.allSlalomSigns);

		player.handleScores();

		repaint();
	}

	/**
	 * clears a location, specified by the parameters ,of all obstacles in
	 * {@link SkiPanel.collideables}
	 * 
	 * @param x  int, x-coordinate of upper-left corner
	 * @param y  int, y-coordinate of upper-left corner
	 * @param x2 int, x-coordinate of lower-right corner
	 * @param y2 int, y-coordinate of lower-right corner
	 */
	private void clearArea(int x, int y, int x2, int y2) {
		ArrayList<Collideable> collideablesToRemove = new ArrayList<Collideable>();
		for (Collideable c : collideables) {
			if (c.x >= x && c.x <= x2 && c.y >= y && c.y <= y2) {
				collideablesToRemove.add(c);
			}
		}
		collideables.removeAll(collideablesToRemove);
		drawables.removeAll(collideablesToRemove);
	}

	private void spawnLift() {

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
		new StationUpper(x, -600);
		new StationMiddle(x, 8500);
		new StationLower(x, 15480);
	}

	private void spawnSlalomCourse() {
		// player starts at x = 5000, y = -600

		int x = 4300;
		int y = 250;

		int rangeX = 175;

		clearArea(x - 200, y - 500, x + 200, y - 200);

		Sign.newSignSlalom(x, y - 450);

		Sign.newSignStartLeft(x + 75, y - 250);
		Sign.newSignStartRight(x - 75, y - 250);
		new StartLine(x, y - 249);

		for (; y < 8000; y += 250) {

			int spawnX = (int) (x + Math.random() * rangeX * 2 - rangeX);
			clearArea(spawnX - 100, y - 100, spawnX + 100, y + 100);

			if (y % 500 == 0) {
				PoleSlalomBlue.spawnSlalomPair(spawnX, y);
			} else {
				PoleSlalomRed.spawnSlalomPair(spawnX, y);
			}

		}

		Sign.newSignFinishLeft(x + 75, y);
		Sign.newSignFinishRight(x - 75, y);
		new FinishLine(x, y + 1);
	}

	private void spawnObstacles() {

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

	}

	protected void paintComponent(Graphics g) {
		// is called by repaint();

		super.paintComponent(g);

		// g.drawString("sorttime: " + (sortEnd - sortStart), 5, 60);

		Collections.sort(drawables);

		drawtime = System.currentTimeMillis();

		for (Drawable d : drawables) {
			d.drawMe(g);
		}

		for (Drawable d : drawables) {
			if (d instanceof LiftMast) {
				// ((LiftMast) d).drawCables(g);
			}
		}

		// drawDebug(g);
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
