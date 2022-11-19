package edu.wm.cs.cs301.amazebyshamsullahahmadzai.gui;

import android.content.Intent;
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

/**
 * This activity represents the second screen of the app, where the maze gets generated and a driver
 * algorithm and sensor reliability is chosen.
 * 
 * A progress bar shows how much of the maze has been generated. While it's generating, the user can
 * select the robot driver and robot reliability they want to use. When the progress bar reaches 100%,
 * the activity will check that the user has selected a driver and a reliability before starting the
 * respective playing activity. If not, it will show a snackbar prompting the user to select a driver and a reliability
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
    private final int LENGTH_SHORT = 800;
    private final String LOG_TAG = "GeneratingActivity";
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generating_layout);

        Intent intent = getIntent();                                                        // Get the intent that started this activity
        Bundle data = intent.getExtras();                                                   // Get the data that was sent with the intent
        Log.v(LOG_TAG, "The difficulty level is: " + data.getInt("skill_level", 0));        // Log the difficulty level
        Log.v(LOG_TAG, "The maze gen method is: " + data.getString("gen_method"));          // Log the maze gen method
        Log.v(LOG_TAG, "Will rooms be generated? " + data.getBoolean("gen_rooms"));         // Log whether rooms will be generated
        Log.v(LOG_TAG, "The maze seed is: " + data.getInt("seed"));                         // Log the maze seed

        // Initialize and start listeners for the drive method & skill spinners
        initSpinners();

        // Start the maze generation thread and update the progress bar
        updateProgressBar();
    }

    /**
     * This method overrides the default back button behavior to take the user 
     * back to the title screen instead of the previous activity.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GeneratingActivity.this, AMazeActivity.class);           // Create an intent to start the title screen
        thread.interrupt();                                                                 // Interrupt the thread to stop the maze generation
        startActivity(intent);                                                              // Start the title screen activity
    }

    /**
     * Method that sets up the spinners and starts the listeners for them.
     * The listeners update the drive mode and robot reliability variables when the user changes the
     * value of the spinners.
     */
    private void initSpinners() {
        Spinner dmethodSpinner = findViewById(R.id.dmethod_spinner);                                                            // Get the drive method spinner
        Spinner dskillSpinner  = findViewById(R.id.dskill_spinner);                                                             // Get the robot reliability spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.dmethods, R.layout.custom_spinner);  // Create an adapter for the drive method spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);                                         // Set the adapter for the drive method spinner   

        // Setting the ArrayAdapter data on the Driver Method Spinner
        dmethodSpinner.setAdapter(adapter);
        dmethodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0)                                                                 // If the user selects the blank option do nothing              
                    driveModeSelected = false;
                else {
                    String text = adapterView.getItemAtPosition(i).toString();              // Get the text of the selected item
                    driveMode = text;                                                       // Set the drive mode variable to the selected item
                    driveModeSelected = true;                                               // Set the drive mode selected variable to true
                    Log.v(LOG_TAG, "User selected driving method: " + text);                // Log the selected driving method
                    
                    if (mazeGenComplete)                                                    // If the maze generation is complete
                        // Create a snackbar to tell the user that the game will start shortly
                        Snackbar.make(view, "The game will start shortly.", Snackbar.LENGTH_SHORT).setDuration(LENGTH_SHORT).show();
                    else
                        // Create a snackbar to tell the user a driver method and type must be selected
                        Snackbar.make(view, "A driver type needs to be selected in order to play.", Snackbar.LENGTH_SHORT).setDuration(LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                driveModeSelected = false;
            }

        });

        // Setting the ArrayAdapter data on the Driver Skill Spinner
        adapter = ArrayAdapter.createFromResource(this, R.array.dskills, R.layout.custom_spinner);                 // Create an adapter for the robot reliability spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);                            // Set the dropdown view for the robot reliability spinner
        dskillSpinner.setAdapter(adapter);                                                                         // Set the adapter for the robot reliability spinner

        dskillSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)  {
                if (i == 0)
                    robotReliabilitySelected = false;
                else {
                    String text = adapterView.getItemAtPosition(i).toString();
                    robotReliability = text;
                    robotReliabilitySelected = true;
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
     * Method that updates the progress bar by using a thread with a while loop that runs until the
     * progress bar is full. The thread sleeps for 50 milliseconds after each update to the progress
     * bar. On completion of the loop the thread checks if the user has selected a drive mode and
     * robot reliability.
     */
    private void updateProgressBar() {
        ProgressBar genProgress = findViewById(R.id.gen_progress);
        thread = new Thread(() -> {
            try {
                int i = 1;
                while(genProgress.getProgress() < 100) {
                    genProgress.setProgress(i);
                    i++;
                    Thread.sleep(50);
                }
                mazeGenComplete = true;
                // Check if user has selected choices for method and reliability and move accordingly
                hasUserSelected(Thread.currentThread());

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    /**
     * Method that checks if the user has selected a drive mode and robot reliability. If the user
     * has selected both the method will start the play activity. If the user has not selected both
     * the method will create a snackbar to tell the user to select both.
     * @param thread The thread that called this method
     */
    private void hasUserSelected(Thread thread) {

        if (driveModeSelected && robotReliabilitySelected) {                                    // If the user has selected both
            Intent intent;        

            if (driveMode.equals("Manual"))                                                     // If the user selected manual
                intent = new Intent(GeneratingActivity.this, PlayManuallyActivity.class);       // Create an intent to start the play manually activity
            else
                intent = new Intent(GeneratingActivity.this, PlayAnimationActivity.class);      // Create an intent to start the play animation activity
            
            startActivity(intent);                                                              // Start the play activity

            thread.interrupt();                                                                 // Interrupt the generation thread now that the maze is generated  
        } else {
            Log.v(LOG_TAG, "User needs to select an option for driving method and skill.");     // Log that the user needs to select both
            
            try {
                thread.sleep(1000);                                                             // Sleep the thread for 1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            hasUserSelected(thread);                                                            // Call the method again to check if the user has selected both
        }
    }

}
