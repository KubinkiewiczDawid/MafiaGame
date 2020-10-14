package com.example.mafiagame.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mafiagame.R;
import com.example.mafiagame.ZoomOutPageTransformer;
import com.example.mafiagame.databinding.FragmentHowToPlayBinding;


public class HowToPlayFragment extends Fragment {

    private static final int NUM_PAGES = 5;

    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;

    private FragmentHowToPlayBinding howToPlayBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        howToPlayBinding = FragmentHowToPlayBinding.inflate(inflater, container, false);

        pagerAdapter = new HowToPlayFragmentAdapter(getActivity());
        pagerAdapter.createFragment(0);
        howToPlayBinding.viewpager.setPageTransformer(new ZoomOutPageTransformer());
        howToPlayBinding.viewpager.setAdapter(pagerAdapter);

        return howToPlayBinding.getRoot();
    }


    private class HowToPlayFragmentAdapter extends FragmentStateAdapter {
        public HowToPlayFragmentAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            return new HowToPlayFragment();
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }
}