package edu.wm.cs.cs301.amazebyshamsullahahmadzai.gui;

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

public class PlayManuallyActivity extends AppCompatActivity {

    private int distanceTravelled;
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
    }

    @Override
    public void onBackPressed() {
        // Handle the back button event by going back to the title page
        Intent intent = new Intent(PlayManuallyActivity.this, AMazeActivity.class);
        startActivity(intent);
    }

    private void setUpButtons() {
        // Setting up the NewMazeButton and adding a click listener
        Button jumpToLose = findViewById(R.id.jumpToLose);
        jumpToLose.setOnClickListener(view -> {
            Intent intent = new Intent(this, LosingActivity.class);
            intent.putExtra("distance", distanceTravelled);
            Log.v(LOG_TAG, "Jumping to the losing screen.");
            startActivity(intent);
        });

        // Setting up the OldMazeButton and adding a click listener
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

    // Sets up the buttons that toggle the visibility of the solution and the maze
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