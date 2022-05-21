package com.example.DriverApp.ui.freerides;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.DriverApp.databinding.FragmentFreeridesBinding;


public class FreeRidesFragment extends Fragment{
    private FreeRidesViewModel freeridesViewModel;
    private FragmentFreeridesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        freeridesViewModel =
                new ViewModelProvider(this).get(FreeRidesViewModel.class);

        binding = FragmentFreeridesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
