package com.example.scorejudge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class DisplayScoresActivity extends AppCompatActivity {
    private TextView battleName;
    private String name;
    private RecyclerView recyclerView;
    private MyDatabaseHelper myDB;
    private ArrayList<String> scores1;
    private ArrayList<String> scores2;
    private ArrayList<String> scores3;
    private ScoreCalculatorAdapter scoreCalculatorAdapter;

    private String newScoreLine,newScoreLine1,newScoreLine2,newScoreLine3;
    private String[] scoreList, scoreList1, scoreList2,scoreList3;
    private String[] addedScores;
    private String scoresAdded;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_scores);
        battleName = findViewById(R.id.view_score_battle_name);
        getAndSetIntentData();

        recyclerView = findViewById(R.id.recyclerScore);

        myDB = new MyDatabaseHelper(DisplayScoresActivity.this);
        scores1 = new ArrayList<>();

        scores1 = storeDataInArrays();
        scoreCalculatorAdapter = new ScoreCalculatorAdapter(DisplayScoresActivity.this, scores1);
        recyclerView.setAdapter(scoreCalculatorAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(DisplayScoresActivity.this));
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

    private ArrayList<String> storeDataInArrays(){
        //using cursor
        Cursor cursor1 = myDB.getJudgeScoreColumn1(name);
        Cursor cursor2 = myDB.getJudgeScoreColumn2(name);
        Cursor cursor3 = myDB.getJudgeScoreColumn3(name);

        // judge scores 1
        if (cursor1.getCount() > 1){
            scores1.add("No Data Found");
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor1.moveToNext()){
                scores1.add(cursor1.getString(0));
            }
        }
        if (scores1.isEmpty()){
            scores1.add("No Data");
            scores1.add("Judge Battle to View Scores");
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }

        scores1 = calculateScores(scores1.get(0));
        return scores1;
    }

    // not yet
    private String addAllScores(String score1,String score2,String score3){
        if (score3 == null){
            score3 = "0";
        }
        if (score2 == null){
            score3 = "0";
        }
        newScoreLine1 = score1.replace("[", "").replace("]", "").replace(" ", "");
        newScoreLine2 = score2.replace("[", "").replace("]", "").replace(" ", "");
        newScoreLine3 = score3.replace("[", "").replace("]", "").replace(" ", "");
        scoreList1 = newScoreLine1.split(",");
        scoreList2 = newScoreLine2.split(",");
        scoreList3 = newScoreLine3.split(",");

        for (int i = 0; i < scoreList1.length;i++){
            addedScores[i] = String.valueOf(Integer.parseInt(scoreList1[i]) +
                    Integer.parseInt(scoreList2[i]) + Integer.parseInt(scoreList3[i]));
        }
        score1 = Arrays.toString(addedScores);
        return score1;
    }

    private ArrayList<String> calculateScores(String score){
        ArrayList<Ranking> rankingArrayList = new ArrayList<>();
        if (score.equals("No Data")){
            return scores1;
        }
        newScoreLine = score.replace("[", "").replace("]", "").replace(" ", "");
        scoreList = newScoreLine.split(",");

        for (int i = 0; i < scoreList.length;i++){
            rankingArrayList.add(new Ranking(0,(i+1),Integer.parseInt(scoreList[i])));
        }
        Ranking rank;
        for(int i = 0; i < rankingArrayList.size(); i++)
        {
            for(int x = 0; x < rankingArrayList.size() - 1; x++)
            {
                if(rankingArrayList.get(x).getCompetitorScore() < rankingArrayList.get(x + 1).getCompetitorScore())
                {
                    rank = rankingArrayList.get(x);
                    rankingArrayList.set(x,rankingArrayList.get(x + 1));
                    rankingArrayList.set(x + 1, rank);
                }
            }
        }

        for (int i = 0; i < rankingArrayList.size();i++){
            rankingArrayList.get(i).setCompetitorRank(i+1);
            scores1.add("Rank " + rankingArrayList.get(i).getCompetitorRank() +" position "+ rankingArrayList.get(i).getCompetitorPosition() +
                    " Score " + rankingArrayList.get(i).getCompetitorScore());
        }
        scores1.remove(0);
        return scores1;
    }
}