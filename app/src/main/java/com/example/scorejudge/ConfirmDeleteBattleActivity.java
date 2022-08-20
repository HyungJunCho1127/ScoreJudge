package com.example.scorejudge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ConfirmDeleteBattleActivity extends AppCompatActivity {
    private TextView delete_battle_name;
    private String name;
    private Button yes,no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_delete_battle);

        delete_battle_name = findViewById(R.id.delete_battle_name);
        yes = findViewById(R.id.yes_delete_button);
        no = findViewById(R.id.no_delete_button);

        if (getIntent().hasExtra("battleName")){
            name = getIntent().getStringExtra("battleName");
            delete_battle_name.setText(name);
        } else {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(ConfirmDeleteBattleActivity.this);
                myDB.deleteRow(name);
                backHome();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backHome();
            }
        });
    }

    public void backHome(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}