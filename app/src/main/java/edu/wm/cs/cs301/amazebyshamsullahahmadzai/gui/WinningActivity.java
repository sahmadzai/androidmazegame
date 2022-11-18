package edu.wm.cs.cs301.amazebyshamsullahahmadzai.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import edu.wm.cs.cs301.amazebyshamsullahahmadzai.R;

public class WinningActivity extends AppCompatActivity {

    private int distanceTraveled;
    private final String LOG_TAG = "WinningActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning);

        Intent intent = getIntent();
        distanceTraveled = intent.getIntExtra("distance", 0);

        String tempString = "Total Path Length: " + distanceTraveled;
        TextView path_len = findViewById(R.id.path_length);
        path_len.setText(tempString);

        TextView energyConsumed = findViewById(R.id.energy_consumption);
        tempString = "Total Energy Consumption: " + (3500-distanceTraveled);
        energyConsumed.setText(tempString);

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