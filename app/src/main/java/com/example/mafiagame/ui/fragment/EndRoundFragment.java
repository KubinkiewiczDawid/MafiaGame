package com.example.mafiagame.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.mafiagame.components.Citizen;
import com.example.mafiagame.activity.GameActivity;
import com.example.mafiagame.components.Player;
import com.example.mafiagame.components.Police;
import com.example.mafiagame.R;


public class EndRoundFragment extends Fragment {

    private static final String TAG = "EndRoundFragment";
    private String killedPlayerString;
    private String checkedPlayerString;
    private TextView killedPlayerText;
    private TextView killedText;
    private TextView checkedPlayerText;
    private ImageView killedPlayerImage;
    private TextView policeHitText;
    private ImageView endRoundMafiaBackground;
    private ImageView endRoundPoliceBackground;
    private Drawable policeOfficer;
    private Drawable citizen;
    private Player killedPlayer;
    private View endRoundKilledCard;
    private View endRoundCheckedCard;
    private boolean policeHit;

    private MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_end_round, container, false);
        findViews(view);

        policeHit = false;
        killedPlayerText.setText(killedPlayerString);
//        checkedPlayerText.setText(checkedPlayerString);
        Drawable[] shape = {ContextCompat.getDrawable(getActivity(), R.drawable.card_shape), ContextCompat.getDrawable(getActivity(), R.drawable.card_shape_mafia)};
        TransitionDrawable trans = new TransitionDrawable(shape);
        //This will work also on old devices. The latest API says you have to use setBackground instead.
        endRoundMafiaBackground.setImageDrawable(trans);
        trans.startTransition(this.getResources().getInteger(R.integer.end_round_info_timer));

        citizen = ContextCompat.getDrawable(getActivity(), R.drawable.citizen);
        policeOfficer = ContextCompat.getDrawable(getActivity(), R.drawable.police_officer);

        if(killedPlayer.getRole().getClass() == Police.class){
            killedPlayerImage.setImageDrawable(policeOfficer);
        }else if (killedPlayer.getRole().getClass() == Citizen.class){
            killedPlayerImage.setImageDrawable(citizen);
        }

        return view;
    }

    private void findViews(View view){
        killedPlayerImage = view.findViewById(R.id.killed_player_image);
        killedPlayerText = view.findViewById(R.id.killed_player_text);
        killedText = view.findViewById(R.id.killed_text);
        endRoundMafiaBackground = view.findViewById(R.id.end_round_mafia_background);
        endRoundPoliceBackground = view.findViewById(R.id.end_round_police_background);
        endRoundKilledCard = view.findViewById(R.id.end_round_killed_card);
        endRoundCheckedCard = view.findViewById(R.id.end_round_checked_card);
        policeHitText = view.findViewById(R.id.police_hit_text);
//        checkedPlayerText = view.findViewById(R.id.checkedPlayerText);
    }

    public void updateKilledRole(Player player){
       // killedPlayerString = player.getName() + " " + player.getRole().toString().toLowerCase();
        killedPlayerString = player.getName();
        killedPlayer = player;
    }

    public void updateCheckedPlayerText(Player player){
        if(player != null){
            checkedPlayerString = "hit!";
            policeHit = true;
        }else{
            checkedPlayerString = "didn't hit";
            policeHit = false;
        }
        policeHitText.setText(checkedPlayerString);
    }

    public void killedCardPunchAnim(){
        endRoundKilledCard.setVisibility(View.VISIBLE);
        AnimatorSet mKillTextFadeIn = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.punching_in_card_animation);
        mKillTextFadeIn.setTarget(endRoundKilledCard);
        mKillTextFadeIn.start();
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.m1_gunfire);
        mediaPlayer.start();
    }

    public void policeHitInfoAnim(){
        Drawable[] shape = {ContextCompat.getDrawable(getActivity(), R.drawable.card_shape), ContextCompat.getDrawable(getActivity(), R.drawable.card_shape_police)};
        TransitionDrawable trans = new TransitionDrawable(shape);
        //This will work also on old devices. The latest API says you have to use setBackground instead.
        endRoundPoliceBackground.setImageDrawable(trans);
        trans.startTransition(this.getResources().getInteger(R.integer.end_round_info_timer));

        endRoundCheckedCard.setVisibility(View.VISIBLE);

        AnimatorSet mKillTextFadeIn = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.punching_in_card_animation);
        mKillTextFadeIn.setTarget(endRoundCheckedCard);
        mKillTextFadeIn.start();

        if(policeHit){
            mediaPlayer = MediaPlayer.create(getContext(), R.raw.police_siren);
            mediaPlayer.start();
        }
    }

    public void throwEndRoundKilledCardAnim(){
        AnimatorSet mThrow = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.card_throw_animation);
        mThrow.setTarget(endRoundKilledCard);
        mThrow.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ((GameActivity)getActivity()).endRoundFragmentTimer.start();
                policeHitInfoAnim();
            }
        });
        mThrow.start();
    }
}
