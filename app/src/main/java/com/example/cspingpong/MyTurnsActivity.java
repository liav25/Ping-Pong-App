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
        String username = incomingIntent.getStringExtra("username");
        Server server = incomingIntent.getParcelableExtra("server");
        welcomePlayerTxt.setText(username +"'s Turns:");

        myTurnAdapter = new MyTurnAdapter(this, getMyList());
        myTurnsRecyclerView.setAdapter(myTurnAdapter);

    }


    private ArrayList<MyTurnSlot> getMyList(){

//        int length = server.length;
        
        ArrayList<MyTurnSlot> turns = new ArrayList<>();
        MyTurnSlot slot = new MyTurnSlot();
        slot.setTurnTime("Turn's Time: 30.11.19 13:30");
        slot.setTurnAgainst("Your turn is against: Nir");
        slot.setSlotImage(R.drawable.two_racket_icon);
        turns.add(slot);

        slot = new MyTurnSlot();
        slot.setTurnTime("Turn's Time: 1.12.19 12:00");
        slot.setTurnAgainst("Your turn is against: No one");
        slot.setSlotImage(R.drawable.single_racket_icon);
        turns.add(slot);

        slot = new MyTurnSlot();
        slot.setTurnTime("Turn's Time: 2.12.19 12:30");
        slot.setTurnAgainst("Your turn is against: Avner");
        slot.setSlotImage(R.drawable.two_racket_icon);
        turns.add(slot);

        slot = new MyTurnSlot();
        slot.setTurnTime("Turn's Time: 2.12.19 14:00");
        slot.setTurnAgainst("Your turn is against: No one");
        slot.setSlotImage(R.drawable.single_racket_icon);
        turns.add(slot);

        slot = new MyTurnSlot();
        slot.setTurnTime("Turn's Time: 3.12.19 14:30");
        slot.setTurnAgainst("Your turn is against: Liav");
        slot.setSlotImage(R.drawable.two_racket_icon);
        turns.add(slot);
        return turns;

    }
}
