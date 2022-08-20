package com.example.scorejudge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ScoringActivity extends AppCompatActivity {
    private TextView battleName, competitorCount, competitor_text;
    private RadioGroup radioGroup;
    private RadioButton rb1,rb2,rb3,rb4,rb5,rb6,rb7,rb8,rb9,rb10;
    private Button nextButton;
    private String name;
    private Score score;
    private int competitorTotal;
    private int position;
    private boolean answered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoring);
        battleName = findViewById(R.id.scoringName);
        // set battle name
        name = getAndSetIntentData();
        // declare
        competitor_text = findViewById(R.id.competitor_text);
        radioGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        rb4 = findViewById(R.id.radio_button4);
        rb5 = findViewById(R.id.radio_button5);
        rb6 = findViewById(R.id.radio_button6);
        rb7 = findViewById(R.id.radio_button7);
        rb8 = findViewById(R.id.radio_button8);
        rb9 = findViewById(R.id.radio_button9);
        rb10 = findViewById(R.id.radio_button10);
        nextButton = findViewById(R.id.next_button);
        competitorCount = findViewById(R.id.competitor_count_text);
        //start battle
        score = startBattle(name);
        score.setBattleName(name);
        competitorTotal = score.getScoreArray().length;

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check if battle should end
                if (!answered) {
                    // check if rb is checked
                        if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()
                                || rb5.isChecked() || rb6.isChecked() || rb7.isChecked() || rb8.isChecked()
                                || rb9.isChecked() || rb10.isChecked()) {
                            // get next position
                            getNextBattler();
                        } else {
                            Toast.makeText(ScoringActivity.this, "Please Select Score", Toast.LENGTH_SHORT).show();
                        }

                } else {
                    loadInformation();
                }
            }
        });

    }

    private void loadInformation(){
        radioGroup.clearCheck();

        if (position < competitorTotal) {
            position++;
            answered = false;
            nextButton.setText("Confirm");
        }
    }

    private void getNextBattler(){

        RadioButton rbSelected = findViewById(radioGroup.getCheckedRadioButtonId());
        // save input into int array
        int input = radioGroup.indexOfChild(rbSelected) +1;
        switch (input) {
            case 1:
                input = 10;
                break;
            case 2:
                input = 9;
                break;
            case 3:
                input = 8;
                break;
            case 4:
                input = 7;
                break;
            case 5:
                input = 6;
                break;
            case 6:
                input = 5;
                break;
            case 7:
                input = 4;
                break;
            case 8:
                input = 3;
                break;
            case 9:
                input = 2;
                break;
            case 10:
                input = 1;
                break;
        }
        score.getScoreArray()[score.getPosition()] = input;

        score.increasePosition(score.getPosition());

        if (position >= competitorTotal) {
            finishBattle();
        }
        position++;
        if (score.getPosition() < competitorTotal){
            nextButton.setText("Next");
        } else {
            nextButton.setText("Finish");
        }
        if (position  > competitorTotal){
            competitor_text.setText("Finished");
        } else {
            competitorCount.setText(String.valueOf(position));
        }

        radioGroup.clearCheck();
    }

    private void finishBattle(){
        // insert into database
        MyDatabaseHelper myDB = new MyDatabaseHelper(ScoringActivity.this);
        myDB.insertJudgeScore(score.getBattleName() ,score.getScoreArray());

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private String getAndSetIntentData(){
        if (getIntent().hasExtra("battleName")){
            name = getIntent().getStringExtra("battleName");
            battleName.setText(name);
        } else {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }
        return name;
    }
    private Score startBattle(String battleName){
        score = new Score();
        MyDatabaseHelper myDB = new MyDatabaseHelper(ScoringActivity.this);
        Cursor cursor = myDB.getCompetitorCount(battleName);
        if (cursor.getCount() == 0){
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()){
                score.setScoreArray(new int[Integer.parseInt(cursor.getString(0))]);
            ;
            }
        }
        //set text for competitor
        position = score.getPosition();
        competitorCount.setText(String.valueOf(position + 1));
        position++;
        return score;
    }
}