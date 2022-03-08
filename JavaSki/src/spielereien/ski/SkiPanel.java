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
import spielereien.ski.obstacle.GondolaDown;
import spielereien.ski.obstacle.GondolaUp;
import spielereien.ski.obstacle.LargeHill;
import spielereien.ski.obstacle.LiftMast;
import spielereien.ski.obstacle.Ramp;
import spielereien.ski.obstacle.Rock;
import spielereien.ski.obstacle.SignSlalom;
import spielereien.ski.obstacle.PoleSlalom;
import spielereien.ski.obstacle.PoleSlalomLeft;
import spielereien.ski.obstacle.PoleSlalomRight;
import spielereien.ski.obstacle.SmallHill;
import spielereien.ski.obstacle.Station;
import spielereien.ski.obstacle.Stump;
import spielereien.ski.obstacle.Tree;
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

	static Dimension windowDimension;

	Map<String, String> heldKeys = new HashMap<String, String>();
	Map<String, String> pressedKeys = new HashMap<String, String>();

	List<Drawable> drawables = new LinkedList<Drawable>();

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
		player.updateDimension(windowDimension);

		spawnObstacles();
		clearLocation(4000, 250, 6000, 750);
		spawnStation();
		spawnSlalom();
		
		//spawnDebugging();

		Collections.sort(collideables);
		drawables.addAll(collideables);

		paused = false;
		// sortDrawables();

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
				player.x = 0;
				player.y = 0;
			} else if (keyPressed.equals("F")) {
				if (player.turbo < 2) {
					player.turbo = 3;
				} else {
					player.turbo = 1;
				}
			}
		}
		if (keyPressed.equals("P")) {
			paused = !paused;
		}
	}

	private void reset() {
		player.x = 5000;
		player.y = 500;
		player.z = 0;
		player.speed = 0;
		player.heading = 0;
		player.currentScoreTimer = 0;
		player.state = 2;

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
			player.updateDimension(windowDimension);
			Collideable.updateDimension(windowDimension);

		}

		player.step();
		player.wrap();

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
	private void clearLocation(int x, int y, int x2, int y2) {
		ArrayList<Collideable> collideablesToRemove = new ArrayList<Collideable>();
		for (Collideable c : collideables) {
			if (c.x >= x && c.x <= x2 && c.y >= y && c.y <= y2) {
				collideablesToRemove.add(c);
			}
		}
		collideables.removeAll(collideablesToRemove);
	}

	private void spawnStation() {
		// to see if the sprites work
		new LiftMast(5000, 1500);
		new GondolaUp(5022, 1600);
		new GondolaDown(4978, 1700);
		new Station(5000, 2000);
	}
	
	private void spawnDebugging() {
		
		new DeepSnow(5100,1200);
		new Rock(5100,1220);
	}

	private void spawnSlalom() {
		int row = 5000;

		// player starts at x=5000, y = 500

		int x = 4250;

		new SignSlalom(x + 250, 350);
		new SignSlalom(x + 250 + row, 350);

		for (int y = 1000; y < 7500; y += 250) {
			if (y % 500 == 0) {
				PoleSlalomRight.spawnSlalomPair(x, y, row);
			} else {
				PoleSlalomLeft.spawnSlalomPair(x, y, row);
			}
		}
	}

	private void spawnObstacles() {

		/*
		 * creates obstacles in two rows at the same height, with player's wrap() that
		 * creates the illusion of an endless world (horizontally at least)
		 */

		int row = 5000;

		for (int x = 0; x < row; x += 10) {
			for (int y = 0; y < 15000; y += 10) {

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

					} else if (rnd < 0.925) {
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

		drawtime = System.currentTimeMillis();
		boolean playerDrawn = false;
		for (Drawable d : drawables) {

			if (!playerDrawn && d.getDrawHeight() > player.getDrawHeight()) {
				player.drawMe(g);
				playerDrawn = true;
			}
			d.drawMe(g);

		}
		if (!playerDrawn) {
			player.drawMe(g);
		}

		// drawDebug(g);
	}

	public void drawDebug(Graphics g) {
		g.setColor(Color.BLACK);

		g.drawString("objects in collideables: " + collideables.size(), 5, 20);
		g.drawString("Player x: " + (int) player.x + " y: " + (int) player.y, 5, 30);

		g.drawString("drawtime: " + (System.currentTimeMillis() - drawtime), 5, 40);
		g.drawString("collisiontime: " + (collisiontime - prevCollisiontime), 5, 50);
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
