package edu.wm.cs.cs301.amazebyshamsullahahmadzai;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class PlayManuallyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_manually);

        CustomView maze_view = new CustomView(this);
    }
}