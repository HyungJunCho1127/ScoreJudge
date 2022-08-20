package com.example.scorejudge;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Arrays;

public class MyDatabaseHelper extends SQLiteOpenHelper {

        private Context context;
        private long result;
        private static final String DATABASE_NAME = "Table.db";
        private static final int DATABASE_VERSION = 1;
        private static final String TABLE_NAME = "battle_scores";
        private static final String COLUMN_ID = "_id";
        private static final String COLUMN_NAME = "battle_name";
        private static final String COLUMN_JUDGE = "judge_count";
        private static final String COLUMN_ENTRY = "entry_count";
        private static final String COLUMN_SCORE1 = "score1_count";
        private static final String COLUMN_SCORE2 = "score2_count";
        private static final String COLUMN_SCORE3 = "score3_count";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT," +
                COLUMN_JUDGE + " TEXT," +
                COLUMN_ENTRY + " TEXT," +
                COLUMN_SCORE1 + " TEXT," +
                COLUMN_SCORE2 + " TEXT," +
                COLUMN_SCORE3 + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public void createBattle(String name, String judgeNo, String entry){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_JUDGE, judgeNo);
        cv.put(COLUMN_ENTRY, entry);
        cv.put(COLUMN_SCORE1, "empty");
        cv.put(COLUMN_SCORE2, "empty");
        cv.put(COLUMN_SCORE3, "empty");
        long result = db.insert(TABLE_NAME, null,cv);
        if (result == -1){
            Toast.makeText(context, "Failed to create", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Battle Created", Toast.LENGTH_SHORT).show();
        }

    }

    public void insertJudgeScore(String battleName, int[] scoreArray){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        // cursor to check if column is null
        Cursor cursor1 = getJudgeScoreColumn1(battleName);
        Cursor cursor2 = getJudgeScoreColumn2(battleName);
        Cursor cursor3 = getJudgeScoreColumn3(battleName);
        int i = 0;
        while (i != 1) {
            cursor1.moveToNext();
            cursor2.moveToNext();
            cursor3.moveToNext();
            if (cursor1.getString(0).equals("empty")) {
                cv.put(COLUMN_SCORE1, Arrays.toString(scoreArray));
            }  else if (cursor2.getString(0).equals("empty")){
                cv.put(COLUMN_SCORE2, Arrays.toString(scoreArray));
            } else if (cursor3.getString(0).equals("empty")){
                cv.put(COLUMN_SCORE3, Arrays.toString(scoreArray));
            }
            i++;

        }



        try {
            result = db.update(TABLE_NAME,cv,"battle_name=?", new String[]{battleName});
        } catch (IllegalArgumentException e){
            Toast.makeText(context, "Battle Already Full", Toast.LENGTH_SHORT).show();
        }

        if (result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Scores Added", Toast.LENGTH_SHORT).show();
        }

    }

    public Cursor readAllData(){
        String query = "SELECT " + COLUMN_NAME + " FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null){
            cursor =db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor getCompetitorCount(String battleName){
        String query = "SELECT entry_count FROM " + TABLE_NAME + " WHERE battle_name = " + "'" +battleName+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);

        }
        return cursor;
    }

    public Cursor getJudgeScoreColumn1(String battleName){
        String query = "SELECT " + COLUMN_SCORE1 + " FROM " + TABLE_NAME + " WHERE battle_name = " + "'" +battleName+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null){
            cursor =db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor getJudgeScoreColumn2(String battleName){
        String query = "SELECT " + COLUMN_SCORE2 + " FROM " + TABLE_NAME + " WHERE battle_name = " + "'" +battleName+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null){
            cursor =db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor getJudgeScoreColumn3(String battleName){
        String query = "SELECT " + COLUMN_SCORE3 + " FROM " + TABLE_NAME + " WHERE battle_name = " + "'" +battleName+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null){
            cursor =db.rawQuery(query, null);
        }
        return cursor;
    }
}
