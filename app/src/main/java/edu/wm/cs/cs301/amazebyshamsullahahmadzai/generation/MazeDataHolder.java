package edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation;

/**
 * Static class that holds all the information for the maze;
 * this allows for simple data sharing between activities.
 *
 * @author Shamsullah Ahmadzai
 *
 */
public class MazeDataHolder {
    public static Maze maze;
    public static int[][] dists;
    public static int skill;
    public static int width;
    public static int height;
    public static int startX;
    public static int startY;
    public static int rooms;
    public static String generationAlgorithm;
    public static String driver;
    public static String driver_lvl;
    public static String sensorArr;

    /**
     * Set the distances matrix
     * @param set_distance - the 2d distance array we want to set the class member to
     */
    public static void setDistance(int[][] set_distance){
        dists = set_distance;
    }

    /**
     * Get the distances matrix
     * @return distance -- the distance matrix
     */
    public static int[][] getDistance(){
        return dists;
    }

    /**
     * Get the maze configuration
     * @return maze -- the maze configuration
     */
    public static Maze getMaze(){
        return maze;
    }

    /**
     * Set the maze object
     * @param set_maze - the maze we want to set the class member to
     */
    public static void setMaze(Maze set_maze){
        maze = set_maze;
    }

    /**
     * Set the skill level
     * @param set_skill - the skill level we want to set the class member to
     */
    public static void setSkill(int set_skill){
        skill = set_skill;
    }

    /**
     * Get the skill level
     * @return skill -- the skill level
     */
    public static int getSkill(){
        return skill;
    }

    /**
     * Get the number of rooms
     * @return rooms -- the number of rooms
     */
    public static int getRooms(){
        return Constants.SKILL_ROOMS[skill];
    }

    /**
     * Set the maze width
     * @param set_width -- the width
     */
    public static void setWidth(int set_width){
        width = set_width;
    }

    /**
     * Get the maze width
     * @return width
     */
    public static int getWidth(){
        return width;
    }

    /**
     * Set the maze height
     * @param set_height - the height we want to set the class member to
     */
    public static void setHeight(int set_height){
        height = set_height;
    }

    /**
     * Get the maze height
     * @return height -- the height
     */
    public static int getHeight(){
        return height;
    }

    /**
     * Set the starting X position
     * @param startXSetter -- the x position
     */
    public static void setStartX(int startXSetter){
        startX = startXSetter;
    }

    /**
     * @return startX -- the starting X position
     */
    public static int getStartX(){
        return startX;
    }

    /**
     * @param startYSetter -- the starting Y position
     */
    public static void setStartY(int startYSetter){
        startY = startYSetter;
    }

    /**
     * @return startY -- the starting Y position
     */
    public static int getStartY(){
        return startY;
    }

    /**
     * Set the generation Algorithm
     * @param gen_algo -- the algorithm
     */
    public static void setGenerationAlgorithm(String gen_algo){
        generationAlgorithm = gen_algo;
    }

    /**
     * Return the generation algorithm
     * @return generationAlgorithm
     */
    public static String getGenerationAlgorithm(){
        return generationAlgorithm;
    }

    /**
     * Set the driver type
     * @param driver_input -- the driver type
     */
    public static void setDriver(String driver_input) {
        driver = driver_input;
    }

    /**
     * Get the driver type
     */
    public static String getDriver() {
        return driver;
    }

    /**
     * Set the driver type
     * @param driverlvl_input -- the driver type
     */
    public static void setDriverLvl(String driverlvl_input) {
        driver_lvl = driverlvl_input;
    }

    /**
     * Get the driver type
     */
    public static String getDriverLvl() {
        return driver_lvl;
    }

    public static String getSensorString() {
        switch (driver_lvl) {
            case "Premium":
                sensorArr = "1111";
            case "Mediocre":
                sensorArr = "1001";
            case "Soso":
                sensorArr = "0110";
            case "Shaky":
                sensorArr = "0000";
        }
        return sensorArr;
    }
}
