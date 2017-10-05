import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JPanel{
	static Graphics2D g2d;
	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		repaint();
	}
	
	public static void main(String args[]) {
		Main main = new Main();
		JFrame frame = new JFrame("Main");
		frame.setSize(800, 600);
		frame.setVisible(true);
		
	}
}
