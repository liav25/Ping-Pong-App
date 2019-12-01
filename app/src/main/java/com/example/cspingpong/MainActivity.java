package com.example.cspingpong;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import com.github.florent37.expansionpanel.ExpansionLayout;
import com.maxproj.calendarpicker.Builder;
import com.maxproj.calendarpicker.Models.YearMonthDay;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private static final int GAMES_PER_HOUR = Server.MINUTES_IN_HOUR / Server.SLOT_TIME;
    private static final int MIN_HOUR_PICK = 0;
    private static final int MAX_HOUR_PICK = 23;
    private static final int MAX_USERNAME_LEN = 9;

    private int selectedDate;
    private int selectedHour;
    private Server server;
    private String username;
    private NumberPicker hourPicker;
    private TextView welcomePlayerTxt;
    private NameDialog nameDialog;
    private Button dateButton;
//    private Button myTurnsBtn;

    private ExpansionLayout[] slotExpansions = new ExpansionLayout[GAMES_PER_HOUR];
    private TextView[] headerTexts = new TextView[GAMES_PER_HOUR];
    private ImageView[] headerRacketIcons = new ImageView[GAMES_PER_HOUR];
    private Button[] leftJoinButtons = new Button[GAMES_PER_HOUR];
    private Button[] rightJoinButtons = new Button[GAMES_PER_HOUR];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        server = new Server();

        connectViewsToXML();

        setHourPickerValues();

        setHourPickerListener();

        setDefaultDateAndTime();

        server.fabricateGames(selectedDate);

        updateHeaders();

        launchNameDialog();
    }

    public void moveToMyTurnsActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), MyTurnsActivity.class);
        intent.putExtra("username", this.username);

        intent.putParcelableArrayListExtra("game_list", server.getPlayerAgenda(username));

        startActivity(intent);
    }

    private void updateHeaders() {
        updateHeaderIcons();
        updateHeaderTimes();
    }

    private void launchNameDialog() {
        FragmentManager fm = getSupportFragmentManager();
        nameDialog = NameDialog.newInstance("Welcome!");
        nameDialog.show(fm, "fragment_edit_name");
    }

    /**
     * set time picker default value to current time
     */
    private void setDefaultDateAndTime() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        String datetime = dateFormat.format(date.getTime());

        selectedDate = Integer.parseInt(datetime);
        hourPicker.setValue(calendar.get(Calendar.HOUR_OF_DAY));
        selectedHour = hourPicker.getValue() * Server.INTERVAL;

        // fixes default hour being invisible
        View firstItem = hourPicker.getChildAt(0);
        if (firstItem != null) {
            firstItem.setVisibility(View.INVISIBLE);
        }
    }

    public void selectDate(View button) {

        Builder builder = new Builder(MainActivity.this, new Builder.CalendarPickerOnConfirm() {
            @Override
            public void onComplete(YearMonthDay date) {

                dateButton.setText(
                        getString(R.string.date_button_text, date.day, date.month, date.year));
                selectedDate = date.year + date.month * 10000 + date.day * 1000000;

                updateHeaderIcons();
                updateExpansions();
            }
        })
                // design
                .setPromptText("Select a day to play !")
                .setMonthBaseBgColor(0xF2FCFCFC)
                .setSelectedBgColor(0xFFF9AD90);
        builder.show();
    }

    /**
     * time picker on value changed listener
     */
    private void setHourPickerListener() {

        hourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                for (ExpansionLayout e : slotExpansions) {
                    e.collapse(true);
                }
                selectedHour = newVal * Server.INTERVAL;
                updateHeaders();
                updateExpansions();
            }
        });
    }

    private void updateHeaderTimes() {

        String[] headerTimes = getResources().getStringArray(R.array.header_times);

        for (int i = 0; i < GAMES_PER_HOUR; i++) {

            headerTexts[i].setText(String.format(headerTimes[i], hourPicker.getValue()));
        }
    }

    /**
     * Connect between Objects and XML representation of them
     */
    private void connectViewsToXML() {
        hourPicker = findViewById(R.id.hour_picker);

        dateButton = findViewById(R.id.dateButton);

//        myTurnsBtn = findViewById(R.id.savedTurnBtn);

        welcomePlayerTxt = findViewById(R.id.welcomePlayerTxt);

        slotExpansions[0] = findViewById(R.id.expansionLayout1);
        slotExpansions[1] = findViewById(R.id.expansionLayout2);
        slotExpansions[2] = findViewById(R.id.expansionLayout3);
        slotExpansions[3] = findViewById(R.id.expansionLayout4);

        headerTexts[0] = findViewById(R.id.header_text1);
        headerTexts[1] = findViewById(R.id.header_text2);
        headerTexts[2] = findViewById(R.id.header_text3);
        headerTexts[3] = findViewById(R.id.header_text4);

        headerRacketIcons[0] = findViewById(R.id.racket_icon1);
        headerRacketIcons[1] = findViewById(R.id.racket_icon2);
        headerRacketIcons[2] = findViewById(R.id.racket_icon3);
        headerRacketIcons[3] = findViewById(R.id.racket_icon4);

        leftJoinButtons[0] = findViewById(R.id.join_button_left1);
        leftJoinButtons[1] = findViewById(R.id.join_button_left2);
        leftJoinButtons[2] = findViewById(R.id.join_button_left3);
        leftJoinButtons[3] = findViewById(R.id.join_button_left4);

        rightJoinButtons[0] = findViewById(R.id.join_button_right1);
        rightJoinButtons[1] = findViewById(R.id.join_button_right2);
        rightJoinButtons[2] = findViewById(R.id.join_button_right3);
        rightJoinButtons[3] = findViewById(R.id.join_button_right4);
    }

    private void updateHeaderIcons() {
        ArrayList<Game> games = server.getHourAgenda(selectedDate, selectedHour);

        for (int i = 0; i < 4; i++) {
            switch (games.get(i).empty_slots()) {
                case 0:
                    headerRacketIcons[i].setImageResource(R.drawable.two_racket_icon);
                    headerRacketIcons[i].setVisibility(View.VISIBLE);
                    break;
                case 1:
                    headerRacketIcons[i].setImageResource(R.drawable.single_racket_icon);
                    headerRacketIcons[i].setVisibility(View.VISIBLE);
                    break;
                case 2:
                    headerRacketIcons[i].setVisibility(View.INVISIBLE);
                    break;
            }
        }
    }

    private void setHourPickerValues() {

        final String[] hourStrings = getResources().getStringArray(R.array.hour_picker_strings);

        hourPicker.setMinValue(MIN_HOUR_PICK);
        hourPicker.setMaxValue(MAX_HOUR_PICK);

        hourPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                // TODO Auto-generated method stub ???
                return hourStrings[value];
            }
        });
    }

    private int timeOffset(int buttonId) {
        switch (buttonId) {
            case R.id.join_button_left2:
            case R.id.join_button_right2:
                return 15;

            case R.id.join_button_left3:
            case R.id.join_button_right3:
                return 30;

            case R.id.join_button_left4:
            case R.id.join_button_right4:
                return 45;

            default:
                return 0;
        }
    }

    public void joinButtonHandler(View view) {
        Button joinButton = (Button) view;
        Drawable bg = joinButton.getBackground();
        bg = DrawableCompat.wrap(bg);

        int time = selectedHour + timeOffset(joinButton.getId());
        Game chosenGame = server.getGame(selectedDate, time);

        if (chosenGame.addPlayer(username)) {
            DrawableCompat.setTint(bg, getResources().getColor(R.color.apple));
            joinButton.setText(username);
            Toast.makeText(this, getString(R.string.join_message), Toast.LENGTH_SHORT).show();

        } else if (joinButton.getText().toString().equals(username)) {
            DrawableCompat.setTint(bg, getResources().getColor(R.color.orange));
            joinButton.setText(R.string.join_button_init_text);
            chosenGame.removePlayer(username);

        } else {
            Toast.makeText(this, getString(R.string.join_twice_message), Toast.LENGTH_SHORT).show();
        }
        updateHeaderIcons();
    }

    public void confirmName(View view) {
        // todo - do something if name is empty
        EditText tx = nameDialog.mEditText;
        username = tx.getText().toString();

        if (username.length() == 0) {
            tx.getBackground().setTint(Color.RED);
        }
        else if (username.length() > MAX_USERNAME_LEN)
        {
            Toast.makeText(this, getString(R.string.name_too_long_message), Toast.LENGTH_SHORT).show();
        }
        else {
            nameDialog.dismiss();
            username = username.toLowerCase();
            username = username.substring(0, 1).toUpperCase() + username.substring(1);
            updateExpansions();
            welcomePlayerTxt.setText(getString(R.string.welcome_text, username));
        }
    }

    public void updateExpansions() {
        ArrayList<Game> games = server.getHourAgenda(selectedDate, selectedHour);

        for (int i = 0; i < GAMES_PER_HOUR; i++) {

            updateJoinButton(leftJoinButtons[i], games.get(i).getPlayer1());

            updateJoinButton(rightJoinButtons[i], games.get(i).getPlayer2());
        }
    }

    private void updateJoinButton(Button joinButton, String playerName) {
        Drawable bg = joinButton.getBackground();
        bg = DrawableCompat.wrap(bg);

        if (playerName == null) {
            joinButton.setText(R.string.join_button_init_text);
            DrawableCompat.setTint(bg, getResources().getColor(R.color.orange));
            joinButton.setClickable(true);

        } else if (username.equals(playerName)) {
            DrawableCompat.setTint(bg, getResources().getColor(R.color.apple));
            joinButton.setText(playerName);
            joinButton.setClickable(true);

        } else {
            DrawableCompat.setTint(bg, getResources().getColor(R.color.com_maxproj_calendarpicker_transparent));
            joinButton.setText(playerName);
            joinButton.setClickable(false);

        }
    }
}
