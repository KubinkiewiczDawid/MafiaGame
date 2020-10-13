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
import com.example.mafiagame.activity.MainActivity;
import com.example.mafiagame.components.Mafia;
import com.example.mafiagame.components.Player;
import com.example.mafiagame.components.Police;
import com.example.mafiagame.R;
import com.example.mafiagame.databinding.FragmentEndRoundBinding;


public class EndRoundFragment extends Fragment {

    private static final String TAG = "EndRoundFragment";
    private String checkedPlayerString;
    private Drawable policeOfficer;
    private Drawable citizen;
    private Player killedPlayer;
    private boolean policeHit;

    private MediaPlayer mediaPlayer;

    private FragmentEndRoundBinding endRoundBinding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        endRoundBinding = FragmentEndRoundBinding.inflate(inflater, container, false);

        policeHit = false;

//        checkedPlayerText.setText(checkedPlayerString);
        Drawable[] shape = {ContextCompat.getDrawable(getActivity(), R.drawable.card_shape), ContextCompat.getDrawable(getActivity(), R.drawable.card_shape_mafia)};
        TransitionDrawable trans = new TransitionDrawable(shape);
        //This will work also on old devices. The latest API says you have to use setBackground instead.
        endRoundBinding.endRoundKilledCard.endRoundMafiaBackground.setImageDrawable(trans);
        trans.startTransition(this.getResources().getInteger(R.integer.end_round_info_timer));

        citizen = ContextCompat.getDrawable(getActivity(), R.drawable.citizen);
        policeOfficer = ContextCompat.getDrawable(getActivity(), R.drawable.police_officer);

        return endRoundBinding.getRoot();
    }

    public void setEndFragmentDrawables(){
        if(killedPlayer.getRole().getClass() == Police.class){
            endRoundBinding.endRoundKilledCard.killedPlayerImage.setImageDrawable(policeOfficer);
        }else if (killedPlayer.getRole().getClass() == Citizen.class){
            endRoundBinding.endRoundKilledCard.killedPlayerImage.setImageDrawable(citizen);
        }
    }

    public void updateKilledRole(Player player){
        Mafia.killPlayer(player);
        endRoundBinding.endRoundKilledCard.killedPlayerText.setText(player.getName());
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
        endRoundBinding.endRoundCheckedCard.policeHitText.setText(checkedPlayerString);
    }

    public void killedCardPunchAnim(){
        endRoundBinding.endRoundKilledFrame.setVisibility(View.VISIBLE);
        AnimatorSet mKillCardPunchIn = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.punching_in_card_animation);
        mKillCardPunchIn.setTarget(endRoundBinding.endRoundKilledFrame);
        mKillCardPunchIn.start();
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.m1_gunfire);
        mediaPlayer.start();
    }

    public void policeHitInfoAnim(){
        Drawable[] shape = {ContextCompat.getDrawable(getActivity(), R.drawable.card_shape), ContextCompat.getDrawable(getActivity(), R.drawable.card_shape_police)};
        TransitionDrawable trans = new TransitionDrawable(shape);
        //This will work also on old devices. The latest API says you have to use setBackground instead.
        endRoundBinding.endRoundCheckedCard.endRoundPoliceBackground.setImageDrawable(trans);
        trans.startTransition(this.getResources().getInteger(R.integer.end_round_info_timer));

        endRoundBinding.endRoundCheckedFrame.setVisibility(View.VISIBLE);

        AnimatorSet mCheckedCardPunchIn = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.punching_in_card_animation);
        mCheckedCardPunchIn.setTarget(endRoundBinding.endRoundCheckedFrame);
        mCheckedCardPunchIn.start();

        if(policeHit){
            mediaPlayer = MediaPlayer.create(getContext(), R.raw.police_siren);
            mediaPlayer.start();
        }
    }

    public void throwEndRoundKilledCardAnim(){
        AnimatorSet mThrow = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.card_throw_animation);
        mThrow.setTarget(endRoundBinding.endRoundKilledFrame);
        mThrow.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ((MainActivity)getActivity()).endRoundFragmentTimer.start();
                policeHitInfoAnim();
            }
        });
        mThrow.start();
    }
}
