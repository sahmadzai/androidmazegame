package edu.wm.cs.cs301.amazebyshamsullahahmadzai.gui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import edu.wm.cs.cs301.amazebyshamsullahahmadzai.CustomView_Manual;
import edu.wm.cs.cs301.amazebyshamsullahahmadzai.R;

public class PlayManuallyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_manually);

        CustomView_Manual maze_view = new CustomView_Manual(this);
    }
}