package edu.wm.cs.cs301.amazebyshamsullahahmadzai.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import edu.wm.cs.cs301.amazebyshamsullahahmadzai.R;

public class WinningActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning);
    }

    @Override
    public void onBackPressed() {
        // Handle the back button event by going back to the title page
        Intent intent = new Intent(WinningActivity.this, AMazeActivity.class);
        startActivity(intent);
    }
}