package com.example.scorejudge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CreateNewBattleActivity extends AppCompatActivity {
    private EditText battleName, entry;
    private Spinner spinner;
    private Button createBattleButton;
    private Boolean checker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_battle);

        // spinner for judge list. values are in Strings XML in values.
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(CreateNewBattleActivity.this,
                    android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.judgeNo));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(myAdapter);

        battleName = findViewById(R.id.battleName);
        entry = findViewById(R.id.competitorNo);
        createBattleButton = findViewById(R.id.createBattleButton);

        createBattleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (battleName.getText().toString().length() == 0 || spinner.getSelectedItem().toString().length() == 0
                        || entry.getText().toString().length() == 0) {
                    Toast.makeText(CreateNewBattleActivity.this, "Please fill out all blank sections", Toast.LENGTH_SHORT).show();
                } else {
                    checker = checkIfBattleNameExists(battleName.getText().toString());
                    if (!checker){
                        createBattle(battleName.getText().toString(), spinner.getSelectedItem().toString(), entry.getText().toString());
                        backToHome();
                    } else {
                        Toast.makeText(CreateNewBattleActivity.this, "Battle Already Exists \n Choose Different Name", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void backToHome(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private boolean checkIfBattleNameExists(String battleName){
        MyDatabaseHelper myDB = new MyDatabaseHelper(this);
        Cursor cursor = myDB.checkBattleNameExists(battleName);
        cursor.moveToNext();
        if (cursor.isLast()) {
            return true;
        }
        return false;
    }

    private void createBattle(String battleName, String judge, String competitor){
        MyDatabaseHelper myDB = new MyDatabaseHelper(CreateNewBattleActivity.this);
        myDB.createBattle(battleName, judge, competitor);

    }
}