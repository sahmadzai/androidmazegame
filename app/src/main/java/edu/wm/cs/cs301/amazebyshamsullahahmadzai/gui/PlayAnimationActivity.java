package edu.wm.cs.cs301.amazebyshamsullahahmadzai.gui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import edu.wm.cs.cs301.amazebyshamsullahahmadzai.CustomView_Anim;
import edu.wm.cs.cs301.amazebyshamsullahahmadzai.R;

/**
 * This class is responsible for the automatic robot moving through the maze. It has a seekbar that
 * allows the user to control the speed of the animation/robot. It also has toggle buttons that allow
 * the user to show the solution path, full map of the maze, or the walls of the maze.
 * 
 * It also contains visual indicators for the robot's sensor states.
 * 
 * @author Shamsullah Ahmadzai
 * 
 */

public class PlayAnimationActivity extends AppCompatActivity {

    private int animSpeed;
    private final int LENGTH_SHORT = 800;
    private final String LOG_TAG = "PlayAnimationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_animation);

        CustomView_Anim maze_view = new CustomView_Anim(this);                      // Creating a new CustomView_Anim object to display the maze
        setUpButtons();                                                             // Setting up the seekbar and adding a change listener
        setUpToggleButtons();                                                       // Setting up the toggle buttons and adding a click listener
        setUpAnimationSpeed();                                                      // Setting up the animation speed and adding a change listener
    }

    /**
     * This method overrides the default back button behavior to take the user 
     * back to the title screen instead of the previous activity.
     */
    @Override
    public void onBackPressed() {
        // Handle the back button event by going back to the title page
        Intent intent = new Intent(PlayAnimationActivity.this, AMazeActivity.class);
        startActivity(intent);
    }

    /**
     * This method sets up the buttons that allow the user to jump to the winning or losing screen.
     * It also sets up a click listener for each button that takes the user to the appropriate screen
     * through an intent and also logs the event.
     */
    private void setUpButtons() {
        // Setting up the JumpToLose button and adding a click listener
        Button jumpToLose = findViewById(R.id.jumpToLose);
        jumpToLose.setOnClickListener(view -> {
            Intent intent = new Intent(this, LosingActivity.class);
            Log.v(LOG_TAG, "Jumping to the losing screen.");
            startActivity(intent);
        });

        // Setting up the JumpToWin button and adding a click listener
        Button jumpToWin = findViewById(R.id.jumpToWin);
        jumpToWin.setOnClickListener(view -> {
            Intent intent = new Intent(this, WinningActivity.class);
            Log.v(LOG_TAG, "Jumping to the winning screen.");
            startActivity(intent);
        });
    }

    /**
     * This method sets up the toggle buttons that allow the user to show the solution path, full map
     * of the maze, or the walls of the maze. It also sets up a click listener for each button that
     * logs the event and shows a snackbar to the user.
     */
    private void setUpToggleButtons() {
        Button showWalls = findViewById(R.id.shw_walls);
        showWalls.setOnClickListener(view -> {
            // Set the button text to on or off depending on the current state and change the color to light gray or dark gray
            if (showWalls.getText().equals("ON")) {
                showWalls.setText(R.string.state_off);
                showWalls.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_gray));
                showWalls.setTextColor(Color.WHITE);
                Snackbar.make(view, "Walls Hidden", Snackbar.LENGTH_SHORT).setDuration(LENGTH_SHORT).show();
            } else {
                showWalls.setText(R.string.state_on);
                showWalls.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray));
                showWalls.setTextColor(Color.BLACK);
                Snackbar.make(view, "Walls Visible", Snackbar.LENGTH_SHORT).setDuration(LENGTH_SHORT).show();
            }
        });

        Button showSolution = findViewById(R.id.shw_sol);
        showSolution.setOnClickListener(view -> {
            // Set the button text to on or off depending on the current state and change the color to light gray or dark gray
            if (showSolution.getText().equals("ON")) {
                showSolution.setText(R.string.state_off);
                showSolution.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_gray));
                showSolution.setTextColor(Color.WHITE);
                Snackbar.make(view, "Solution Hidden", Snackbar.LENGTH_SHORT).setDuration(LENGTH_SHORT).show();
            } else {
                showSolution.setText(R.string.state_on);
                showSolution.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray));
                showSolution.setTextColor(Color.BLACK);
                Snackbar.make(view, "Solution Visible", Snackbar.LENGTH_SHORT).setDuration(LENGTH_SHORT).show();
            }
        });

        Button showMaze = findViewById(R.id.shw_fullmaze);
        showMaze.setOnClickListener(view -> {
            // Set the button text to on or off depending on the current state and change the color to light gray or dark gray
            if (showMaze.getText().equals("ON")) {
                showMaze.setText(R.string.state_off);
                showMaze.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_gray));
                showMaze.setTextColor(Color.WHITE);
                Snackbar.make(view, "Maze Hidden", Snackbar.LENGTH_SHORT).setDuration(LENGTH_SHORT).show();
            } else {
                showMaze.setText(R.string.state_on);
                showMaze.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray));
                showMaze.setTextColor(Color.BLACK);
                Snackbar.make(view, "Maze Visible", Snackbar.LENGTH_SHORT).setDuration(LENGTH_SHORT).show();
            }
        });

        Button pauseAnim = findViewById(R.id.animPause);
        pauseAnim.setOnClickListener(view -> {
            // Set the button text to play or pause depending on the current state and change the color to light gray or dark gray
            if (pauseAnim.getText().equals("Play")) {
                pauseAnim.setText(R.string.state_pause);
                pauseAnim.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_gray));
                pauseAnim.setTextColor(Color.WHITE);
                Snackbar.make(view, "Animation Playing", Snackbar.LENGTH_SHORT).setDuration(LENGTH_SHORT).show();
            } else {
                pauseAnim.setText(R.string.state_play);
                pauseAnim.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray));
                pauseAnim.setTextColor(Color.BLACK);
                Snackbar.make(view, "Animation Paused", Snackbar.LENGTH_SHORT).setDuration(LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This method sets up the seek bar that allows the user to change the animation speed. It also
     * sets up a change listener for the seek bar that logs the event and shows a snackbar to the user
     * with the new animation speed.
     */
    private void setUpAnimationSpeed() {
        SeekBar animSpeedSlider = findViewById(R.id.animSpeed);                                         // Get the seek bar from the layout
        animSpeedSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {              // Set up a change listener for the seek bar
            
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                animSpeed = i;                                                                          // Set the animation speed to the value of the seek bar
                Log.v(LOG_TAG, "Animation speed is now: " + (animSpeed+1));                             // Log the new animation speed
                String text = "Animation speed is now: " + (animSpeed+1);                               // Create a string to show in the snackbar
                Snackbar.make(seekBar, text, Snackbar.LENGTH_SHORT).setDuration(LENGTH_SHORT).show();   // Show a snackbar with the new animation speed
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }
}