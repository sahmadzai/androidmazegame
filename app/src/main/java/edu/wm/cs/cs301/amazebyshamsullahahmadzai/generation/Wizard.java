package edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation;

import android.util.Log;

import java.util.Arrays;

import edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation.Robot.Direction;
import edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation.Robot.Turn;

/**
 * CRC Card for Wizard:
 * Responsibilities:
 *   - Creates a robot and a maze to use for other classes
 *   - Gets various information that relates to energy consumption, driving to exit, and attaining the length of the path it takes.
 *   - Supposed to use information about the maze to find the quickest path to the exit by looking for a neighbor closer to the exit.
 *   - If this driver fails by hitting a wall or obstacle then the robot resorts to using it's sensors.
 * 
 * Collaborators:
 * 1. RobotDriver
 * 
 * 
 * @author Shamsullah Ahmadzai
 *
 */
public class Wizard implements RobotDriver {
	/*
	 * Initialize class variables for:
	 *    - Robot object
	 *    - Maze object
	 *    - Width, Height
	 *    - Integer to store path length
	 *    - Integer to the initial battery level
     *    - Int array of cardinal directions that store the numerical values of the directions
	 */
    private Robot robot;
    private Maze maze;
    private int width;
    private int height;
    private int pathLength;
    private float initialBatteryLevel;
    private int[] EAST = {1, 0}; 
	private int[] WEST = {-1, 0};
	private int[] NORTH = {0 , -1};
	private int[] SOUTH = {0, 1};
	
	/**
	 * Assigns a robot platform to the driver. 
	 * The driver uses a robot to perform, this method provides it with this necessary information.
	 * @param r robot to operate
	 */
	@Override
	public void setRobot(Robot r) {
		/*
		 * Take the robot object passed in and set it to the robot variable.
         * Set the robot controller to the control.
		 * Set the initial energy/battery level by calling getBatteryLevel()
		 * in Robot Driver.
		 */
        robot = r;
        initialBatteryLevel = robot.getBatteryLevel();
        this.pathLength = 0;
	}

	/**
	 * Provides the robot driver with the maze information.
	 * Only some drivers such as the wizard rely on this information to find the exit.
	 * @param maze represents the maze, must be non-null and a fully functional maze object.
	 */
	@Override
	public void setMaze(Maze maze) {
		/*
		 * Set the maze object to the maze that is passed in and update any 
		 * corresponding values accordingly, such as width, height, or a maze.
		 */
        this.maze = maze;
        this.width = maze.getWidth();
        this.height = maze.getHeight();
        System.out.println("maze has been initialized: " + width + "x" + height);
	}

	/**
	 * Drives the robot towards the exit following
	 * its solution strategy and given the exit exists and  
	 * given the robot's energy supply lasts long enough. 
	 * When the robot reached the exit position and its forward
	 * direction points to the exit the search terminates and 
	 * the method returns true.
	 * If the robot failed due to lack of energy or crashed, the method
	 * throws an exception.
	 * If the method determines that it is not capable of finding the
	 * exit it returns false, for instance, if it determines it runs
	 * in a cycle and can't resolve this.
	 * @return true if driver successfully reaches the exit, false otherwise
	 * @throws Exception thrown if robot stopped due to some problem, e.g. lack of energy
	 */
	@Override
	public boolean drive2Exit() throws Exception {
		/* A while loop to continue performing below code.
		 * Check that the robot has not reached the exit yet, if it hasn't
		 * then get the coordinates of the robot's current position and get
		 * its current direction as well. Find the neighboring cell that is 
		 * closest to the exit using getNeighborCloserToExit() and get its
		 * coordinates. If the difference between the neighbor cell and the current
		 * cell position is equal to any of the direction arrays then move in that direction.
		 * 
		 * 
		 * If the robot has reached the exit and the robot cannot see infinity
		 * turn the robot towards the left. Otherwise just move one space forward
		 * if none of the conditionals work.
		 */
		while(!robot.isAtExit()) {
			System.out.println("Robot battery level: " + robot.getBatteryLevel());
//			if (robot.getBatteryLevel() <= 0) {
//			throw new Exception("Robot battery is dead, no action possible.");
			if (robot.hasStopped()) {
				throw new Exception("Robot has crashed or run out of energy.");
			}
			else {
				int[] currentPosition = robot.getCurrentPosition();
				System.out.println("The current position is at: " + currentPosition[0] + ", " + currentPosition[1]);

				CardinalDirection currentDirection = robot.getCurrentDirection();
				System.out.println("Current direction is: " + currentDirection);

				int[] neighborClosestToExit = maze.getNeighborCloserToExit(currentPosition[0], currentPosition[1]);
				
				// Subtract neighborClosestToExit from currentPosition to get the difference
				int[] difference = {neighborClosestToExit[0] - currentPosition[0], neighborClosestToExit[1] - currentPosition[1]};
				System.out.println("Neighbor closer to exit is: " + neighborClosestToExit[0] + ", " + neighborClosestToExit[1]);

				if(Arrays.equals(difference, EAST)) {
					checkEast(currentDirection);
				} else if(Arrays.equals(difference, WEST)) {
					checkWest(currentDirection);
				} else if(Arrays.equals(difference, NORTH)) {
					checkNorth(currentDirection);
				} else if(Arrays.equals(difference, SOUTH)) {
					checkSouth(currentDirection);
				}
				System.out.println();
			}
		}
		if(robot.canSeeThroughTheExitIntoEternity(Direction.FORWARD)) {
			robot.setBatteryLevel(robot.getBatteryLevel()-1);
			robot.move(1);
			return true;
		}
		robot.setBatteryLevel(robot.getBatteryLevel()-1);
		robot.rotate(Turn.LEFT);
		return false;
	}

	/**
	 * Drives the robot one step towards the exit following
	 * its solution strategy and given the exists and 
	 * given the robot's energy supply lasts long enough.
	 * It returns true if the driver successfully moved
	 * the robot from its current location to an adjacent
	 * location.
	 * At the exit position, it rotates the robot 
	 * such that if faces the exit in its forward direction
	 * and returns false. 
	 * If the robot failed due to lack of energy or crashed, the method
	 * throws an exception. 
	 * @return true if it moved the robot to an adjacent cell, false otherwise
	 * @throws Exception thrown if robot stopped due to some problem, e.g. lack of energy
	 */
	@Override
	public boolean drive1Step2Exit() throws Exception {
		/*
		 * Check that the robot has not reached the exit yet, if it hasn't
		 * then get the coordinates of the robot's current position and get
		 * its current direction as well. Find the neighboring cell that is
		 * closest to the exit using getNeighborCloserToExit() and get its
		 * coordinates. If the difference between the neighbor cell and the current
		 * cell position is equal to any of the direction arrays then move one step
		 * in that direction. 
		 * 
		 * If the robot has reached the cell before the exit just turn towards
		 * the exit s.t. the front of the bot is facing the exit and return false.
		 */
		if (robot.hasStopped()) {
			throw new Exception("Robot has crashed or run out of energy.");
		}
		if(!robot.isAtExit()) {
			int[] currentPosition = robot.getCurrentPosition();
			CardinalDirection currentDirection = robot.getCurrentDirection();
			int[] neighborClosestToExit = maze.getNeighborCloserToExit(currentPosition[0], currentPosition[1]);
			// Subtract neighborClosestToExit from currentPosition to get the difference
			int[] difference = {neighborClosestToExit[0] - currentPosition[0], neighborClosestToExit[1] - currentPosition[1]};
			if(Arrays.equals(difference, EAST)) {
				checkEast(currentDirection);
			} else if(Arrays.equals(difference, WEST)) {
				checkWest(currentDirection);
			} else if(Arrays.equals(difference, NORTH)) {
				checkNorth(currentDirection);
			} else if(Arrays.equals(difference, SOUTH)) {
				checkSouth(currentDirection);
			}
			pathLength++;
			System.out.println();
			return true;
		}
		if(robot.canSeeThroughTheExitIntoEternity(Direction.FORWARD)) {
				robot.setBatteryLevel(robot.getBatteryLevel()-1);
				robot.move(1);
				return true;
		} else {
			robot.setBatteryLevel(robot.getBatteryLevel()-1);
			robot.rotate(Turn.LEFT);
			return false;
		}
		
	}
	
	/**
	 * This helper method checks if the robot is facing north and if it is not then it
	 * rotates the robot to face north. If the robot is facing north then it moves one
	 * step forward.
	 * @param currentDirection the direction the robot is currently facing
	 */
	private void checkNorth(CardinalDirection currentDirection) {
		System.out.println("The difference is the NORTH direction.");
		if(currentDirection == CardinalDirection.East) {
			robot.rotate(Turn.RIGHT);
		} else if(currentDirection == CardinalDirection.North) {
			robot.move(1);
			pathLength++;
		} else if(currentDirection == CardinalDirection.South) {
			robot.rotate(Turn.AROUND);
		} else if(currentDirection == CardinalDirection.West) {
			robot.rotate(Turn.LEFT);
		}
	}
	
	/**
	 * This helper method checks if the robot is facing south and if it is not then it
	 * rotates the robot to face south. If the robot is facing south then it moves one
	 * step forward.
	 * @param currentDirection the direction the robot is currently facing
	 */
	private void checkSouth(CardinalDirection currentDirection) {
		System.out.println("The difference is the SOUTH direction.");
		if(currentDirection == CardinalDirection.East) {
			robot.rotate(Turn.LEFT);
		} else if(currentDirection == CardinalDirection.North) {
			robot.rotate(Turn.AROUND);
		} else if(currentDirection == CardinalDirection.South) {
			robot.move(1);
			pathLength++;
		} else if(currentDirection == CardinalDirection.West) {
			robot.rotate(Turn.RIGHT);
		}
	}
	
	/**
	 * This helper method checks if the robot is facing east and if it is not then it
	 * rotates the robot to face east. When the robot is facing east it moves one step
	 * forward.
	 * @param currentDirection the direction the robot is currently facing
	 */
	private void checkEast(CardinalDirection currentDirection) {
		System.out.println("The difference is the EAST direction.");
		if(currentDirection == CardinalDirection.East) {
			robot.move(1);
			pathLength++;
		} else if(currentDirection == CardinalDirection.North) {
			robot.rotate(Turn.LEFT);
		} else if(currentDirection == CardinalDirection.South) {
			robot.rotate(Turn.RIGHT);
		} else if(currentDirection == CardinalDirection.West) {
			robot.rotate(Turn.AROUND);
		}
	}
	
	/**
	 * This helper method checks if the robot is facing west and if it is not then it
	 * rotates the robot till it is facing west. When the robot is facing west
	 * it moves one step forward.
	 * @param currentDirection the direction the robot is currently facing
	 */
	private void checkWest(CardinalDirection currentDirection) {
		System.out.println("The difference is the WEST direction.");
		if(currentDirection == CardinalDirection.East) {
			robot.rotate(Turn.AROUND);
		} else if(currentDirection == CardinalDirection.North) {
			robot.rotate(Turn.RIGHT);
		} else if(currentDirection == CardinalDirection.South) {
			robot.rotate(Turn.LEFT);
		} else if(currentDirection == CardinalDirection.West) {
			robot.move(1);
			pathLength++;
		}
	}
	
	/**
	 * Returns the total energy consumption of the journey, i.e.,
	 * the difference between the robot's initial energy level at
	 * the starting position and its energy level at the exit position. 
	 * This is used as a measure of efficiency for a robot driver.
	 * @return the total energy consumption of the journey
	 */
	@Override
	public float getEnergyConsumption() {
		/*
		 * Return the initial energy subtracted by the robots current
		 * battery level to get the energy left.
		 */
        return this.initialBatteryLevel - this.robot.getBatteryLevel();
	}
	
	/**
	 * Returns the total length of the journey in number of cells traversed. 
	 * Being at the initial position counts as 0. 
	 * This is used as a measure of efficiency for a robot driver.
	 * @return the total length of the journey in number of cells traversed
	 */
	@Override
	public int getPathLength() {
		/*
		 * Return the path length variable that is incremented with every step.
		 */
        return this.pathLength;
	}
	
	/**
	 * Returns the initial battery level of the robot to be used in other
	 * classes/the losing screen.
	 * @return the initial battery level stored in the initalBatteryLevel variable
	 */
	@Override
	public float getInitialBatteryLevel() {
		/*
		 * Return the path length variable that is incremented with every step.
		 */
        return this.initialBatteryLevel;
	}

}