package spielereien.ski;

import java.awt.Graphics;

public interface Drawable {

	public void drawMe(Graphics g);

	public double getDrawHeight();

	public int getDrawX();

	public int getDrawY();

}