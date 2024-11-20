package agario;

public class Vector2D {
	private double[] v;
	
	public Vector2D() { //Constructor 1 
		v = new double[2];
		v[0] = 0;
		v[1] = 0;	
	}
	
	public Vector2D (double x, double y) { //Constructor 2
		v = new double[2];
		v[0] = x;
		v[1] = y;
	}
	
	public double length() {
		return Math.sqrt((v[0]*v[0]) + (v[1]*v[1]));
	}
	
	public Vector2D add(Vector2D other) {
		return new Vector2D(v[0] + other.v[0], v[1] + other.v[1]);
	}
	
	public Vector2D sub(Vector2D other) {
		return new Vector2D(v[0] - other.v[0], v[1] - other.v[1]);
	}
	
	public Vector2D mul(double s) {
		return new Vector2D(v[0] * s, v[1] * s);
	}
	
	public double dot(Vector2D other) {
		return (v[0] * other.v[0]) + (v[1] * other.v[1]);
	}
	
	public Vector2D normal() { 
		return mul(1/length());
	}
	
	public double getX() {
		return v[0];
	}
	
	public double getY() {
		return v[1];
	}
	public String toString() { //Overrides normal java toString method
		return "(" + v[0] + ", " + v[1] + ")";
	}

}
