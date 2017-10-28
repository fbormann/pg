import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.JTextComponent;
import Jama.*;

public class Points extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
	public static ArrayList<MyPoint> controlPoints = new ArrayList<MyPoint>();
	public static ArrayList<MyPoint> pointsOfCurve = new ArrayList<MyPoint>();
	static Graphics2D g2d;
	static Graphics2D g2;
	static final double radius = 8;
	static final int CURVE_PRECISION = 1000;

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g2d = (Graphics2D) g;

		g2d.setColor(Color.black);
		// g2d.setStroke(new BasicStroke(1));

		Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 9 }, 0);
		g2d.setStroke(dashed);

		for (MyPoint mp : controlPoints)
			MyPoint.drawPoint(mp);

		//Draw Polygon
		for (int i = 0; i < controlPoints.size() - 1; i++) {
			MyPoint current = controlPoints.get(i);
			MyPoint next = controlPoints.get(i + 1);
			g2d.drawLine((int) current.x, (int) current.y, (int) next.x, (int) next.y);
		}

		//Draw curve
		if (controlPoints.size() > 1) {
			g2d.setColor(Color.RED);
			for (int i = 0; i < pointsOfCurve.size() - 1; i++) {
				MyPoint current = pointsOfCurve.get(i);
				MyPoint next = pointsOfCurve.get(i + 1);
				g2d.drawLine((int) current.x, (int) current.y, (int) next.x, (int) next.y);
			}
		}
		pointsOfCurve.clear();
	}

	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		MyPoint p1 = new MyPoint(x, y);
		// Add a new point
		if (e.getButton() == MouseEvent.BUTTON1) {
			controlPoints.add(p1);
			pointsOfCurve = casteljau(CURVE_PRECISION, controlPoints);
			repaint();
		}
		// remove the nearest point of click
		if (e.getButton() == MouseEvent.BUTTON3) {
			int i = getNearestPointIndex(new MyPoint(e.getX(), e.getY()));
			controlPoints.remove(i);
			pointsOfCurve = casteljau(CURVE_PRECISION, controlPoints);
			repaint();
		}
	}

	public void mouseDragged(MouseEvent e) {
		MyPoint m = new MyPoint(e.getX(), e.getY());
		int i = getNearestPointIndex(m);
		controlPoints.get(i).setLocation(e.getX(), e.getY());
		pointsOfCurve = casteljau(CURVE_PRECISION, controlPoints);
		repaint();
		/*
		 * it takes only the point which the click was in. Moving cursor fast, it breaks
		 * the point. It's caused by thread. double d =
		 * MyPoint.distanceBetwPoints(m.getX(), m.getY(), controlPoints.get(i).getX(),
		 * controlPoints.get(i).getY()); if(d<=MyPoint.radius) {
		 * controlPoints.get(i).setLocation(e.getX(), e.getY()); pointsOfCurve =
		 * castejour(1000, controlPoints); repaint(); }
		 */
	}

	public int getNearestPointIndex(MyPoint point) {
		MyPoint mp = new MyPoint(0, 0);
		int indice = 0;
		for (int i = 0; i < controlPoints.size(); i++) {
			MyPoint p = controlPoints.get(i);
			double d1 = MyPoint.distanceBetwPoints(mp.getX(), mp.getY(), point.getX(), point.getY());
			double d2 = MyPoint.distanceBetwPoints(p.getX(), p.getY(), point.getX(), point.getY());
			if (d1 > d2) {
				mp = p;
				indice = i;
			}
		}

		return indice;
	}

	// Returns t controlPoints that form curve
	public ArrayList<MyPoint> casteljau(double numberOfPoints, ArrayList<MyPoint> controlPoints) {
		double t = (double) 1 / numberOfPoints;
		double counter = t;
		ArrayList<MyPoint> returnSetOfPoints = new ArrayList<MyPoint>();

		// add first point from original control controlPoints
		returnSetOfPoints.add(controlPoints.get(0));

		while (counter <= 1) {
			returnSetOfPoints.add(bezierPointInCurve(counter, controlPoints));
			counter += t;

			// add the last control point
			if ((1 - counter) < t)
				returnSetOfPoints.add(controlPoints.get(controlPoints.size() - 1));
		}

		return returnSetOfPoints;
	}

	// Returns a point in curve
	public MyPoint bezierPointInCurve(double t, ArrayList<MyPoint> cPoints) {
		int n = cPoints.size();
		// Point currentPoint = new Point();

		ArrayList<MyPoint> temp = new ArrayList<MyPoint>();
		ArrayList<MyPoint> controlPointsTemp = (ArrayList<MyPoint>) cPoints.clone();
		int k = n;
		for (int i = 0; i < n - 1; i++) {
			for (int j = 0; (j < k - 1); j++) {
				// Linear interpolation
				MyPoint a = controlPointsTemp.get(j);
				MyPoint b = controlPointsTemp.get(j + 1);
				MyPoint auxPoint = new MyPoint(0, 0);
				if (a != null && b != null) {
					auxPoint = MyPoint.sumPoints(MyPoint.multiplyPointByConstant((1 - t), a),
							MyPoint.multiplyPointByConstant(t, b));
					temp.add(auxPoint);
				}
			}
			controlPointsTemp = (ArrayList<MyPoint>) temp.clone();
			temp.clear();
			k = controlPointsTemp.size();
		}
		// System.out.println("TAMANHO "+controlPointsTemp.size());
		// it has to return just one point
		return controlPointsTemp.get(0);
	}


	public void mouseMoved(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void keyPressed(KeyEvent arg0) {
	}

	public void keyTyped(KeyEvent arg0) {
	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if(controlPoints.size()==1)controlPoints.clear(); repaint();
		if (e.getKeyChar() == 'r' && controlPoints.size()>0) {
			int currentDegree = controlPoints.size();
			int targetDegree = currentDegree - 1;

			Matrix baseMatrix = new Matrix(currentDegree, targetDegree);
			baseMatrix.set(0, 0, 1.0);
			baseMatrix.set(currentDegree - 1, targetDegree - 1, 1.0);

			for (int i = 1; i < targetDegree; i++) {
				baseMatrix.set(i, i, 1 - (i / (targetDegree + 1)));
				baseMatrix.set(i, i - 1, i / (targetDegree + 1));
			}

			Matrix transposeMatrix = baseMatrix.transpose();
			Matrix inverseMatrix = transposeMatrix.times(baseMatrix).inverse();
			Matrix reductionMatrix = inverseMatrix.times(transposeMatrix);

			Matrix xCoordinate = new Matrix(currentDegree, 1);
			Matrix yCoordinate = new Matrix(currentDegree, 1);
			
			for (int i = 0; i < currentDegree; i++) {
				xCoordinate.set(i, 0, controlPoints.get(i).x);
				yCoordinate.set(i, 0, controlPoints.get(i).y);
			}
			
			Matrix xCoordinateReduced = reductionMatrix.times(xCoordinate);
			Matrix yCoordinateReduced = reductionMatrix.times(yCoordinate);
			
			controlPoints.clear();
			
			for(int i = 0;i<targetDegree;i++) {
				controlPoints.add(new MyPoint(xCoordinateReduced.get(i, 0),yCoordinateReduced.get(i, 0)));
			}

			pointsOfCurve = casteljau(CURVE_PRECISION, controlPoints);
			repaint();
		}
	}
	public static void main(String[] args) {
		Points controlPoints = new Points();
		JFrame frame = new JFrame("Degree Reduction of Bézier Curve - Press 'r' to reduce degree");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(controlPoints);
		frame.setSize(1000, 700);
		frame.setLocationRelativeTo(controlPoints);

		frame.setVisible(true);
		frame.addMouseListener(controlPoints);
		frame.addMouseMotionListener(controlPoints);
		frame.addKeyListener(controlPoints);

	}


}
