package com.example.scorejudge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    Button judgeBattleButton, viewJudgeScores;
    FloatingActionButton add_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add_button = findViewById(R.id.addBattleButton);
        add_button.setOnClickListener(view -> openCreateBattle());

        judgeBattleButton = findViewById(R.id.judgeBattleButton);
        judgeBattleButton.setOnClickListener(view -> judgeExistingBattle());

        viewJudgeScores = findViewById(R.id.viewJudgeScores);
        viewJudgeScores.setOnClickListener(view -> viewScores());

    }

    private void openCreateBattle(){
        Intent intent = new Intent(this, CreateNewBattleActivity.class);
        startActivity(intent);
    }

    private void judgeExistingBattle(){
        Intent intent = new Intent(this, SelectBattleActivity.class);
        startActivity(intent);
    }

    private void viewScores(){
        Intent intent = new Intent(this, ShowBattlesActivity.class);
        startActivity(intent);
    }

}