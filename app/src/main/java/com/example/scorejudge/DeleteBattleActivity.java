package com.example.scorejudge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class DeleteBattleActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    MyDatabaseHelper myDB;
    ArrayList<String> battle_name;
    DeleteListAdapter deleteListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_battle);
        recyclerView = findViewById(R.id.view);

        myDB = new MyDatabaseHelper(DeleteBattleActivity.this);
        battle_name = new ArrayList<>();

        storeDataInArrays();
        deleteListAdapter = new DeleteListAdapter(DeleteBattleActivity.this, battle_name);
        recyclerView.setAdapter(deleteListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(DeleteBattleActivity.this));
    }

    private void storeDataInArrays(){
        //using cursor
        Cursor cursor = myDB.readAllData();
        if (cursor.getCount() == 0){
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()){
                battle_name.add(cursor.getString(0));
            }
        }
    }
}