package com.example.cspingpong;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.Image;
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
import com.github.florent37.expansionpanel.ExpansionHeader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private static final int GAMES_PER_HOUR = Server.MINUTES_IN_HOUR/Server.SLOT_TIME;
    private static final int MIN_HOUR_PICK = 0;
    private static final int MAX_HOUR_PICK = 23;


    private int selectedDate;
    private Server server;
    private String username;

    private ExpansionHeader[] slotHeaders = new ExpansionHeader[GAMES_PER_HOUR];
    private ExpansionLayout[] slotExpansions = new ExpansionLayout[GAMES_PER_HOUR];
    private TextView[] headerTexts = new TextView[GAMES_PER_HOUR];
    private String[] slotIntervalsSuffix = new String[GAMES_PER_HOUR];
    private ImageView[] racketIcons = new ImageView[GAMES_PER_HOUR];

    private NumberPicker hourPicker;
    private TextView welcomePlayerTxt;

    private ImageView racketIcon1;
    private ImageView racketIcon2;
    private ImageView racketIcon3;
    private ImageView racketIcon4;

    // used for requesting name from user
    private NameDialog nameDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        server = new Server();
        server.fabricate_games();

        connectViewsToXML();

        setHourPickerValues();

        // todo more elegant
        setSlotIntervalStrings();

        setHourPickerListener();

        setDefaultDateAndTime();

        updateHeaders();

        FragmentManager fm = getSupportFragmentManager();
        nameDialog = NameDialog.newInstance("Welcome!");
        nameDialog.show(fm, "fragment_edit_name");
    }

    /**
     * set time picker default value to current time
     */
    private void setDefaultDateAndTime() {
        Calendar cal = Calendar.getInstance();
        int currentHour = cal.get(Calendar.HOUR_OF_DAY);
        Date date = cal.getTime();
        SimpleDateFormat dateformat = new SimpleDateFormat("ddMMyyyy");
        String datetime = dateformat.format(date.getTime());
        selectedDate = Integer.parseInt(datetime);
        hourPicker.setValue(currentHour);

        // fixes default hour being invisible
        View firstItem = hourPicker.getChildAt(0);
        if (firstItem != null) {
            firstItem.setVisibility(View.INVISIBLE);
        }
        setHeaderTime();
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
                setHeaderTime();
            }
        });
    }

    private void setHeaderTime() {
        int pickedHour = hourPicker.getValue();

        for (int i = 0; i < GAMES_PER_HOUR; i++) {
            headerTexts[i].setText(pickedHour + slotIntervalsSuffix[i]);
        }

        updateHeaders();
    }


    private void setSlotIntervalStrings() {
        slotIntervalsSuffix[0] = ":00";
        slotIntervalsSuffix[1] = ":15";
        slotIntervalsSuffix[2] = ":30";
        slotIntervalsSuffix[3] = ":45";
    }

    /**
     * Connect between Objects and XML representation of them
     */
    private void connectViewsToXML() {
        hourPicker = findViewById(R.id.hour_picker);

        welcomePlayerTxt = findViewById(R.id.welcomePlayerTxt);

        slotHeaders[0] = findViewById(R.id.slot_header_1);
        slotHeaders[1] = findViewById(R.id.slot_header_2);
        slotHeaders[2] = findViewById(R.id.slot_header_3);
        slotHeaders[3] = findViewById(R.id.slot_header_4);

        slotExpansions[0] = findViewById(R.id.expansionLayout1);
        slotExpansions[1] = findViewById(R.id.expansionLayout2);
        slotExpansions[2] = findViewById(R.id.expansionLayout3);
        slotExpansions[3] = findViewById(R.id.expansionLayout4);

        headerTexts[0] = findViewById(R.id.header_text1);
        headerTexts[1] = findViewById(R.id.header_text2);
        headerTexts[2] = findViewById(R.id.header_text3);
        headerTexts[3] = findViewById(R.id.header_text4);

        racketIcons[0] = (ImageView) findViewById(R.id.racket_icon1);
        racketIcons[1] = (ImageView) findViewById(R.id.racket_icon2);
        racketIcons[2] = (ImageView) findViewById(R.id.racket_icon3);
        racketIcons[3] = (ImageView) findViewById(R.id.racket_icon4);

//


    }

    private void updateHeaders() {
        ArrayList<Game> games = server.get_hour_agenda(selectedDate, hourPicker.getValue() * server.INTERVAL);

        for (int i = 0; i < 4; i++) {
            switch (games.get(i).empty_slots()) {
                case 0:
                    slotHeaders[i].setClickable(false);
                    slotHeaders[i].setBackgroundColor(getResources().getColor(R.color.GREY));
                    racketIcons[i].setImageResource(R.drawable.two_racket_icon);
                    racketIcons[i].setVisibility(View.VISIBLE);
                    break;
                case 1:
                    slotHeaders[i].setBackgroundColor(getResources().getColor(R.color.apple));
                    racketIcons[i].setImageResource(R.drawable.single_racket_icon);
                    racketIcons[i].setVisibility(View.VISIBLE);

                    break;
                case 2:
                    slotHeaders[i].setClickable(true);
                    slotHeaders[i].setBackgroundColor(getResources().getColor(R.color.white));
                    racketIcons[i].setVisibility(View.INVISIBLE);
//                    racketIcon.setImageResource(R.drawable.ic_android_black_24dp);
                    break;
            }
        }
    }

    private void setHourPickerValues() {
        // TODO generate automatically?
        final String[] arrayString = new String[]{
                "00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00",
                "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00",
                "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};
        hourPicker.setMinValue(MIN_HOUR_PICK);
        hourPicker.setMaxValue(MAX_HOUR_PICK);


        hourPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                // TODO Auto-generated method stub
                return arrayString[value];
            }
        });
    }


    @SuppressLint("ResourceAsColor")
    public void joinButtonHandler(View view) { // TODO better function name
        Button joinButton = (Button) view;
        int time = hourPicker.getValue();
        String sTime = "";

        switch (joinButton.getId()) {
            case R.id.join_button_left1:
            case R.id.join_button_right1:
                sTime = hourPicker.getValue() + slotIntervalsSuffix[0];
                break;
            case R.id.join_button_left2:
            case R.id.join_button_right2:
                time += 15;
                sTime = hourPicker.getValue() + slotIntervalsSuffix[1];
                break;
            case R.id.join_button_left3:
            case R.id.join_button_right3:
                time += 30;
                sTime = hourPicker.getValue() + slotIntervalsSuffix[2];
                break;
            case R.id.join_button_left4:
            case R.id.join_button_right4:
                time += 45;
                sTime = hourPicker.getValue() + slotIntervalsSuffix[3];
                break;
        }

        server.addPlayer(selectedDate, time, username);
        updateHeaders();

        joinButton.setText(username);
        joinButton.setBackgroundTintList(
                ContextCompat.getColorStateList(getApplicationContext(), R.color.apple));
        joinButton.setClickable(false); // TODO change when leaving a game will be possible

        String message = "You chose to play in " + selectedDate + " at " + sTime;
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

            welcomePlayerTxt.setText("Welcome " + username + "!");
        }
    }

    public void selectDate(View button) {

        Builder builder = new Builder(MainActivity.this, new Builder.CalendarPickerOnConfirm() {
            @Override
            public void onComplete(YearMonthDay yearMonthDay) {

                Button daySlotBtn = findViewById(R.id.daySlotBtn);
                daySlotBtn.setText(yearMonthDay.year + "-" + yearMonthDay.month + "-" + yearMonthDay.day);
                selectedDate = yearMonthDay.year + yearMonthDay.month * 10000 + yearMonthDay.day * 1000000;

                updateHeaders();
            }
        })
                ///here design
                .setPromptText("Select a day to play !")
                .setMonthBaseBgColor(0xF2FCFCFC)
                .setSelectedBgColor(0xFFF9AD90);
        builder.show();
    }

    @SuppressLint("ResourceAsColor")
    public void updateExpansion(View slotHeader) {
        Game chosenGame;
        Button joinLeft, joinRight;

        switch (slotHeader.getId()) {

            case R.id.slot_header_1:
                chosenGame = server.getGame(selectedDate, hourPicker.getValue());
                joinLeft = findViewById(R.id.join_button_left1);
                joinRight = findViewById(R.id.join_button_right1);
                break;
            case R.id.slot_header_2:
                chosenGame = server.getGame(selectedDate, hourPicker.getValue() + 15);
                joinLeft = findViewById(R.id.join_button_left2);
                joinRight = findViewById(R.id.join_button_right2);
                break;
            case R.id.slot_header_3:
                chosenGame = server.getGame(selectedDate, hourPicker.getValue() + 30);
                joinLeft = findViewById(R.id.join_button_left3);
                joinRight = findViewById(R.id.join_button_right3);
                break;
            default:
                chosenGame = server.getGame(selectedDate, hourPicker.getValue() + 45);
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
            joinRight.setClickable(chosenGame.getPlayer2().equals(username));
        }
        else {
            joinRight.setText(R.string.join_button_text);
            joinRight.setBackgroundTintList(
                    ContextCompat.getColorStateList(getApplicationContext(), R.color.orange));

        }
        joinRight.setClickable(chosenGame.getPlayer1().equals(username));
    }
}
