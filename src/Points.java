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
	static Graphics2D g2d;
	static Graphics2D g2;
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g2d = (Graphics2D) g;
		
		g2d.setColor(Color.red);
		g2d.setStroke(new BasicStroke(2));
		

		if (points.size() > 1) {
			for (int i = 0; i < points.size() - 1; i++) {
				g2d.drawLine((int)points.get(i).getX(), (int)points.get(i).getY(), (int)points.get(i + 1).getX(), (int)points.get(i + 1).getY());
			}
		}
	}

	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		MyPoint p1 = new MyPoint(x, y);
		if (e.getButton() == MouseEvent.BUTTON1) {
			points.add(p1);
		}

		if(points.size()==4) {
			points = castejour(100, points);
		
			System.out.println(points.size()+" Pontos da curva");
			for(int i = 0;i<points.size();i++)System.out.println(points.get(i).getX() +" "+ points.get(i).getY()+ "   PONTO");
			repaint();
		}
		
	}
	
	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	
	public ArrayList<MyPoint> castejour(double t, ArrayList<MyPoint>controlPoints) {
		double aux = 1/t;
		ArrayList<MyPoint> p = new ArrayList<MyPoint>();
		p.add(controlPoints.get(0));
		while(aux<=1) {
			p.add(bezierPointInCurve(aux, controlPoints));
			aux+=t;
		}
		p.add(controlPoints.get(controlPoints.size()-1));
		return p;
	}
	
	public MyPoint bezierPointInCurve (double t, ArrayList<MyPoint> p){
		int n = p.size();
		//Point currentPoint = new Point();
		
		ArrayList<MyPoint> c = new ArrayList<MyPoint>();
		ArrayList<MyPoint> aux = (ArrayList<MyPoint>) p.clone();
		
	
		for(int i = 0;i<n-1;i++) {
			for(int j=0; (j<n-i-1); j++) {
				//Linear interpolation
				MyPoint a = aux.get(j);
				MyPoint b = aux.get(j+1);
				MyPoint p1 = new MyPoint(0,0);
				if(a!=null && b!=null) {
					p1 = sumPoints(multiplyPointByConstant((1-t), a), multiplyPointByConstant(t, b));	
				}
				c.add(p1);
			}
			aux = (ArrayList<MyPoint>) c.clone();
			System.out.println(aux.size());
			if(aux.size()==1)return aux.get(0);
			c.clear();
			n = aux.size();
		}
		
		return null;
	}
	
	public MyPoint sumPoints(MyPoint a, MyPoint b) {
		MyPoint r = new MyPoint(a.x+b.x, a.y+b.y);
		return r;
	}
	
	public MyPoint multiplyPointByConstant(double constant, MyPoint p) {
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

		//frame.setJMenuBar(menuBar);

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
