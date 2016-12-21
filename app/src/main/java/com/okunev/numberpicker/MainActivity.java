package com.okunev.numberpicker;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.okunev.numberpickerview.NumberPickerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NumberPickerView view = (NumberPickerView) findViewById(R.id.numbers);
        ArrayList<Integer> tags = new ArrayList<>();
        tags.addAll(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        view.withNumberOfElements(10)
                .withMultipleSelect(true)
                .withHeaderTemplate("bet", "bets")
                .withHeaderTextColor(Color.GREEN)
                .withHighLightedTextColor(Color.CYAN)
                .withElementsTags(tags)
                .build();

        view = (NumberPickerView) findViewById(R.id.numbers2);
        HashMap<Integer, Integer> footerValues = new HashMap<>();
        footerValues.put(0, 4);
        footerValues.put(5, 6);
        view.withNumberOfElements(10)
                .withMultipleSelect(true)
                .withHeaderTemplate("bid", "bids")
                .withHeaderTextColor(Color.RED)
                .withHighLightedTextColor(Color.YELLOW)
                .withDefaultTextColor(Color.WHITE)
                .withHighLightedColor(Color.MAGENTA)
                .withElementsTags(tags)
                .withFooterValues(footerValues)
                .withOnChosenNumberListener(new NumberPickerView.OnChosenNumberListener() {
                    @Override
                    public void OnChosenNumber(Integer selectedNumber) {
                        Toast.makeText(MainActivity.this, "Number is " + String.valueOf(selectedNumber), Toast.LENGTH_SHORT).show();
                    }
                })
                .build();
    }

}
