package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myapplication.ui.login.LoginActivity;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }
    public void sign_in(View v){

        Intent mint=new Intent(StartActivity.this, LoginActivity.class);
        StartActivity.this.startActivity(mint);
    }
    public void sign_up(View v){

        Intent mint=new Intent(StartActivity.this,MainActivity.class);
        StartActivity.this.startActivity(mint);
    }
//    public void read_up(View v){
//
//        Intent mint=new Intent(StartActivity.this,readdata1.class);
//        StartActivity.this.startActivity(mint);
//    }
}