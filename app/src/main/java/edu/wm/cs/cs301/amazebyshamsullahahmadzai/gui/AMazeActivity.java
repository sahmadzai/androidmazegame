package edu.wm.cs.cs301.amazebyshamsullahahmadzai.gui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.Random;

import edu.wm.cs.cs301.amazebyshamsullahahmadzai.R;

/**
 * This activity represents the title screen of the maze app. It has welcome text, a SeekBar to
 * select the skill level for the maze, a Spinner to select the maze generation method and some radio
 * buttons to choose whether rooms should be generated or not.
 *
 * The user presses either the "Load Old Maze" button or the "Load New Maze" button to proceed.
 * Based on their choice, the activity starts a new activity, GeneratingActivity, which generates
 * the maze and shows the progress of the generation.
 *
 * @author Shamsullah Ahmadzai
 *
 */
public class AMazeActivity extends AppCompatActivity {

    /**
     * Class variables that are used to store the different values that are selected by the user
     * and some other constant values.
     */
    private String gen_method = "DFS";
    private int skill_level = 0;
    private boolean rooms = true;
    private int seed;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private MediaPlayer mp;
    private MediaPlayer touchmp;
    private Vibrator vibe;
    private final int LENGTH_SHORT = 800;
    private final String LOG_TAG = "AMazeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title_layout);

        vibe =  (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        mp = MediaPlayer.create(this, R.raw.title_music);
        mp.setLooping(true);
        mp.start();

        touchmp = MediaPlayer.create(this, R.raw.touch);

        // Initialize the values before adding in data
        pref = getApplicationContext().getSharedPreferences("edu.wm.cs301.amazebyshamsullahahmadzai.preferences", 0);
        editor = pref.edit();

        // Setup and start the seekbar listener
        initSeekBar();
        // Initialize the generation method spinner and fill in values
        initSpinner();
        // Set up the maze buttons and start the listeners
        setUpButtons();
    }

    /**
     * Method that sets up the seekbar and starts the listener for it.
     * The listener updates the skill level variable when the user changes the value of the seekbar.
     */
    private void initSeekBar() {
        // Initialize seekbar
        SeekBar skillSeekBar = findViewById(R.id.difficulty);
        skillSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                skill_level = i;
                Log.v(LOG_TAG, "Difficulty is now: " + skill_level);
                String text = "Difficulty is now: " + skill_level;
                Snackbar.make(seekBar, text, Snackbar.LENGTH_SHORT).setDuration(LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                vibe.vibrate(50);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                vibe.vibrate(100);
            }
        });
    }

    /**
     * This method sets up the spinner and fills in the values for the different generation methods from strings.xml.
     * It also sets up the listener for the spinner and updates the gen_method variable when the user selects a new
     * generation method.
     */
    private void initSpinner() {
        Spinner spinner = findViewById(R.id.genmethod_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gen_methods, R.layout.custom_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                touchmp.start();
                if (i == 0)
                    Log.v(LOG_TAG, "User has not selected anything yet.");
                else {
                    String text = parent.getItemAtPosition(i).toString();
                    gen_method = text;
                    Snackbar.make(parent, text, Snackbar.LENGTH_SHORT).setDuration(LENGTH_SHORT).show();
                    Log.v(LOG_TAG, "User selected " + text + " as the generation method.");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.v(LOG_TAG, "Nothing has been selected for the generation method.");
                gen_method = null;
            }
        });
    }

    /**
     * This method sets up the buttons for the maze and starts the listeners for them.
     * The listeners start the GeneratingActivity when the user presses either the "Load Old Maze" or
     * the "Load New Maze" button. Based on the button that was pressed, the GeneratingActivity will
     * either load the old maze or generate a new maze.
     *
     * The data is passed to the GeneratingActivity through a Bundle object using the Intent.putExtras() method.
     */
    private void setUpButtons() {
        // Generate the seed value using the Random class
        Random random = new Random();
        seed = random.nextInt();
        // Setting up the NewMazeButton and adding a click listener
        Button newMazeBtn = findViewById(R.id.newmazebtn);
        newMazeBtn.setOnClickListener(view -> {
            touchmp.start();
            pushToPreferences();
            Intent intent = new Intent(this, GeneratingActivity.class);     // Create new intent
            Log.v(LOG_TAG, "Generating a NEW Maze.");                       // Log the event
            mp.stop();
            startActivity(intent);                                          // Start the GeneratingActivity
        });

        // Setting up the OldMazeButton and adding a click listener
        Button oldMazeBtn = findViewById(R.id.oldmazebtn);
        oldMazeBtn.setOnClickListener(view -> {
            touchmp.start();
            Intent intent = new Intent(this, GeneratingActivity.class);
            Log.v(LOG_TAG, "Revisiting the OLD Maze.");
            mp.stop();
            startActivity(intent);
        });
    }

    /**
     * This method is called when the user presses an option from radio group.
     * It updates the rooms variable to true if the user selects the "Yes"
     * option and false if the user selects the "No" option.
     *
     * @param view The view that was clicked.
     */
    public void onRoomChoice(View view) {
        touchmp.start();
        RadioButton radio_yes = findViewById(R.id.radio_yes);

        if (radio_yes.isChecked()) {
            rooms = true;
            Log.v(LOG_TAG, "User selected YES for room generation.");
        } else {
            rooms = false;
            Log.v(LOG_TAG, "User selected NO for room generation.");
        }
    }

    private void pushToPreferences() {


        // Clear the file before adding anything in
        editor.clear();
        editor.commit();

        // Add in the user inputs to the pref file
        editor.putInt("skill_level", skill_level);
        editor.putString("gen_method", gen_method);
        editor.putBoolean("gen_rooms", rooms);
        editor.putInt("seed", seed);
        editor.commit();
    }
}