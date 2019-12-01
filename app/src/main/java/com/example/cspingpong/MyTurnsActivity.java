package com.example.cspingpong;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MyTurnsActivity extends AppCompatActivity {

    private TextView welcomePlayerTxt;
    private Button returnBtn;
    private String username;
    private ArrayList<Game> gameList;

    RecyclerView myTurnsRecyclerView;
    MyTurnAdapter myTurnAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_turns);

        myTurnsRecyclerView = findViewById(R.id.recyclerViewTurns);
        myTurnsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        welcomePlayerTxt = findViewById(R.id.welcomePlayerTxt);

        returnBtn = findViewById(R.id.returnBtn);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent incomingIntent = getIntent();
        username = incomingIntent.getStringExtra("username");
        gameList = incomingIntent.getParcelableArrayListExtra("game_list");

        welcomePlayerTxt.setText(username +"'s Turns:");

        myTurnAdapter = new MyTurnAdapter(this, getMyList());
        myTurnsRecyclerView.setAdapter(myTurnAdapter);
    }

    private ArrayList<MyTurnSlot> getMyList() {
        
        ArrayList<MyTurnSlot> turns = new ArrayList<>();

        for (Game game : gameList)
        {
            MyTurnSlot slot = new MyTurnSlot();

            slot.setTurnTime(game.getDateString() + " at " + game.getTimeString());

            if (username.equals(game.getPlayer1())) {
                slot.setTurnAgainst("Playing against: " + game.getPlayer2());
            }
            else if (username.equals(game.getPlayer2())) {
                slot.setTurnAgainst("Playing against: " + game.getPlayer1());
            }
            else {
                slot.setTurnAgainst("Waiting for an opponent");
            }

            if (game.isFull()) {
                slot.setSlotImage(R.drawable.two_racket_icon);
            }
            else {
                slot.setSlotImage(R.drawable.single_racket_icon);
            }

            turns.add(slot);
        }

        return turns;
    }
}
