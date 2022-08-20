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
    private ArrayList<Ranking> rankingArrayList;
    private ArrayList<Ranking> addedRankingList;
    private ArrayList<String> scoredAndAddedList;
    private ArrayList<Ranking> rank1;
    private ArrayList<Ranking> rank2;
    private ArrayList<Ranking> rank3;
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
        scores2 = new ArrayList<>();
        scores3 = new ArrayList<>();

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
        scoreList1 = new String[1];
        scoreList2 = new String[1];
        scoreList3 = new String[1];

        int i = 0;
        while (i != 1) {
            cursor1.moveToNext();
            cursor2.moveToNext();
            cursor3.moveToNext();


            scoreList1 = makeIntoStringArray(cursor1.getString(0));
            scoreList2 = makeIntoStringArray(cursor2.getString(0));
            scoreList3 = makeIntoStringArray(cursor3.getString(0));
            i++;
        }
        scoredAndAddedList = putRankings(scoreList1, scoreList2, scoreList3);
        return scoredAndAddedList;
    }

    private String[] makeIntoStringArray(String score){
        newScoreLine = score.replace("[", "").replace("]", "").replace(" ", "");
        scoreList = newScoreLine.split(",");
        return scoreList;

    }

    private ArrayList<String> putRankings(String[] score1, String[] score2, String[] score3){
        rank1 = new ArrayList<>();
        rank2 = new ArrayList<>();
        rank3 = new ArrayList<>();
        System.out.println("Score 1 is "  + score1[0]);
        if (score1[0].equals("empty")){
            rank1.add(new Ranking(0,0,-1));
        } else {
            rank1 = addPositionAndParse(score1);
        }
        if (score2[0].equals("Closed")){
            rank2.add(new Ranking(0,0,-1));
        } else {
            rank2 = addPositionAndParse(score2);
        }
        if (score3[0].equals("Closed")){
            rank3.add(new Ranking(0,0,-1));
        } else {
            rank3 = addPositionAndParse(score3);
        }

        rankingArrayList = addAllScores(rank1,rank2,rank3);
        Ranking temp;
        // check if there is no scores big inside If
        scoredAndAddedList = new ArrayList<>();
        if (rankingArrayList.get(0).getCompetitorScore() != -1) {

            for (int i = 0; i < rankingArrayList.size(); i++) {
                for (int x = 0; x < rankingArrayList.size() - 1; x++) {
                    if (rankingArrayList.get(x).getCompetitorScore() < rankingArrayList.get(x + 1).getCompetitorScore()) {
                        temp = rankingArrayList.get(x);
                        rankingArrayList.set(x, rankingArrayList.get(x + 1));
                        rankingArrayList.set(x + 1, temp);
                    }
                }
            }
            for (int i = 0; i < rankingArrayList.size(); i++) {
                rankingArrayList.get(i).setCompetitorRank(i + 1);
                scoredAndAddedList.add("Rank " + rankingArrayList.get(i).getCompetitorRank() +
                        "    position " + rankingArrayList.get(i).getCompetitorPosition() +
                        "    Score " + rankingArrayList.get(i).getCompetitorScore());

            }

        } else {
            scoredAndAddedList.add("No Data");
        }
        return scoredAndAddedList;
    }

    private ArrayList<Ranking> addPositionAndParse(String[] score){
        rankingArrayList = new ArrayList<>();
        if (score[0].equals("empty")){
            rankingArrayList.add(new Ranking(0,(0),-1));
            return rankingArrayList;
        } else {
            for (int i = 0; i < score.length;i++){
                rankingArrayList.add(new Ranking(0,(i+1),Integer.parseInt(score[i])));
            }
        }
        return rankingArrayList;
    }

    private ArrayList<Ranking> addAllScores(ArrayList<Ranking> score1, ArrayList<Ranking> score2, ArrayList<Ranking> score3){

        addedRankingList = new ArrayList<>();
        if (score3.get(0).getCompetitorScore() == -1 && score2.get(0).getCompetitorScore() == -1){
            for (int i = 0; i < score1.size();i++){
                addedRankingList.add( new Ranking(0, (i+1),score1.get(i).getCompetitorScore()));
            }

        } else if (score3.get(0).getCompetitorScore() == -1 && score2.get(0).getCompetitorScore() != -1){
            for (int i = 0; i < score1.size();i++){
                addedRankingList.add( new Ranking(0, (i+1),(score1.get(i).getCompetitorScore() + score2.get(i).getCompetitorScore())));
            }

        } else  {
            for (int i = 0; i < score1.size();i++){
                addedRankingList.add( new Ranking(0, (i+1),(score1.get(i).getCompetitorScore() + score2.get(i).getCompetitorScore()
                + score3.get(i).getCompetitorScore())));
            }
        }
        return addedRankingList;
    }
}