package com.example.mafiagame.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.example.mafiagame.activity.MainActivity;
import com.example.mafiagame.databinding.FragmentGameMenuBinding;

public class GameMenuFragment extends Fragment {

    public static final String TAG = "StartActivity";
    public static final String EXTRA_MESSAGE = "com.example.mafiagame.MESSAGE";
    private static final int MIN_PLAYERS_NO = 6;
    private static final int MAX_PLAYERS_NO = 12;
    public int numberOfPlayers;
    

    FragmentGameMenuBinding gameMenuBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        gameMenuBinding = FragmentGameMenuBinding.inflate(inflater, container, false);

        int numberOfQuestionMarks = ((ViewGroup)gameMenuBinding.questionMarksFrame.questionMarksView).getChildCount();
        Log.v(TAG, "Number of questionmarks: " + numberOfQuestionMarks);

        numberOfPlayers = MIN_PLAYERS_NO;

        gameMenuBinding.gameMenuFrame.uiPlayersNumberButton.numberOfPlayersText.setText(String.valueOf(numberOfPlayers));

        gameMenuBinding.gameMenuFrame.uiPlayersNumberButton.increasePlayerButton.setOnClickListener((View.OnClickListener) v -> {
            if(numberOfPlayers < MAX_PLAYERS_NO) {
                numberOfPlayers++;
                ((ViewGroup) gameMenuBinding.questionMarksFrame.questionMarksView).getChildAt(numberOfPlayers-1).animate()
                        .alpha(0.6f)
                        .setDuration(300).start();
                gameMenuBinding.gameMenuFrame.gameTitle.animate()
                        .alpha(gameMenuBinding.gameMenuFrame.gameTitle.getAlpha() + 0.02f)
                        .setDuration(300).start();
                gameMenuBinding.gameMenuFrame.uiPlayersNumberButton.numberOfPlayersText.setText(String.valueOf(numberOfPlayers));
            }
        });

        gameMenuBinding.gameMenuFrame.uiPlayersNumberButton.decreasePlayerButton.setOnClickListener((View.OnClickListener) v -> {
            if(numberOfPlayers > MIN_PLAYERS_NO){
                ((ViewGroup) gameMenuBinding.questionMarksFrame.questionMarksView).getChildAt(numberOfPlayers-1).animate()
                        .alpha(0.0f)
                        .setDuration(300)
                        .start();
                gameMenuBinding.gameMenuFrame.gameTitle.animate()
                        .alpha(gameMenuBinding.gameMenuFrame.gameTitle.getAlpha() - 0.02f)
                        .setDuration(300).start();
                numberOfPlayers--;
                gameMenuBinding.gameMenuFrame.uiPlayersNumberButton.numberOfPlayersText.setText(String.valueOf(numberOfPlayers));
            }
        });

        gameMenuBinding.gameMenuFrame.uiPlayButton.playButton.setOnClickListener(v -> {
            menuFadeOut();
            Animation anim = new ScaleAnimation(
                    1f, 1f, // Start and end values for the X axis scaling
                    1f, 2f, // Start and end values for the Y axis scaling
                    Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                    Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
            anim.setFillAfter(false); // Needed to keep the result of the animation
            anim.setDuration(1000);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    ((MainActivity)getActivity()).setViewPager(MainActivity.PLAYER_ASSIGNMENT_FRAGMENT);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            gameMenuBinding.gameMenuFrame.gangsterImage.startAnimation(anim);
        });

        gameMenuBinding.gameMenuFrame.uiHowToPlayButton.howToPlayButton.setOnClickListener(v -> ((MainActivity)getActivity()).setViewPager(MainActivity.HOW_TO_PLAY_FRAGMENT));

        return gameMenuBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        gameMenuBinding.gameMenu.setAlpha(1);
        gameMenuBinding.questionMarks.setAlpha(1);
    }

    public int getNumberOfPlayers(){
        return numberOfPlayers;
    }

    private void menuFadeOut(){
        gameMenuBinding.gameMenu.animate()
                .alpha(0.0f)
                .setDuration(1000)
                .start();
        gameMenuBinding.questionMarks.animate()
                .alpha(0.0f)
                .setDuration(1000)
                .start();
    }
}