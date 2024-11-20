package agario;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Agario extends Application {
	public final static int WIDTH = 1280;
	public final static int HEIGHT = 720;
	
	private int amountOfCircles = 20;
	private double time = 0;
	
	private ArrayList<AgarioCircle> circles;
	private int playerRadius = 30;
	
	private Color backgroundColor = Color.WHITE;
	private AgarioCircle player = new AgarioCircle(WIDTH/2, HEIGHT/2, playerRadius, Color.RED);
	
	private boolean gameLost = false;
	private boolean gameWon = false;
	private static boolean paused = false;
	private boolean start = false;
	private int points = 0;
	
	private GraphicsContext gc;

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		// Create the Canvas
		Canvas canvas = new Canvas(WIDTH, HEIGHT);
		// Set the width of the Canvas
		canvas.setWidth(WIDTH);
		// Set the height of the Canvas
		canvas.setHeight(HEIGHT);

		// Get the graphics context of the canvas
		gc = canvas.getGraphicsContext2D();

		circles = new ArrayList<AgarioCircle>();
		for (int i = 0; i < amountOfCircles; i++)
		{
			circles.add(new AgarioCircle());
		}

		// Create the Pane
		Pane root = new Pane();
		// Set the Style-properties of the Pane
//		root.setStyle("-fx-padding: 10;" + 
//				"-fx-border-style: solid inside;" + 
//				"-fx-border-width: 2;" + 
//				"-fx-border-insets: 5;" + 
//				"-fx-border-radius: 5;" + 
//				"-fx-border-color: blue;");

		AnimationTimer timer = new AnimationTimer() {
			long lastTime = System.nanoTime();
			
		@Override
		public void handle(long currentTime) {
				double delta_Time = (currentTime - lastTime)/1000000000.0;
				lastTime = currentTime;
				upDate(delta_Time);
				// draw the scene using the GraphicsContext gc
				render(gc);
			}
		};
		
		
		timer.start();	// starts the loop

		// Add the Canvas to the Pane
		root.getChildren().add(canvas);
		// Create the Scene
		Scene scene = new Scene(root);
		
		// define keyboard event handlers
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
		
		@Override
		public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.S)
				{
					start = true;
					if(gameWon || gameLost)
					{
						startNewGame();
						gameWon = false;
						gameLost = false;
					}
				}
				else if(event.getCode() == KeyCode.SPACE)
				{
					paused = !paused;
				}
			}
		});
		
		// define mouse event handlers
		scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				System.out.println("Mouse button clicked.");
			}
		});

		scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				player.createDirection(event.getX(), event.getY());
			}
		});

		// Add the Scene to the Stage
		stage.setScene(scene);
		// Set the Title of the Stage
		stage.setTitle("Mohamed Abdu - Culminating");
		// Display the Stage
		stage.show();		
	}
	
	public void startNewGame() {
		circles.clear();
		player.setRadius(playerRadius);
		player.setPosition(WIDTH/2+player.getRadius(), HEIGHT/2+player.getRadius());
		for (int i = 0; i < amountOfCircles; i++)
		{
			circles.add(new AgarioCircle());
		}
		start = true;
		points = 0;
	}

	public void upDate(double deltaT)
	{
		// write code that updates the positions of all your objects here
		if(start && !paused && !gameWon && !gameLost) //If the game has started, and it is not paused, and the game hasn't been won or lost
		{
			if (!gameLost && !gameWon) //If The game hasn't ended
			{
				time += deltaT; //Adds deltaT to time
				if(time > 5) //If time is more than 5 seconds
				{
					circles.add(new AgarioCircle()); //Creates a new circle
					time = 0; //Resets time to 0
				}
			}
			
			player.upDate(deltaT); //Updates the player's position
			player.detectWallCollision(); //Checks if the player has hit a wall to keep them from going out of bounds
			for (int i = 0; i < circles.size(); i++) //Checks if circle i is hitting circle x
			{
				circles.get(i).upDate(deltaT); //Updates all of the circles' positions
				circles.get(i).detectWallCollision(); //Detects if the current circle is hitting any walls and bounces them off the wall
				for (int x = 0; x < circles.size(); x++)
				{
					if(circles.get(i).detectCircleCollision(circles.get(x))) //Checks if there is a collision between circle i and x
					{
						if(circles.get(i).getRadius() > circles.get(x).getRadius()) //If circle i's radius is larger than circle x's radius
						{
							double newRadius = circles.get(x).getRadius() * 0.25 + circles.get(i).getRadius(); //Creates a new radius that is the size of the bigger circle's radius plus 1/4 of the smaller circle's
							circles.get(i).setRadius(newRadius); //Sets the radius of the larger circle to the new radius we created
							circles.get(i).setVelocity(circles.get(i).getVelocity().add(circles.get(x).getVelocity())); //Sets the velocity of larger circle to the combined velocity of both of the circles
							circles.remove(x); //Removes the smaller circle
						}
						if(circles.get(x).getRadius() > circles.get(i).getRadius()) //If circle x's radius is larger than circle i's radius
						{
							double newRadius = circles.get(i).getRadius() * 0.25 + circles.get(x).getRadius(); //Creates a new radius that is the size of the bigger circle's radius plus 1/4 of the smaller circle's
							circles.get(x).setRadius(newRadius); //Sets the radius of the larger circle to the new radius we created
							circles.get(x).setVelocity(circles.get(x).getVelocity().add(circles.get(i).getVelocity())); //Sets the velocity of larger circle to the combined velocity of both of the circles
							circles.remove(i); //Removes the smaller circle
						}
					}
				}
					if(circles.get(i).detectCircleCollision(player)) //Checks if the player circle is colliding with any other circles
					{
						if(circles.get(i).getRadius() > player.getRadius()) //Checks if the other circle is larger than the player circle
						{
							double newRadius = player.getRadius() * 0.25 + circles.get(i).getRadius(); //Creates a new radius that is the size of the other circle's radius plus 1/4 of the player circle's
							circles.get(i).setRadius(newRadius);  //Sets the radius of the other circle to the new radius we created
							player.setRadius(0); //Sets the radius of the player circle to 0
							gameLost = true; //Sets the game state to lost
						}
						if(player.getRadius() > circles.get(i).getRadius())
						{
							double newRadius = circles.get(i).getRadius() * 0.25 + player.getRadius(); //Creates a new radius that is the size of the player circle's radius plus 1/4 of the other circle's
							player.setRadius(newRadius); //Sets the radius of the player circle to the new radius we created
							points += circles.get(i).getRadius(); //Increases the score of the player
							circles.remove(i); //Removes the other circle
							
						}
				}
					
					if(circles.size() == 0) //Checks if there are no circles left
					{
						gameWon = true; //Sets the game state to won
					}
			}
		}
		
		if(!start) //Checks if the game hasn't started
		{
			if (!gameLost && !gameWon) //Checks if the game hasn't been won or lost
			{
				time += deltaT; //Adds deltaT to time
				if(time > 5) //Checks if time is more than 5 seconds
				{
					circles.add(new AgarioCircle()); //Adds a new circle 
					time = 0; //Resets time to 0
				}
			}
			for (int i = 0; i < circles.size(); i++) //Checks if circle i is hitting circle x
			{
				circles.get(i).upDate(deltaT); //Updates all of the circles' positions
				circles.get(i).detectWallCollision(); //Detects if the current circle is hitting any walls and bounces them off the wall
				for (int x = 0; x < circles.size(); x++)
				{
					if(circles.get(i).detectCircleCollision(circles.get(x))) //Checks if there is a collision between circle i and x
					{
						if (i == x)
							break;
						if(circles.get(i).getRadius() > circles.get(x).getRadius()) //If circle i's radius is larger than circle x's radius
						{
							double newRadius = circles.get(x).getRadius() * 0.25 + circles.get(i).getRadius(); //Creates a new radius that is the size of the bigger circle's radius plus 1/4 of the smaller circle's
							circles.get(i).setRadius(newRadius); //Sets the radius of the larger circle to the new radius we created
							circles.get(i).setVelocity(circles.get(i).getVelocity().add(circles.get(x).getVelocity())); //Sets the velocity of larger circle to the combined velocity of both of the circles
							circles.remove(x); //Removes the smaller circle
						}
						if(circles.get(x).getRadius() > circles.get(i).getRadius()) //If circle x's radius is larger than circle i's radius
						{
							double newRadius = circles.get(i).getRadius() * 0.25 + circles.get(x).getRadius(); //Creates a new radius that is the size of the bigger circle's radius plus 1/4 of the smaller circle's
							circles.get(x).setRadius(newRadius); //Sets the radius of the larger circle to the new radius we created
							circles.get(x).setVelocity(circles.get(x).getVelocity().add(circles.get(i).getVelocity())); //Sets the velocity of larger circle to the combined velocity of both of the circles
							circles.remove(i); //Removes the smaller circle
						}
					}
				}
			}
			if (circles.size() == 0)
			{
				startNewGame();
			}
		}
	}

	public void render(GraphicsContext gc)
	{
		// clear the screen
		gc.setFill(backgroundColor); //Sets the fill color to the background color which is decided above
		gc.fillRect(0, 0, WIDTH, HEIGHT); //Fills a rectangle the size of the screen

		if (!paused) //If the game is not currently in pause mode, do the following
		{
			if (start)
				player.draw(gc);
			for (int i = 0; i < circles.size(); i++) //For loop that goes through every circle in the arraylist
			{
				circles.get(i).draw(gc); //Draws the current circle to the screen
			}
			
			String pointsToString = "Score: " + points; //Turns the points into a string
			gc.strokeText(pointsToString, WIDTH-60, HEIGHT-25); //Draws the current score to the screen
			
			if(gameLost) //Checks if the game has been lost
			{
				gc.strokeText("You lost! GAME OVER", WIDTH/2-45, HEIGHT/2-15); //Tells the user that they lost the game
				gc.strokeText("Press S to Start a New Game", WIDTH/2-65 , HEIGHT/2); //Draws text to the screen telling the user to press S to start the game	
				start = false;
			}
			
			if(gameWon) //Checks if the game has been won
			{
				gc.strokeText("You win! GAME OVER", WIDTH/2-45, HEIGHT/2-15); //Tells the user that they won the game
				gc.strokeText("Press S to Start a New Game", WIDTH/2-65 , HEIGHT/2); //Draws text to the screen telling the user to press S to start the game
				start = false;
			}
		}
		
		if (paused && !gameWon && !gameLost) //If the game is currently in pause mode, do the following
		{	
			if (start)
				player.draw(gc);
			for (int i = 0; i < circles.size(); i++) //For loop that goes through every circle in the arraylist
			{
				circles.get(i).draw(gc); //Draws the current circle to the screen
			}
			if (paused && start)
				{
					gc.strokeText("GAME PAUSED", WIDTH/2-65 , HEIGHT/2); //Tells the user that the game is paused
				}
		}
		if(!start) //Checks if the game hasn't started
			gc.strokeText("Press S to Start a New Game", WIDTH/2-65 , HEIGHT/2); //Draws text to the screen telling the user to press S to start the game
		}
		
	}
