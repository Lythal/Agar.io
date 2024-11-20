package agario;

public class Point2D {
	private double[] p;
		
	public Point2D() {
		p = new double[2];
		p[0] = 0;
		p[1] = 0;
	}
	
	public Point2D(double x, double y) {
		p = new double[2];
		p[0] = x;
		p[1] = y;
	}
	
	public Point2D add(Vector2D v) {
		return new Point2D(p[0] + v.getX(), p[1] + v.getY());
	}
	
	public Vector2D sub(Point2D other) {
		return new Vector2D(p[0] - other.getX(), p[1] - other.getY());
	}
	
	public double getX() {
		return p[0];
	}
	
	public double getY() {
		return p[1];
	}
	
	public String toString() { //Overrides normal java toString method
		return "(" + p[0] + ", " + p[1] + ")";
	}
}
