package edu.wm.cs.cs301.amazebyshamsullahahmadzai.gui;

import static edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation.MazeDataHolder.getMaze;
import static edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation.MazeDataHolder.maze;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import edu.wm.cs.cs301.amazebyshamsullahahmadzai.R;
import edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation.Constants;
import edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation.MazeDataHolder;
import edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation.MazePanel;
import edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation.Robot;
import edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation.RobotDriver;
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
    private float energyConsumed;

    private ImageView forwardSensor;
    private ImageView leftSensor;
    private ImageView rightSensor;
    private ImageView backSensor;
    private ProgressBar energy;

    private final int LENGTH_SHORT = 800;
    private int SLEEP_INTERVAL = 5000;
    Handler playAnimHandler = new Handler();
    Runnable playAnimRunnable;
    StatePlaying playState;
    private boolean[] sensorArr;

    private MediaPlayer mp;
    private final String LOG_TAG = "PlayAnimationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_animation);

        mp = MediaPlayer.create(this, R.raw.playmanualsound);
        mp.setLooping(true);
        mp.start();

        setUpButtons();                                                             // Setting up the seekbar and adding a change listener
        setUpToggleButtons();                                                       // Setting up the toggle buttons and adding a click listener
        setUpAnimationSpeed();                                                      // Setting up the animation speed and adding a change listener

        playState = new StatePlaying(this);
        playState.setMaze(getMaze());
        MazePanel panel = findViewById(R.id.maze_view);
        playState.start(panel);

        forwardSensor = findViewById(R.id.fsensor_state);
        leftSensor    = findViewById(R.id.lsensor_state);
        rightSensor   = findViewById(R.id.rsensor_state);
        backSensor    = findViewById(R.id.bsensor_state);

        energy        = findViewById(R.id.energy_bar);

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
        mp.stop();
        startActivity(intent);
    }

    /**
     * This method sets up the buttons that allow the user to jump to the winning or losing screen.
     * It also sets up a click listener for each button that takes the user to the appropriate screen
     * through an intent and also logs the event.
     */
    private void setUpButtons() {
        // Setting up the JumpToLose button and adding a click listener
//        Button jumpToLose = findViewById(R.id.jumpToLose);
//        jumpToLose.setOnClickListener(view -> switchToLosing(0, driver.getEnergyConsumption()));

          // Setting up the JumpToWin button and adding a click listener
//        Button jumpToWin = findViewById(R.id.jumpToWin);
//        jumpToWin.setOnClickListener(view -> switchToWinning(0, driver.getEnergyConsumption()));

        // Setting up the zoom in button and adding a click listener
        Button zoom_in = findViewById(R.id.zoom_in);
        zoom_in.setOnClickListener(view -> {
            Snackbar.make(view, "Zooming In", Snackbar.LENGTH_SHORT).setDuration(LENGTH_SHORT).show();
            Log.v(LOG_TAG, "User pressed the zoom in button.");
            playState.handleUserInput(Constants.UserInput.ZOOMIN, 0);
        });

        // Setting up the zoom out button and adding a click listener
        Button zoom_out = findViewById(R.id.zoom_out);
        zoom_out.setOnClickListener(view -> {
            Snackbar.make(view, "Zooming Out", Snackbar.LENGTH_SHORT).setDuration(LENGTH_SHORT).show();
            Log.v(LOG_TAG, "User pressed the zoom out button.");
            playState.handleUserInput(Constants.UserInput.ZOOMOUT, 0);
        });
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
                Log.v(LOG_TAG, "User has now toggled walls to be hidden.");
                playState.handleUserInput(Constants.UserInput.TOGGLELOCALMAP, 0);
            } else {
                showWalls.setText(R.string.state_on);
                showWalls.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray));
                showWalls.setTextColor(Color.BLACK);
                Log.v(LOG_TAG, "User has now toggled walls to be visible.");
                playState.handleUserInput(Constants.UserInput.TOGGLELOCALMAP, 0);
            }
        });

        Button showSolution = findViewById(R.id.shw_sol);
        showSolution.setOnClickListener(view -> {
            // Set the button text to on or off depending on the current state and change the color to light gray or dark gray
            if (showSolution.getText().equals("ON")) {
                showSolution.setText(R.string.state_off);
                showSolution.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_gray));
                showSolution.setTextColor(Color.WHITE);
                Log.v(LOG_TAG, "User has now toggled the solution to be hidden.");
                playState.handleUserInput(Constants.UserInput.TOGGLESOLUTION, 0);
            } else {
                showSolution.setText(R.string.state_on);
                showSolution.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray));
                showSolution.setTextColor(Color.BLACK);
                Log.v(LOG_TAG, "User has now toggled the solution to be visible.");
                playState.handleUserInput(Constants.UserInput.TOGGLESOLUTION, 0);
            }
        });

        Button showMaze = findViewById(R.id.shw_fullmaze);
        showMaze.setOnClickListener(view -> {
            // Set the button text to on or off depending on the current state and change the color to light gray or dark gray
            if (showMaze.getText().equals("ON")) {
                showMaze.setText(R.string.state_off);
                showMaze.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_gray));
                showMaze.setTextColor(Color.WHITE);
                Log.v(LOG_TAG, "User has now toggled the maze to be hidden.");
                playState.handleUserInput(Constants.UserInput.TOGGLEFULLMAP, 0);
            } else {
                showMaze.setText(R.string.state_on);
                showMaze.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray));
                showMaze.setTextColor(Color.BLACK);
                Log.v(LOG_TAG, "User has now toggled the maze to be visible.");
                playState.handleUserInput(Constants.UserInput.TOGGLEFULLMAP, 0);
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
                Log.v(LOG_TAG, "User has started playing the animation.");
                Snackbar.make(view, "Animation Playing", Snackbar.LENGTH_SHORT).setDuration(LENGTH_SHORT).show();
            } else {
                pauseAnim.setText(R.string.state_play);
                pauseAnim.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray));
                pauseAnim.setTextColor(Color.BLACK);
                stopAnimation(pauseAnim);
                Log.v(LOG_TAG, "User has paused the animation");
                Snackbar.make(view, "Animation Paused", Snackbar.LENGTH_SHORT).setDuration(LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This method sets up the seek bar that allows the user to change the animation speed. It also
     * sets up a change listener for the seek bar that logs the event and shows a snack-bar to the user
     * with the new animation speed.
     */
    private void setUpAnimationSpeed() {
        SeekBar animSpeedSlider = findViewById(R.id.animSpeed);                                         // Get the seek bar from the layout
        animSpeedSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {              // Set up a change listener for the seek bar
            
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                updateSLEEP_INTERVAL(i);
                Log.v(LOG_TAG, "Animation speed is now: " + (SLEEP_INTERVAL));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void updateSLEEP_INTERVAL(int i) {
        switch (i) {
            case 0:
                SLEEP_INTERVAL = 5000;
                break;
            case 1:
                SLEEP_INTERVAL = 4000;
                break;
            case 2:
                SLEEP_INTERVAL = 2000;
                break;
            case 3:
                SLEEP_INTERVAL = 1000;
                break;
            case 4:
                SLEEP_INTERVAL = 0;
                break;
        }
    }

    private int getSleepInterval(){
        return SLEEP_INTERVAL;
    }

    /**
     * Thread that handles the call to sensor startFailandRepairProcess
     * This allows for the delay to be made between different sensors
     * without causing the app to become unresponsive.
     */
    private final Thread sensorProcess = new Thread(new Runnable() {
        @Override
        public void run() {
            robot.setUnreliableSensors(sensorArr, maze);
            driver.setMaze(MazeDataHolder.getMaze());
            driver.setRobot(robot);
            playState.startFailAndRepairProcess(sensorArr, robot);
        }
    });

    private void setUpDriver() {
        if (MazeDataHolder.getDriver().equals("Wizard")) {
            Log.v(LOG_TAG, "Wizard driver has been selected and is running.");
            startWizard();
        } else if (MazeDataHolder.getDriver().equals("WallFollower")) {
            Log.v(LOG_TAG, "WallFollower driver has been selected and is running.");
            startWallFollower();
        }
    }

    private void startWizard() {
        robot = new UnreliableRobot(playState);
        driver = new Wizard();
        sensorArr = playState.getSensorArray(MazeDataHolder.getSensorString());

        // visibility settings
        playState.showMaze = true;
        playState.showSolution = true;
        playState.mapMode = true;

        sensorProcess.start();

        playAnimRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if (!robot.isAtExit()) {
                        driver.drive1Step2Exit();
                        updateVisualSensors(robot);
                        updateEnergyBar(driver.getEnergyConsumption());
                        playAnimHandler.postDelayed(this, getSleepInterval());
                    } else {
                        robot.move(1);
                        sensorProcess.interrupt();
                        switchToWinning(robot.getOdometerReading(), robot.getBatteryLevel());
                    }
                } catch (Exception e) {
                    /*
                     * If an error or exception occurs, then set a crash
                     * variable to true which will print out a losing page.
                     */
                    e.printStackTrace();
                    energyConsumed = driver.getEnergyConsumption();
                    playState.stopFailAndRepairProcess(sensorArr, robot);
                    switchToLosing(robot.getOdometerReading(), robot.getBatteryLevel());
                }
            }
        };
    }

    private void startWallFollower() {
        robot = new UnreliableRobot(playState);
        driver = new WallFollower();
        Log.v(LOG_TAG, "Sensor String is: " + MazeDataHolder.getSensorString());
        sensorArr = playState.getSensorArray(MazeDataHolder.getSensorString());

        // visibility settings
        playState.showMaze = true ;
        playState.showSolution = true ;
        playState.mapMode = true;

        sensorProcess.start();

        playAnimRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if (!robot.isAtExit()) {
                        driver.drive1Step2Exit();
                        updateVisualSensors(robot);
                        updateEnergyBar(driver.getEnergyConsumption());
                        playAnimHandler.postDelayed(this, getSleepInterval());
                    } else {
                        Log.v(LOG_TAG, "Made it to exit");
                        robot.move(1);
                        sensorProcess.interrupt();
                        switchToWinning(robot.getOdometerReading(), robot.getBatteryLevel());
                    }
                } catch (Exception e) {
                    /*
                     * If an error or exception occurs, then set a crash
                     * variable to true which will print out a losing page.
                     */
                    e.printStackTrace();
                    energyConsumed = driver.getEnergyConsumption();
                    playState.stopFailAndRepairProcess(sensorArr, robot);
                    switchToLosing(robot.getOdometerReading(), robot.getBatteryLevel());
                }
            }
        };

        Log.v(LOG_TAG, "The animation speed is: " + SLEEP_INTERVAL);
    }

    public void startAnimation(View view) {
        playAnimRunnable.run();
    }

    public void stopAnimation(View view) {
        playAnimHandler.removeCallbacks(playAnimRunnable);
    }

    private void updateVisualSensors(Robot robot) {
        if(robot.getForwardSensor().getSensorState())
            forwardSensor.setImageResource(R.drawable.snsrstate_green);
        else
            forwardSensor.setImageResource(R.drawable.snsrstate_red);

        if(robot.getLeftSensor().getSensorState())
            leftSensor.setImageResource(R.drawable.snsrstate_green);
        else
            leftSensor.setImageResource(R.drawable.snsrstate_red);

        if(robot.getRightSensor().getSensorState())
            rightSensor.setImageResource(R.drawable.snsrstate_green);
        else
            rightSensor.setImageResource(R.drawable.snsrstate_red);

        if(robot.getBackSensor().getSensorState())
            backSensor.setImageResource(R.drawable.snsrstate_green);
        else
            backSensor.setImageResource(R.drawable.snsrstate_red);
    }

    public void switchToWinning(int pathLength, float battery_lvl) {
        sensorProcess.interrupt();
        Intent intent = new Intent(this, WinningActivity.class);
        intent.putExtra("distance", pathLength);
        intent.putExtra("energyConsumed", (3500 - battery_lvl));
        Log.v(LOG_TAG, "Jumping to the winning screen. " + pathLength);
        mp.stop();
        startActivity(intent);
    }

    public void switchToLosing(int pathLength, float battery_lvl) {
        sensorProcess.interrupt();
        Intent intent = new Intent(this, LosingActivity.class);
        intent.putExtra("distance", pathLength);
        intent.putExtra("energyConsumed", (3500 - battery_lvl));
        Log.v(LOG_TAG, "Jumping to the losing screen. " + pathLength);
        mp.stop();
        startActivity(intent);
    }

    private void updateEnergyBar(float batteryLevel) {
        int battery_lvl = 3500 - (int) batteryLevel;
        Log.v(LOG_TAG, "The battery level is: " + battery_lvl);
        energy.setProgress(battery_lvl);
    }
}