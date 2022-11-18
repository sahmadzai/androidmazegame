package edu.wm.cs.cs301.amazebyshamsullahahmadzai.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import edu.wm.cs.cs301.amazebyshamsullahahmadzai.R;

public class LosingActivity extends AppCompatActivity {

    private final String LOG_TAG = "LosingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_losing);

        Button playAgain = findViewById(R.id.play_again);
        playAgain.setOnClickListener(view -> {
            onBackPressed();
            Log.v(LOG_TAG, "User pressed the play again button.");
        });
    }

    @Override
    public void onBackPressed() {
        // Handle the back button event by going back to the title page
        Intent intent = new Intent(LosingActivity.this, AMazeActivity.class);
        startActivity(intent);
    }
}