package com.example.mafiagame;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TalkActionFragment extends Fragment {

    TextView beginVoteTimeText;
    TextView killedPlayerName;
    TextView checkedPlayerResult;
    TextView policeText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.talk_action_layout, container, false);

        beginVoteTimeText = view.findViewById(R.id.beginVoteTimeText);
        killedPlayerName = view.findViewById(R.id.killedPlayerName);
        checkedPlayerResult = view.findViewById(R.id.checked_player_result);
        policeText = view.findViewById(R.id.police_text);

        Button beginVoteButton = view.findViewById(R.id.beginVoteButton);
        beginVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameActivity)getActivity()).setViewPager(4);
            }
        });

        return view;
    }

    public void setKilledPlayerNameText(Player player){
        killedPlayerName.setText(player.getName());
    }

    public void setCheckedPlayerResult(Player player){
        if(player == null){
            policeText.setTextColor(getResources().getColor(R.color.grey, null));
            checkedPlayerResult.setTextColor(getResources().getColor(R.color.grey, null));
            checkedPlayerResult.setText("didn't hit");
        } else if(player.getRole().getClass() == Mafia.class) {
            policeText.setTextColor(getResources().getColor(R.color.darkerBlue, null));
            checkedPlayerResult.setTextColor(getResources().getColor(R.color.darkerBlue, null));
            checkedPlayerResult.setText("hit!");
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
}
