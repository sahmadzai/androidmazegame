package edu.wm.cs.cs301.amazebyshamsullahahmadzai.gui;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import edu.wm.cs.cs301.amazebyshamsullahahmadzai.R;

/**
 * This activity is called when the user wins the game. It displays a winning title and phrase,
 * the path length, the energy consumption, and the shortest possible path length. It also allows the
 * user to go back to the title page and play again.
 * 
 * @author Shamsullah Ahmadzai
 * 
 */

public class WinningActivity extends AppCompatActivity {

    MediaPlayer mp;
    private final String LOG_TAG = "WinningActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning);

        // Media player for the losing sound effect
        mp = MediaPlayer.create(this, R.raw.winning);
        mp.setLooping(true);
        mp.start();

        Intent intent = getIntent();                                                         // Get the intent that started this activity
        int distanceTraveled = intent.getIntExtra("distance", 0);                      // Get the distance traveled from the intent
        int shortest_path = intent.getIntExtra("shortest_path", 0);

        String tempString = "Total Path Length: " + distanceTraveled;                        // Create a string that displays the total path length given by the Intent
        TextView path_len = findViewById(R.id.path_length);                                  // Get the text view that displays the total path length
        path_len.setText(tempString);                                                        // Set the text view to the string

        TextView shortest_path_len = findViewById(R.id.shortest_path);
        tempString = "Shortest Path Length: " + shortest_path;
        shortest_path_len.setText(tempString);

        if (intent.hasExtra("energyConsumed")) {
            float energyConsumed = intent.getFloatExtra("energyConsumed", 0);
            TextView energyConsumption = findViewById(R.id.energy_consumption);                  // Get the text view that displays the energy consumption
            tempString = "Total Energy Consumption: " + energyConsumed;                          // Create a string that displays the total energy consumption given by the Intent
            energyConsumption.setText(tempString);                                               // Set the text view to the string
        }
        Button playAgain = findViewById(R.id.play_again);                                    // Get the button that allows the user to play again
        playAgain.setOnClickListener(view -> {                                               // Set the on click listener for the button
            onBackPressed();                                                                 // Call the onBackPressed method which takes the user back to the title page
            Log.v(LOG_TAG, "User pressed the play again button.");                      // Log that the user pressed the play again button
        });
    }

    /**
     * This method overrides the default back button behavior to take the user 
     * back to the title screen instead of the previous activity.
     */
    @Override
    public void onBackPressed() {
        // Handle the back button event by going back to the title page
        Intent intent = new Intent(WinningActivity.this, AMazeActivity.class);
        startActivity(intent);
        mp.stop();
    }
}