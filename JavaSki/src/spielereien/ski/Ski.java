package spielereien.ski;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;

class Ski {
	public static void main(String[] args) {
		System.out.println("Created GUI on EDT? " + SwingUtilities.isEventDispatchThread());
		JFrame f = new JFrame("Ski");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		SkiPanel panel = new SkiPanel();

		f.add(panel);
		f.setResizable(true);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setExtendedState(f.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		f.setVisible(true);
	}
}
