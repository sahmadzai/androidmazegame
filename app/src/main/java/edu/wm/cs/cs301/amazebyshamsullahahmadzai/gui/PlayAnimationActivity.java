package edu.wm.cs.cs301.amazebyshamsullahahmadzai.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import edu.wm.cs.cs301.amazebyshamsullahahmadzai.CustomView_Anim;
import edu.wm.cs.cs301.amazebyshamsullahahmadzai.R;

public class PlayAnimationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_animation);

        CustomView_Anim maze_view = new CustomView_Anim(this);
    }
}