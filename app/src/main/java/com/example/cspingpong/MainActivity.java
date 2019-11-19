package com.example.cspingpong;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.expansionpanel.ExpansionHeader;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private NumberPicker hourPicker;
    TextView textView;
    TextView textView2;
    private Button button;
    private ExpansionHeader header0;
    private ExpansionHeader header1;
    ArrayList<ArrayList<String>> names;
    private boolean[] availablity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        /* Connect between Objects and XML representation of them         */
        hourPicker = (NumberPicker) findViewById(R.id.hour_picker);
        textView = (TextView) findViewById(R.id.text1);
        textView2 = findViewById(R.id.lower);
        button = (Button) findViewById(R.id.check);
        /*expanded headers*/
        header0 = findViewById(R.id.header_0);
        header1 = findViewById(R.id.header_1);

        /* set range for the hours picker*/
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(2);

        /* init data for testing*/
        names = initNames();

        /* time picker on value changed listener*/
        hourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                int idx = hourPicker.getValue();
                textView.setText(names.get(idx).get(0));
                textView2.setText(names.get(idx).get(1));
                availablity = checkAvailablity(idx);
                setHeaderColors();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Toast.makeText(getApplicationContext(), "Toast", Toast.LENGTH_SHORT).show();

            }
        });

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
