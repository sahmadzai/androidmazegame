package edu.wm.cs.cs301.amazebyshamsullahahmadzai.gui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import edu.wm.cs.cs301.amazebyshamsullahahmadzai.CustomView_Anim;
import edu.wm.cs.cs301.amazebyshamsullahahmadzai.R;

public class PlayAnimationActivity extends AppCompatActivity {

    private final String LOG_TAG = "PlayAnimationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_animation);

        CustomView_Anim maze_view = new CustomView_Anim(this);

        setUpButtons();

        setUpToggleButtons();
    }

    @Override
    public void onBackPressed() {
        // Handle the back button event by going back to the title page
        Intent intent = new Intent(PlayAnimationActivity.this, AMazeActivity.class);
        startActivity(intent);
    }

    private void setUpButtons() {
        // Setting up the NewMazeButton and adding a click listener
        Button newMazeBtn = findViewById(R.id.jumpToLose);
        newMazeBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, LosingActivity.class);
            Log.v(LOG_TAG, "Jumping to the losing screen.");
            startActivity(intent);
        });

        // Setting up the OldMazeButton and adding a click listener
        Button oldMazeBtn = findViewById(R.id.jumpToWin);
        oldMazeBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, WinningActivity.class);
            Log.v(LOG_TAG, "Jumping to the winning screen.");
            startActivity(intent);
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
            } else {
                showWalls.setText(R.string.state_on);
                showWalls.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray));
                showWalls.setTextColor(Color.BLACK);
            }
        });

        Button showSolution = findViewById(R.id.shw_sol);
        showSolution.setOnClickListener(view -> {
            // Set the button text to on or off depending on the current state and change the color to light gray or dark gray
            if (showSolution.getText().equals("ON")) {
                showSolution.setText(R.string.state_off);
                showSolution.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_gray));
                showSolution.setTextColor(Color.WHITE);
            } else {
                showSolution.setText(R.string.state_on);
                showSolution.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray));
                showSolution.setTextColor(Color.BLACK);
            }
        });

        Button showMaze = findViewById(R.id.shw_fullmaze);
        showMaze.setOnClickListener(view -> {
            // Set the button text to on or off depending on the current state and change the color to light gray or dark gray
            if (showMaze.getText().equals("ON")) {
                showMaze.setText(R.string.state_off);
                showMaze.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_gray));
                showMaze.setTextColor(Color.WHITE);
            } else {
                showMaze.setText(R.string.state_on);
                showMaze.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray));
                showMaze.setTextColor(Color.BLACK);
            }
        });
    }
}