package com.example.scorejudge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.TextView;
import android.widget.Toast;

public class ViewSelectedBattleActivity extends AppCompatActivity {
    private TextView selected_battle_name,selected_battle_judge,selected_battle_competitors;
    private String name, judge, competitor;
    private MyDatabaseHelper myDB;
    private Button start_judge_button, delete_battle_button;
    private int checker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_selected_battle);

        selected_battle_name = findViewById(R.id.selected_battle_name);
        selected_battle_judge = findViewById(R.id.selected_battle_judge);
        selected_battle_competitors = findViewById(R.id.selected_battle_competitors);
        name = getAndSetIntentData();
        selected_battle_name.setText(name);
        judge = getJudgeNumber(name);
        selected_battle_judge.setText(judge);
        competitor = getCompetitorNumber(name);
        selected_battle_competitors.setText(competitor);

        start_judge_button = findViewById(R.id.start_judge_button);
        start_judge_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker = checkBattleIsNotFull(name);
                System.out.println("Checker is "+ checker);
                if (checker == 1){
                    startBattle();
                }  else {
                    Toast.makeText(ViewSelectedBattleActivity.this, "Battle is already judged", Toast.LENGTH_SHORT).show();
                }

            }
        });
        delete_battle_button = findViewById(R.id.delete_battle_button);
        delete_battle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDeleteBattle();
            }
        });

    }

    private String getAndSetIntentData(){
        if (getIntent().hasExtra("battleName")){
            name = getIntent().getStringExtra("battleName");
        } else {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }
        return name;
    }

    private String getJudgeNumber(String battleName){
        myDB = new MyDatabaseHelper(ViewSelectedBattleActivity.this);
        Cursor cursor = myDB.getJudgeCount(battleName);
        if (cursor.getCount() == 0){
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()){
                judge = cursor.getString(0);

            }
        }
        return judge;
    }

    private String getCompetitorNumber(String battleName){
        myDB = new MyDatabaseHelper(ViewSelectedBattleActivity.this);
        Cursor cursor = myDB.getCompetitorCount(battleName);
        if (cursor.getCount() == 0){
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()){
                competitor = cursor.getString(0);

            }
        }
        return competitor;
    }

    private void confirmDeleteBattle(){
        Intent intent = new Intent(this, ConfirmDeleteBattleActivity.class);
        intent.putExtra("battleName", name);
        startActivity(intent);
    }

    private void startBattle(){
        Intent intent = new Intent(this,ScoringActivity.class);
        intent.putExtra("battleName", name);
        startActivity(intent);
    }

    private int checkBattleIsNotFull(String battleName){
        System.out.println("hit at battle is not full on activity ");
        myDB = new MyDatabaseHelper(ViewSelectedBattleActivity.this);
        checker = myDB.battleFullChecker(battleName,getJudgeNumber(battleName));

        return checker;
    }
}