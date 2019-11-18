package com.example.cspingpong;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private NumberPicker hourPicker;
    private String game1header;
    TextView textView;
    TextView textView2;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        game1header = "someNumber";

        /* Connect between Objects and XML representation of them         */
        hourPicker = (NumberPicker) findViewById(R.id.hour_picker);
        textView = (TextView) findViewById(R.id.text1);
        textView2 = findViewById(R.id.lower);
        button = (Button) findViewById(R.id.check);

        /* set range for the hours picker*/
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(2);

        /* init data for testing*/
        final ArrayList<ArrayList<String>> names = initNames();

        /* time picker on value changed listener*/
        hourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                int idx = hourPicker.getValue();
                textView.setText(names.get(idx).get(0));
                textView2.setText(names.get(idx).get(1));
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Toast.makeText(getApplicationContext(), game1header, Toast.LENGTH_SHORT).show();

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

//hi

}
