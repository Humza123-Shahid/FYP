package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class get_strt_dest extends AppCompatActivity {
    EditText start;
    EditText dest;
    String start_address;
    String dest_address;
    Button getDirection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_strt_dest);
        getDirection = findViewById(R.id.getdirection);
        start=findViewById(R.id.start);
        dest=findViewById(R.id.destination);

        getDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    start_address = start.getText().toString();
                    dest_address = dest.getText().toString();
                    start.setText("");
                    dest.setText("");
//                SharedPreferences sharedPref = get_strt_dest.this.getPreferences(Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPref.edit();
//                editor.putString("start", start_address);
//                editor.putString("dest",dest_address );
//                editor.apply();
                    Intent mint=new Intent(get_strt_dest.this,Route_Finding.class);
                    mint.putExtra("start",start_address);
                    mint.putExtra("dest",dest_address);
                    get_strt_dest.this.startActivity(mint);
                    }
                catch (Exception ex)
                {
                        Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
                }


            }
        });
    }
}