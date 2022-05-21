package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class fragment_trips extends Activity {

    ListView simpleList;
    String tripno[] = {"Trip#1", "Trip#2", "Trip#3", "Trip#4", "Trip#5", "Trip#6"};
    String status[] = {"Amount:Rs.100,Distance:15km","Amount:Rs.120,Distance:15km","Amount:Rs.80,Distance:10km","Amount:Rs.150,Distance:20km","Amount:Rs.200,Distance:25km","Amount:Rs.175,Distance:18km" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        simpleList = (ListView) findViewById(R.id.simpleListView);
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), tripno, status);
        simpleList.setAdapter(customAdapter);
    }
}
