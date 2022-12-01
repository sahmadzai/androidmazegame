package edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation;

import edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation.Robot.Direction;

/**
 * CRC Card for ReliableSensor:
 * Responsibilities:
 *  - Gets distances to walls or other objects and returns them after setting up the maze.
 *  - Sets the direction of each sensor.
 *  - Calcualtes the energy requirment for sensing
 *  - Handles the repair process when the sensors breakdown 
 * 
 * Collaborators:
 * 1. Cardinal Direction
 * 2. Distance Sensor
 * 
 * 
 * @author Shamsullah Ahmadzai
 *
 */
public class ReliableSensor implements DistanceSensor {
	
	/*
	 * Initialize class variables for:
	 *    - Maze objectds
	 *    - Mount Direction of Sensor
	 */
    protected Maze maze;
    protected Direction mountedDir;
	protected int energyForSensing = 1;
	private boolean sensorState = true;
	
    public ReliableSensor(Maze maze, Direction dir) {
    	setMaze(maze);
    	setSensorDirection(dir);
    }
    
	/**
	 * Tells the distance to an obstacle (a wallboard) that the sensor
	 * measures. The sensor is assumed to be mounted in a particular
	 * direction relative to the forward direction of the robot.
	 * Distance is measured in the number of cells towards that obstacle, 
	 * e.g. 0 if the current cell has a wallboard in this direction, 
	 * 1 if it is one step in this direction before directly facing a wallboard,
	 * Integer.MaxValue if one looks through the exit into eternity.
	 * 
	 * This method requires that the sensor has been given a reference
	 * to the current maze and a mountedDirection by calling 
	 * the corresponding set methods with a parameterized constructor.
	 * 
	 * @param currentPosition is the current location as (x,y) coordinates
	 * @param currentDirection specifies the direction of the robot
	 * @param powersupply is an array of length 1, whose content is modified 
	 * to account for the power consumption for sensing
	 * @return number of steps towards obstacle if obstacle is visible 
	 * in a straight line of sight, Integer.MAX_VALUE otherwise.
	 * @throws Exception with message 
	 * SensorFailure if the sensor is currently not operational
	 * PowerFailure if the power supply is insufficient for the operation
	 * @throws IllegalArgumentException if any parameter is null
	 * or if currentPosition is outside of legal range
	 * ({@code currentPosition[0] < 0 || currentPosition[0] >= width})
	 * ({@code currentPosition[1] < 0 || currentPosition[1] >= height}) 
	 * @throws IndexOutOfBoundsException if the powersupply is out of range
	 * ({@code powersupply < 0}) 
	 */
	@Override
	public int distanceToObstacle(int[] currentPosition, CardinalDirection currentDirection, float[] powersupply)
			throws Exception {
		/*
		 * Using the parameters passed in calculate the distance
		 * to any obstacles in a straight path in the direction given. 
		 * If there is an obstacle get the distance in number of cells 
		 * away from the robot and return it, if at exit return
		 * Integer.MaxValue. Throw an IllegalArgumentException if any 
		 * of the parameters are null or the currentPosition is outside
		 * the legal range. Throw an Exception with message, SensorFailure
		 * if the sensor is currently not operational or throw an exception
		 * with the message PowerFailure if the power supply is insufficient
		 * for the operation.
		 */
        if (currentPosition == null || currentDirection == null || powersupply == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        if (currentPosition[0] < 0 || currentPosition[0] >= maze.getWidth()) {
            throw new IllegalArgumentException("Current position is outside of legal range");
        }
        if (currentPosition[1] < 0 || currentPosition[1] >= maze.getHeight()) {
            throw new IllegalArgumentException("Current position is outside of legal range");
        }
        if (powersupply[0] < 0) {
            throw new IndexOutOfBoundsException("Power supply is out of range");
        }
        if (powersupply[0] < 3) {
            throw new Exception("PowerFailure");
        }
        int distance = 0;
        boolean wallhit = false;
        int[] currentPos = currentPosition;
        while(!wallhit) {
			if (maze.hasWall(currentPos[0], currentPos[1], currentDirection)) {
				wallhit = true;
			} else {
				distance++;
				currentPos = moveForward(currentPos, currentDirection);
			}
		}
//		powersupply[0] -= energyForSensing;
		return distance;
	}

	/**
	 * This method is used to move the robot forward one cell in the direction it is facing.
	 * @param currentPosition is the current location as (x,y) coordinates
	 * @param currentDirection specifies the direction of the robot
	 * @return the new position (int array of size 2) of the robot after moving forward one cell
	 */
	protected int[] moveForward(int[] currentPosition, CardinalDirection currentDirection) {
		/*
		 * Move the robot forward one cell in the direction given
		 * and return the new position as a 2 integer array.
		 */
		int[] newPos = new int[2];
		switch (currentDirection) {
		case North:
			newPos[0] = currentPosition[0];
			newPos[1] = currentPosition[1] - 1;
			break;
		case East:
			newPos[0] = currentPosition[0] + 1;
			newPos[1] = currentPosition[1];
			break;
		case South:
			newPos[0] = currentPosition[0];
			newPos[1] = currentPosition[1] + 1;
			break;
		case West:
			newPos[0] = currentPosition[0] - 1;
			newPos[1] = currentPosition[1];
			break;
		default:
			break;
		}
		return newPos;
	}
	
	/**
	 * Provides the maze information that is necessary to make
	 * a DistanceSensor able to calculate distances.
	 * @param maze the maze for this game
	 * @throws IllegalArgumentException if parameter is null
	 * or if it does not contain a floor plan
	 */
	@Override
	public void setMaze(Maze maze) {
		/*
		 * Set the maze object to the maze that is passed in and update any 
		 * corresponding values accordingly, such as width, height, or a maze.
		 */
        if (maze == null) {
            throw new IllegalArgumentException("Maze cannot be null");
        }
        if (maze.getFloorplan() == null) {
            throw new IllegalArgumentException("Maze does not contain a floor plan");
        }
        this.maze = maze;
	}

	/**
	 * Provides the angle, the relative direction at which this 
	 * sensor is mounted on the robot.
	 * If the direction is left, then the sensor is pointing
	 * towards the left hand side of the robot at a 90 degree
	 * angle from the forward direction. 
	 * @param mountedDirection is the sensor's relative direction
	 * @throws IllegalArgumentException if parameter is null
	 */
	@Override
	public void setSensorDirection(Direction mountedDirection) {
		/*
		 * Calling the robot it sets the sensor at the given direction
		 * to a corresponding angle direction onto the robot.
		 */
        if (mountedDirection == null) {
            throw new IllegalArgumentException("Direction cannot be null");
        }
        mountedDir = mountedDirection;
	}

	/**
	 * Returns the amount of energy this sensor uses for 
	 * calculating the distance to an obstacle exactly once.
	 * This amount is a fixed constant for a sensor.
	 * @return the amount of energy used for using the sensor once
	 */
	@Override
	public float getEnergyConsumptionForSensing() {
		/*
		 * Return the sensor battery cost based on the constants
		 * in the robot class.
		 */
        return energyForSensing;
	}
	
	/** This method sets the sensor to not working or to working based on the boolean passed in.
     * @param boolean state
     */
    public void setSensorState(boolean state) {
        this.sensorState = state;
    }

	/**
	 * Method starts a concurrent, independent failure and repair
	 * process that makes the sensor fail and repair itself.
	 * This creates alternating time periods of up time and down time.
	 * Up time: The duration of a time period when the sensor is in 
	 * operational is characterized by a distribution
	 * whose mean value is given by parameter meanTimeBetweenFailures.
	 * Down time: The duration of a time period when the sensor is in repair
	 * and not operational is characterized by a distribution
	 * whose mean value is given by parameter meanTimeToRepair.
	 * 
	 * This an optional operation. If not implemented, the method
	 * throws an UnsupportedOperationException.
	 * 
	 * @param meanTimeBetweenFailures is the mean time in seconds, must be greater than zero
	 * @param meanTimeToRepair is the mean time in seconds, must be greater than zero
	 * @throws UnsupportedOperationException if method not supported
	 */
	@Override
	public void startFailureAndRepairProcess(int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		/*
		 * Optional method will implement in project 4. Throw
		 * exception UnsupportedOperationException for now.
		 */
		throw new UnsupportedOperationException();
	}
	
	/**
	 * This method stops a failure and repair process and
	 * leaves the sensor in an operational state.
	 * 
	 * It is complementary to starting a 
	 * failure and repair process. 
	 * 
	 * Intended use: If called after starting a process, this method
	 * will stop the process as soon as the sensor is operational.
	 * 
	 * If called with no running failure and repair process, 
	 * the method will return an UnsupportedOperationException.
	 * 
	 * This an optional operation. If not implemented, the method
	 * throws an UnsupportedOperationException.
	 * 
	 * @throws UnsupportedOperationException if method not supported
	 */
	@Override
	public void stopFailureAndRepairProcess() throws UnsupportedOperationException {
		/*
		 * Optional method will implement in project 4. Throw
		 * exception UnsupportedOperationException for now.
		 */
		throw new UnsupportedOperationException();
	}

}