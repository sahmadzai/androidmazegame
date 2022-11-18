package edu.wm.cs.cs301.amazebyshamsullahahmadzai.gui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import edu.wm.cs.cs301.amazebyshamsullahahmadzai.R;

public class GeneratingActivity extends AppCompatActivity {

    private String  driveMode;
    private String  robotReliability;
    private Boolean driveModeSelected = false;
    private Boolean robotReliabilitySelected = false;
    private Boolean mazeGenComplete = false;
    private final int LENGTH_SHORT = 800;
    private final String LOG_TAG = "GeneratingActivity";
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generating_layout);

        // Get the data passed in from AMazeActivity and print it in log


        // Initialize and start listeners for the drive method & skill spinners
        initSpinners();

        updateProgressBar();
    }

    @Override
    public void onBackPressed() {
        // Handle the back button event by going back to the title page
        Intent intent = new Intent(GeneratingActivity.this, AMazeActivity.class);
        thread.interrupt();
        startActivity(intent);
    }

    private void initSpinners() {
        Spinner dmethodSpinner = findViewById(R.id.dmethod_spinner);
        Spinner dskillSpinner  = findViewById(R.id.dskill_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.dmethods, R.layout.custom_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Setting the ArrayAdapter data on the Driver Method Spinner
        dmethodSpinner.setAdapter(adapter);
        dmethodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0)
                    driveModeSelected = false;
                else {
                    String text = adapterView.getItemAtPosition(i).toString();
                    driveMode = text;
                    driveModeSelected = true;
                    Log.v(LOG_TAG, "User selected driving method: " + text);
                    if (mazeGenComplete)
                        Snackbar.make(view, "The game will start shortly.", Snackbar.LENGTH_SHORT).setDuration(LENGTH_SHORT).show();
                    else
                        Snackbar.make(view, "A driver type needs to be selected in order to play.", Snackbar.LENGTH_SHORT).setDuration(LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                driveModeSelected = false;
            }
        });

        // Setting the ArrayAdapter data on the Driver Skill Spinner
        adapter = ArrayAdapter.createFromResource(this, R.array.dskills, R.layout.custom_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dskillSpinner.setAdapter(adapter);
        dskillSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)  {
                if (i == 0)
                    robotReliabilitySelected = false;
                else {
                    String text = adapterView.getItemAtPosition(i).toString();
                    robotReliability = text;
                    robotReliabilitySelected = true;
                    Log.v(LOG_TAG, "User selected skill level: " + text);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                robotReliabilitySelected = false;
            }
        });
    }

    private void updateProgressBar() {
        ProgressBar genProgress = findViewById(R.id.gen_progress);
        thread = new Thread(() -> {
            try {
                int i = 1;
                while(genProgress.getProgress() < 100) {
                    genProgress.setProgress(i);
                    i++;
                    Thread.sleep(50);
                }
                mazeGenComplete = true;
                // Check if user has selected choices for method and reliability and move accordingly
                hasUserSelected(Thread.currentThread());

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    private void hasUserSelected(Thread thread) {
        if (driveModeSelected && robotReliabilitySelected) {
            Intent intent;
            if (driveMode.equals("Manual"))
                intent = new Intent(GeneratingActivity.this, PlayManuallyActivity.class);
            else
                intent = new Intent(GeneratingActivity.this, PlayAnimationActivity.class);
            startActivity(intent);
            thread.interrupt();
        } else {
            Log.v(LOG_TAG, "User needs to select an option for driving method and skill.");
            try {
                thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            hasUserSelected(thread);
        }
    }


}
