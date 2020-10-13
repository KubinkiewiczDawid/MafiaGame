package com.example.mafiagame.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mafiagame.activity.MainActivity;
import com.example.mafiagame.components.Mafia;
import com.example.mafiagame.components.Player;
import com.example.mafiagame.R;
import com.example.mafiagame.databinding.FragmentTalkActionBinding;

public class TalkActionFragment extends Fragment {


    private FragmentTalkActionBinding talkActionBinding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        talkActionBinding = FragmentTalkActionBinding.inflate(inflater, container, false);

        talkActionBinding.beginVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setViewPager(MainActivity.VOTE_ACTION_FRAGMENT);
            }
        });

        return talkActionBinding.getRoot();
    }

    public void setKilledPlayerNameText(Player player){
        talkActionBinding.killedPlayerName.setText(player.getName());
    }

    public void setCheckedPlayerResult(Player player){
        if(player == null){
            talkActionBinding.policeText.setTextColor(getResources().getColor(R.color.grey, null));
            talkActionBinding.checkedPlayerResult.setTextColor(getResources().getColor(R.color.grey, null));
            talkActionBinding.checkedPlayerResult.setText("didn't hit");
        } else if(player.getRole().getClass() == Mafia.class) {
            talkActionBinding.policeText.setTextColor(getResources().getColor(R.color.darkerBlue, null));
            talkActionBinding.checkedPlayerResult.setTextColor(getResources().getColor(R.color.darkerBlue, null));
            talkActionBinding.checkedPlayerResult.setText("hit!");
        }
    }

    public void timeTextChange(int value){
        int seconds = (int) value / 1000;
        String timeString = String.format("%02d:%02d", (seconds / 60), (seconds % 60));
        Log.v("seekBar progress changed", timeString);
        talkActionBinding.beginVoteTimeText.setText(timeString);
        Log.v("Seconds left", String.valueOf((value / 1000)));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
}
