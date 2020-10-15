package com.example.mafiagame.ui.fragment;

import android.os.Bundle;

import androidx.annotation.IntDef;
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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


public class HowToPlayFragment extends Fragment {

    @IntDef({VISIBLE, INVISIBLE, GONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Visibility{}

    private static final int NUM_PAGES = 5;

    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;

    private FragmentHowToPlayBinding howToPlayBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        howToPlayBinding = FragmentHowToPlayBinding.inflate(inflater, container, false);

        setViewVisibility(INVISIBLE);

        pagerAdapter = new HowToPlayFragmentAdapter(getActivity());
        pagerAdapter.createFragment(0);
        howToPlayBinding.viewpager.setPageTransformer(new ZoomOutPageTransformer());
        howToPlayBinding.viewpager.setAdapter(pagerAdapter);

        return howToPlayBinding.getRoot();
    }

    public void setViewVisibility(@Visibility int visibility){
        howToPlayBinding.howToPlayParentView.setVisibility(visibility);
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