package com.example.cspingpong;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import com.maxproj.calendarpicker.Builder;
import com.maxproj.calendarpicker.Config.MyConfig;
import com.maxproj.calendarpicker.Models.YearMonthDay;
import com.github.florent37.expansionpanel.ExpansionHeader;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private static final int GAMES_PER_HOUR = 4;
    private static final int MIN_HOUR_PICK = 0;
    private static final int MAX_HOUR_PICK = 23;
    private int selectedDate;

    private Server server;
    private ExpansionHeader[] slotHeaders = new ExpansionHeader[GAMES_PER_HOUR];
    //    private ExpansionLayout[] slotExpansions = new ExpansionLayout[GAMES_PER_HOUR];
    private TextView[] headerTexts = new TextView[GAMES_PER_HOUR];
    private String[] slotIntervalsSuffix = new String[GAMES_PER_HOUR];

    private NumberPicker hourPicker;

    // used for requesting name from user
    private NameDialog nameDialog;
    private String username;

    private TextView welcomePlayerTxt;


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


        setHourPickerDefault();

        updateHeaderColors();

        FragmentManager fm = getSupportFragmentManager();
        nameDialog = NameDialog.newInstance("Welcome!");
        nameDialog.show(fm, "fragment_edit_name");
    }

    /**
     * set time picker default value to current time
     */
    private void setHourPickerDefault() {
        Calendar cal = Calendar.getInstance();
        int currentHour = cal.get(Calendar.HOUR_OF_DAY);
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
                setHeaderTime();
            }
        });
    }

    private void setHeaderTime() {
        int pickedHour = hourPicker.getValue();

        for (int i = 0; i < GAMES_PER_HOUR; i++) {
            headerTexts[i].setText(pickedHour + slotIntervalsSuffix[i]);
        }

        updateHeaderColors();
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

        headerTexts[0] = findViewById(R.id.header_text1);
        headerTexts[1] = findViewById(R.id.header_text2);
        headerTexts[2] = findViewById(R.id.header_text3);
        headerTexts[3] = findViewById(R.id.header_text4);

    }

    private void updateHeaderColors() {
        ArrayList<Game> games = server.get_hour_agenda(selectedDate, hourPicker.getValue());

        for (int i = 0; i < 4; i++) {
            switch (games.get(i).empty_slots()) {
                case 0:
                    slotHeaders[i].setBackgroundTintList(
                            ContextCompat.getColorStateList(getApplicationContext(), R.color.button_gray));
                    slotHeaders[i].setClickable(false);
                    break;
                case 1:
                    slotHeaders[i].setBackgroundTintList(
                            ContextCompat.getColorStateList(getApplicationContext(), R.color.apple));
                    break;
                case 2:
                    slotHeaders[i].setBackgroundTintList(
                            ContextCompat.getColorStateList(getApplicationContext(), R.color.white));
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

    public void chooseGame(View slotButton) {
        int time = hourPicker.getValue()*server.INTERVAL;
        String sTime = Integer.toString(time);

        switch (slotButton.getId()) {
            case R.id.join_button_1:
                sTime = hourPicker.getValue()+ slotIntervalsSuffix[0];
                break;
            case R.id.join_button_2:
                time += 15;
                sTime = hourPicker.getValue() + slotIntervalsSuffix[1];
                break;
            case R.id.join_button_3:
                time += 30;
                sTime = hourPicker.getValue() + slotIntervalsSuffix[2];
                break;
            case R.id.join_button_4:
                time += 45;
                sTime = hourPicker.getValue() + slotIntervalsSuffix[3];
                break;
        }
        server.addPlayer(selectedDate, time, username);
        System.out.println("********!!!!!!!");
        System.out.println(time);
        updateHeaderColors();

        String message = "You chose to play in " + selectedDate + " at " + sTime;
        Toast gameInfo = Toast.makeText(this, message, Toast.LENGTH_LONG);
        gameInfo.show();
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


    public void selectDate(View button){

        Builder builder = new Builder(MainActivity.this, new Builder.CalendarPickerOnConfirm() {
            @Override
            public void onComplete(YearMonthDay yearMonthDay) {

                Button daySlotBtn = findViewById(R.id.daySlotBtn);
                daySlotBtn.setText(yearMonthDay.year+"-"+yearMonthDay.month+"-"+yearMonthDay.day);
                selectedDate = yearMonthDay.year+yearMonthDay.month*1000+yearMonthDay.day*100000;
                updateHeaderColors();


            }
        });
        builder.show();
    }
}
