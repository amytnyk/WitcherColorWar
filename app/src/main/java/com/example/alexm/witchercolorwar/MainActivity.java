package com.example.alexm.witchercolorwar;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {
   private SurfaceView mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        mainView = new GameView(this);
        setContentView(mainView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mainView = new GameView(this);
        setContentView(mainView);
    }

}
