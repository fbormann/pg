
public class MyPoint {
	double x, y;
	
	public MyPoint(double x, double y) {
		this.x = x;
		this.y = y;
		
	}
	
	public void setLocation(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return this.x;
	}
	public double getY() {
		return this.y;
	}
	public void setX(double x) {
		this.x = x;
	}public void setY(double y) {
		this.y = y;
	}
	
	public static MyPoint sumPoints(MyPoint a, MyPoint b) {
		MyPoint r = new MyPoint(a.x + b.x, a.y + b.y);
		return r;
	}

	public static MyPoint multiplyPointByConstant(double constant, MyPoint p) {
		double a = p.getX() * constant;
		double b = p.getY() * constant;
		MyPoint r = new MyPoint(a, b);
		return r;
	}
	
	public String toString() {
		return ("X: "+(int)this.x+"Y: "+(int)this.y);
	}
	
	public static void drawPoint(MyPoint point) {
		Points.g2d.drawOval((int)point.x, (int)point.y, 10000, 10000);
	}
}
