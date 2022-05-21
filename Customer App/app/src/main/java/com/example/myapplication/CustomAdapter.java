package com.example.myapplication;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.zip.Inflater;

public class CustomAdapter extends BaseAdapter {
    Context context;
    String tripno[];
    String status[];
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, String[] tripno, String[] status) {
        this.context = context;
        this.tripno = tripno;
        this.status = status;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return tripno.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.activity_listview, null);
        TextView trip = (TextView) view.findViewById(R.id.trip);
        TextView stat = (TextView) view.findViewById(R.id.stat);
        trip.setText(tripno[i]);
        stat.setText(status[i]);
        return view;
    }
}