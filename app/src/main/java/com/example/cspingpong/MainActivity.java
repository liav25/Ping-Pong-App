package com.example.cspingpong;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.github.florent37.expansionpanel.ExpansionHeader;
import com.github.florent37.expansionpanel.ExpansionLayout;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {

    private static final int GAMES_PER_HOUR = 4;
    private static final int MIN_HOUR_PICK = 0;
    private static final int MAX_HOUR_PICK = 23;

    private Server server;
    private ExpansionHeader[] slotHeaders = new ExpansionHeader[GAMES_PER_HOUR];
    private ExpansionLayout[] slotExpansions = new ExpansionLayout[GAMES_PER_HOUR];
    private TextView[] headerTexts = new TextView[GAMES_PER_HOUR];
    private TextView[] expansionTexts = new TextView[GAMES_PER_HOUR];
    private String[] slotIntervalText = new String[GAMES_PER_HOUR];

    private NumberPicker hourPicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        server = new Server();

        connectViewsToXML();

        setHourPickerValues();

        // todo more elegant
        setSlotIntervalStrings();

        setHourPickerListener();

        updateHeaderColors();
    }


    /**
     * time picker on value changed listener
     */
    private void setHourPickerListener() {

        hourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                int pickedHour = hourPicker.getValue();

                for (int i = 0; i < GAMES_PER_HOUR; i++)
                {
                    headerTexts[i].setText(pickedHour + slotIntervalText[i]);
                }

                updateHeaderColors();
            }
        });
    }


    private void setSlotIntervalStrings() {
        slotIntervalText[0] = ":00";
        slotIntervalText[1] = ":15";
        slotIntervalText[2] = ":30";
        slotIntervalText[3] = ":45";
    }

    /**
     * Connect between Objects and XML representation of them
     */
    private void connectViewsToXML() {
        hourPicker = findViewById(R.id.hour_picker);

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

        expansionTexts[0] = findViewById(R.id.expansion_text1);
        expansionTexts[1] = findViewById(R.id.expansion_text2);
        expansionTexts[2] = findViewById(R.id.expansion_text3);
        expansionTexts[3] = findViewById(R.id.expansion_text4);
    }


    private void updateHeaderColors() {
        ArrayList<Game> games = server.get_hour_agenda(22122019, 1200);

        for (int i = 0; i < 4; i++)
        {
            if (games.get(i).isFull())
            {
                slotHeaders[i].setBackgroundColor(Color.GRAY);
                slotHeaders[i].setClickable(false);
            }
        }
    }


    private void setHourPickerValues() {
        // TODO generate automatically?
        final String[] arrayString= new String[] {"00:00","01:00","02:00","03:00","04:00"
                ,"05:00","06:00","07:00","08:00","09:00","10:00","11:00","12:00","13:00",
                "14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00","22:00","23:00"};
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


//    /**
//     * this function is just for testing
//     * create some data for our tryouts
//     * @return a list of lists of names
//     */
//    private ArrayList<ArrayList<String>> initNames(){
//        ArrayList<ArrayList<String>> names = new ArrayList<>();
//        ArrayList<String> temp = new ArrayList<>();
//        temp.add("Liav");
//        temp.add("Avner");
//        names.add(new ArrayList<String>(temp));
//        temp.clear();
//        temp.add("Nir");
//        temp.add("Eyal");
//        names.add(new ArrayList<String>(temp));
//        temp.clear();
//        temp.add("Yoni");
//        temp.add("Ran");
//        names.add(temp);
//
//        return names;
//    }


//    private boolean[] checkAvailablity(int idx){
//        boolean[] availabilty = new boolean[2];
//        for (int i =0; i<2;i++){
//            availabilty[i] = names.get(idx).get(i).equals("Liav");
//        }
//        return availabilty;
//
//    }


//    public void chooseGame(View slotButton) {
//        int time = 0;
//
//        switch (slotButton.getId())
//        {
//            case R.id.slot_1:
//                time = 1200;
//                break;
//            case R.id.slot_2:
//                time = 1215;
//                break;
//            case R.id.slot_3:
//                time = 1230;
//                break;
//            case R.id.slot_4:
//                time = 1245;
//                break;
//        }
//        server.join(22122019, time, "Yoni");
//        updateButtonInfo();
//
//        String message = "Hi Yoni, You chose to play in " + 22122019 + " at " + time;
//        Toast gameInfo = Toast.makeText(this, message, Toast.LENGTH_LONG);
//        gameInfo.show();
//    }
}
