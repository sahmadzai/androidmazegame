package edu.wm.cs.cs301.amazebyshamsullahahmadzai.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import edu.wm.cs.cs301.amazebyshamsullahahmadzai.R;

public class AMazeActivity extends AppCompatActivity {

    private String gen_method = "DFS";
    private int skill_level = 0;
    private boolean rooms = true;
    private int seed;
    private final int LENGTH_SHORT = 800;
    private final String LOG_TAG = "AMazeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title_layout);

        // Setup and start the seekbar listener
        initSeekBar();
        // Initialize the generation method spinner and fill in values
        initSpinner();
        // Set up the maze buttons and start the listeners
        setUpButtons();
    }

    private void initSeekBar() {
        // Initialize seekbar
        SeekBar skillSeekBar = findViewById(R.id.difficulty);
        skillSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                skill_level = i;
                Log.v(LOG_TAG, "Difficulty is now: " + skill_level);
                String text = "Difficulty is now: " + skill_level;
                Snackbar.make(seekBar, text, Snackbar.LENGTH_SHORT).setDuration(LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initSpinner() {
        Spinner spinner = findViewById(R.id.genmethod_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gen_methods, R.layout.custom_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                if (i == 0)
                    Log.v(LOG_TAG, "User has not selected anything yet.");
                else {
                    String text = parent.getItemAtPosition(i).toString();
                    gen_method = text;
                    Snackbar.make(parent, text, Snackbar.LENGTH_SHORT).setDuration(LENGTH_SHORT).show();
                    Log.v(LOG_TAG, "User selected " + text + " as the generation method.");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.v(LOG_TAG, "Nothing has been selected for the generation method.");
                gen_method = null;
            }
        });
    }

    private void setUpButtons() {
        // Setting up the NewMazeButton and adding a click listener
        Button newMazeBtn = findViewById(R.id.newmazebtn);
        newMazeBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, GeneratingActivity.class);
            Log.v(LOG_TAG, "Generating a NEW Maze.");
            Bundle extras = new Bundle();
            extras.putInt("skill_level", skill_level);
            extras.putString("gen_method", gen_method);
            extras.putBoolean("gen_rooms", rooms);
            extras.putInt("seed", seed);
            intent.putExtras(extras);
            startActivity(intent);
        });

        // Setting up the OldMazeButton and adding a click listener
        Button oldMazeBtn = findViewById(R.id.oldmazebtn);
        oldMazeBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, GeneratingActivity.class);
            Log.v(LOG_TAG, "Revisiting the OLD Maze.");
            Bundle extras = new Bundle();
            extras.putInt("skill_level", skill_level);
            extras.putString("gen_method", gen_method);
            extras.putBoolean("gen_rooms", rooms);
            extras.putInt("seed", seed);
            intent.putExtras(extras);
            startActivity(intent);
        });
    }

    public void onRoomChoice(View view) {
        RadioButton radio_yes = findViewById(R.id.radio_yes);

        if (radio_yes.isChecked()) {
            rooms = true;
            Log.v(LOG_TAG, "User selected YES for room generation.");
            Snackbar.make(view, "Rooms enabled", Snackbar.LENGTH_SHORT).setDuration(LENGTH_SHORT).show();
        } else {
            rooms = false;
            Log.v(LOG_TAG, "User selected NO for room generation.");
            Snackbar.make(view, "Rooms disabled", Snackbar.LENGTH_SHORT).setDuration(LENGTH_SHORT).show();
        }
    }
}