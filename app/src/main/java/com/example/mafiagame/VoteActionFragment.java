package com.example.mafiagame;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.mafiagame.GameActivity.getPlayer;

public class VoteActionFragment extends Fragment {

    private static final String TAG = "VoteActionFragment";

    private View mainView;
    private ArrayList<Player> playersList;
    private AnimatorSet mSetRightOut;
    private AnimatorSet mSetLeftIn;
    private View cardVotingFrontLayout;
    private View cardVotingBackLayout;
    private TextView votePlayerNameText;
    private Player votingPlayer;
    private int votingPlayerNo;
    private GridLayout gridLayout;
    private boolean playerVoted;
    private boolean voteFinished;
    private TextView votingTextInfo;
    private ImageView votingTextArrow;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.vote_action_layout, container, false);

        findViews();

        playersList = ((GameActivity)getActivity()).playersList;

        return mainView;
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            initVote();
            loadFrontAnimations();
            swipeListeners(mainView);
            voteProcess();
        }
    }

    private void findViews(){
        cardVotingFrontLayout = mainView.findViewById(R.id.card_voting_front_layout);
        cardVotingBackLayout = mainView.findViewById(R.id.card_voting_back_layout);
        votePlayerNameText = mainView.findViewById(R.id.vote_player_name_text);
        gridLayout = mainView.findViewById(R.id.voting_grid_buttons_layout);
        votingTextInfo = mainView.findViewById(R.id.voting_textInfo);
        votingTextArrow = mainView.findViewById(R.id.voting_text_arrow);
    }

    public void voteProcess(){
        if(votingPlayerNo == playersList.size()) {
            voteFinished = true;
            votePlayerNameText.setText("");
            votingTextInfo.setText("Voting finished");
            votingTextArrow.setRotationY(180f);
        }else if (playersList.get(votingPlayerNo).isAlive()){
            votingPlayer = playersList.get(votingPlayerNo);
            votePlayerNameText.setText(votingPlayer.getName());
            votingPlayerNo++;
        }else{
            votingPlayerNo++;
            voteProcess();
        }
    }

    private void initVote(){
        float x = cardVotingFrontLayout.getTranslationX();
        if(x == -2000){
            cardVotingFrontLayout.setTranslationX(0);
            votingTextInfo.setText("Swipe to vote");
            votingTextArrow.setRotationY(0);
        }
        votingPlayerNo = 0;
        playerVoted = false;
        voteFinished = false;
    }

    private void swipeListeners(View view) {
        cardVotingFrontLayout.setOnTouchListener(new OnSwipeTouchListener(getActivity()){
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSwipeRight() {
                if(!voteFinished) {
                    setButtonsLayout(cardVotingBackLayout);
                    rotateFrontToBack();
                }
            }
            @Override
            public void onSwipeLeft() {
                if(voteFinished) {
                    cardAnimationToFinish();
                }
            }
        });
        cardVotingBackLayout.setOnTouchListener(new OnSwipeTouchListener(getActivity()){
            @Override
            public void onSwipeLeft() {
                if(playerVoted) {
                    voteProcess();
                    if(voteFinished){
                        for (Player player : playersList) {
                            if (player.isAlive()) Log.v(TAG, player.toString() + " voted on " + player.getVotedOnPlayer().getName());
                        }
                    }
                    rotateBackToFront();
                    playerVoted = false;
                } else {
                    Toast.makeText(getContext(), "You didnt vote!", Toast.LENGTH_LONG).show();
                }
            }

        });
    }

    private void loadFrontAnimations() {
        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), R.animator.out_animation);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), R.animator.in_animation);
    }

    private void loadBackAnimations() {
        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), R.animator.out_animation_back);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), R.animator.in_animation_back);
    }

    private void rotateFrontToBack() {
        cardVotingBackLayout.setVisibility(View.VISIBLE);
        mSetRightOut.setTarget(cardVotingFrontLayout);
        mSetRightOut.start();
        mSetRightOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                cardVotingFrontLayout.setVisibility(View.GONE);
                loadBackAnimations();
            }
        });
        mSetLeftIn.setTarget(cardVotingBackLayout);
        mSetLeftIn.start();
    }
    private void rotateBackToFront() {
        cardVotingFrontLayout.setVisibility(View.VISIBLE);
        mSetRightOut.setTarget(cardVotingBackLayout);
        mSetRightOut.start();
        mSetRightOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                cardVotingBackLayout.setVisibility(View.GONE);
                loadFrontAnimations();
                clearButtonsLayout();
            }
        });
        mSetLeftIn.setTarget(cardVotingFrontLayout);
        mSetLeftIn.start();
    }

    private void cardAnimationToFinish(){
        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), R.animator.out_left_animation);
        mSetRightOut.setTarget(cardVotingFrontLayout);
        mSetRightOut.start();
        mSetRightOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ((GameActivity)getActivity()).setViewPager(GameActivity.VOTE_RESULTS_ACTION_FRAGMENT);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setButtonsLayout(View parentView){

        int playersAmount = playersList.size();
        int buttonsMargins = 10;

 //       gridLayout.setRowCount(playersAmount%2==0? playersAmount/2 : (playersAmount/2)+1);
        if(playersAmount <= 6){
            gridLayout.setColumnCount(2);
        } else {
            gridLayout.setColumnCount(3);
            buttonsMargins = 10;
        }


        int columnCount = playersAmount%2==0? playersAmount/2 : (playersAmount/2)+1;

        for(int i = 0, c = 0; i < playersAmount; i++, c++){

            if(i == columnCount)
            {
                c = 0;
            }

            String playerName = playersList.get(i).getName();


            Button btnTag = new Button(getActivity());
            btnTag.setText(playerName);
            Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.stencil1935);
            btnTag.setTypeface(typeface);
            btnTag.setAutoSizeTextTypeUniformWithConfiguration(35, 60, 2, TextView.AUTO_SIZE_TEXT_TYPE_NONE);
            btnTag.setTextColor(getResources().getColor(R.color.black, null));
            btnTag.setId(i+1);
            btnTag.setTag(playerName);
            btnTag.setBackground(getResources().getDrawable(R.drawable.voting_buttons_background, null));

            if(playersList.get(i).isAlive()){
                setVoteButtonOnClickListener(btnTag);
            } else {
                btnTag.setEnabled(false);
                btnTag.setBackground(getResources().getDrawable(R.drawable.dead_player_background, null));
                btnTag.setTextColor(getResources().getColor(R.color.darkRed, null));
                btnTag.setAlpha(0.7f);
            }

            FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            parentView.setLayoutParams(param);

            GridLayout.LayoutParams gridParams = new GridLayout.LayoutParams();
            if(playersAmount <= 6){
                gridParams.width = 300;
            } else {
                gridParams.width = 250;
            }

            gridParams.setMargins(buttonsMargins, buttonsMargins, buttonsMargins, buttonsMargins);
            btnTag.setLayoutParams(gridParams);
            //add button to the layout
            if(!playersList.get(i).equals(votingPlayer)) {
                gridLayout.addView(btnTag);
            }
        }
    }

    private void clearButtonsLayout(){
        gridLayout.removeAllViews();
    }

    private void setVoteButtonOnClickListener(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectButton(v, button);

                Player playerVotedOn = getPlayer(button.getTag().toString());
                votingPlayer.vote(playersList.get(playersList.indexOf(playerVotedOn)));

                playerVoted = true;
            }
        });
    }

    private void selectButton(View view, Button button){
        ViewGroup buttons = ((ViewGroup)gridLayout);
        Log.v(TAG, buttons.getChildCount() + "");
        for (int i = 0; i < buttons.getChildCount(); i++) {
            String buttonTag = buttons.getChildAt(i).getTag().toString();
            Log.v(TAG, buttonTag);
            if (Objects.equals(buttons.getChildAt(i).getBackground().getConstantState(), getResources().getDrawable(R.drawable.voted_buttons_background, null).getConstantState())) {
                buttons.getChildAt(i).setBackground(getResources().getDrawable(R.drawable.voting_buttons_background, null));
                buttons.getChildAt(i).setElevation(0f);
                buttons.getChildAt(i).setTranslationZ(0f);
            }
        }
        button.setBackground(getResources().getDrawable(R.drawable.voted_buttons_background, null));
        button.setElevation(15f);
        button.setTranslationZ(15f);
        button.setStateListAnimator(null);
    }
}
