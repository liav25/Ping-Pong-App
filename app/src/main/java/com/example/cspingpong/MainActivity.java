package com.example.cspingpong;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private NumberPicker hourPicker;
    private String game1header;
    TextView textView;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        game1header = "someNumber";

        hourPicker = (NumberPicker) findViewById(R.id.hour_picker);
        textView = (TextView) findViewById(R.id.text1);
        button = (Button) findViewById(R.id.check);

        textView.setText(game1header);


        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(5);

        final ArrayList<String> names = initNames();

        hourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                int idx = hourPicker.getValue();
                textView.setText(names.get(idx));

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Toast.makeText(getApplicationContext(), game1header, Toast.LENGTH_SHORT).show();

            }
        });

    }

    private ArrayList<String> initNames(){
        ArrayList<String> names = new ArrayList<String>();
        names.add("Liav");
        names.add("Avner");
        names.add("Nir");
        names.add("Eyal");
        names.add("Yoni");
        names.add("Ran");

        return names;
    }

//hi

}
