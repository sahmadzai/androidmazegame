package edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation;

import java.util.Arrays;

/**
 * CRC Card for ReliableRobot:
 * Responsibilities:
 *  - Takes in a statePlayingler object that it uses to move the robot
 *  - Adds a distance sensor to different directions or sides of the robot (North, South, East, West)
 *  - Returns the current position of the robot, the current direction it is facing, and the distance to the wall in the direction it is facing
 *  - Tracks and sets the battery level, odometer, and the amount of energy it uses to move, rotate, jump, and sense
 *
 * Collaborators:
 * 1. CardinalDirection
 * 2. statePlaying (statePlayingler)
 * 3. RobotDriver
 * 5. StatePlaying
 *
 *
 * @author Shamsullah Ahmadzai
 *
 */
public class ReliableRobot implements Robot {

    protected StatePlaying statePlaying;
    protected float[] batteryLevel = new float[1];
    protected int odometer;
    protected int energyForQuarterTurn = 3;
    protected int energyForStepForward = 6;
    protected int energyForJump = 40;
    protected int energyForSensing = 1;
    protected boolean stopped;
    protected int x;
    protected int y;
    protected DistanceSensor left;
    protected DistanceSensor right;
    protected DistanceSensor forward;
    protected DistanceSensor backward;

    protected enum Heading {NORTH, EAST, SOUTH, WEST}

    protected Heading currDirection;

    public ReliableRobot(StatePlaying statePlaying) {
        super();
        this.statePlaying = statePlaying;
        this.batteryLevel[0] = 3500;
        this.odometer = 0;
        this.stopped = false;
        this.x = statePlaying.getCurrentPosition()[0];
        this.y = statePlaying.getCurrentPosition()[1];

        left = new ReliableSensor(statePlaying.getMaze(), Direction.LEFT);
        right = new ReliableSensor(statePlaying.getMaze(), Direction.RIGHT);
        forward = new ReliableSensor(statePlaying.getMaze(), Direction.FORWARD);
        backward = new ReliableSensor(statePlaying.getMaze(), Direction.BACKWARD);
        addDistanceSensor(left, Direction.LEFT);
        addDistanceSensor(right, Direction.RIGHT);
        addDistanceSensor(forward, Direction.FORWARD);
        addDistanceSensor(backward, Direction.BACKWARD);
    }

    /**
     * Adds a distance sensor to the robot such that it measures in the given direction.
     * This method is used when a robot is initially configured to get ready for operation.
     * The point of view is that one mounts a sensor on the robot such that the robot
     * can measure distances to obstacles or walls in that particular direction.
     * For example, if one mounts a sensor in the forward direction, the robot can tell
     * with the distance to a wall for its current forward direction, more technically,
     * a method call distanceToObstacle(FORWARD) will return a corresponding distance.
     * So a robot with a left and forward sensor will internally have 2 DistanceSensor
     * objects at its disposal to calculate distances, one for the forward, one for the
     * left direction.
     * A robot can have at most four sensors in total, and at most one for any direction.
     * If a robot already has a sensor for the given mounted direction, adding another
     * sensor will replace/overwrite the current one for that direction with the new one.
     *
     * @param sensor           is the distance sensor to be added
     * @param mountedDirection is the direction that it points to relative to the robot's forward direction
     */
    @Override
    public void addDistanceSensor(DistanceSensor sensor, Direction mountedDirection) {
        /*
         * Given the sensor and a direction mount the sensor to the class object in that direction.
         * We can set the direction of each sensor by using the setSensorDirection and passing in the direction
         */
        if (sensor == null) {
            throw new IllegalArgumentException("Sensor cannot be null");
        }
        if (mountedDirection == null) {
            throw new IllegalArgumentException("Direction cannot be null");
        }
        switch (mountedDirection) {
            case LEFT:
                left = sensor;
                left.setSensorDirection(Direction.LEFT);
                break;
            case RIGHT:
                right = sensor;
                right.setSensorDirection(Direction.RIGHT);
                break;
            case FORWARD:
                forward = sensor;
                forward.setSensorDirection(Direction.FORWARD);
                break;
            case BACKWARD:
                backward = sensor;
                backward.setSensorDirection(Direction.BACKWARD);
                break;
        }
    }

    /**
     * Provides the current position as (x,y) coordinates for
     * the maze as an array of length 2 with [x,y].
     *
     * @return array of length 2, x = array[0], y = array[1]
     * and ({@code 0 <= x < width, 0 <= y < height}) of the maze
     */
    @Override
    public int[] getCurrentPosition() {
        /*
         * Use the statePlaying class's getCurrentPosition() method.
         * With that just return the x,y position as an int array with length 2.
         */
        int[] position = new int[2];
        position[0] = this.x;
        position[1] = this.y;
        return position;
    }

    /**
     * Provides the robot's current direction.
     *
     * @return cardinal direction is the robot's current direction in absolute terms
     */
    @Override
    public CardinalDirection getCurrentDirection() {
        /*
         * statePlaying has a function to get the current direction that is being faced.
         * Using the that we can then return the direction the robot is facing.
         */
        return statePlaying.getCurrentDirection();
    }

    /**
     * Returns the current battery level.
     * The robot has a given battery level (energy level)
     * that it draws energy from during operations.
     * The particular energy consumption is device dependent such that a call
     * for sensor distance2Obstacle may use less energy than a move forward operation.
     * If battery {@code level <= 0} then robot stops to function and hasStopped() is true.
     *
     * @return current battery level, {@code level > 0} if operational.
     */
    @Override
    public float getBatteryLevel() {
        /*
         * Check the value of the battery variable and return it.
         */
        return batteryLevel[0];
    }

    /**
     * Sets the current battery level.
     * The robot has a given battery level (energy level)
     * that it draws energy from during operations.
     * The particular energy consumption is device dependent such that a call
     * for distance2Obstacle may use less energy than a move forward operation.
     * If battery {@code level <= 0} then robot stops to function and hasStopped() is true.
     *
     * @param level - is the current battery level
     * @throws IllegalArgumentException - if level is negative
     */
    @Override
    public void setBatteryLevel(float level) {
        /*
         * Sets the battery variable to to the level param passed in.
         */
        if (level < 0) {
            batteryLevel[0] = Integer.MIN_VALUE;
            throw new IllegalArgumentException("Battery level cannot be negative.");
        }
        this.batteryLevel[0] = level;
    }

    /**
     * Gives the energy consumption for a full 360 degree rotation.
     * Scaling by other degrees approximates the corresponding consumption.
     *
     * @return energy for a full rotation
     */
    @Override
    public float getEnergyForFullRotation() {
        /*
         * Through the constant variables, calculate the energy by multiplying
         * the quarter turn constant by 4 and return the value for a full turn.
         */
        return (float) (energyForQuarterTurn * 4);
    }

    /**
     * Gives the energy consumption for moving forward for a distance of 1 step.
     * For simplicity, we assume that this equals the energy necessary
     * to move 1 step and that for moving a distance of n steps
     * takes n times the energy for a single step.
     *
     * @return energy for a single step forward
     */
    @Override
    public float getEnergyForStepForward() {
        /*
         * Using the constant variables, calculate the energy it would take
         * to make one move forward. Return the value for one step forward.
         */
        return energyForStepForward;
    }

    /**
     * Gets the distance traveled by the robot.
     * The robot has an odometer that calculates the distance the robot has moved.
     * Whenever the robot moves forward, the distance
     * that it moves is added to the odometer counter.
     * The odometer reading gives the path length if its setting is 0 at the start of the game.
     * The counter can be reset to 0 with resetOdometer().
     *
     * @return the distance traveled measured in single-cell steps forward
     */
    @Override
    public int getOdometerReading() {
        /*
         * An odometer variable stores the odometer reading which is returned
         * when the method is called. Maybe include a conditional that resets
         * the odometer if the path is completed/exit has been reached.
         * Return the odometer reading.
         */
        return odometer;
    }

    /**
     * Resets the odometer counter to zero.
     * The robot has an odometer that calculates the distance the robot has moved.
     * Whenever the robot moves forward, the distance
     * that it moves is added to the odometer counter.
     * The odometer reading gives the path length if its setting is 0 at the start of the game.
     */
    @Override
    public void resetOdometer() {
        /*
         * Set the odometer variable back to zero.
         */
        odometer = 0;
    }

    /**
     * Turn robot on the spot for amount of degrees.
     * If robot runs out of energy, it stops,
     * which can be checked by hasStopped() == true and by checking the battery level.
     *
     * @param turn is the direction to turn and relative to current forward direction.
     */
    @Override
    public void rotate(Turn turn) {
        /*
         * Check that the battery level is enough to perform the action and check hasStoppepd() is not true.
         * If that conditional comes out to true then the state should be failure and the fail menu should pop up.
         * Have a switch statement that checks for the three types of rotations: Left, Right, and Around
         * set a current direction variable to the turn direction specified and rotate the robot using the rotate
         * method in the StatePlaying.java class which takes in 1 or -1. After turning decrement the battery level
         * based on the constant variable that holds the battery costs for different turns.
         */
		if (batteryLevel[0] < energyForQuarterTurn) {
			stopped = true;
			System.out.println("--------- BATTERY too LOW for ROTATE ---------" + batteryLevel[0]);
		} if (hasStopped()) {
			System.out.println("Robot has crashed either due to hitting a wall or no energy.");
		} else {
			switch (turn) {
			case LEFT:
				statePlaying.rotate(1);
				batteryLevel[0] -= energyForQuarterTurn;
				break;
			case RIGHT:
				statePlaying.rotate(-1);
				batteryLevel[0] -= energyForQuarterTurn;
				break;
			case AROUND:
				statePlaying.rotate(1);
				statePlaying.rotate(1);
				batteryLevel[0] -= energyForQuarterTurn*2;
				if (batteryLevel[0] < 0) {
					statePlaying.rotate(-1);
					statePlaying.rotate(-1);
				}
				break;
			}
		}
    }

    /**
     * Moves robot forward a given number of steps. A step matches a single cell.
     * If the robot runs out of energy somewhere on its way, it stops,
     * which can be checked by hasStopped() == true and by checking the battery level.
     * If the robot hits an obstacle like a wall, it remains at the position in front
     * of the obstacle and also hasStopped() == true as this is not supposed to happen.
     * This is also helpful to recognize if the robot implementation and the actual maze
     * do not share a consistent view on where walls are and where not.
     *
     * @param distance is the number of cells to move in the robot's current forward direction
     * @throws IllegalArgumentException if distance not positive
     */
    @Override
    public void move(int distance) {
        /*
         * Check that hasStopped() is not true, otherwise end the game and set the state to failure.
         * Have a loop that runs while hasStopped is false and that the distance is not zero.
         * Check that the distance to any obstacle or wall is not 0 which means we are at the obstacle,
         * or if there isnt enough battery left for us to move. If this comes out to true then set a stopped
         * variable to true and restore the battery energy used by sensing the distance to obstacle. Then
         * move one step forward.
         *
         * Otherwise, using a switch statment to check the current direction, set the direction coordinates
         * with respect to the direction the bot is facing. Then move/walk one step forward and decrement the
         * distance and the battery level respectively.
         */
		if (distance < 0) {
			throw new IllegalArgumentException("Distance cannot be negative.");
		}
		if (batteryLevel[0] < energyForStepForward) {
			stopped = true;
			System.out.println("--------- BATTERY too LOW for MOVE ---------");
		}
		if (hasStopped()) {
			System.out.println("Robot has crashed either due to hitting a wall or no energy.");
		} else {
			while(distance > 0 && !hasStopped()) {
				try {
					switch(statePlaying.getCurrentDirection()) {
					case North:
						if (y <= statePlaying.getMaze().getHeight()) {
							y--;
							statePlaying.walk(1);
							break;
						}
						break;
					case South:
						if (y >= 0) {
							y++;
							statePlaying.walk(1);
							break;
						}
						break;
					case East:
						if (x >= 0) {
							x++;
							statePlaying.walk(1);
							break;
						}
						break;
					case West:
						if (x <= statePlaying.getMaze().getWidth()) {
							x--;
							statePlaying.walk(1);
							break;
						}
						break;
				}
					distance--;
					odometer++;
					batteryLevel[0] -= getEnergyForStepForward();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

    }

    /**
     * Makes robot move in a forward direction even if there is a wall
     * in front of it. In this sense, the robot jumps over the wall
     * if necessary. The distance is always 1 step and the direction
     * is always forward.
     * If the robot runs out of energy somewhere on its way, it stops,
     * which can be checked by hasStopped() == true and by checking the battery level.
     * If the robot tries to jump over an exterior wall and
     * would land outside of the maze that way,
     * it remains at its current location and direction,
     * hasStopped() == true as this is not supposed to happen.
     */
    @Override
    public void jump() {
        /*
         * Similar to the move method but only moving 1 step, it also has a conditional prior to movement
         * that will check if the wall in the direction the robot facing is not a border wall. If it is a
         * border wall then go back to the previous location and direction before the jump and make sure
         * battery level doesn't change.
         */
		if (batteryLevel[0] < energyForJump) {
			stopped = true;
			System.out.println("--------- BATTERY too LOW for JUMP ---------");
		}
		if (hasStopped()) {
			System.out.println("Robot has crashed either due to hitting a wall or no energy.");
		} else {
			System.out.println("Now jumping");
			try {
				switch(statePlaying.getCurrentDirection()) {
				case North:
					if (y <= statePlaying.getMaze().getHeight()) {
						y--;
						statePlaying.walk(1);
						odometer++;
						break;
					}
					break;
				case South:
					if (y >= 0) {
						y++;
						statePlaying.walk(1);
						odometer++;
						break;
					}
					break;
				case East:
					if (x >= 0) {
						x++;
						statePlaying.walk(1);
						odometer++;
						break;
					}
					break;
				case West:
					if (x <= statePlaying.getMaze().getWidth()) {
						x--;
						statePlaying.walk(1);
						odometer++;
						break;
					}
					break;
			}
				batteryLevel[0] -= energyForJump;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    }

    /**
     * Tells if the current position is right at the exit but still inside the maze.
     * The exit can be in any direction. It is not guaranteed that
     * the robot is facing the exit in a forward direction.
     *
     * @return true if robot is at the exit, false otherwise
     */
    @Override
    public boolean isAtExit() {
        /*
         * Compare the current x,y coordinates of the robot to the exit position in
         * the maze, if they are equal then return true, otherwise return false.
         */
        return Arrays.equals(statePlaying.getCurrentPosition(), statePlaying.getMaze().getExitPosition());
    }

    /**
     * Tells if current position is inside a room.
     *
     * @return true if robot is inside a room, false otherwise
     */
    @Override
    public boolean isInsideRoom() {
        /*
         * Pass the current x,y coordinates of the robot to the maze's isInRoom()
         * method, return the results of the method call which will be true or false.
         */
		return statePlaying.getMaze().isInRoom(statePlaying.getCurrentPosition()[0], statePlaying.getCurrentPosition()[1]);
    }

    /**
     * Tells if the robot has stopped for reasons like lack of energy,
     * hitting an obstacle, etc.
     * Once a robot is has stopped, it does not rotate or
     * move anymore.
     *
     * @return true if the robot has stopped, false otherwise
     */
    @Override
    public boolean hasStopped() {
        /*
         * Check if the battery level is not less than or equal to 0 or that a stop
         * variable is not equal to false. This stop variable can be changed in any function.
         * Return the result of that conditional.
         */
        return batteryLevel[0] <= 0 || stopped;
    }

    /**
     * NOT USED FOR RELIABLE ROBOT
     * This method takes in a boolean array of unreliable sensors and then sets the sensors
     * in the robot object to be unreliable. The order of the sensors in the array is
     * Forward, Left, Right, Backward.
     *
     * @param unreliableSensors - array of unreliable sensors
     */
    public void setUnreliableSensors(boolean[] unreliableSensors) {
        /*
         * If the array is not null then loop through the array and check that the value at each index
         * is either true or false. If it is true then the sensor at that index is unreliable and if it is
         * false then the sensor at that index is reliable. The order of the booleans in the array is
         * Forward, Left, Right, Backward which corresponds to the array indices 0, 1, 2, 3, respectively.
         */
//		System.out.println("ReliableRobot - Sensors are now being set, the array is: ");
//		if (unreliableSensors.length != 4) {
//			throw new IllegalArgumentException("The array must have 4 elements.");
//		}
//		for (int i = 0; i < unreliableSensors.length; i++) {
//			System.out.print(unreliableSensors[i] + ", ");
//			if (unreliableSensors[i]) {
//				if (i == 0) {
//					forward = new UnreliableSensor(statePlaying.getMaze(), Direction.FORWARD);
//					addDistanceSensor(forward, Direction.FORWARD);
//				} else if (i == 1) {
//					left = new UnreliableSensor(statePlaying.getMaze(), Direction.LEFT);
//					addDistanceSensor(left, Direction.LEFT);
//				} else if (i == 2) {
//					right = new UnreliableSensor(statePlaying.getMaze(), Direction.RIGHT);
//					addDistanceSensor(right, Direction.RIGHT);
//				} else if (i == 3) {
//					backward = new UnreliableSensor(statePlaying.getMaze(), Direction.BACKWARD);
//					addDistanceSensor(backward, Direction.BACKWARD);
//				}
//			}
//		}
//		System.out.println();
    }

    /**
     * Tells the distance to an obstacle (a wall)
     * in the given direction.
     * The direction is relative to the robot's current forward direction.
     * Distance is measured in the number of cells towards that obstacle,
     * e.g. 0 if the current cell has a wallboard in this direction,
     * 1 if it is one step forward before directly facing a wallboard,
     * Integer.MaxValue if one looks through the exit into eternity.
     * The robot uses its internal DistanceSensor objects for this and
     * delegates the computation to the DistanceSensor which need
     * to be installed by calling the addDistanceSensor() when configuring
     * the robot.
     *
     * @param direction specifies the direction of interest
     * @return number of steps towards obstacle if obstacle is visible
     * in a straight line of sight, Integer.MAX_VALUE otherwise
     * @throws UnsupportedOperationException if robot has no sensor in this direction
     *                                       or the sensor exists but is currently not operational
     */
    @Override
    public int distanceToObstacle(Direction direction) throws UnsupportedOperationException {
        /*
         * Check that a sensor is present on the robot in given direction or that the battery
         * level is less than one, If comes out to true then set state to failed and throw exception.
         * Otherwise, set distance to 0 and based on the direction, using a switch statement
         * call a helper function that checks each direction through the maze. These helper
         * functions will return a distance value and then break the switch.
         * Decrement the battery level by the sensing energy constant and then return the
         * distance value.
         */
        CardinalDirection currentDir = getCurrentDirection();
        if (batteryLevel[0] < energyForSensing) {
            System.out.println("Not enough energy to sense. Battery level: " + batteryLevel[0]);
            throw new UnsupportedOperationException();
        }
        int distance = 0;
        try {
            switch (direction) {
                case FORWARD:
                    distance = forward.distanceToObstacle(getCurrentPosition(), getSensorDirection(currentDir, direction), batteryLevel);
                    break;
                case BACKWARD:
                    distance = backward.distanceToObstacle(getCurrentPosition(), getSensorDirection(currentDir, direction), batteryLevel);
                    break;
                case LEFT:
                    distance = left.distanceToObstacle(getCurrentPosition(), getSensorDirection(currentDir, direction), batteryLevel);
                    break;
                case RIGHT:
                    distance = right.distanceToObstacle(getCurrentPosition(), getSensorDirection(currentDir, direction), batteryLevel);
                    break;
            }
        } catch (Exception e) {
            throw new UnsupportedOperationException();
        }
        batteryLevel[0] -= energyForSensing;
        return distance;
    }

    public CardinalDirection getSensorDirection(CardinalDirection currentDir, Direction sensorDir) {
        switch (currentDir) {
            case North:
                if (sensorDir == Direction.LEFT)
                    return CardinalDirection.East;
                else if (sensorDir == Direction.RIGHT)
                    return CardinalDirection.West;
                else if (sensorDir == Direction.BACKWARD)
                    return CardinalDirection.South;
                else
                    return CardinalDirection.North;
            case South:
                if (sensorDir == Direction.LEFT)
                    return CardinalDirection.West;
                else if (sensorDir == Direction.RIGHT)
                    return CardinalDirection.East;
                else if (sensorDir == Direction.BACKWARD)
                    return CardinalDirection.North;
                else
                    return CardinalDirection.South;
            case East:
                if (sensorDir == Direction.LEFT)
                    return CardinalDirection.South;
                else if (sensorDir == Direction.RIGHT)
                    return CardinalDirection.North;
                else if (sensorDir == Direction.BACKWARD)
                    return CardinalDirection.West;
                else
                    return CardinalDirection.East;
            case West:
                if (sensorDir == Direction.LEFT)
                    return CardinalDirection.North;
                else if (sensorDir == Direction.RIGHT)
                    return CardinalDirection.South;
                else if (sensorDir == Direction.BACKWARD)
                    return CardinalDirection.East;
                else
                    return CardinalDirection.West;
            default:
                return currentDir;
        }
    }

    /**
     * Tells if a sensor can identify the exit in the given direction relative to
     * the robot's current forward direction from the current position.
     * It is a convenience method is based on the distanceToObstacle() method and transforms
     * its result into a boolean indicator.
     *
     * @param direction is the direction of the sensor
     * @return true if the exit of the maze is visible in a straight line of sight
     * @throws UnsupportedOperationException if robot has no sensor in this direction
     *                                       or the sensor exists but is currently not operational
     */
    @Override
    public boolean canSeeThroughTheExitIntoEternity(Direction direction) throws UnsupportedOperationException {
        /*
         * Check that the robot has a sensor at the given direction, if it doesn't then throw the exception.
         * Check that the robot's x,y coordinates are not equal to the exit coordinates, if they are not equal then return false.
         * Otherwise check that the robot's coordinates are equal to the exit coordinates, if they are then check that the
         * distance in the given direction is equal to a large integer value (Max value), return the result (true or false)
         * If no if statements run then just return false.
         */
		if (!Arrays.equals(statePlaying.getCurrentPosition(), statePlaying.getMaze().getExitPosition())) {
			return false;
		}
		if (Arrays.equals(statePlaying.getCurrentPosition(), statePlaying.getMaze().getExitPosition())) {
            return distanceToObstacle(direction) == Integer.MAX_VALUE;
		}
        return false;
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
     * <p>
     * This an optional operation. If not implemented, the method
     * throws an UnsupportedOperationException.
     *
     * @param direction               the direction the sensor is mounted on the robot
     * @param meanTimeBetweenFailures is the mean time in seconds, must be greater than zero
     * @param meanTimeToRepair        is the mean time in seconds, must be greater than zero
     * @throws UnsupportedOperationException if method not supported
     */
    @Override
    public void startFailureAndRepairProcess(Direction direction, int meanTimeBetweenFailures, int meanTimeToRepair)
            throws UnsupportedOperationException {
        /* For P3 and the ReliableSensor class,
         * it is sufficient to just throw the UnsupportedOperationException.
         */
        throw new UnsupportedOperationException();
    }

    /**
     * This method stops a failure and repair process and
     * leaves the sensor in an operational state.
     * <p>
     * It is complementary to starting a
     * failure and repair process.
     * <p>
     * Intended use: If called after starting a process, this method
     * will stop the process as soon as the sensor is operational.
     * <p>
     * If called with no running failure and repair process,
     * the method will return an UnsupportedOperationException.
     * <p>
     * This an optional operation. If not implemented, the method
     * throws an UnsupportedOperationException.
     *
     * @param direction the direction the sensor is mounted on the robot
     * @throws UnsupportedOperationException if method not supported
     */
    @Override
    public void stopFailureAndRepairProcess(Direction direction) throws UnsupportedOperationException {
        /* For P3 and the ReliableSensor class,
         * it is sufficient to just throw the UnsupportedOperationException.
         */
        throw new UnsupportedOperationException();
    }
}