package edu.wm.cs.cs301.amazebyshamsullahahmadzai.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import edu.wm.cs.cs301.amazebyshamsullahahmadzai.R;

public class WinningActivity extends AppCompatActivity {

    private final String LOG_TAG = "WinningActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning);

        Button playAgain = findViewById(R.id.play_again);
        playAgain.setOnClickListener(view -> {
            onBackPressed();
            Log.v(LOG_TAG, "User pressed the play again button.");
        });
    }

    @Override
    public void onBackPressed() {
        // Handle the back button event by going back to the title page
        Intent intent = new Intent(WinningActivity.this, AMazeActivity.class);
        startActivity(intent);
    }
}