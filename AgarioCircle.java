package agario;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class AgarioCircle {
	private double radius;
	private Color circleColor;
	private Point2D position;
	private Vector2D velocity; 
	private int maxSpeed = 100;
	
	public static int INIT_RADIUS_SMALL = 10;
	public static int INIT_RADIUS_LARGE = 40;
	private Color[] colorsList = {Color.GREEN, Color.YELLOW, Color.ORANGE, Color.VIOLET, Color.BLUE, Color.DARKBLUE, 
			Color.DARKGREEN, Color.DARKORANGE, Color.DARKCYAN, Color.DARKVIOLET, Color.LIGHTBLUE, Color.LIGHTGREEN,
			Color.BROWN, Color.CYAN, Color.GRAY};
	
	
	public AgarioCircle() { //NPC Constructor
		createSize();          //Calls the createSize() method that gives the circle a random size
		createColor();         //Calls the createColor() method that gives the circle a random color
		createSpawnPosition(); //Calls the createSpawnPosition() method that gives the circle a random spawn position
		
		Point2D spawn = new Point2D(getX(), getY()); //Makes a Point2D with the spawn position
		Point2D randomLoc = new Point2D((Math.random()*(ABDU_MOHAMED_CULMINATING.WIDTH-0+1) + 0), (Math.random()*(ABDU_MOHAMED_CULMINATING.HEIGHT-0+1) + 0)); 
		//Makes a Point2D with a random position on the screen
		
		velocity = new Vector2D(spawn.getX() - randomLoc.getX(), spawn.getY() - randomLoc.getY()); //Creates a random direction
		velocity = velocity.normal(); //Normalizes the direction
		double speed = Math.random()*41+1; //Sets the speed of the circle to a random speed between 1pixels/sec amd 30pixels/sec
		velocity = velocity.mul(speed); //Gives the circle the speed
	}
	
	public AgarioCircle (double spawnX, double spawnY, int rad, Color color) { //Player Constructor
		position = new Point2D(spawnX, spawnY); //Creates the position of the player circle
		radius = rad; 							//Creates the radius of the player circle
		circleColor = color; 					//Creates the color of the player circle
	}
	
	public void createSpawnPosition() { 
		position = new Point2D((Math.random()*(ABDU_MOHAMED_CULMINATING.WIDTH-radius) + radius), 
							   (Math.random()*(ABDU_MOHAMED_CULMINATING.HEIGHT-radius) + radius)); 
		//Creates a random spawn position between inside the screen with a buffer zone
	}
	
	public void createColor() { 
		circleColor = colorsList[(int)(Math.random()*(15-0) + 0)]; //Gives the circle a random color from the color list
	}
	
	public void createSize() {
		radius = (int)(Math.random()*(INIT_RADIUS_LARGE-INIT_RADIUS_SMALL+1) + INIT_RADIUS_SMALL); 
		//Gives the circle a random size between 10pixels and 40pixels
	}
	
	public void createDirection(double mouseX, double mouseY) {
		velocity = new Vector2D(mouseX - position.getX(),mouseY - position.getY()); 
		//Creates a velocity between the current position of the cirlce and the position of mouse
		velocity = velocity.normal(); //Normalizes the velocity
		velocity = velocity.mul(maxSpeed); //Sets the speed of the circle to the max speed
	}
	
	public void draw (GraphicsContext gc) //Draws the circle to the screen
	{
		gc.setFill(circleColor); 																//Draws using the color of circle
		gc.fillOval(position.getX() - radius, position.getY() - radius, radius*2, radius*2); 	//Draws the circle at the current position
	}
	
	public void upDate (double deltaT) { //Updates the position of the circle
		position = position.add(velocity.mul(deltaT));
	}
	
	public void detectWallCollision() {
		if (position.getX() >= ABDU_MOHAMED_CULMINATING.WIDTH || position.getX()-radius <= 0) //Checks if the X position of the circle is out of bounds
		{
			Vector2D newX = new Vector2D(velocity.getX() * -1, velocity.getY()); //Creates a new velocity for the circle with a -X value
			velocity = newX; //Sets the velocity of the circle to the new velocity
		}
		if (position.getY() >= ABDU_MOHAMED_CULMINATING.HEIGHT || position.getY()-radius <= 0) //Checks if the Y position of the circle is out of bounds
		{
			Vector2D newY = new Vector2D(velocity.getX(), velocity.getY() * -1); //Creates a new velocity for the circle with a -Y value
			velocity = newY; //Sets the velocity of the circle to the new velocity 
		}
	}
	
	public boolean detectCircleCollision(AgarioCircle other) { 
		//Checks to see if the distance between two circles is less than or equal to zero, returns true if it is below 0
		double distance = Math.sqrt(((position.getX() - other.getX())*(position.getX() - other.getX())) + 
									((position.getY() - other.getY())*(position.getY() - other.getY())));
		//Gets the distance between the two points
		double totalRadius = getRadius() + other.getRadius();  //Gets the total radius
		if(distance - totalRadius <= 0) //Checks if the distance between 2 circles is less than or equal to 0
		{
			return true; //Returns true if it is
		}
		else
			return false; //Returns false if it isn't
	}
	
	
	public void updatePosition(Point2D newPosition) {
		position = newPosition;
	}
	
	public double getX() {
		return position.getX();
	}
	
	public double getY() {
		return position.getY();
	}
	
	public Point2D getPosition() {
		return position;
	}
	
	public void setPosition(double x, double y) {
		Point2D newPos = new Point2D(x, y);
		position = newPos;	
	}
	
	public Vector2D getVelocity() {
		return velocity;
	}
	
	public void setVelocity(Vector2D newVelocity) {
		velocity = newVelocity;
	}
	
	public double getRadius() {
		return radius;
	}
	
	public void setRadius(double newRadius) {
		radius = newRadius;
	}
	
	public Color getColor() {
		return circleColor;
	}

}
