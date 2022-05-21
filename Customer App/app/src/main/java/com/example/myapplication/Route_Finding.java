package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.data.Result;
import com.example.myapplication.data.model.LoggedInUser;
import com.example.myapplication.ui.login.LoginActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Route_Finding extends AppCompatActivity implements OnMapReadyCallback,TaskLoadedCallback {
    private GoogleMap mMap;
    private MarkerOptions place1, place2;
    private Polyline currentPolyline;
    String start_address;
    String dest_address;
    String name;
    DatabaseReference reference,reference1,reference2;
    List<LatLng> mPointsOfRoute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_finding);

        try {
            SharedPreferences sharedPref = getSharedPreferences("myKey",MODE_PRIVATE);
//          start_address = sharedPref.getString("start","");
//          dest_address = sharedPref.getString("dest","");
            name =sharedPref.getString("cust_name","");
            Intent in=getIntent();

            start_address=in.getStringExtra("start");
            dest_address=in.getStringExtra("dest");
            reference= FirebaseDatabase.getInstance().getReference().child("Cust_Route");
//            LatLng start=getLatLangFromAddress(start_address);
//            LatLng dest=getLatLangFromAddress(dest_address);
//            place1 = new MarkerOptions().position(new LatLng(start.latitude,start.longitude)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title(start_address);
//            place2 = new MarkerOptions().position(new LatLng(dest.latitude,dest.longitude)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(dest_address);
//            mMap.addMarker(place1);
//            mMap.addMarker(place2);
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(start.latitude,start.longitude), 17));
            GeoCodeLocation locationAddress = new GeoCodeLocation();
            locationAddress.getAddressFromLocation(start_address,dest_address,Route_Finding.this,new GeoCoderHandler());

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map123);
            mapFragment.getMapAsync(this);

        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }

    }
    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap=googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(33.6844, 73.0479), 17));
    }

    private class GeoCoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            try {
                String locationAddress;
                switch (message.what) {
                    case 1:
                        Bundle bundle = message.getData();
                        locationAddress = bundle.getString("address");
                        break;
                    default:
                        locationAddress = null;
                }
                String[] loglat = locationAddress.split("\\s+");
                place1 = new MarkerOptions().position(new LatLng(Double.parseDouble(loglat[0]), Double.parseDouble(loglat[1]))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title(start_address);
                place2 = new MarkerOptions().position(new LatLng(Double.parseDouble(loglat[2]), Double.parseDouble(loglat[3]))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title(dest_address);
                if (mMap != null) {
                    mMap.clear();
                }

                mMap.addMarker(place2);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(loglat[0]), Double.parseDouble(loglat[1])), 17));

                new FetchURL(Route_Finding.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
            }
            catch (Exception ex)
            {
                Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
        mPointsOfRoute=currentPolyline.getPoints();
        addpoints(mPointsOfRoute);
    }

    private void addpoints(List<LatLng> mPointsOfRoute) {
        try{
            double s1lat;
            double s1lng;

            for(int i=0;i<mPointsOfRoute.size();i++)
            {
                LatLng latlng=mPointsOfRoute.get(i);
                s1lat = latlng.latitude;
                s1lng = latlng.longitude;
                MyLocation obj=new MyLocation(s1lat,s1lng);
                reference.child(name).child(Integer.toString(i+1)).setValue(obj);
            }
            Sizesetter size_obj=new Sizesetter(mPointsOfRoute.size());
            reference.child(name).child("0").setValue(size_obj);
        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }
       search_drivers();
    }
    private void search_drivers() {
        reference1 = FirebaseDatabase.getInstance().getReference().child("Driver_Route");
        // TODO: handle loggedInUser authentication

        reference1.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    for(DataSnapshot ds : snapshot.getChildren()) {
                        Sizesetter r_obj = ds.child("0").getValue(Sizesetter.class);
                        int r_size = r_obj.getRoute_size();
                        MyLocation myLocation = ds.child("1").getValue(MyLocation.class);
                        double latitude1 = myLocation.getLatitude();
                        double longitude1 = myLocation.getLongitude();
                        LatLng latlng = new LatLng(latitude1, longitude1);
                        MyLocation myLocation1 = ds.child(Integer.toString(r_size)).getValue(MyLocation.class);
                        double latitude2 = myLocation1.getLatitude();
                        double longitude2 = myLocation1.getLongitude();
                        LatLng latlng2 = new LatLng(latitude2, longitude2);

                        matchpoints(latlng,latlng2);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", error.getMessage());
            }
        });

    }
//    public void search_childs(String key){
//
//        reference2 = FirebaseDatabase.getInstance().getReference().child("Driver_Route").child(key);
//        // TODO: handle loggedInUser authentication
//        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
//            double latitude1[],longitude1[];
//            int i=0;
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    for(DataSnapshot ds : snapshot.getChildren()) {
//                        String key = ds.getKey();
//                        latitude1[i]=ds.child(key).child("latitude").getValue(double.class);
//                        longitude1[i]=ds.child(key).child("longitude").getValue(double.class);
//                        i++;
//                    }
//                    matchpoints();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("TAG", error.getMessage());
//            }
//        });
//    }
    public void matchpoints(LatLng l1,LatLng l2) {
        try {
            double s1lat;
            double s1lng;
            double s2lat;
            double s2lng;
            LatLng latlng = mPointsOfRoute.get(0);
            s1lat = latlng.latitude;
            s1lng = latlng.longitude;
            s2lat = l1.latitude;
            s2lng = l1.longitude;
            if (s1lat == s2lat && s1lng == s2lng)
            {
                latlng = mPointsOfRoute.get(mPointsOfRoute.size()-1);
                s1lat = latlng.latitude;
                s1lng = latlng.longitude;
                s2lat = l2.latitude;
                s2lng = l2.longitude;
                if (s1lat == s2lat && s1lng == s2lng)
                {
                    place1 = new MarkerOptions().position(new LatLng(l1.latitude, l1.longitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_marker)).title(start_address);

                    mMap.addMarker(place1);
                }
            }


        }catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
        }

    }

    public void move_menu(View v){
        Query checkUser= FirebaseDatabase.getInstance().getReference("Customer").orderByChild("name").equalTo(name);
        // TODO: handle loggedInUser authentication
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Result<LoggedInUser> obj;
                if(snapshot.exists()){
                    String email = snapshot.child(name).child("email").getValue(String.class);
                    Intent mint=new Intent(Route_Finding.this, MainActivity2.class);
                    mint.putExtra("name",name);
                    mint.putExtra("email",email);
                    Route_Finding.this.startActivity(mint);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Route_Finding.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }

        });

    }
//    public void isLocationOnPath(LatLng current,List<LatLng> pointslist,boolean a,int b)
//    {
//
//    }
}
