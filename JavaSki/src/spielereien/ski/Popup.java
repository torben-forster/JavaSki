package spielereien.ski;

import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;

public class Popup extends Drawable {

	static ArrayList<Popup> allPopups = new ArrayList<Popup>();

	String text;
	int timer;
	Color color;
	double offsetX;

	public Popup(String text, Color color) {
		this.text = text;
		this.timer = 15;
		this.color = color;

		this.offsetX = 30;

		allPopups.add(this);
	}

	@Override
	public void drawMe(Graphics g) {
		g.setColor(color);

		g.drawString(text, getDrawX(), getDrawY());
	}

	@Override
	public double getDrawHeight() {
		return 0;
	}

	public static void advanceAllTimers() {

		ArrayList<Popup> popupsToRemove = new ArrayList<Popup>();
		for (Popup p : allPopups) {
			if (p.timer < 0) {
				popupsToRemove.add(p);
			}
			p.timer--;

		}
		allPopups.removeAll(popupsToRemove);
	}

	public static void drawAllPopups(Graphics g) {
		for (Popup p : allPopups) {
			p.drawMe(g);
		}
	}

	public int getDrawX() {
		return (int) (SkiPanel.player.getDrawX() + offsetX);
	}

	public int getDrawY() {
		return (int) (SkiPanel.player.getDrawY() - 30 + timer);
	}

	@Override
	public int compareTo(Drawable drawable) {
		// TODO Auto-generated method stub
		return 0;
	}

}
