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
	public static ArrayList<Point> points = new ArrayList();
	public static ArrayList<Point> dots = new ArrayList();
	static Graphics2D g2d;
	static Graphics2D g2;
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g2d = (Graphics2D) g;
		//g2 = (Graphics2D) g2d;
		
		g2d.setColor(Color.red);
		g2d.setStroke(new BasicStroke(2));
		
		//g2.setStroke(new BasicStroke(10));
		//g2.setColor(Color.BLACK);
		
//		Ellipse2D e;
		
		if (points.size() > 1) {
			for (int i = 0; i < points.size() - 1; i++) {
				//System.out.println(points.get(i).x+" "+ points.get(i).y+" "+ points.get(i + 1).x +" "+ points.get(i + 1).y);
				g2d.drawLine(points.get(i).x, points.get(i).y, points.get(i + 1).x, points.get(i + 1).y);
				//g2d.fill(new Ellipse2D.Double(points.get(i).x, points.get(i).y, 10, 10));
				
				//g2.drawLine(points.get(i).x, points.get(i).y,points.get(i).x, points.get(i).y);
			}
		} else if (!points.isEmpty()) {
			g2d.drawLine(points.get(0).x, points.get(0).y, points.get(0).x, points.get(0).y);
		}
		/*
		 * for (int i = 0; i <= 100000; i++) { Dimension size = getSize(); int w =
		 * size.width ; int h = size.height;
		 * 
		 * Random r = new Random(); int x = Math.abs(r.nextInt()) % w; int y =
		 * Math.abs(r.nextInt()) % h; g2d.drawLine(x, y, x, y); }
		 */
	}

	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		System.out.println(x+" "+y);
		Point p1 = new Point();
		p1.setLocation(x, y);
		//SwingUtilities.convertPointFromScreen(p1, this);
		
		if (e.getButton() == MouseEvent.BUTTON1) {
			points.add(p1);	
			dots.add(p1);
		}
		

		//System.out.println(e.getButton()+" "+points.size()+" points");
		if (e.getButton() == MouseEvent.BUTTON3) {
			int aux = points.size();
			/*if(aux>=0){
				//points.removeAll(points);
				points.remove(0);
			}*/
		}
		/**
		points.clear();
		points.add(new Point(0, 0));
		points.add(new Point(100, 100));
		points.add(new Point(150, 50));
		bezierPointInCurve(0.5, points);
		*/
		if(points.size()==7) {
			points = castejour(0.1, points);	
			repaint();
		}
		
		//System.out.println(points.size());
	}
	
	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	
	public ArrayList<Point> castejour(double t, ArrayList<Point>controlPoints) {
		double aux = t;
		ArrayList<Point> p = new ArrayList<Point>();
		p.add(controlPoints.get(0));
		while(aux<=1) {
			p.add(bezierPointInCurve(aux, controlPoints));
			aux+=t;
		}
		p.add(controlPoints.get(controlPoints.size()-1));
		return p;
	}
	
	public Point bezierPointInCurve (double t, ArrayList<Point> p){
		int n = p.size();
		//Point currentPoint = new Point();
		
		ArrayList<Point> c = new ArrayList<Point>();
		ArrayList<Point> aux = (ArrayList<Point>) p.clone();
		
	
		for(int i = 0;i<n-1;i++) {
			for(int j=0; (j<i+1) && (i+1<=aux.size()); j++) {
				//Linear interpolation
				Point a = aux.get(j);
				Point b = aux.get(j+1);
				Point p1 = new Point(0,0);
				if(a!=null && b!=null) {
					p1 = sumPoints(multiplyPointByConstant((1-t), a), multiplyPointByConstant(t, b));	
				}
				c.add(p1);
			}
			aux = (ArrayList<Point>) c.clone();
			System.out.println(aux.size());
			if(aux.size()==1)return aux.get(0);
			c.clear();
			n = aux.size();
		}
		
		return null;
	}
	
	public Point sumPoints(Point a, Point b) {
		Point r = new Point(a.x+b.x, a.y+b.y);
		return r;
	}
	
	public Point multiplyPointByConstant(double constant, Point p) {
		double a = p.getX()*constant;
		double b =p.getY()*constant;
		p.setLocation(a, b);
		return p;
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

		frame.setJMenuBar(menuBar);

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
