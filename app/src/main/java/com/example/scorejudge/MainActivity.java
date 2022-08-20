package com.example.scorejudge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button newBattleButton, judgeBattleButton, viewJudgeScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newBattleButton = findViewById(R.id.newBattleButton);
        newBattleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreateBattle();
            }
        });

        judgeBattleButton = findViewById(R.id.judgeBattleButton);
        judgeBattleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                judgeExistingBattle();
            }
        });

        viewJudgeScores = findViewById(R.id.viewJudgeScores);
        viewJudgeScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewScores();
            }
        });

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