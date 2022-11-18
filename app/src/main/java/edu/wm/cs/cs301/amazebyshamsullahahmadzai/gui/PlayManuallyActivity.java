package edu.wm.cs.cs301.amazebyshamsullahahmadzai.gui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import edu.wm.cs.cs301.amazebyshamsullahahmadzai.CustomView_Manual;
import edu.wm.cs.cs301.amazebyshamsullahahmadzai.R;

public class PlayManuallyActivity extends AppCompatActivity {

    private final int LENGTH_SHORT = 800;
    private final String LOG_TAG = "PlayManuallyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_manually);

        CustomView_Manual maze_view = new CustomView_Manual(this);
    }

    @Override
    public void onBackPressed() {
        // Handle the back button event by going back to the title page
        Intent intent = new Intent(PlayManuallyActivity.this, AMazeActivity.class);
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
}