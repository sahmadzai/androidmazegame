package edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation;

import android.util.Log;

import edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation.Robot.Direction;

/**
 * CRC Card for UnreliableSensor:
 * Responsibilities:
 *  - Sensor class that handles the different functions of 
 *    the sensors on the robot but the sensors will be unreliable as can be infered from the classes name.
 * 
 * Collaborators:
 * 1. ReliableSensor
 *
 * 
 * @author Shamsullah Ahmadzai
 *
 */
public class UnreliableSensor extends ReliableSensor {
	
	// Variable to hold the sensor's current state.
	private String LOG_TAG = "unreliable sensor";
    private boolean sensorState;
    private Thread thread;
	private boolean wallhit;

    /**
     * Since this class is a subclass of ReliableSensor, it will have all the same methods and variables
     * as ReliableSensor. The only difference is that any sensor that is unreliable will be handled in this class.
     * The constructor calls the super constructor and passes in the same parameters.
     * @param maze
     * @param direction
     */
    public UnreliableSensor(Maze maze, Direction direction) {
        super(maze, direction);
    }

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
        } if (!sensorState) {
        	throw new Exception("SensorFailure");
        }
        int distance = 0;
        wallhit = false;
        int[] currentPos = currentPosition;
        while(!wallhit) {
			if (maze.hasWall(currentPos[0], currentPos[1], currentDirection)) {
				wallhit = true;
			} else {
				distance++;
				currentPos = moveForward(currentPos, currentDirection);
			}
		}
        //powersupply[0] -= energyForSensing;
		return distance;
	}
	@Override
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
		Log.v(LOG_TAG, "MOVING FORWARD TO DETECT DISTANCE: " + newPos[0] + ", " + newPos[1]);
		if (newPos[0] < 0 || newPos[1] < 0) {
			wallhit = true;
			Log.v(LOG_TAG, "Exit has been detected, OVER NEGATIVE");
		}
		else if (newPos[0] > maze.getWidth() || newPos[1] > maze.getHeight()) {
			wallhit = true;
			Log.v(LOG_TAG, "Exit has been detected, OVER POSITIVE");
		}
		return newPos;
	}


    /**
     * This method handles the state of the sensor. If the sensor is unreliable, it will return true.
     * If the sensor is reliable, it will return false.
     * @return boolean
     */
    public boolean getSensorState() {
        return sensorState;
    }

    /** This method sets the sensor to not working or to working based on the boolean passed in.
     * @param state
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
	public void startFailureAndRepairProcess(int meanTimeBetweenFailures, int meanTimeToRepair) throws UnsupportedOperationException {
        /**
         * This method will start a new thread for the sensor to fail and repair itself.
         * The thread will set the sensors state to not working and then sleep for based on the meanTimeToRepair.
         * After the thread wakes up, it will set the sensor to working and then sleep for the meanTimeBetweenFailures.
         * This will continue until the thread is interrupted, which will happen when the robot is done exploring the maze or
         * when the stopFailureAndRepairProcess method is called.
         */
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!Thread.currentThread().isInterrupted()) {
                    try {
                        setSensorState(false);
                        Thread.sleep(meanTimeToRepair * 1000);
                        setSensorState(true);
                        Thread.sleep(meanTimeBetweenFailures * 1000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        });
        thread.start();
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
        /**
         * Using a loop check that the sensor is operational and if it is, interrupt the thread otherwise wait for the sensor to become operational.
         * If there is no thread running, throw an UnsupportedOperationException.
         */
        while(!getSensorState()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        thread.interrupt();
	}

}