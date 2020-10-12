package com.example.mafiagame.ui.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.mafiagame.components.Citizen;
import com.example.mafiagame.components.Police;
import com.example.mafiagame.components.Mafia;
import com.example.mafiagame.activity.GameActivity;
import com.example.mafiagame.components.Player;

import com.example.mafiagame.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VoteResultsActionFragment extends Fragment {

    private static final String TAG = "VoteResultsActionFragment";
    private ArrayList<Player> playersList;
    ImageView killedPlayerRoleImage;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vote_results_action, container, false);

        killedPlayerRoleImage = view.findViewById(R.id.killed_role_image);
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

    @Override
    public void onDetach() {
        super.onDetach();
        killedPlayerRoleImage.setVisibility(View.GONE);
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

                LinearLayout innerLayout = new LinearLayout(getActivity());
                innerLayout.setOrientation(LinearLayout.HORIZONTAL);
 //               innerLayout.setBackground(getActivity().getDrawable(R.drawable.white_rectangle_with_black_boarder));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                params.gravity = Gravity.CENTER;

                TextView playerNameText = new TextView(getActivity());
                playerNameText.setText(playerName + " " + voteCount);
                playerNameText.setTextSize(35f);
                Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.stencil1935);
                playerNameText.setTypeface(typeface);
                playerNameText.setGravity(Gravity.CENTER);
                playerNameText.setLayoutParams(params);

                ProgressBar voteCountProgressBar = new ProgressBar(getActivity(), null, R.style.MyProgressBarTwo, R.style.MyProgressBarTwo );
            //    voteCountProgressBar.setBackground(getActivity().getDrawable(R.drawable.white_rectangle_with_black_boarder));
                voteCountProgressBar.setProgress(voteCount);
                voteCountProgressBar.setMax(playersList.size());
                voteCountProgressBar.setLayoutParams(params);

                //add text to the layout
                innerLayout.addView(playerNameText);
                innerLayout.addView(voteCountProgressBar);

                votingResultsLinearLayout.addView(innerLayout);
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

        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.stencil1935);

        if(numberOfPlayersWithMostVotes > 1){
            TextView voteAgainText = new TextView(getActivity());
            voteAgainText.setText("Tie!\nPress to vote again");
            voteAgainText.setGravity(Gravity.CENTER);
            voteAgainText.setTextSize(45);
            voteAgainText.setTypeface(typeface);
            LinearLayout.LayoutParams voteAgainTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            voteAgainTextParams.bottomMargin = 100;
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
            killedPlayerText.setGravity(Gravity.CENTER);
            killedPlayerText.setTextColor(getActivity().getColor(R.color.darkRed));
            killedPlayerText.setTextSize(40f);
            killedPlayerText.setTypeface(typeface);
            killedPlayerText.setId(R.id.whoCityKilledText);
            LinearLayout.LayoutParams killedPlayerTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            killedPlayerText.setLayoutParams(killedPlayerTextParams);

            Button gameOnButton = new Button(getActivity());
            gameOnButton.setText("Game on!");
            gameOnButton.setTextSize(35f);
            gameOnButton.setTypeface(typeface);
            gameOnButton.setBackground(getActivity().getDrawable(R.drawable.white_rectangle_with_black_boarder));
            gameOnButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((GameActivity)getActivity()).setViewPager(GameActivity.MAFIA_ACTION_FRAGMENT);
                }
            });
            LinearLayout.LayoutParams gameOnTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            gameOnTextParams.bottomMargin = 100;
            gameOnButton.setLayoutParams(gameOnTextParams);
            votingResultsLinearLayout.addView(killedPlayerText);
            votingResultsLinearLayout.addView(gameOnButton);

            if(playerWithMostVotes.getRole().getClass() == Mafia.class){
                killedPlayerRoleImage.setImageDrawable(getActivity().getDrawable(R.drawable.gangster_pick));
            } else if(playerWithMostVotes.getRole().getClass() == Police.class){
                killedPlayerRoleImage.setImageDrawable(getActivity().getDrawable(R.drawable.police_officer));
            } else if(playerWithMostVotes.getRole().getClass() == Citizen.class){
                killedPlayerRoleImage.setImageDrawable(getActivity().getDrawable(R.drawable.citizen));
            }

            killedPlayerRoleImage.setVisibility(View.VISIBLE);
        }
    }
}
