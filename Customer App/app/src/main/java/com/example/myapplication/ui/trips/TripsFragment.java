package com.example.myapplication.ui.trips;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.CustomAdapter;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentFreeridesBinding;
import com.example.myapplication.databinding.FragmentTripsBinding;

public class TripsFragment extends Fragment {
    private FragmentTripsBinding binding;
    ListView simpleList;
    String tripno[] = {"Trip#1", "Trip#2", "Trip#3", "Trip#4", "Trip#5", "Trip#6"};
    String status[] = {"Amount:Rs.100,Distance:15km","Amount:Rs.120,Distance:15km","Amount:Rs.80,Distance:10km","Amount:Rs.150,Distance:20km","Amount:Rs.200,Distance:25km","Amount:Rs.175,Distance:18km" };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentTripsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
