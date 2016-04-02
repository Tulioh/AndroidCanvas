package com.tulio.projecttotestthings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    private void initialize() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_content, CanvasFragment.newInstance())
                .commit();
    }
}
