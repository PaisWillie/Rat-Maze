
/* =============================================================================================================================================================================
 * Maze Runner
 * Willie Pai
 * November 4th 2019
 * Java, v1.8.5
 * =============================================================================================================================================================================
 * Problem Definition - This program determines the shortest path in a given maze from the rat/mouse to the cheese, and then the exit
 * Input - A maze file that contains the layout of the maze and the location of the rat, cheese and exit
 * Output - Original layout of the maze, shortest path from the rat/mouse to the cheese, and from the cheese to the exit
 * Processing - Calculates the shortest path through the maze from point A to point B 
 * =============================================================================================================================================================================
 */

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * The_Maze class
 * 
 * This class handles the events of each process in a specified order
 * 
 * @author Willie Pai
 */
public class The_Maze {

	/**
	 * Default constructor
	 * 
	 */
	public The_Maze() {

	}

	/**
	 * main method
	 * 
	 * This procedural method is automatically called to trigger each process/event
	 * in order, from reading the file, initializing the step count array, printing
	 * the original maze, displaying the solution from mouse/rat to cheese, clearing
	 * the solution, and then displaying the solution from cheese to exit.
	 * 
	 * @param args <type String>
	 * @throws IOException
	 * @return void
	 */
	public static void main(String[] args) throws IOException {
		File File = new File();
		Maze Maze = new Maze();
		Print Print = new Print();
		Process Process = new Process();
		File.fileRead();
		Maze.resetMazeSteps();
		Print.printMazeMap();
		Process.findCheese();
		Maze.resetMaze();
		Process.findExit();
	}
}

/**
 * Print class
 * 
 * Class that handles all the print/output to the console to show visuals to the
 * user
 * 
 * @author Willie Pai
 */
class Print {

	/**
	 * printMazeMap method
	 * 
	 * Procedural method that prints the original maze layout by printing each
	 * individual character of the maze array
	 * 
	 * @return void
	 */
	public void printMazeMap() {
		Maze Maze = new Maze();
		System.out.println("Maze Layout");
		for (int y = 0; y < Maze.getLengthMap(); y++) {
			for (int x = 0; x < Maze.getLengthMap(y) - 1; x++)
				System.out.print(Maze.getMap(x, y) + "\t");
			System.out.println();
		}
		System.out.println();
	}

	/**
	 * printMazePath method
	 * 
	 * Procedural method that prints the shortest pathway between two points by
	 * printing out each individual character value of the maze with the pathway
	 * 
	 * @param intDestinationX - x-coordinate of the destination object on the map
	 *                        <type int>
	 * @param intDestinationY - y-coordinate of the destination object on the map
	 *                        <type int>
	 * @return void
	 */
	public void printMazePath(int intDestinationX, int intDestinationY) {
		Maze Maze = new Maze();
		for (int y = 0; y < Maze.getLengthPath(); y++) {
			for (int x = 0; x < Maze.getLengthPath(y); x++)
				System.out.print(Maze.getPath(x, y) + "\t");
			System.out.println();
		}
		System.out.println("Step Count: " + (Maze.getSteps(intDestinationX, intDestinationY) - 1));
	}
}

/**
 * Process class
 * 
 * Class that handles all the processing of the program's function
 * 
 * @author Willie Pai
 */
class Process {

	/**
	 * arrayCopy method
	 * 
	 * Procedural method that copies an array's char value one row at a time to a
	 * target array without copying its reference by value
	 * 
	 * @param chrSource      - character 2D array of the source that the method is
	 *                       copying from <type char>
	 * @param chrDestination - character 2D array of the source that the method is
	 *                       copying to <type char>
	 * @return void
	 */
	public void arrayCopy(char[][] chrSource, char[][] chrDestination) {
		for (int i = 0; i < chrDestination.length; i++) {
			System.arraycopy(chrSource[i], 0, chrDestination[i], 0, chrDestination[i].length);
		}
	}

	/**
	 * isInside method
	 * 
	 * Functional method that determines whether or not the x,y coordinates are
	 * inside the intMazeSteps array's range
	 * 
	 * intTest - Test variable to use in try/catch <type Int>
	 * 
	 * @param intX - x-coordinate of the array <type int>
	 * @param intY - y-coordinate of the array <type int>
	 * @return true/false - whether the x,y coordinates are inside <type boolean>
	 */
	public boolean isInside(int intX, int intY) {
		try {
			int intTest = Maze.intMazeSteps[intY][intX];
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
		return true;
	}

	/**
	 * calcSteps method
	 * 
	 * Functional recursive method that calculates and saves the number of steps it
	 * takes to arrive at a certain location in the maze based on starting
	 * coordinates. By replacing each spot with the shortest amount of steps it
	 * takes to arrive (i.e. if it took 8 steps to reach a position in the maze, but
	 * another solution only took 4 steps, it would replace the number in that
	 * position with four), it prevents any errors that may be caused by loops and
	 * dead ends in the maze, while also generating the shortest path between two
	 * points (i.e. starting at the position of the rat/mouse, which is step 1, ).
	 * 
	 * @param x     - x-coordinate of the maze <type int>
	 * @param y     - y-coordinate of the maze <type int>
	 * @param steps - keeps track of the number of steps that the method is on when
	 *              checking each coordinate of the maze <type int>
	 * @return void
	 */
	public void calcSteps(int x, int y, int steps) {
		Maze Maze = new Maze();
		if (isInside(x, y))
			if (Maze.getSteps(x, y) == 0 || steps < Maze.getSteps(x, y)) {
				Maze.setSteps(x, y, steps);
				steps++;
				calcSteps(x + 1, y, steps);
				calcSteps(x - 1, y, steps);
				calcSteps(x, y + 1, steps);
				calcSteps(x, y - 1, steps);
			}
	}

	/**
	 * findCheese method
	 * 
	 * Functional method that handles all the events to determine the path from the
	 * rat/mouse to the cheese, calculating the distances of each position in the
	 * maze from the rat/mouse starting position, tracing the shortest path by
	 * backtracking from the cheese, and displaying the maze to the user in the
	 * console.
	 * 
	 * @return void
	 */
	public void findCheese() {
		Location Location = new Location();
		Maze Maze = new Maze();
		Print Print = new Print();
		calcSteps(Location.getRatCoords('X'), Location.getRatCoords('Y'), 1);
		shortestPath(Location.getCheeseCoords('X'), Location.getCheeseCoords('Y'), Location.getRatCoords('X'),
				Location.getRatCoords('Y'),
				Maze.getSteps(Location.getCheeseCoords('X'), Location.getCheeseCoords('Y')));
		System.out.println("Rat to Cheese");
		Print.printMazePath(Location.getCheeseCoords('X'), Location.getCheeseCoords('Y'));
	}

	/**
	 * findExit method
	 * 
	 * Functional method that handles all the trigger of events to determine the
	 * path from the cheese to the exit, calculating the distances of each position
	 * in the maze from the cheese starting position, tracing the shortest path by
	 * backtracking from the exit, and displaying the maze to the user in the
	 * console.
	 * 
	 * @return void
	 */
	public void findExit() {
		Location Location = new Location();
		Maze Maze = new Maze();
		Print Print = new Print();
		calcSteps(Location.getCheeseCoords('X'), Location.getCheeseCoords('Y'), 1);
		shortestPath(Location.getExitCoords('X'), Location.getExitCoords('Y'), Location.getCheeseCoords('X'),
				Location.getCheeseCoords('Y'), Maze.getSteps(Location.getExitCoords('X'), Location.getExitCoords('Y')));
		System.out.println("Cheese to Exit");
		Print.printMazePath(Location.getExitCoords('X'), Location.getExitCoords('Y'));
	}

	/**
	 * shortestPath method
	 * 
	 * Functional method that backtracks through the maze by using the shortest
	 * distance values to each location in the maze, drawing a '+' symbol on the
	 * shortest path between two points. It starts from the destination x,y
	 * coordinates (i.e. the cheese of the path from mouse to cheese), taking the
	 * number of steps it takes to get to it (i.e. 15 steps) and counting backwards
	 * by an increment of -1 while checking which position around its current one
	 * contains the number (i.e. starting at the cheese at 15 steps and checking
	 * which adjacent position contains the number 14, which is the shortest path
	 * back to the position of the rat/mouse, which contains step count of 1).
	 * 
	 * @param intX      - x-coordinate of the maze <type int>
	 * @param intY      - y-coordinate of the maze <type int>
	 * @param intfinalX - x-coordinate of the destination location <type int>
	 * @param intfinalY - y-coordinate of the destination location <type int>
	 * @param intSteps  - counts the steps backwards from the end location back to
	 *                  step 1, the starting location <type int>
	 * @return true/false - returns true when reached the specified location of an
	 *         object <type boolean>
	 */
	public boolean shortestPath(int intX, int intY, int intfinalX, int intfinalY, int intSteps) {
		Maze Maze = new Maze();
		if (isInside(intX, intY))
			if (Maze.getSteps(intX, intY) == intSteps) {
				intSteps--;
				if (intX == intfinalX && intY == intfinalY) {
					Maze.setPath(intX, intY, '+');
					return true;
				} else if (shortestPath(intX + 1, intY, intfinalX, intfinalY, intSteps)
						|| shortestPath(intX - 1, intY, intfinalX, intfinalY, intSteps)
						|| shortestPath(intX, intY + 1, intfinalX, intfinalY, intSteps)
						|| shortestPath(intX, intY - 1, intfinalX, intfinalY, intSteps)) {
					Maze.setPath(intX, intY, '+');
					return true;
				}
			}
		return false;
	}

}

/**
 * File class
 * 
 * Class that handles all the data exchange between the program and the text
 * file of the maze
 * 
 * @author Willie Pai
 */
class File {

	Location location = new Location();

	/**
	 * isFile method
	 * 
	 * Determines whether or not the maze.txt text file that contains the maze
	 * exists
	 * 
	 * @return true/false - whether the maze text file exists in the program folder
	 */
	public boolean isFile() throws IOException {
		try {
			FileReader fr = new FileReader("maze.txt");
			fr.close();
		} catch (FileNotFoundException e) {
			System.out.println("maze.txt file does not exist.");
			return false;
		}
		return true;
	}

	/**
	 * fileRead method
	 * 
	 * Procedural method that reads the maze from the text file and saves it to an
	 * array, while also noting the x,y coordinates of the rat/mouse, cheese, and
	 * exit, and printing an error if any missing or unknown elements occur
	 * 
	 * boolIsRat - whether or not a rat exists in the maze file <type boolean>
	 * boolIsCheese - whether or not a cheese exists in the maze file <type boolean>
	 * boolIsExit - whether or not an exit exists in the maze file <type boolean>
	 * chrInput - keeps track of each individual character read from the
	 * maze.txt text file <type char>
	 * 
	 * @throws FileNotFoundException, IOException
	 * @return void
	 */
	public void fileRead() throws FileNotFoundException, IOException {
		Maze Maze = new Maze();
		if (isFile()) {
			FileReader fr = new FileReader("h:/maze.txt");
			boolean boolIsRat, boolIsCheese, boolIsExit;
			boolIsRat = boolIsCheese = boolIsExit = false;
			char chrInput;
			for (int y = 0; y < Maze.getLengthMap(); y++) {
				for (int x = 0; x < Maze.getLengthMap(y); x++) {
					chrInput = (char) fr.read();
					Maze.setMap(x, y, chrInput);
					if (chrInput == 'B')
						Maze.setSteps(x, y, -1);
					else if (chrInput == 'R') {
						boolIsRat = true;
						location.setRatCoords(x, y);
					} else if (chrInput == 'C') {
						boolIsCheese = true;
						location.setCheeseCoords(x, y);
					} else if (chrInput == 'X') {
						boolIsExit = true;
						location.setExitCoords(x, y);
					}
				}
				fr.read();
			}
			fr.close();
			Maze.resetMaze();
			if (!boolIsRat | !boolIsCheese | !boolIsExit)
				System.out.println("The maze file is missing some element(s).\n");
		}
	}

}

/**
 * Location class
 * 
 * Class that handles the storage, change, and sharing of the x,y coordinates of
 * the rat, cheese, and exit
 * 
 * intRatX - x-coordinate of the rat in the maze <type int> intRatY -
 * y-coordinate of the rat in the maze <type int> intCheeseX - x-coordinate of
 * the cheese in the maze <type int> intCheeseY - y-coordinate of the cheese in
 * the maze <type int> intExitX - x-coordinate of the exit in the maze <type
 * int> intExitY - y-coordinate of the exit in the maze <type int>
 * 
 * @author Willie Pai
 */
class Location {

	private static int intRatX, intRatY, intCheeseX, intCheeseY, intExitX, intExitY;

	/**
	 * setRatCoords mutator method
	 * 
	 * Procedural method that sets the mouse/rat's coordinates in the maze
	 * 
	 * @param intX - x-coordinate of object <type int>
	 * @param intY - y-coordinate of object <type int>
	 * @return void
	 */
	public void setRatCoords(int intX, int intY) {
		intRatX = intX;
		intRatY = intY;
	}

	/**
	 * getRatCoords accessor method
	 * 
	 * Functional method that returns the x or y coordinate of the mouse/rat in the
	 * maze
	 * 
	 * @param charXY - determines requested x or y coordinate <type char>
	 * @return x or y coordinate of the mouse/rat <type int>
	 */
	public int getRatCoords(char charXY) {
		if (charXY == 'X')
			return intRatX;
		else if (charXY == 'Y')
			return intRatY;
		return -1;
	}

	/**
	 * setCheeseCoords mutator method
	 * 
	 * Procedural method that sets the cheese's coordinates in the maze
	 * 
	 * @param intX - x-coordinate of object <type int>
	 * @param intY - y-coordinate of object <type int>
	 * @return void
	 */
	public void setCheeseCoords(int intX, int intY) {
		intCheeseX = intX;
		intCheeseY = intY;
	}

	/**
	 * getCheeseCoords accessor method
	 * 
	 * Functional method that returns the x or y coordinate of the cheese in the
	 * maze
	 * 
	 * @param charXY - determines requested x or y coordinate <type char>
	 * @return x or y coordinate of the cheese <type int>
	 */
	public int getCheeseCoords(char charXY) {
		if (charXY == 'X')
			return intCheeseX;
		else if (charXY == 'Y')
			return intCheeseY;
		return -1;
	}

	/**
	 * setExitCoords mutator method
	 * 
	 * Procedural method that sets the exit's coordinates in the maze
	 * 
	 * @param intX - x-coordinate of object <type int>
	 * @param intY - y-coordinate of object <type int>
	 * @return void
	 */
	public void setExitCoords(int intX, int intY) {
		intExitX = intX;
		intExitY = intY;
	}

	/**
	 * getExitCoords accessor method
	 * 
	 * Functional method that returns the x or y coordinate of the exit in the maze
	 * 
	 * @param charXY - determines requested x or y coordinate <type char>
	 * @return x or y coordinate of the exit <type int>
	 */
	public int getExitCoords(char charXY) {
		if (charXY == 'X')
			return intExitX;
		else if (charXY == 'Y')
			return intExitY;
		return -1;
	}

}

/**
 * Maze Class
 * 
 * Class that handles the creation, change, return, and display of the maze's
 * map and pathway
 * 
 * intMazeSizeLength - length of maze <type int> intMazeSizeWidth - width of
 * maze <type int> chrMazeMap - array of original map <type char> chrMazePath -
 * array of pathway solution between two points on map <type char> intMazeSteps
 * - array of least number of steps to each point in maze <type int>
 * 
 * @author Willie Pai
 */
class Maze {

	private static int intMazeSizeLength = 8, intMazeSizeWidth = 12;
	private static char[][] chrMazeMap = new char[intMazeSizeLength][intMazeSizeWidth + 1],
			chrMazePath = new char[intMazeSizeLength][intMazeSizeWidth];
	static int[][] intMazeSteps = new int[intMazeSizeLength][intMazeSizeWidth];

	/**
	 * getMap accessor method
	 * 
	 * Functional method that sends the object on the maze
	 * 
	 * @param intX - x coordinate of maze <type int>
	 * @param intY - y coordinate of maze <type int>
	 * @return object (wall, space, rat, cheese , or exit) at specific location of
	 *         maze <type char> <type char>
	 */
	public char getMap(int intX, int intY) {
		return chrMazeMap[intY][intX];
	}

	/**
	 * changeMap mutator method
	 * 
	 * Procedural method that changes the object on maze
	 * 
	 * @param intX      - x coordinate of maze <type int>
	 * @param intY      - y coordinate of maze <type int>
	 * @param chrObject - character value of object <type char>
	 * @return void
	 */
	public void setMap(int intX, int intY, char chrObject) {
		chrMazeMap[intY][intX] = chrObject;
	}

	/**
	 * lengthMap accessor method
	 * 
	 * Functional method that returns the height/column length of the maze
	 * 
	 * @return size of maze <type int>
	 */
	public int getLengthMap() {
		return chrMazeMap.length;
	}

	/**
	 * lengthMap accessor method
	 * 
	 * Functional method that returns the width/row length of the maze
	 * 
	 * @param intY row number of maze <type int>
	 * @return size of maze <type int>
	 */
	public int getLengthMap(int intY) {
		return chrMazeMap[intY].length;
	}

	/**
	 * getPath accessor method
	 * 
	 * Functional method that sends the path on the maze
	 * 
	 * @param intX - x coordinate of maze <type int>
	 * @param intY - y coordinate of maze <type int>
	 * @return character (wall, space, or pathway) at specific location of maze
	 *         <type char>
	 */
	public char getPath(int intX, int intY) {
		return chrMazePath[intY][intX];
	}

	/**
	 * changePath mutator method
	 * 
	 * Procedural method that changes the path on maze
	 * 
	 * @param intX      - x coordinate of maze <type int>
	 * @param intY      - y coordinate of maze <type int>
	 * @param chrObject - character value of object <type char>
	 * @return void
	 */
	public void setPath(int intX, int intY, char chrPath) {
		chrMazePath[intY][intX] = chrPath;
	}

	/**
	 * lenghtPath accessor method
	 * 
	 * Functional method that returns the height/column length of the maze
	 * 
	 * @return length of path array <type int>
	 */
	public int getLengthPath() {
		return chrMazePath.length;
	}

	/**
	 * lengthPath accessor method
	 * 
	 * Functional method that returns the width/row length of the maze
	 * 
	 * @param intY row number of maze <type int>
	 * @return size of maze <type int>
	 */
	public int getLengthPath(int intY) {
		return chrMazePath[intY].length;
	}

	/**
	 * getSteps accessor method
	 * 
	 * Functional method that returns the distance value at the specific coordinate
	 * of the maze
	 * 
	 * @param intX - x-coordinate of array <type int>
	 * @param intY - y-coordinate of array <type int>
	 * @return distance <type int>
	 */
	public int getSteps(int intX, int intY) {
		return intMazeSteps[intY][intX];
	}

	/**
	 * changeSteps mutator method
	 * 
	 * Procedural method that changes the distance value at the specific coordinate
	 * of the maze
	 * 
	 * @param intX     - x-coordinate of array <type int>
	 * @param intY     - y-coordinate of array <type int>
	 * @param intSteps - number of steps taken <type int>
	 * @return void
	 */
	public void setSteps(int intX, int intY, int intSteps) {
		intMazeSteps[intY][intX] = intSteps;
	}

	/**
	 * lengthSteps accessor method
	 * 
	 * Functional method that returns the height/column length of the maze
	 * 
	 * @return length of path array <type int>
	 */
	public int getLengthSteps() {
		return intMazeSteps.length;
	}

	/**
	 * lengthSteps accesor method
	 * 
	 * Functional method that returns the width/row length of the maze
	 * 
	 * @param intY - row number of maze array <type int>
	 * @return length of path array <type int>
	 */
	public int getLengthSteps(int intY) {
		return intMazeSteps[intY].length;
	}

	/**
	 * resetMaze method
	 * 
	 * Procedural method that resets the maze path solution by re-copying it from
	 * the original maze while also resetting the array that holds distance value to
	 * each location of the maze
	 * 
	 * @return void
	 */
	public void resetMaze() {
		Process Process = new Process();
		Process.arrayCopy(chrMazeMap, chrMazePath);
		resetMazeSteps();
	}

	/**
	 * resetMazeSteps method
	 * 
	 * Procedural method that resets the distance values of each element of the
	 * array back to default of 0, to be calculated again from a different starting
	 * postition
	 * 
	 * @return void
	 */
	public void resetMazeSteps() {
		for (int y = 0; y < intMazeSteps.length; y++)
			for (int x = 0; x < intMazeSteps[y].length; x++)
				if (intMazeSteps[y][x] != -1)
					intMazeSteps[y][x] = 0;
	}

}