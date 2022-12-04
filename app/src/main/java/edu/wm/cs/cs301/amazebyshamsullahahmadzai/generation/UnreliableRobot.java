package edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation;

/**
 * CRC Card for UnreliableRobot:
 * Responsibilities:
 *  - Creates a robot object that has at least one unreliable sensor.
 * 
 * Collaborators:
 *  - Robot
 *  - UnReliableSensor
 *  - Sensor
 *  - Control/StatePlaying
 * 
 * @author Shamsullah Ahmadzai
 */
public class UnreliableRobot extends ReliableRobot {

	public UnreliableRobot(StatePlaying statePlaying) {
		super(statePlaying);
	}

	/**
     * This method takes in a boolean array of unreliable sensors and then sets the sensors
     * in the robot object to be unreliable. The order of the sensors in the array is 
     * Forward, Left, Right, Backward.
     * @param unreliableSensors
     */
    public void setUnreliableSensors(boolean[] unreliableSensors, Maze maze) {
        /**
         * If the array is null or the array is not of length 4, throw an IllegalArgumentException.
         * If the array is not null then loop through the array and check that the value at each index
         * is either true or false. If it is true then the sensor at that index is unreliable and if it is
         * false then the sensor at that index is reliable. The order of the booleans in the array is 
         * Forward, Left, Right, Backward which corresponds to the array indices 0, 1, 2, 3, respectively.
         */
        if (unreliableSensors.length != 4) {
            throw new IllegalArgumentException("The array must have 4 elements.");
        }
        for (int i = 0; i < unreliableSensors.length; i++) {
            if (unreliableSensors[i]) {
                if (i == 0) {
                	forward = new UnreliableSensor(maze, Direction.FORWARD);
                    super.addDistanceSensor(forward, Direction.FORWARD);
                } else if (i == 1) {
                	left = new UnreliableSensor(maze, Direction.LEFT);
                    super.addDistanceSensor(left, Direction.LEFT);
                } else if (i == 2) {
                	right = new UnreliableSensor(maze, Direction.RIGHT);
                    super.addDistanceSensor(right, Direction.RIGHT);
                } else if (i == 3) {
                	backward = new UnreliableSensor(maze, Direction.BACKWARD);
                    super.addDistanceSensor(backward, Direction.BACKWARD);
                }
            } else {
            	if (i == 0) {
                	forward = new ReliableSensor(maze, Direction.FORWARD);
                    super.addDistanceSensor(forward, Direction.FORWARD);
                } else if (i == 1) {
                	left = new ReliableSensor(maze, Direction.LEFT);
                    super.addDistanceSensor(left, Direction.LEFT);
                } else if (i == 2) {
                	right = new ReliableSensor(maze, Direction.RIGHT);
                    super.addDistanceSensor(right, Direction.RIGHT);
                } else if (i == 3) {
                	backward = new ReliableSensor(maze, Direction.BACKWARD);
                    super.addDistanceSensor(backward, Direction.BACKWARD);
                }
            }
        }
        System.out.println();
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
	 * @param direction the direction the sensor is mounted on the robot
	 * @param meanTimeBetweenFailures is the mean time in seconds, must be greater than zero
	 * @param meanTimeToRepair is the mean time in seconds, must be greater than zero
	 * @throws UnsupportedOperationException if method not supported
	 */
	@Override
	public void startFailureAndRepairProcess(Direction direction, int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		/* 
         * Check that the meanTimeBetweenFailures and meanTimeToRepair are greater than 0, if not then throw exception.
         * Check that the robot has a sensor at the given direction, if it doesn't then throw the exception.
         * Call the startFailureAndRepairProcess method in the sensor class of the given direction.
		 * and pass in the meanTimeBetweenFailures and meanTimeToRepair.
         */
        if (meanTimeBetweenFailures <= 0 || meanTimeToRepair <= 0) {
            throw new UnsupportedOperationException();
        } else if (direction == Direction.FORWARD) {
			forward.startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
		} else if (direction == Direction.LEFT) {
			left.startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
		} else if (direction == Direction.RIGHT) {
			right.startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
		} else if (direction == Direction.BACKWARD) {
			backward.startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
		} else {
			throw new UnsupportedOperationException();
		}
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
	 * @param direction the direction the sensor is mounted on the robot
	 * @throws UnsupportedOperationException if method not supported
	 */
	@Override
	public void stopFailureAndRepairProcess(Direction direction) throws UnsupportedOperationException {
		/* 
		 * Check that the robot has a sensor at the given direction, if it doesn't then throw the exception.
		 * Call the stopFailureAndRepairProcess method in the sensor class of the given direction.
		 */
		if (direction == Direction.FORWARD) {
			forward.stopFailureAndRepairProcess();
		} else if (direction == Direction.LEFT) {
			left.stopFailureAndRepairProcess();
		} else if (direction == Direction.RIGHT) {
			right.stopFailureAndRepairProcess();
		} else if (direction == Direction.BACKWARD) {
			backward.stopFailureAndRepairProcess();
		} else {
			throw new UnsupportedOperationException();
		}
	}

}