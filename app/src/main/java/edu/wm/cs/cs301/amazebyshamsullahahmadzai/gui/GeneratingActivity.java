package edu.wm.cs.cs301.amazebyshamsullahahmadzai.gui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import edu.wm.cs.cs301.amazebyshamsullahahmadzai.R;
import edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation.DefaultOrder;
import edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation.Maze;
import edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation.MazeDataHolder;
import edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation.MazeFactory;
import edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation.MazeFileReader;
import edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation.Order;

/**
 * This activity represents the second screen of the app, where the maze gets generated and a driver
 * algorithm and sensor reliability is chosen.
 * 
 * A progress bar shows how much of the maze has been generated. While it's generating, the user can
 * select the robot driver and robot reliability they want to use. When the progress bar reaches 100%,
 * the activity will check that the user has selected a driver and a reliability before starting the
 * respective playing activity. If not, it will show a snack-bar prompting the user to select a driver and a reliability
 * level.
 * 
 * @author Shamsullah Ahmadzai
 *
 */

public class GeneratingActivity extends AppCompatActivity {

    /**
     * Class variables that are used to store the different values that are selected by the user
     * and some other constant values.
     */
    private String  driveMode;
    private String  robotReliability;
    private Boolean driveModeSelected = false;
    private Boolean robotReliabilitySelected = false;
    private Boolean mazeGenComplete = false;

    protected static Maze maze;
    protected static MazeFactory mazeFactory;
    protected static Order.Builder builder;
    DefaultOrder order;
    private MazeDataHolder mazeData;
    private MazeFileReader reader;
    private MediaPlayer mp;
    private MediaPlayer touchmp;

    private final int LENGTH_SHORT = 800;
    private final String LOG_TAG = "GeneratingActivity";
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generating_layout);

        /*
         * Set up the media player objects for touches and for the background music
         */
        mp = MediaPlayer.create(this, R.raw.generating);
        mp.setLooping(true);
        mp.start();

        touchmp = MediaPlayer.create(this, R.raw.touch);

        /*
         * Get the sharedPref file and save the information from AMazeActivity into variables
         * Log the items from sharedPref to confirm they were saved properly.
         */
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences("edu.wm.cs301.amazebyshamsullahahmadzai.preferences", Context.MODE_PRIVATE);
        int skill_lvl = sharedPref.getInt("skill_level", -1);
        String gen_method = sharedPref.getString("gen_method", null);
        Boolean gen_rooms = sharedPref.getBoolean("gen_rooms", false);
        int seed = sharedPref.getInt("seed", -1);

        Log.v(LOG_TAG, "The difficulty level is: " + sharedPref.getInt("skill_level", -1));
        Log.v(LOG_TAG, "The maze gen method is: " + sharedPref.getString("gen_method", null));
        Log.v(LOG_TAG, "Will rooms be generated? " + sharedPref.getBoolean("gen_rooms", false));
        Log.v(LOG_TAG, "The maze seed is: " + sharedPref.getInt("seed", -1));

        // Initialize and start listeners for the drive method & skill spinners
        initSpinners();

        // generate the maze
        generateMaze(skill_lvl, gen_method, gen_rooms, seed);
    }

    private void generateMaze(int skill_lvl, String gen_method, Boolean rooms, int seed) {
        ProgressBar genProgress = findViewById(R.id.gen_progress);

        /*
         * Set the builder based on the generation method user chose.
         */
        switch (gen_method) {
            case "Prim":
                builder = Order.Builder.Prim;
            case "Boruvka":
                builder = Order.Builder.Boruvka;
            default:
                builder = Order.Builder.DFS;
        }

        /*
         * Create a background thread that sends the maze order and generates it.
         * This thread also wait for maze to be delivered and saves the maze in
         * a separate class/file called MazeDataHolder.
         */
        Thread gen_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mazeFactory = new MazeFactory();
                order = new DefaultOrder(skill_lvl, builder, rooms, seed);
                mazeFactory.order(order);
                sendMazeToDataHolder(skill_lvl);
            }

            private void sendMazeToDataHolder(int skill_lvl) {
                mazeFactory.waitTillDelivered();
                order.deliver(order.getMaze());
                maze = order.getMaze();

                MazeDataHolder.setMaze(maze);
                MazeDataHolder.setHeight(maze.getHeight());
                MazeDataHolder.setWidth(maze.getWidth());
                MazeDataHolder.setDistance(maze.getMazedists().getAllDistanceValues());
                MazeDataHolder.setStartX(maze.getStartingPosition()[0]);
                MazeDataHolder.setStartY(maze.getStartingPosition()[1]);
                MazeDataHolder.setSkill(skill_lvl);
                MazeDataHolder.setGenerationAlgorithm(String.valueOf(builder));
            }
        });

        /*
         * This new thread handles the progress bar update. By having this in a
         * separate thread, the UI does not lag or become unresponsive and we get a
         * smooth progress bar.
         */
        thread = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Update progress bar to the value returned by order.getProgress()
            while (order.getProgress() <= 100) {
                genProgress.setProgress(order.getProgress());
                if (order.getProgress() == 100) {break;}
            }

            mazeGenComplete = true;
            // Check if user has selected choices for method and reliability and move accordingly
            hasUserSelected(Thread.currentThread());

        });
        gen_thread.start();
        thread.start();
    }

    /**
     * This method overrides the default back button behavior to take the user 
     * back to the title screen instead of the previous activity.
     */
    @Override
    public void onBackPressed() {
        touchmp.start();
        Intent intent = new Intent(GeneratingActivity.this, AMazeActivity.class);           // Create an intent to start the title screen
        thread.interrupt();
        mp.stop();
        startActivity(intent);                                                                           // Start the title screen activity
    }

    /**
     * Method that sets up the spinners and starts the listeners for them.
     * The listeners update the drive mode and robot reliability variables when the user changes the
     * value of the spinners.
     */
    private void initSpinners() {
        /*
         * Initialize variables and set them to the spinners from the XML file.
         * Then by using an array adapter we can add in an array into the spinners
         * while also setting the spinner layout, style, and dropdown layout.
         */
        Spinner dmethodSpinner = findViewById(R.id.dmethod_spinner);
        Spinner dskillSpinner  = findViewById(R.id.dskill_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.dmethods, R.layout.custom_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Setting the ArrayAdapter data on the Driver Method Spinner
        dmethodSpinner.setAdapter(adapter);
        dmethodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                touchmp.start();

                // If the default blank choice is selected ignore it
                if (i == 0)
                    driveModeSelected = false;
                else {

                    // If the first choice, which is Manual, is selected set driveModeSelected to true to move on.
                    if (i == 1) {
                        driveMode = adapterView.getItemAtPosition(i).toString();
                        driveModeSelected = true;
                        MazeDataHolder.setDriver(driveMode);
                        Log.v(LOG_TAG, "User selected driving method: " + driveMode);
                    } else {
                        String text = adapterView.getItemAtPosition(i).toString();
                        driveMode = text;
                        MazeDataHolder.setDriver(driveMode);
                        Log.v(LOG_TAG, "User selected driving method: " + text);
                    }

                    // Check if maze generation is complete if so show a snack bar to the user
                    if (mazeGenComplete)
                        // Create a snack-bar to tell the user that the game will start shortly
                        Snackbar.make(view, "The game will start shortly.", Snackbar.LENGTH_SHORT).setDuration(LENGTH_SHORT).show();
                    else
                        // Create a snack-bar to tell the user a driver method and type must be selected
                        Snackbar.make(view, "A driver type needs to be selected in order to play.", Snackbar.LENGTH_SHORT).setDuration(LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                driveModeSelected = false;
            }

        });

        // Setting the ArrayAdapter data on the Driver Skill Spinner
        adapter = ArrayAdapter.createFromResource(this, R.array.dskills, R.layout.custom_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dskillSpinner.setAdapter(adapter);
        dskillSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)  {
                touchmp.start();
                if (i == 0)
                    robotReliabilitySelected = false;
                else {
                    String text = adapterView.getItemAtPosition(i).toString();
                    robotReliability = text;
                    robotReliabilitySelected = true;
                    driveModeSelected = true;
                    MazeDataHolder.setDriverLvl(robotReliability);
                    Log.v(LOG_TAG, "User selected skill level: " + text);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                robotReliabilitySelected = false;
            }

        });
    }

    /**
     * Method that checks if the user has selected a drive mode and robot reliability. If the user
     * has selected both the method will start the play activity. If the user has not selected both
     * the method will create a snack-bar to tell the user to select both.
     * @param thread The thread that called this method
     */
    private void hasUserSelected(Thread thread) {

        if (driveModeSelected || robotReliabilitySelected) {                                                 // If the user has selected both
            Intent intent;        

            if (driveMode.equals("Manual"))                                                                  // If the user selected manual
                intent = new Intent(GeneratingActivity.this, PlayManuallyActivity.class);       // Create an intent to start the play manually activity
            else
                intent = new Intent(GeneratingActivity.this, PlayAnimationActivity.class);      // Create an intent to start the play animation activity
            mp.stop();
            thread.interrupt();                                                                              // Interrupt the generation thread now that the maze is generated
            startActivity(intent);                                                                           // Start the play activity
        } else {
            Log.v(LOG_TAG, "User needs to select an option for driving method and skill.");             // Log that the user needs to select both
            
            try {
                thread.sleep(4000);                                                                    // Sleep the thread for 4 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            hasUserSelected(thread);                                                                         // Call the method again to check if the user has selected both
        }
    }

}
