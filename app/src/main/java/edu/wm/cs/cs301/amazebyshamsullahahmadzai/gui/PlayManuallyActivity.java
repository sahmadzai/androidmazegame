package edu.wm.cs.cs301.amazebyshamsullahahmadzai.gui;

import static edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation.MazeDataHolder.getMaze;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import edu.wm.cs.cs301.amazebyshamsullahahmadzai.CustomView_Manual;
import edu.wm.cs.cs301.amazebyshamsullahahmadzai.R;
import edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation.Constants;
import edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation.MazePanel;
import edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation.StatePlaying;

/**
 * This class is responsible for the manual robot moving through the maze. It has toggle buttons that allow
 * the user to show the solution path, full map of the maze, or the walls of the maze. It also contains
 * buttons that allow the user to rotate the robot, move the robot forward, or make the robot jump.
 * 
 * @author Shamsullah Ahmadzai
 * 
 */

public class PlayManuallyActivity extends AppCompatActivity {

    private int distanceTravelled;
    private StatePlaying playState;
    private final int LENGTH_SHORT = 800;
    private final String LOG_TAG = "PlayManuallyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_manually);

        CustomView_Manual maze_view = new CustomView_Manual(this);
        setUpButtons();
        setUpToggleButtons();
        setUpMoveButtons();

        playState = new StatePlaying();
        playState.setMaze(getMaze());
        MazePanel panel = new MazePanel(getApplicationContext());
        playState.start(panel);
    }

    /**
     * This method overrides the default back button behavior to take the user 
     * back to the title screen instead of the previous activity.
     */
    @Override
    public void onBackPressed() {
        // Handle the back button event by going back to the title page
        Intent intent = new Intent(PlayManuallyActivity.this, AMazeActivity.class);
        startActivity(intent);
    }

    /**
     * This method sets up the buttons that allow the user to jump to the winning or losing screen.
     * It also sets up a click listener for each button that takes the user to the appropriate screen
     * through an intent and also logs the event.
     */
    private void setUpButtons() {
        // Setting up the jumpToLose button and adding a click listener
        Button jumpToLose = findViewById(R.id.jumpToLose);
        jumpToLose.setOnClickListener(view -> {
            Intent intent = new Intent(this, LosingActivity.class);
            intent.putExtra("distance", distanceTravelled);
            Log.v(LOG_TAG, "Jumping to the losing screen.");
            startActivity(intent);
        });

        // Setting up the jumpToWin and adding a click listener
        Button jumpToWin = findViewById(R.id.jumpToWin);
        jumpToWin.setOnClickListener(view -> {
            Intent intent = new Intent(this, WinningActivity.class);
            intent.putExtra("distance", distanceTravelled);
            Log.v(LOG_TAG, "Jumping to the winning screen.");
            startActivity(intent);
        });

        // Setting up the zoom in button and adding a click listener
        Button zoom_in = findViewById(R.id.zoom_in);
        zoom_in.setOnClickListener(view -> {
            Snackbar.make(view, "Zooming In", Snackbar.LENGTH_SHORT).setDuration(LENGTH_SHORT).show();
            Log.v(LOG_TAG, "User pressed the zoom in button.");
        });

        // Setting up the zoom out button and adding a click listener
        Button zoom_out = findViewById(R.id.zoom_out);
        zoom_out.setOnClickListener(view -> {
            Snackbar.make(view, "Zooming Out", Snackbar.LENGTH_SHORT).setDuration(LENGTH_SHORT).show();
            Log.v(LOG_TAG, "User pressed the zoom out button.");
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
                Log.v(LOG_TAG, "User has now toggled walls to be hidden.");
                playState.handleUserInput(Constants.UserInput.TOGGLELOCALMAP, 0);
            } else {
                showWalls.setText(R.string.state_on);
                showWalls.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray));
                showWalls.setTextColor(Color.BLACK);
                Snackbar.make(view, "Walls Visible", Snackbar.LENGTH_SHORT).setDuration(LENGTH_SHORT).show();
                Log.v(LOG_TAG, "User has now toggled walls to be visible.");
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
                Log.v(LOG_TAG, "User has now toggled the solution to be hidden.");
            } else {
                showSolution.setText(R.string.state_on);
                showSolution.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray));
                showSolution.setTextColor(Color.BLACK);
                Snackbar.make(view, "Solution Visible", Snackbar.LENGTH_SHORT).setDuration(LENGTH_SHORT).show();
                Log.v(LOG_TAG, "User has now toggled the solution to be visible.");
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
                Log.v(LOG_TAG, "User has now toggled the maze to be hidden.");

            } else {
                showMaze.setText(R.string.state_on);
                showMaze.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray));
                showMaze.setTextColor(Color.BLACK);
                Snackbar.make(view, "Maze Visible", Snackbar.LENGTH_SHORT).setDuration(LENGTH_SHORT).show();
                Log.v(LOG_TAG, "User has now toggled the maze to be visible.");
            }
        });
    }

    /**
     * This method sets up the movement buttons that allow the user to move the robot forward, rotate
     * the robot left, rotate the robot right, or make the robot jump. It also sets up a click listener
     * for each button that logs the event and shows a snackbar to the user. It also updates the distance
     * travelled by the robot which will be passed to the winning or losing activity.
     */
    private void setUpMoveButtons() {
        Button moveForward = findViewById(R.id.move_up);
        Button jump        = findViewById(R.id.jump_btn);
        Button turnLeft    = findViewById(R.id.move_left);
        Button turnRight   = findViewById(R.id.move_right);

        moveForward.setOnClickListener(view -> {
            Snackbar.make(view, "Moving Forward", Snackbar.LENGTH_SHORT).setDuration(LENGTH_SHORT).show();
            Log.v(LOG_TAG, "User pressed the move forward button.");
            distanceTravelled++;
        });
        jump.setOnClickListener(view -> {
            Snackbar.make(view, "Jumping", Snackbar.LENGTH_SHORT).setDuration(LENGTH_SHORT).show();
            Log.v(LOG_TAG, "User pressed the jump button.");
            distanceTravelled++;
        });
        turnLeft.setOnClickListener(view -> {
            Snackbar.make(view, "Turning Left", Snackbar.LENGTH_SHORT).setDuration(LENGTH_SHORT).show();
            Log.v(LOG_TAG, "User pressed the turn left button.");
        });
        turnRight.setOnClickListener(view -> {
            Snackbar.make(view, "Turning Right", Snackbar.LENGTH_SHORT).setDuration(LENGTH_SHORT).show();
            Log.v(LOG_TAG, "User pressed the turn right button.");
        });
    }


}