package edu.wm.cs.cs301.amazebyshamsullahahmadzai.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import edu.wm.cs.cs301.amazebyshamsullahahmadzai.R;

public class AMazeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title_layout);

        Spinner spinner = findViewById(R.id.genmethod_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gen_methods, R.layout.custom_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        // Setting up the NewMazeButton and adding a click listener
        Button newMazeBtn = findViewById(R.id.newmazebtn);
        newMazeBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, GeneratingActivity.class);
            startActivity(intent);
        });

        // Setting up the OldMazeButton and adding a click listener
        Button oldMazeBtn = findViewById(R.id.oldmazebtn);
        oldMazeBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, GeneratingActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(),text,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void onRadioButtonClicked(View view) {
        Toast.makeText(view.getContext(), "Checkbox has been clicked", Toast.LENGTH_SHORT).show();
    }
}