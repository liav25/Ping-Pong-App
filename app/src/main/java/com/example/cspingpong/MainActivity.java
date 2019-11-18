package com.example.cspingpong;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private Server server;
    private Button[] slotButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        server = new Server();

        slotButtons = new Button[4];
        slotButtons[0] = findViewById(R.id.slot_1);
        slotButtons[1] = findViewById(R.id.slot_2);
        slotButtons[2] = findViewById(R.id.slot_3);
        slotButtons[3] = findViewById(R.id.slot_4);

        updateButtonInfo();
    }

    private void updateButtonInfo() {
        ArrayList<Game> games = server.get_games_by_date_and_time(22122019, 1200);

        for (int i = 0; i < 4; i++)
        {
            if (games.get(i).isFull())
            {
                slotButtons[i].setHighlightColor(Color.RED);
            }
            else if (games.get(i).isEmpty())
            {
                slotButtons[i].setHighlightColor(Color.GREEN);
            }
            else {
                slotButtons[i].setHighlightColor(Color.BLUE);
            }
        }
    }

    public void chooseGame(View slotButton) {
        int time = 0;

        switch (slotButton.getId())
        {
            case R.id.slot_1:
                time = 1200;
                break;
            case R.id.slot_2:
                time = 1215;
                break;
            case R.id.slot_3:
                time = 1230;
                break;
            case R.id.slot_4:
                time = 1245;
                break;
        }
        server.join(22122019, time, "Yoni");
        updateButtonInfo();

        String message = "Hi Yoni, You chose to play in " + 22122019 + " at " + time;
        Toast gameInfo = Toast.makeText(this, message, Toast.LENGTH_LONG);
        gameInfo.show();
    }
}
