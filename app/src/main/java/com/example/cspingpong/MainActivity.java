package com.example.cspingpong;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import android.annotation.SuppressLint;
import android.graphics.Color;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private static final int GAMES_PER_HOUR = Server.MINUTES_IN_HOUR/Server.SLOT_TIME;
    private static final int MIN_HOUR_PICK = 0;
    private static final int MAX_HOUR_PICK = 23;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private int selectedDate;
    private Server server;
    private String username;private
    NumberPicker hourPicker;
    private TextView welcomePlayerTxt;
    private NameDialog nameDialog;

//    private ExpansionHeader[] slotHeaders = new ExpansionHeader[GAMES_PER_HOUR];
    private ExpansionLayout[] slotExpansions = new ExpansionLayout[GAMES_PER_HOUR];
    private TextView[] headerTexts = new TextView[GAMES_PER_HOUR];
    private ImageView[] headerRacketIcons = new ImageView[GAMES_PER_HOUR];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        server = new Server();
        server.fabricate_games();

        connectViewsToXML();

        setHourPickerValues();

        setHourPickerListener();

        setDefaultDateAndTime();

        updateHeaders();

        launchNameDialog();
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

        // fixes default hour being invisible
        View firstItem = hourPicker.getChildAt(0);
        if (firstItem != null) {
            firstItem.setVisibility(View.INVISIBLE);
        }
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
                updateHeaders();
            }
        });
    }

    private void updateHeaderTimes() {
        int pickedHour = hourPicker.getValue();
        String[] headerTimes = getResources().getStringArray(R.array.header_times);

        for (int i = 0; i < GAMES_PER_HOUR; i++) {

            headerTexts[i].setText(String.format(headerTimes[i], pickedHour));
        }
    }

    /**
     * Connect between Objects and XML representation of them
     */
    private void connectViewsToXML() {
        hourPicker = findViewById(R.id.hour_picker);

        welcomePlayerTxt = findViewById(R.id.welcomePlayerTxt);

//        slotHeaders[0] = findViewById(R.id.slot_header_1);
//        slotHeaders[1] = findViewById(R.id.slot_header_2);
//        slotHeaders[2] = findViewById(R.id.slot_header_3);
//        slotHeaders[3] = findViewById(R.id.slot_header_4);

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
    }

    private void updateHeaderIcons() {
        ArrayList<Game> games
                = server.get_hour_agenda(selectedDate, hourPicker.getValue() * Server.INTERVAL);

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

    public void joinButtonHandler(View view) {
        Button joinButton = (Button) view;
        int time = hourPicker.getValue() * Server.INTERVAL;

        switch (joinButton.getId()) {
            case R.id.join_button_left1:
            case R.id.join_button_right1:
                break;
            case R.id.join_button_left2:
            case R.id.join_button_right2:
                time += 15;
                break;
            case R.id.join_button_left3:
            case R.id.join_button_right3:
                time += 30;
                break;
            case R.id.join_button_left4:
            case R.id.join_button_right4:
                time += 45;
                break;
        }

        server.addPlayer(selectedDate, time, username);
        updateHeaderIcons();

        joinButton.setText(username);
        joinButton.setBackgroundTintList(
                ContextCompat.getColorStateList(getApplicationContext(), R.color.apple));
        joinButton.setClickable(false); // TODO change when leaving a game will be possible

        String message = "You chose to play in " + selectedDate + " at " + time;
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void confirmName(View view) {
        // todo - do something if name is empty
        EditText tx = nameDialog.mEditText;
        username = tx.getText().toString();

        if (username.length() == 0) {
            tx.getBackground().setTint(Color.RED);
        } else {
            nameDialog.dismiss();

            welcomePlayerTxt.setText(getString(R.string.welcome_text, username));
        }
    }

    public void selectDate(View button) {

        Builder builder = new Builder(MainActivity.this, new Builder.CalendarPickerOnConfirm() {
            @Override
            public void onComplete(YearMonthDay date) {

                Button dateButton = findViewById(R.id.dateButton);
                dateButton.setText(
                        getString(R.string.date_button_text, date.day, date.month, date.year));
                selectedDate = date.year + date.month * 10000 + date.day * 1000000;

                updateHeaderIcons();
            }
        })
                // design
                .setPromptText("Select a day to play !")
                .setMonthBaseBgColor(0xF2FCFCFC)
                .setSelectedBgColor(0xFFF9AD90);
        builder.show();
    }

    public void updateExpansion(View slotHeader) {
        Game chosenGame;
        Button joinLeft, joinRight;

        switch (slotHeader.getId()) {

            case R.id.slot_header_1:
                chosenGame = server.getGame(selectedDate, hourPicker.getValue()*Server.INTERVAL);
                joinLeft = findViewById(R.id.join_button_left1);
                joinRight = findViewById(R.id.join_button_right1);
                break;
            case R.id.slot_header_2:
                chosenGame = server.getGame(selectedDate, hourPicker.getValue()*Server.INTERVAL + 15);
                joinLeft = findViewById(R.id.join_button_left2);
                joinRight = findViewById(R.id.join_button_right2);
                break;
            case R.id.slot_header_3:
                chosenGame = server.getGame(selectedDate, hourPicker.getValue()*Server.INTERVAL + 30);
                joinLeft = findViewById(R.id.join_button_left3);
                joinRight = findViewById(R.id.join_button_right3);
                break;
            default:
                chosenGame = server.getGame(selectedDate, hourPicker.getValue()*Server.INTERVAL + 45);
                joinLeft = findViewById(R.id.join_button_left4);
                joinRight = findViewById(R.id.join_button_right4);
                break;
        }

        if (chosenGame.getPlayer1() != null) {
            joinLeft.setText(chosenGame.getPlayer1());
            joinLeft.setBackgroundTintList(
                    ContextCompat.getColorStateList(getApplicationContext(), R.color.button_gray));
        }
        else {
            joinLeft.setText(R.string.join_button_text);
            joinLeft.setBackgroundTintList(
                    ContextCompat.getColorStateList(getApplicationContext(), R.color.orange));

        }
        joinLeft.setClickable(chosenGame.getPlayer1().equals(username));

        if (chosenGame.getPlayer2() != null) {
            joinRight.setText(chosenGame.getPlayer2());
            joinRight.setBackgroundTintList(
                    ContextCompat.getColorStateList(getApplicationContext(), R.color.button_gray));
        }
        else {
            joinRight.setText(R.string.join_button_text);
            joinRight.setBackgroundTintList(
                    ContextCompat.getColorStateList(getApplicationContext(), R.color.orange));

        }
        joinRight.setClickable(chosenGame.getPlayer2().equals(username));
    }
}
