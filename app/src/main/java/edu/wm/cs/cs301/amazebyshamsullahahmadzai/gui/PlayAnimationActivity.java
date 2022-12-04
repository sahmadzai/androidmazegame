package edu.wm.cs.cs301.amazebyshamsullahahmadzai.gui;

import static edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation.MazeDataHolder.getMaze;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import edu.wm.cs.cs301.amazebyshamsullahahmadzai.R;
import edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation.MazeDataHolder;
import edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation.MazePanel;
import edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation.ReliableRobot;
import edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation.Robot;
import edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation.RobotDriver;
import edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation.State;
import edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation.StatePlaying;
import edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation.UnreliableRobot;
import edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation.WallFollower;
import edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation.Wizard;

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

    private Robot robot;
    private RobotDriver driver;
    private boolean crashed;
    private float energyConsumed;
    private float initialEnergy;

    private final int LENGTH_SHORT = 800;
    private int SLEEP_INTERVAL;
    Handler playAnimHandler = new Handler();
    Runnable playAnimRunnable;
    StatePlaying playState;
    private final String LOG_TAG = "PlayAnimationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_animation);

        setUpButtons();                                                             // Setting up the seekbar and adding a change listener
        setUpToggleButtons();                                                       // Setting up the toggle buttons and adding a click listener
        setUpAnimationSpeed();                                                      // Setting up the animation speed and adding a change listener

        playState = new StatePlaying(this);
        playState.setMaze(getMaze());
        MazePanel panel = findViewById(R.id.maze_view);
        playState.start(panel);

        setUpDriver();
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
        jumpToLose.setOnClickListener(view -> switchToLosing(0));

        // Setting up the JumpToWin button and adding a click listener
        Button jumpToWin = findViewById(R.id.jumpToWin);
        jumpToWin.setOnClickListener(view -> switchToWinning(0));
    }

    /**
     * This method sets up the toggle buttons that allow the user to show the solution path, full map
     * of the maze, or the walls of the maze. It also sets up a click listener for each button that
     * logs the event.
     */
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
            } else {
                showMaze.setText(R.string.state_on);
                showMaze.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray));
                showMaze.setTextColor(Color.BLACK);
            }
        });

        Button pauseAnim = findViewById(R.id.animPause);
        pauseAnim.setOnClickListener(view -> {
            // Set the button text to play or pause depending on the current state and change the color to light gray or dark gray
            if (pauseAnim.getText().equals("Play")) {
                pauseAnim.setText(R.string.state_pause);
                pauseAnim.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_gray));
                pauseAnim.setTextColor(Color.WHITE);
                startAnimation(pauseAnim);
                Snackbar.make(view, "Animation Playing", Snackbar.LENGTH_SHORT).setDuration(LENGTH_SHORT).show();
            } else {
                pauseAnim.setText(R.string.state_play);
                pauseAnim.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray));
                pauseAnim.setTextColor(Color.BLACK);
                stopAnimation(pauseAnim);
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
                SLEEP_INTERVAL = i;
                Log.v(LOG_TAG, "Animation speed is now: " + (SLEEP_INTERVAL+1));
                String text = "Animation speed is now: " + (SLEEP_INTERVAL+1);
                Snackbar.make(seekBar, text, Snackbar.LENGTH_SHORT).setDuration(LENGTH_SHORT).show();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void setUpDriver() {
        if (MazeDataHolder.getDriver().equals("Wizard")) {
            Log.v(LOG_TAG, "Wizard driver has been selected and is running.");
            if (MazeDataHolder.getDriverLvl().equals("Premium"))
                robot = new ReliableRobot(playState);
            else
                robot = new UnreliableRobot(playState);
            driver = new Wizard();
            boolean[] sensorArr = playState.getSensorArray(MazeDataHolder.getSensorString());
            robot.setUnreliableSensors(sensorArr);
            driver.setMaze(MazeDataHolder.getMaze());
            driver.setRobot(robot);
            // visibility settings
            playState.showMaze = true;
            playState.showSolution = true;
            playState.mapMode = true;
            playState.startFailAndRepairProcess(sensorArr, robot);
            playAnimRunnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!robot.isAtExit()) {
                            driver.drive1Step2Exit();
                            playAnimHandler.postDelayed(this, 100);
                        } else {
                            robot.move(1);
                            switchToWinning(driver.getPathLength());
                        }
                    } catch (Exception e) {
                        /*
                         * If an error or exception occurs, then set a crash
                         * variable to true which will print out a losing page.
                         */
                        e.printStackTrace();
                        crashed = true;
                        energyConsumed = driver.getEnergyConsumption();
                        initialEnergy = driver.getInitialBatteryLevel();
                        playState.stopFailAndRepairProcess(sensorArr, robot);
                        switchToLosing(driver.getPathLength());
                    }
                }
            };
        } else if (MazeDataHolder.getDriver().equals("WallFollower")) {
            Log.v(LOG_TAG, "WallFollower driver has been selected and is running.");
            if(MazeDataHolder.getDriverLvl().equals("Premium"))
                robot = new ReliableRobot(playState);
            else
                robot = new UnreliableRobot(playState);
            driver = new WallFollower();
            boolean [] sensorArr = playState.getSensorArray(MazeDataHolder.getSensorString());
            robot.setUnreliableSensors(sensorArr);
            driver.setMaze(MazeDataHolder.getMaze());
            driver.setRobot(robot);
            // visibility settings
            playState.showMaze = true ;
            playState.showSolution = true ;
            playState.mapMode = true;
            playState.startFailAndRepairProcess(sensorArr, robot);
            try {
                if(!driver.drive2Exit()) {
                    energyConsumed = driver.getEnergyConsumption();
                    playState.stopFailAndRepairProcess(sensorArr, robot);
                    switchToWinning(driver.getPathLength());
                }
            } catch (Exception e) {
                /*
                 * If an error or exception occurs, then set a crash
                 * variable to true which will print out a losing page.
                 */
                e.printStackTrace();
                energyConsumed = driver.getEnergyConsumption();
                initialEnergy = driver.getInitialBatteryLevel();
                playState.stopFailAndRepairProcess(sensorArr, robot);
                switchToLosing(driver.getPathLength());
            }
        }
    }

    public void startAnimation(View view) {
        playAnimRunnable.run();
    }

    public void stopAnimation(View view) {
        playAnimHandler.removeCallbacks(playAnimRunnable);
    }

    public void switchToWinning(int pathLength) {
        Intent intent = new Intent(this, WinningActivity.class);
        intent.putExtra("distance", pathLength);
        Log.v(LOG_TAG, "Jumping to the winning screen.");
        startActivity(intent);
    }

    public void switchToLosing(int pathLength) {
        Intent intent = new Intent(this, LosingActivity.class);
        intent.putExtra("distance", pathLength);
        Log.v(LOG_TAG, "Jumping to the losing screen.");
        startActivity(intent);
    }
}