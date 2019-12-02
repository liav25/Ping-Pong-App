package com.example.cspingpong;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyTurnAdapter extends RecyclerView.Adapter<MyTurnHolder> {

    Context c;
    ArrayList<MyTurnSlot> myTurns;

    public MyTurnAdapter(Context c, ArrayList<MyTurnSlot> myTurns) {
        this.c = c;
        this.myTurns = myTurns;
    }

    @NonNull
    @Override
    public MyTurnHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_my_turn,parent, false);

        return new MyTurnHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyTurnHolder holder, int position) {

        holder.mTextView1.setText(myTurns.get(position).getTurnTime());
        holder.mTextView2.setText(myTurns.get(position).getTurnAgainst());
        holder.mImageView.setImageResource(myTurns.get(position).getSlotImage());
        holder.mShareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                c.startActivity(shareIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return myTurns.size();
    }
}
