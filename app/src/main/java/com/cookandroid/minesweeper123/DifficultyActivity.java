package com.cookandroid.minesweeper123;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class DifficultyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);
    }

    public void onSelectDifficulty(View view) {
        int rows = 0, cols = 0, mines = 0;

        int id = view.getId();
        if (id == R.id.easyButton) {
            rows = 10;
            cols = 10;
            mines = 12;
        } else if (id == R.id.mediumButton) {
            rows = 16;
            cols = 16;
            mines = 40;
        } else if (id == R.id.hardButton) {
            rows = 30;
            cols = 16;
            mines = 99;
        } else if (id == R.id.deadlyButton) {
            rows = 36;
            cols = 25;
            mines = 200;
        }

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("rows", rows);
        intent.putExtra("cols", cols);
        intent.putExtra("mines", mines);
        startActivity(intent);
    }
}
