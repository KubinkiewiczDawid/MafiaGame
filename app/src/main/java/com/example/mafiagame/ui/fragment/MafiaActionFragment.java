package com.example.mafiagame.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.mafiagame.activity.MainActivity;
import com.example.mafiagame.components.Player;
import com.example.mafiagame.R;
import com.example.mafiagame.components.Police;
import com.example.mafiagame.databinding.FragmentMafiaActionBinding;

import java.util.Objects;

import static com.example.mafiagame.activity.MainActivity.getPlayer;
import static com.example.mafiagame.activity.MainActivity.isPoliceAlive;

public class MafiaActionFragment extends Fragment {
    public static final String TAG = "mafiaActionFragment";

    private Player playerToKill;

    private FragmentMafiaActionBinding mafiaActionBinding;

    //TODO: for test purposes
    private boolean buttonsSet = false;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mafiaActionBinding = FragmentMafiaActionBinding.inflate(inflater, container, false);

        //TODO: for test purposes
        if(!buttonsSet) {
            if (((MainActivity) getActivity()).testRun) {
                setButtonsLayout();
            }
            buttonsSet = true;
        }

        return mafiaActionBinding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            //Restore the fragment's state here
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        
        //Save the fragment's state here
    }

    public void setButtonsLayout(){
        int playersCount = (MainActivity.playersList.size());
        mafiaActionBinding.playerButtonsFrame.buttonsTop.setVisibility(View.VISIBLE);
        mafiaActionBinding.playerButtonsFrame.buttonsBottom.setVisibility(View.VISIBLE);
        switch (playersCount){
            case 6:
                rotateButtons(45);
                break;
            case 7: case 8:
                rotateButtons(30);
                break;
            case 9: case 10:
                rotateButtons(20);
                break;
        }
        switch (playersCount){
            case 12:
                mafiaActionBinding.playerButtonsFrame.middleTopRightButton.setVisibility(View.VISIBLE);
            case 11:
                mafiaActionBinding.playerButtonsFrame.middleTopLeftButton.setVisibility(View.VISIBLE);
                mafiaActionBinding.playerButtonsFrame.buttonsMiddleTop.setVisibility(View.VISIBLE);
            case 10:
                mafiaActionBinding.playerButtonsFrame.middleRightButton.setVisibility(View.VISIBLE);
            case 9:
                mafiaActionBinding.playerButtonsFrame.middleLeftButton.setVisibility(View.VISIBLE);
                mafiaActionBinding.playerButtonsFrame.buttonsMiddle.setVisibility(View.VISIBLE);
            case 8:
                mafiaActionBinding.playerButtonsFrame.middleBottomRightButton.setVisibility(View.VISIBLE);
            case 7:
                mafiaActionBinding.playerButtonsFrame.middleBottomLeftButton.setVisibility(View.VISIBLE);
                mafiaActionBinding.playerButtonsFrame.buttonsMiddleBottom.setVisibility(View.VISIBLE);
                break;
        }

        int visibleChildren = 0;
        int playerNo = 0;
        ViewGroup outsideViews = ((ViewGroup)mafiaActionBinding.playerButtonsFrame.playerButtonsView);
        for (int i = 0; i < outsideViews.getChildCount(); i++) {
            for (int j = 0; j < ((ViewGroup)outsideViews.getChildAt(i)).getChildCount(); j++) {
                Log.v(TAG, (((ViewGroup)outsideViews.getChildAt(i)).getChildAt(j)).toString());
                ViewGroup insideViews = ((ViewGroup)outsideViews.getChildAt(i));
                if (insideViews.getChildAt(j).getVisibility() == View.VISIBLE && insideViews.getChildAt(j) instanceof Button) {
                    visibleChildren++;
                    String playerName = MainActivity.playersList.get(playerNo).getName();
                    Button playerButton = ((Button) insideViews.getChildAt(j));
                    playerButton.setTag(playerName);
                    playerButton.setText(playerName);
                    if(!MainActivity.playersList.get(playerNo).isAlive()){
                        playerButton.setEnabled(false);
                        playerButton.setBackground(ResourcesCompat.getDrawable(Objects.requireNonNull(getContext()).getResources(), R.drawable.dead_player_background, null));
                    } else {
                        setMafiaButtonOnClickListener(playerButton);
                    }
                    playerNo++;
                }
            }
        }
    }

    private void rotateButtons(int rotateValue){
        mafiaActionBinding.playerButtonsFrame.topLeftButton.setRotation(mafiaActionBinding.playerButtonsFrame.topLeftButton.getRotation() - rotateValue);
        mafiaActionBinding.playerButtonsFrame.topRightButton.setRotation(mafiaActionBinding.playerButtonsFrame.topRightButton.getRotation() + rotateValue);
        mafiaActionBinding.playerButtonsFrame.bottomRightButton.setRotation(mafiaActionBinding.playerButtonsFrame.bottomRightButton.getRotation() - rotateValue);
        mafiaActionBinding.playerButtonsFrame.bottomLeftButton.setRotation(mafiaActionBinding.playerButtonsFrame.bottomLeftButton.getRotation() + rotateValue);
    }

    private void setMafiaButtonOnClickListener(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButton(button);

                Player playerToKill = getPlayer(button.getTag().toString());

                MafiaActionFragment.this.playerToKill = MainActivity.playersList.get((MainActivity.playersList.indexOf(playerToKill)));

                button.setEnabled(false);

                //listener.onInputMafiaSent(killedPlayer);

                if(playerToKill.getRole().getClass().equals(Police.class)) {
                    ((MainActivity)getActivity()).setViewPager(MainActivity.END_ROUND_ACTION_FRAGMENT);
                } else {
                    ((MainActivity) getActivity()).setViewPager(MainActivity.POLICE_ACTION_FRAGMENT);
                }
            }
        });
    }

    private void selectButton(Button button){
        ViewGroup outsideViews = ((ViewGroup)mafiaActionBinding.playerButtonsFrame.playerButtonsView);
        for (int i = 0; i < outsideViews.getChildCount(); i++) {
            for (int j = 0; j < ((ViewGroup)outsideViews.getChildAt(i)).getChildCount(); j++) {
                Log.v(TAG, (((ViewGroup)outsideViews.getChildAt(i)).getChildAt(j)).toString());
                ViewGroup insideViews = ((ViewGroup)outsideViews.getChildAt(i));
                if (insideViews.getChildAt(j).getVisibility() == View.VISIBLE
                        && insideViews.getChildAt(j) instanceof Button
                        && insideViews.getChildAt(j).getBackground() == ResourcesCompat.getDrawable(Objects.requireNonNull(getContext()).getResources(), R.drawable.mafia_selected_button_background, null)) {
                    insideViews.getChildAt(j).setBackground(ResourcesCompat.getDrawable(Objects.requireNonNull(getContext()).getResources(), R.drawable.mafia_buttons_background, null));
                }
            }
        }
        button.setBackground(ResourcesCompat.getDrawable(Objects.requireNonNull(getContext()).getResources(), R.drawable.mafia_selected_button_background, null));
    }

    public Player getPlayerToKill(){
        return playerToKill;
    }

    public void lockFragmentButtons(){
        mafiaActionBinding.playerButtons.setVisibility(View.GONE);
    }

    public void unlockFragmentButtons(){
        mafiaActionBinding.playerButtons.setVisibility(View.VISIBLE);
    }
}
