package com.example.mafiagame.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mafiagame.R;
import com.example.mafiagame.databinding.FragmentGameMenuBinding;

public class GameMenu extends Fragment {

    private FragmentGameMenuBinding gameMenuBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        gameMenuBinding = FragmentGameMenuBinding.inflate(inflater, container, false);

        return gameMenuBinding.getRoot();
    }
}