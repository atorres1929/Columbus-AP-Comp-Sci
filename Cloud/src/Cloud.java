import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class Cloud {
	
	ArrayList<Point2D.Double> clouds;
	private static int radius;		//determines the radius of the circles
	private static int numClouds;	//determines the number of clouds
	private static int width = 1920;//determines the width of the window
	private static int height = 1080;//determines the height of the window
	private static String clear= "1";//default option: Don't clear the circles after done drawing
	private static String delay= "1";//default option: Don't add a delay after each circle is drawn
	
	public static void main(String[] args) {
		String userInput;
		userInput = JOptionPane.showInputDialog("Enter the radius of the circles: (Recommend first time 100):"); //All of the userInput stuff is just getting values from the input, converting it to an integer
		radius = Integer.parseInt(userInput);
		userInput = JOptionPane.showInputDialog("Enter the number of circles to be drawn: (Recommend first time 1000):");
		numClouds = Integer.parseInt(userInput);
		userInput = JOptionPane.showInputDialog("Show advanced settings? (true = 1):");
			
		if (userInput.equalsIgnoreCase("1")){
			userInput = JOptionPane.showInputDialog("Enter the width of the window:");
			width = Integer.parseInt(userInput);
			userInput = JOptionPane.showInputDialog("Enter the height of the window:");
			height = Integer.parseInt(userInput);
			userInput = JOptionPane.showInputDialog("Clear the circles after done drawing? (true = 1, default = true):");
			clear = userInput;
			userInput = JOptionPane.showInputDialog("Add a delay before each circle is drawn? (true = 1, default = true)");
			delay = userInput;
		}
		
		JFrame frame = new JFrame("Circle Drawer");			//creates the window
		Cloud comp = new Cloud();							//creates a new cloud object (an array of circles)
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //sets the default close
		frame.setSize(width, height);						//sets the size of the window
		frame.setVisible(true);								//makes the window visible, I don't know why Java ASSUMES you want it invisible
		Graphics2D gfxx = (Graphics2D) frame.getGraphics();//Creates a graphics component, then converts it to 2D
		comp.draw(gfxx);									//Draws the 2D component (see the draw method)
		
		
		while(true){
			long t0 = System.currentTimeMillis(); //Grabs the current time and sets it to a variable
			comp.reshuffle();						//See reshuffle method
			while(System.currentTimeMillis() - t0 < 5){}	//Adds a 5 millisecond delay (this can be turned on and off in settings)
			
			if(clear.equalsIgnoreCase("1"))
				gfxx.clearRect(0, 0, Cloud.width, Cloud.height); //This will clear the rectangle (this can be turned on and off in settings)
			
			comp.draw(gfxx);
		}
	}
	
	public Cloud(){  //Creates a Cloud object
		clouds = new ArrayList<Point2D.Double>(numClouds); //Creates a new array of type: Point2D.Double (Points with percision the percision of double(can have decimals))
		Random gen = new Random();							//Creates a new Random object (lets you make "random" numbers)
		for(int i = 0; i < numClouds; i++){					//This loop adds a random point to the array, the number of clouds depends how many times it goes through the operation
			Point2D.Double pt = new Point2D.Double(gen.nextInt(width), //creates the point, and can be anywhere within width or height (the size of the window)
					gen.nextInt(height));
			this.add(pt); //Adds the point to the Cloud object
		}
	}

	public void draw(Graphics2D gfx){
		Random gen = new Random(); //random generator
		
		for(int i = 0; i<clouds.size(); i++){
			Ellipse2D.Double gdsf = new Ellipse2D.Double(clouds.get(i).x, //Creates a circle based around a point's X & Y that was randomly created
					clouds.get(i).y,radius, radius);
			gfx.setColor(new Color(gen.nextInt())); //Sets the color of the circle
			gfx.fill(gdsf);							//Draws the Circle, then colors it in (Change this to .draw(gdsf) to just draw the outline
			if (delay.equalsIgnoreCase("1")){		//Sets a delay if wanted
				long t0 = System.currentTimeMillis();
				while(System.currentTimeMillis() - t0 < 10){} // This will set a delay for each circle drawn
			}
		}
	}
	
	public void reshuffle(){
		clouds.clear(); //This will clear the cloud array after drawing to ensure randomness
		Random gen = new Random(); //random generator
		for(int i = 0; i<numClouds; i++){ //Now that the old array was cleared, we can start adding new points
			Point2D.Double pt = new Point2D.Double(gen.nextInt(width), 
					gen.nextInt(height));
			this.add(pt);
		}
	}
	
	public void add(Point2D.Double args){
		clouds.add(args); //Makes it so that .add() in the [public Cloud()] constructor knows what to add (pt) to
	}
}