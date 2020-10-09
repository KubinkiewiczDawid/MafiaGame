package com.example.mafiagame;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.mafiagame.GameActivity.getNumberOfMafiaAlive;
import static com.example.mafiagame.GameActivity.isPlayerMafia;

public class VoteResultsActionFragment extends Fragment {

    private static final String TAG = "VoteResultsActionFragment";
    private ArrayList<Player> playersList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vote_results_action_layout, container, false);

        playersList = ((GameActivity)getActivity()).playersList;

        return view;
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            setVoteResultLayout();
        }
    }

    private void setVoteResultLayout(){

        LinearLayout votingResultsLinearLayout = getView().findViewById(R.id.votingResultsLinearLayout);
        votingResultsLinearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        votingResultsLinearLayout.setPadding(50, 0, 50, 0);
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        param.topMargin = 100;
        param.addRule(RelativeLayout.CENTER_HORIZONTAL);

        votingResultsLinearLayout.setLayoutParams(param);

        Map<Player, Integer> playerWithVotes = new HashMap<>();

        for(int i = 0; i < playersList.size(); i++){

            if(playersList.get(i).isAlive()) {
                String playerName = playersList.get(i).getName();
                int voteCount = 0;
                for (int j = 0; j < playersList.size(); j++) {
                    if (playersList.get(j).isAlive()) {
                        if (playersList.get(j).getVotedOnPlayer().equals(playersList.get(i)))
                            voteCount++;
                    }
                }
                playerWithVotes.put(playersList.get(i), voteCount);

                TextView playerNameText = new TextView(getActivity());
                playerNameText.setText(playerName + " " + voteCount);
                playerNameText.setGravity(Gravity.CENTER);

                ProgressBar voteCountProgressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleHorizontal);
                voteCountProgressBar.setProgress(voteCount);
                voteCountProgressBar.setMax(playersList.size());


                //add text to the layout
                votingResultsLinearLayout.addView(playerNameText);
                votingResultsLinearLayout.addView(voteCountProgressBar);
            }
        }

        int mostVotes = 0;
        for(Map.Entry<Player, Integer> entry: playerWithVotes.entrySet()){
            Player key = entry.getKey();
            int value = entry.getValue();
            Log.v(TAG, key.getName() + " " + value);
            if(mostVotes < value){
                mostVotes = value;
            }
        }

        int numberOfPlayersWithMostVotes = 0;
        for(Map.Entry<Player, Integer> entry: playerWithVotes.entrySet()){
            Player key = entry.getKey();
            int value = entry.getValue();
            Log.v(TAG, key.getName() + " " + value);
            if(value == mostVotes){
                numberOfPlayersWithMostVotes++;
            }
        }
        Log.v(TAG, Integer.toString(mostVotes));
        Log.v(TAG, Integer.toString(numberOfPlayersWithMostVotes));

        if(numberOfPlayersWithMostVotes > 1){
            TextView voteAgainText = new TextView(getActivity());
            voteAgainText.setText("Tie!\nPress to vote again");
            voteAgainText.setGravity(Gravity.CENTER);
            voteAgainText.setTextSize(30);
            RelativeLayout.LayoutParams voteAgainTextParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            voteAgainTextParams.topMargin = 100;
            voteAgainText.setLayoutParams(voteAgainTextParams);
            voteAgainText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    votingResultsLinearLayout.removeAllViews();
                    ((GameActivity)getActivity()).setViewPager(GameActivity.VOTE_ACTION_FRAGMENT);
                }
            });
            votingResultsLinearLayout.addView(voteAgainText);
        } else {
            Player playerWithMostVotes = null;
            for(Map.Entry<Player, Integer> entry: playerWithVotes.entrySet()){
                if(entry.getValue() == mostVotes) {
                    playerWithMostVotes = entry.getKey();
                }
            }
            playerWithMostVotes.setAlive(false);
//            if(checkIfGameIsOver()){
//                ((GameActivity)getActivity()).setViewPager(GameActivity.GAME_OVER_FRAGMENT);
//            }
            TextView killedPlayerText = new TextView(getActivity());
            killedPlayerText.setText("You killed " + playerWithMostVotes.getName() + "\nhe was " + playerWithMostVotes.getRole());
            killedPlayerText.setGravity(Gravity.CENTER_HORIZONTAL);
            killedPlayerText.setTextSize(30);
            killedPlayerText.setId(R.id.whoCityKilledText);
            RelativeLayout.LayoutParams killedPlayerTextParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            killedPlayerTextParams.addRule(RelativeLayout.BELOW, R.id.votingResultsLinearLayout);
            killedPlayerText.setLayoutParams(killedPlayerTextParams);

            Button gameOnButton = new Button(getActivity());
            gameOnButton.setText("Game on!");
            gameOnButton.setGravity(Gravity.CENTER_HORIZONTAL);
            gameOnButton.setTextSize(30);
            gameOnButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //((GameActivity)getActivity()).setViewPager(GameActivity.POLICE_ACTION_FRAGMENT_NO);
                    ((GameActivity)getActivity()).setViewPager(GameActivity.MAFIA_ACTION_FRAGMENT_NO);
                }
            });
            RelativeLayout.LayoutParams gameOnTextParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            gameOnTextParams.addRule(RelativeLayout.BELOW, R.id.whoCityKilledText);
            gameOnButton.setLayoutParams(gameOnTextParams);

            votingResultsLinearLayout.addView(killedPlayerText);
            votingResultsLinearLayout.addView(gameOnButton);
        }
    }
}
