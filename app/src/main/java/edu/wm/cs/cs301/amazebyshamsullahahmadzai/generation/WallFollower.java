/**
 * 
 */
package edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation;

import edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation.Robot.Direction;
import edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation.Robot.Turn;

/**
 * CRC Card for WallFollower:
 * Responsibilities:
 *  - Algorithm that follows the wall for the robot to find the exit.
 * 
 * Collaborators:
 *  - Robot
 * 
 * @author Shamsullah Ahmadzai
 *
 */
public class WallFollower implements RobotDriver {
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
	private boolean lSensor = true;
	private boolean rSensor = true;
	private boolean fSensor = true;
	private boolean bSensor = true;
	
	
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

	
	@Override
	public void setMaze(Maze maze) {
		/*
		 * Set the maze object to the maze that is passed in and update any 
		 * corresponding values accordingly, such as width, height, or a maze.
		 */
        this.maze = maze;
        this.width = maze.getWidth();
        this.height = maze.getHeight();
        System.out.println("Maze has been initialized: " + width + "x" + height);
	}

	@Override
	public boolean drive2Exit() throws Exception {
		/**
		 * While loop checks that the robot is not at the exit yet and 
		 * calls the drive1step2Exit method which handles the movement
		 * and rotation of the robot. Once the method returns false, 
		 * this method will return true ending the movement.
		 */
		while (!robot.isAtExit()) {
			if(!drive1Step2Exit())
				return true;
		}
		return false;
	}


	@Override
	public boolean drive1Step2Exit() throws Exception {
		/**
		 * Check that the robot has not crashed or stopped.
		 * If not then continue onto the next if statement
		 * that checks if the robot has reached the exit.
		 * If it has reached the exit then the robot uses it's 
		 * sensors to find which direction Eternity is in and turns
		 * in that direction.
		 * 
		 * Another if statement will check that the left sensor reads a 
		 * distance greator than 0, if it does then the robot will turn left
		 * and move forward one step and return true to restart the while loop.
		 * 
		 * Another if statement checks the distance in the forward direction being
		 * larger than 0, if it is larger than 0 then the robot moves one step 
		 * forward and returns true to restart the while loop.
		 * 
		 * If no if statement passes then the robot simply rotates to the right and
		 * returns true to restart the while loop in drive2Exit().
		 */
		if (robot.hasStopped()) {
			throw new Exception("Robot has crashed or run out of energy.");
		}
		
		// If robot is in a room, turn left and move forward.
        // If the robot is in the middle of a room and there are no walls to the left, turn left and move forward.
//		if (robot.isInsideRoom()) {
//	        if (robot.distanceToObstacle(Direction.LEFT) == 0) {
//	            robot.rotate(Turn.LEFT);
//	            robot.move(1);
//	            pathLength++;
//	            return true;
//	        }
//		}
		
		if (robot.isAtExit()) {
			if(robot.canSeeThroughTheExitIntoEternity(Direction.LEFT))
				robot.rotate(Turn.LEFT);
			if(robot.canSeeThroughTheExitIntoEternity(Direction.RIGHT))
				robot.rotate(Turn.RIGHT);
			if(robot.canSeeThroughTheExitIntoEternity(Direction.BACKWARD))
				robot.rotate(Turn.AROUND);
			return false;
		}

		if (checkLeftSensorAndMove()) {
			//Rotate back to the original heading before moving
//			rotateToHeading(ogHeading);
			robot.rotate(Turn.LEFT);
			robot.move(1);
			return true;
		}
		if (checkForwardSensorAndMove()) {
			//Rotate back to the original heading before moving
//			rotateToHeading(ogHeading);
			robot.move(1);
			return true;			
		}
		robot.rotate(Turn.RIGHT);
		return true;
	}

	/**
	 * Method to check that the left sensor is operation and if it is then check
	 * that the distance is greater than 0, if it is then turn left and move forward 
	 * one step. Try catch block is used to catch any exceptions that may occur.
	 * @throws Exception
	 */
	private boolean checkLeftSensorAndMove() throws Exception {
		boolean temp = true;
		try {
			temp = (robot.distanceToObstacle(Direction.LEFT) > 0);
		} catch (UnsupportedOperationException e) {
			System.out.println("Left sensor is not operational.");
			robot.rotate(Turn.LEFT);
			temp = checkForwardSensorAndMove();
			robot.rotate(Turn.RIGHT);
		}
		return temp;
	}

	/**
	 * Method to check that the forward sensor is operation and if it is then check
	 * that the distance is greater than 0, if it is then move forward one step. 
	 * Try catch block is used to catch any exceptions that may occur.
	 * @throws Exception
	 */
	private boolean checkForwardSensorAndMove() throws Exception {
		boolean temp = true;
		try {
			temp = (robot.distanceToObstacle(Direction.FORWARD) > 0);
		} catch (UnsupportedOperationException e) {
			System.out.println("Forward sensor is not operational.");
			robot.rotate(Turn.LEFT);
			temp = checkRightSensorAndMove();
			robot.rotate(Turn.RIGHT);
		}
		return temp;
	}

	/**
	 * Method to check that the right sensor is operation and if it is then check
	 * that the distance is greater than 0, if it is then turn right and move forward 
	 * one step. Try catch block is used to catch any exceptions that may occur.
	 * @throws Exception
	 */
	private boolean checkRightSensorAndMove() throws Exception {
		boolean temp = true;
		try {
			temp = (robot.distanceToObstacle(Direction.RIGHT) > 0);
		} catch (UnsupportedOperationException e) {
			System.out.println("Right sensor is not operational.");
			robot.rotate(Turn.LEFT);
			temp = checkBackwardSensorAndMove();
			robot.rotate(Turn.RIGHT);
		}
		return temp;
	}

	/**
	 * Method to check that the backward sensor is operation and if it is then check
	 * that the distance is greater than 0, if it is then turn around and move forward 
	 * one step. Try catch block is used to catch any exceptions that may occur.
	 * @throws Exception
	 */
	private boolean checkBackwardSensorAndMove() throws Exception {
		boolean temp = true;
		try {
			temp = (robot.distanceToObstacle(Direction.BACKWARD) > 0);
		} catch (UnsupportedOperationException e) {
			System.out.println("Backward sensor is not operational.");
			robot.rotate(Turn.LEFT);
			temp = checkLeftSensorAndMove();
			robot.rotate(Turn.RIGHT);
		}
		return temp;
	}

	/**
	 * Method to rotate the robot to the heading that it was in before moving.
	 * Comparing the current heading to the original heading and rotating the robot
	 * accordingly.
	 * @param ogHeading
	 * @throws Exception
	 */
	private void rotateToHeading(CardinalDirection ogHeading) throws Exception {
		CardinalDirection currHeading = robot.getCurrentDirection();
		if (currHeading == CardinalDirection.North) {
			if (ogHeading == CardinalDirection.East) {
				robot.rotate(Turn.RIGHT);
			} else if (ogHeading == CardinalDirection.South) {
				robot.rotate(Turn.AROUND);
			} else if (ogHeading == CardinalDirection.West) {
				robot.rotate(Turn.LEFT);
			}
		} else if (currHeading == CardinalDirection.East) {
			if (ogHeading == CardinalDirection.South) {
				robot.rotate(Turn.RIGHT);
			} else if (ogHeading == CardinalDirection.West) {
				robot.rotate(Turn.AROUND);
			} else if (ogHeading == CardinalDirection.North) {
				robot.rotate(Turn.LEFT);
			}
		} else if (currHeading == CardinalDirection.South) {
			if (ogHeading == CardinalDirection.West) {
				robot.rotate(Turn.RIGHT);
			} else if (ogHeading == CardinalDirection.North) {
				robot.rotate(Turn.AROUND);
			} else if (ogHeading == CardinalDirection.East) {
				robot.rotate(Turn.LEFT);
			}
		} else if (currHeading == CardinalDirection.West) {
			if (ogHeading == CardinalDirection.North) {
				robot.rotate(Turn.RIGHT);
			} else if (ogHeading == CardinalDirection.East) {
				robot.rotate(Turn.AROUND);
			} else if (ogHeading == CardinalDirection.South) {
				robot.rotate(Turn.LEFT);
			}
		}
	}
	
	
	@Override
	public float getEnergyConsumption() {
		/*
		 * Return the initial energy subtracted by the robots current
		 * battery level to get the energy left.
		 */
        return this.initialBatteryLevel - this.robot.getBatteryLevel();
	}
	

	@Override
	public int getPathLength() {
		/*
		 * Return the path length variable that is incremented with every step.
		 */
        return this.pathLength;
	}
	
	
	@Override
	public float getInitialBatteryLevel() {
		/*
		 * Return the path length variable that is incremented with every step.
		 */
        return this.initialBatteryLevel;
	}

}