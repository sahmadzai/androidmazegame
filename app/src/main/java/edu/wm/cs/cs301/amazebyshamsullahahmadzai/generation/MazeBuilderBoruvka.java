package edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;


public class MazeBuilderBoruvka extends MazeBuilder implements Runnable {
	
	private static final Logger LOGGER = Logger.getLogger(MazeBuilderPrim.class.getName());
    private int [][] nodeParents;
    
	public MazeBuilderBoruvka() {
		super();
		LOGGER.config("Using Brouvka's algorithm to generate maze.");
	}

	boolean keepLooping(ArrayList<Node>[] forest) {
    	//if
    	int counter = 0;
    	for(int i = 0; i<forest.length;i++) {
    		if(counter > 1) {
    			return true;
    		}
    		if(!forest[i].isEmpty()) {
    			counter++;
    		}
    	}
    	return false;
    }
    /*
     * This method performs Boruvka’s algorithm in that it deletes internal wallboards from the existing maze to generate a minimal spanning tree. 
     * Note the method call for generatePathways() within the run() method of the superclass MazeBuilder provides the context in which your method is called.
     * So for example rooms are generated before Boruvka’s algorithm is applied. The generatePathways() method takes advantage of the getEdgeWeight() method if it needs to select an edge according to its associated weight or cost. 
     * Note that you can test getEdgeWeight independently of the generatePathways() method.
     */
    @Override
    public void generatePathways() {
        /*
         * Create an array of ArrayLists that hold Node objects of each cell in the maze.
         * The array is indexed by the root of the tree that the cell belongs to.
         */
        ArrayList<Node> [] forest = initForest();
        nodeParents = initParents();
        // Print out nodeParents
        for (int i = 0; i < nodeParents.length; i++) {
            for (int j = 0; j < nodeParents[i].length; j++) {
                System.out.print(nodeParents[i][j] + " ");
            }
            System.out.println();
        }
        
        // Print every tree's coordinates in the forest
        for (int i = 0; i < forest.length; i++) {
            System.out.println("Tree " + i + ":");
            for (int j = 0; j < forest[i].size(); j++) {
                System.out.println(forest[i].get(j).x + ", " + forest[i].get(j).y);
            }
        }

        /*
         * While there is more than one tree in the forest, check the four directions for an edge between two trees.
         * Make sure that the edge is a wall that can be torn down. If it is, tear it down and add the two Nodes to the same tree.
         */
        int emptyTrees = 0;
        while (emptyTrees < (width*height-1)) {
            
            // Iterate through each tree in the forest and get the root of the tree
            for (int i = 0; i < forest.length; i++) {
            	if (!forest[i].isEmpty()) {
                    // For each node in the tree
                    for (int j = 0; j < forest[i].size(); j++) {
                        Node root = forest[i].get(0);
                        int minEdgeWeight = Integer.MAX_VALUE;
                        CardinalDirection minEdgeDirection = null;
                        
                        // Check the four directions for an edge between two trees
                        for (CardinalDirection dir : CardinalDirection.values()) {
                            // Create a new wallboard object and check that if it is part of the border
                            Wallboard wallboard = new Wallboard(root.x, root.y, dir);
                            if (floorplan.canTearDown(wallboard)) {
                                // Get the edge weight in that direction and check if it is the minimum edge weight
                                    int edgeWeight = getEdgeWeight(root.x, root.y, dir);
                                    if (edgeWeight < minEdgeWeight) {
                                        minEdgeWeight = edgeWeight;
                                        minEdgeDirection = dir;
                                    } else {
                                        continue;
                                    }
                            } else {
                            	continue;
                            }
                        }
    
                        // Tear down the wallboard with the minimum edge weight
                    	tearDownWallboard(root.x, root.y, minEdgeDirection);
                    	// Add the node in that direction to the same tree using a union operation
                        union(forest, getIndexofNodes(root.x, root.y, minEdgeDirection)[0], getIndexofNodes(root.x, root.y, minEdgeDirection)[1]);
                        // Print the minimum edge weight and the tree that it belongs to
                        System.out.println("Minimum edge weight: " + minEdgeWeight + " for tree " + i);
                    } 
                } else
                	emptyTrees++;
                    continue;
            }
            System.out.println("Forest length is now: " + forest.length);
        }
            
        // Print every tree's coordinates in the forest
        for (int i = 0; i < forest.length; i++) {
        	System.out.println("Tree " + i + ":");
        	for (int j = 0; j < forest[i].size(); j++) {
        		System.out.println(forest[i].get(j).x + ", " + forest[i].get(j).y);
        	}
        }
        
    }

    /*
     * This method initializes the forest of trees.
     * Each tree is represented by an ArrayList of Node objects.
     * The forest is represented by an array of ArrayLists of Node objects.
     * The array is indexed by the root of the tree.
     */
    private ArrayList<Node> [] initForest() {
        ArrayList<Node> [] forest = new ArrayList[width*height];
        for (int i = 0; i < width*height; i++) {
            forest[i] = new ArrayList<Node>();
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Node node = new Node(i, j);
                forest[i*height+j].add(node);
            }
        }
        return forest;
    }

    /*
     * This method initializes the parents of each node.
     * The parents are represented by a 2D array of integers.
     * The array is indexed by the x and y coordinates of the node and the value is the index of the arraylist that the node belongs to.
     */
    private int [][] initParents() {
        int [][] nodeParents = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                nodeParents[i][j] = i*height+j;
            }
        }
        return nodeParents;
    }

    /*
     * This method updates the 2D array of parents.
     * It takes in the x and y coordinates of the node and the root of the tree that the node belongs to.
     */
    private void updateParents(int xVar, int yVar, int root) {
        nodeParents[xVar][yVar] = root;
    }

    /*
     * This method returns the index of the arraylist that the node belongs to and the index of the cell that is in the direction of the wallboard.
     * It takes in the x and y coordinates of the node.
     */
    private int [] getIndexofNodes(int x, int y, CardinalDirection dir) {
        int [] index = new int[2];
        switch (dir) {
            case North:
                index[0] = x*height+y-1;
                index[1] = x*height+y;
                break;
            case East:
                index[0] = x*height+y;
                index[1] = (x+1)*height+y;
                break;
            case South:
                index[0] = x*height+y;
                index[1] = x*height+y+1;
                break;
            case West:
                index[0] = (x-1)*height+y;
                index[1] = x*height+y;
                break;
        }
        return index;
    }

    /*
     * This method returns true if the wallboard is pointing to itself by checking the index using the 2D array of parents.
     * It takes in the x and y coordinates of the node and the direction of the wallboard.
     */
    private boolean isNotPointingToSelf(int x, int y, CardinalDirection dir) {
        switch (dir) {
            case North:
                if (nodeParents[x][y] == nodeParents[x][y-1])
                    return false;
                break;
            case East:
                if (nodeParents[x][y] == nodeParents[x+1][y])
                    return false;
                break;
            case South:
                if (nodeParents[x][y] == nodeParents[x][y+1])
                    return false;
                break;
            case West:
                if (nodeParents[x][y] == nodeParents[x-1][y])
                    return false;
                break;
        }
        return true;
    }

    /*
     * This method generates a key to a hashmap that represents the edge between two cells.
     * The key is a string that is the concatenation of the coordinates of the two cells and the direction of the edge.
     * The key is used to access the edge weight in the hashmap.
     */
    private String generateKey(int x, int y, CardinalDirection dir) {
        String key = "";
        if (dir == CardinalDirection.North) {
            key = x + "" + y + "" + "N";
        } else if (dir == CardinalDirection.South) {
            key = x + "" + (y-1) + "N";
        } else if (dir == CardinalDirection.East) {
            key = x + "" + y + "E";
        } else if (dir == CardinalDirection.West) {
            key = (x-1) + "" + y + "E";
        }
        return key;
    }

    /*
     * This method generates an edge weight between two cells.
     * The edge weight is a random integer between 0 and height*width*4 which is then stored in an ArrayList for unique edge weights.
     */
    private int generateEdgeWeight(int x, int y, CardinalDirection dir) {
        ArrayList<Integer> edgeWeights = new ArrayList<Integer>();
        int edgeWeight = (int) (Math.random() * (width*height*4));
        if (edgeWeights.contains(edgeWeight)) {
            edgeWeight = (int) (Math.random() * (width*height*4));
        } else {
            edgeWeights.add(edgeWeight);
        }
        return edgeWeight;      
    }

    /*
     * This method returns the edge weight of the edge between two cells.
     * The edge weight is the distance between the two cells.
     * The edge weight is stored in a hashmap.
     */
    private int getEdgeWeight(int x, int y, CardinalDirection dir) {
        // Initialize the hashmap 
        HashMap<String, Integer> edgeWeights = new HashMap<>();
        String key = generateKey(x, y, dir);

        edgeWeights.put(key, generateEdgeWeight(x, y, dir));
    
        return edgeWeights.get(key);    
    }

    /*
     * This method tears down the wallboard between the cell at (x,y) and the cell in the direction dir (North, South, West, East).
     */
    private void tearDownWallboard(int x, int y, CardinalDirection dir) {
    	if (isNotPointingToSelf(x, y, dir)) {
            Wallboard wallboard = new Wallboard(x, y, dir);
            floorplan.deleteWallboard(wallboard);
    	}
    }

    /*
     * This methods adds the nodes at the passed in indexes to the same tree.
     * This moves all the nodes in the tree at index2 to the tree at index1.
     */
    private void union(ArrayList<Node> [] forest, int index1, int index2) {
    	System.out.println("Index is: " + index1 + ", " + index2);
        for (int i = 0; i < forest[index2].size(); i++) {
            forest[index1].add(forest[index2].get(i));
            updateParents(forest[index2].get(i).x, forest[index2].get(i).y, index1);
        }        
        forest[index2] = new ArrayList<Node>();
    }

    /*
     * This class represents a cell in the maze and has references to its coodinates and its parent.
     */
    private class Node {
        protected int x;
        protected int y;
        
        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    
}