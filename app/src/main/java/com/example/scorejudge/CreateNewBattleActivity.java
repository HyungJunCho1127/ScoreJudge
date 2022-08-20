package com.example.scorejudge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class CreateNewBattleActivity extends AppCompatActivity {
    EditText battleName, entry;
    Spinner spinner;
    Button createBattleButton;

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
                MyDatabaseHelper myDB = new MyDatabaseHelper(CreateNewBattleActivity.this);
                myDB.createBattle(battleName.getText().toString(), spinner.getSelectedItem().toString(), entry.getText().toString());
                backToHome();
            }
        });

    }

    private void backToHome(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}