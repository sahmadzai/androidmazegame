package edu.wm.cs.cs301.amazebyshamsullahahmadzai.gui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.wm.cs.cs301.amazebyshamsullahahmadzai.R;

public class GeneratingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generating_layout);

        Spinner dmethodSpinner = findViewById(R.id.dmethod_spinner);
        Spinner dskillSpinner  = findViewById(R.id.dskill_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.dmethods, R.layout.custom_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Setting the ArrayAdapter data on the Driver Method Spinner
        dmethodSpinner.setAdapter(adapter);
        dmethodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(adapterView.getContext(),text,Toast.LENGTH_SHORT).show();
                Log.v("USER INPUT:", "User selected driving method: " + text);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Setting the ArrayAdapter data on the Driver Skill Spinner
        adapter = ArrayAdapter.createFromResource(this, R.array.dskills, R.layout.custom_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dskillSpinner.setAdapter(adapter);
        dskillSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)  {
                String text = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(adapterView.getContext(),text,Toast.LENGTH_SHORT).show();
                Log.v("USER INPUT:", "User selected skill level: " + text);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        updateProgressBar();
    }

    private void updateProgressBar() {
        ProgressBar genProgress = findViewById(R.id.gen_progress);

        Thread thread = new Thread(() -> {
            try {
                int i = 1;
                while(genProgress.getProgress() < 100) {
                    genProgress.setProgress(i);
                    i++;
                    Thread.sleep(50);
                }
                Intent intent = new Intent(GeneratingActivity.this, PlayAnimationActivity.class);
                startActivity(intent);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }


}
