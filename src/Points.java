import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class Points extends JPanel implements MouseListener {
	public static ArrayList<MyPoint> points = new ArrayList<MyPoint>();
	public static ArrayList<MyPoint> aux = new ArrayList<MyPoint>();
	static Graphics2D g2d;
	static Graphics2D g2;

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g2d = (Graphics2D) g;

		g2d.setColor(Color.red);
		g2d.setStroke(new BasicStroke(1));


		if (points.size() > 1) {
			for (int i = 0; i < points.size() - 1; i++) {
				g2d.drawLine((int) points.get(i).getX(), (int) points.get(i).getY(), (int) points.get(i + 1).getX(),
						(int) points.get(i + 1).getY());
				 //System.out.println((int)points.get(i).getX()+" "+ (int)points.get(i).getY()+" "+ (int)points.get(i + 1).getX()+" "+ (int)points.get(i + 1).getY());
			}
		}
		points.clear();
	}

	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		MyPoint p1 = new MyPoint(x, y);
		if (e.getButton() == MouseEvent.BUTTON1) {
			points.add(p1);
			
		}

		//Setting to 4 to print curve (not good at all)
		if (points.size() == 4) {
			//EVERYTHING HAPPENS HERE:
			points = castejour(1000, points);
			repaint();
		}

	}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	
	//Returns t points that from curve
	public ArrayList<MyPoint> castejour(double t, ArrayList<MyPoint> controlPoints) {
		double aux = (double) 1 / t;
		double cont = aux;
		ArrayList<MyPoint> p = new ArrayList<MyPoint>();
		//add first point from original control points
		p.add(controlPoints.get(0));
		int k = 0;
		while (cont <=1) {
			p.add(bezierPointInCurve(cont, controlPoints));
			cont += aux;
			k++;
			// System.out.println(k+"-ésima iteracao");
		}
		//add last point from original control points
		p.add(controlPoints.get(controlPoints.size() - 1));
		
		for(int i = 0;i<controlPoints.size();i++) {
			System.out.println(controlPoints.get(i).getX()+" "+controlPoints.get(i).getY());
		}
		//System.out.println(controlPoints.get(controlPoints.size()-1).getX()+" "+controlPoints.get(controlPoints.size()-1).getY()+" ULTIMO PONTO");
		
		return p;
	}

	//Returns a point in curve
	public MyPoint bezierPointInCurve(double t, ArrayList<MyPoint> p) {
		int n = p.size();
		// Point currentPoint = new Point();

		ArrayList<MyPoint> c = new ArrayList<MyPoint>();
		ArrayList<MyPoint> aux = (ArrayList<MyPoint>) p.clone();

		for (int i = 0; i < n - 1; i++) {
			for (int j = 0; (j < n - i - 1); j++) {
				// Linear interpolation
				MyPoint a = aux.get(j);
				MyPoint b = aux.get(j + 1);
				MyPoint p1 = new MyPoint(0, 0);
				if (a != null && b != null) {
					p1 = sumPoints(multiplyPointByConstant((1 - t), a), multiplyPointByConstant(t, b));
					c.add(p1);
				}
			}
			aux = (ArrayList<MyPoint>) c.clone();
			if (aux.size() == 1)
				return aux.get(0);
			c.clear();
			n = aux.size();
		}

		return null;
	}
	
	public MyPoint sumPoints(MyPoint a, MyPoint b) {
		MyPoint r = new MyPoint(a.x + b.x, a.y + b.y);
//		System.out.println("PONTO A: " + a.getX() + " " + a.getY());
//		System.out.println("PONTO B: " + b.getX() + " " + b.getY());
//		System.out.println("PONTO RESULTANTE: " + r.getX() + " " + r.getY());
		return r;
	}

	public MyPoint multiplyPointByConstant(double constant, MyPoint p) {
		double a = p.getX() * constant;
		double b = p.getY() * constant;
		MyPoint r = new MyPoint(a, b);
		return r;
	}

	public static void main(String[] args) {
		Points points = new Points();
		JFrame frame = new JFrame("Points");
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Configurar");
		JMenuItem menuItem = new JMenuItem("Adicionar Pontos");

		menu.add(menuItem);

		menuBar.add(menu);
		menuBar.setBackground(Color.GRAY);
		menuBar.setPreferredSize(new Dimension(200, 30));

		// frame.setJMenuBar(menuBar);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(points);
		frame.setSize(1000, 700);
		frame.setLocationRelativeTo(points);

		frame.setVisible(true);
		frame.addMouseListener(points);

		// frame.addMouseListener();
		// listener(frame);
	}
}
