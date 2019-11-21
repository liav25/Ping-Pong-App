package com.example.cspingpong;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import com.github.florent37.expansionpanel.ExpansionHeader;
import com.github.florent37.expansionpanel.ExpansionLayout;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {

    private static final int GAMES_PER_HOUR = 4;
    private static final int MIN_HOUR_PICK = 0;
    private static final int MAX_HOUR_PICK = 24;

    private Server server;
    private ExpansionHeader[] slotHeaders;
    private ExpansionLayout[] slotExpansions;
    private TextView[] headerTexts;
    private TextView[] expansionTexts;

    private NumberPicker hourPicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        server = new Server();

        connectViewsToXML();

        /* set range for the hours picker*/
        hourPicker.setMinValue(MIN_HOUR_PICK);
        hourPicker.setMaxValue(MAX_HOUR_PICK);

        /* time picker on value changed listener*/
        hourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                int pickedHour = hourPicker.getValue();

                for (int i = 0; i < GAMES_PER_HOUR; i++)
                {
                    String headerText = pickedHour + ":" + (i*15);

                    headerTexts[i].setText(headerText);
                }
                setHeaderColors();
            }
        });

        updateHeaderColors();
    }

    private void connectViewsToXML() {
        /* Connect between Objects and XML representation of them         */
        hourPicker = (NumberPicker) findViewById(R.id.hour_picker);

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


    /**
     * this function is just for testing
     * create some data for our tryouts
     * @return a list of lists of names
     */
    private ArrayList<ArrayList<String>> initNames(){
        ArrayList<ArrayList<String>> names = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        temp.add("Liav");
        temp.add("Avner");
        names.add(new ArrayList<String>(temp));
        temp.clear();
        temp.add("Nir");
        temp.add("Eyal");
        names.add(new ArrayList<String>(temp));
        temp.clear();
        temp.add("Yoni");
        temp.add("Ran");
        names.add(temp);

        return names;
    }

    private boolean[] checkAvailablity(int idx){
        boolean[] availabilty = new boolean[2];
        for (int i =0; i<2;i++){
            availabilty[i] = names.get(idx).get(i).equals("Liav");
        }
        return availabilty;

    }

    private void setHeaderColors(){
        for(int i=0; i<2; i++){
            if (availablity[0]){
                header0.setBackgroundColor(getResources().getColor(R.color.gray));
            }
            else {header0.setBackgroundColor(getResources().getColor(R.color.apple));}
            if(availablity[1]){
                header1.setBackgroundColor(getResources().getColor(R.color.gray));
            }
            else {header1.setBackgroundColor(getResources().getColor(R.color.apple));}

        }
    }
//hi

}
