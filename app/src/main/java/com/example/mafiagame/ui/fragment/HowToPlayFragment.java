package com.example.mafiagame.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mafiagame.R;
import com.example.mafiagame.databinding.FragmentHowToPlayBinding;


public class HowToPlayFragment extends Fragment {

    private FragmentHowToPlayBinding howToPlayBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        howToPlayBinding = FragmentHowToPlayBinding.inflate(inflater, container, false);


        return howToPlayBinding.getRoot();
    }
}