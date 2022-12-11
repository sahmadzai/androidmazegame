package edu.wm.cs.cs301.amazebyshamsullahahmadzai.gui;

import static edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation.MazeDataHolder.getMaze;
import static edu.wm.cs.cs301.amazebyshamsullahahmadzai.generation.MazeDataHolder.maze;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

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

    MediaPlayer mp;
    private static int distanceTravelled;
    private StatePlaying playState;
    private final int LENGTH_SHORT = 800;
    private final String LOG_TAG = "PlayManuallyActivity";
    private ConstraintLayout constraintLayout;
    private SwipeListener swipeListener;

    private SpeechRecognizer speechRecognizer;
    private Intent intentRecognizer;
    private boolean voiceOn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_manually);

        constraintLayout = findViewById(R.id.constraint_manual);
        swipeListener = new SwipeListener(constraintLayout);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PackageManager.PERMISSION_GRANTED);

        intentRecognizer = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intentRecognizer.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
                Log.v(LOG_TAG, "Ready for speech");
            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String string;
                if (matches != null) {
                    string = matches.get(0);
                    parseSpeech(string);
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        mp = MediaPlayer.create(this, R.raw.playmanualsound);
        mp.setLooping(true);
        mp.start();

        setUpButtons();
        setUpToggleButtons();
        setUpMoveButtons();

        playState = new StatePlaying(this);
        playState.setMaze(getMaze());
        MazePanel panel = findViewById(R.id.maze_view);
        playState.start(panel);

    }

    public void startVoiceListener(View view) {
        speechRecognizer.startListening(intentRecognizer);
        Snackbar.make(view, "Speak your command", Snackbar.LENGTH_SHORT).setDuration(LENGTH_SHORT).show();

    }

    private void parseSpeech(String speechInput) {
        String [] wordArr = speechInput.split("\\s+");
        for (String word :
                wordArr) {
            switch (word) {
                case "forward":
                    Log.v(LOG_TAG, "VC - Moving forward");
                    playState.handleUserInput(Constants.UserInput.UP, 0);
                    break;
                case "left":
                    Log.v(LOG_TAG, "VC - Moving left");
                    playState.handleUserInput(Constants.UserInput.LEFT, 0);
                    break;
                case "right":
                    Log.v(LOG_TAG, "VC - Moving right");
                    playState.handleUserInput(Constants.UserInput.RIGHT, 0);
                    break;
                case "jump":
                    Log.v(LOG_TAG, "VC - Jumping forward");
                    playState.handleUserInput(Constants.UserInput.JUMP, 0);
                    break;
                default:
                    Log.v(LOG_TAG, "Invalid command, please try again.");
            }
        }
    }

    private class SwipeListener implements View.OnTouchListener {

        GestureDetector gestureDetector;

        SwipeListener(View view) {
            int threshold = 100;
            int velocity_threshold = 100;

            GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onDown(MotionEvent e) {
                    return true;
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    float xDiff = e2.getX() - e1.getX();
                    float yDiff = e2.getY() - e1.getY();

                    try {
                        if (Math.abs(xDiff) > Math.abs(yDiff)) {
                            if (Math.abs(xDiff) > threshold && Math.abs(velocityX) > velocity_threshold) {
                                if (xDiff > 0) {
                                    Log.v(LOG_TAG, "Swiped Right");
                                    playState.handleUserInput(Constants.UserInput.RIGHT, 0);
                                } else {
                                    Log.v(LOG_TAG, "Swiped Left");
                                    playState.handleUserInput(Constants.UserInput.LEFT, 0);
                                }
                                return true;
                            }
                        } else {
                            if (Math.abs(yDiff) > threshold && Math.abs(velocityY) > velocity_threshold) {
                                if (yDiff > 0) {
                                    Log.v(LOG_TAG, "Swiped Down");
                                    playState.handleUserInput(Constants.UserInput.JUMP, 0);
                                } else {
                                    Log.v(LOG_TAG, "Swiped Up");
                                    playState.handleUserInput(Constants.UserInput.UP, 0);
                                }
                                return true;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            };
            gestureDetector = new GestureDetector(listener);
            view.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return gestureDetector.onTouchEvent(motionEvent);
        }
    }


    /**
     * This method overrides the default back button behavior to take the user 
     * back to the title screen instead of the previous activity.
     */
    @Override
    public void onBackPressed() {
        // Handle the back button event by going back to the title page
        speechRecognizer.stopListening();
        Intent intent = new Intent(PlayManuallyActivity.this, AMazeActivity.class);
        mp.stop();
        startActivity(intent);
    }

    /**
     * This method sets up the buttons that allow the user to jump to the winning or losing screen.
     * It also sets up a click listener for each button that takes the user to the appropriate screen
     * through an intent and also logs the event.
     */
    private void setUpButtons() {
        // Setting up the jumpToLose button and adding a click listener
//        Button jumpToLose = findViewById(R.id.jumpToLose);
//        jumpToLose.setOnClickListener(view -> switchToLosing(distanceTravelled));

        // Setting up the jumpToWin and adding a click listener
//        Button jumpToWin = findViewById(R.id.jumpToWin);
//        jumpToWin.setOnClickListener(view -> switchToWinning(distanceTravelled));

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
    }

    /**
     * This method sets up the movement buttons that allow the user to move the robot forward, rotate
     * the robot left, rotate the robot right, or make the robot jump. It also sets up a click listener
     * for each button that logs the event. It also updates the distance
     * travelled by the robot which will be passed to the winning or losing activity.
     */
    private void setUpMoveButtons() {
        Button moveForward = findViewById(R.id.move_up);
        Button jump        = findViewById(R.id.jump_btn);
        Button turnLeft    = findViewById(R.id.move_left);
        Button turnRight   = findViewById(R.id.move_right);

        moveForward.setOnClickListener(view -> {
            Log.v(LOG_TAG, "User pressed the move forward button.");
            distanceTravelled++;
            playState.handleUserInput(Constants.UserInput.UP, 0);
        });
        jump.setOnClickListener(view -> {
            Log.v(LOG_TAG, "User pressed the jump button.");
            distanceTravelled++;
            playState.handleUserInput(Constants.UserInput.JUMP, 0);

        });
        turnLeft.setOnClickListener(view -> {
            Log.v(LOG_TAG, "User pressed the turn left button.");
            playState.handleUserInput(Constants.UserInput.LEFT, 0);
        });
        turnRight.setOnClickListener(view -> {
            Log.v(LOG_TAG, "User pressed the turn right button.");
            playState.handleUserInput(Constants.UserInput.RIGHT, 0);
        });
    }

    public void switchToWinning(int pathLength) {
        Intent intent = new Intent(PlayManuallyActivity.this, WinningActivity.class);
        intent.putExtra("distance", pathLength);
        intent.putExtra("shortest_path", maze.getDistanceToExit(maze.getStartingPosition()[0], maze.getStartingPosition()[1]));
        Log.v(LOG_TAG, "Jumping to the winning screen.");
        mp.stop();
        startActivity(intent);
    }

    public void switchToLosing(int pathLength) {
        Intent intent = new Intent(PlayManuallyActivity.this, LosingActivity.class);
        intent.putExtra("distance", pathLength);
        intent.putExtra("shortest_path", maze.getDistanceToExit(maze.getStartingPosition()[0], maze.getStartingPosition()[1]));
        Log.v(LOG_TAG, "Jumping to the losing screen.");
        mp.stop();
        startActivity(intent);
    }


}