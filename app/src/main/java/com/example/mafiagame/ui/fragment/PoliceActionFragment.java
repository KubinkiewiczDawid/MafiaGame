package com.example.mafiagame.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.mafiagame.activity.MainActivity;
import com.example.mafiagame.components.Player;
import com.example.mafiagame.R;
import com.example.mafiagame.databinding.FragmentMafiaActionBinding;
import com.example.mafiagame.databinding.FragmentPoliceActionBinding;

import java.util.Objects;

import static com.example.mafiagame.activity.MainActivity.getPlayer;
import static com.example.mafiagame.components.Police.checkPlayer;

public class PoliceActionFragment extends Fragment {
    public static final String TAG = "PoliceActionFragment";
    private Player checkedPlayer;

    private FragmentPoliceActionBinding policeActionBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        policeActionBinding = FragmentPoliceActionBinding.inflate(inflater, container, false);
        View view = policeActionBinding.getRoot();

        return view;
    }

    public void setButtonsLayout(){
        int playersCount = MainActivity.playersList.size();
        policeActionBinding.playerButtonsFrame.buttonsTop.setVisibility(View.VISIBLE);
        policeActionBinding.playerButtonsFrame.buttonsBottom.setVisibility(View.VISIBLE);
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
                policeActionBinding.playerButtonsFrame.middleTopRightButton.setVisibility(View.VISIBLE);
            case 11:
                policeActionBinding.playerButtonsFrame.middleTopLeftButton.setVisibility(View.VISIBLE);
                policeActionBinding.playerButtonsFrame.buttonsMiddleTop.setVisibility(View.VISIBLE);
            case 10:
                policeActionBinding.playerButtonsFrame.middleRightButton.setVisibility(View.VISIBLE);
            case 9:
                policeActionBinding.playerButtonsFrame.middleLeftButton.setVisibility(View.VISIBLE);
                policeActionBinding.playerButtonsFrame.buttonsMiddle.setVisibility(View.VISIBLE);
            case 8:
                policeActionBinding.playerButtonsFrame.middleBottomRightButton.setVisibility(View.VISIBLE);
            case 7:
                policeActionBinding.playerButtonsFrame.middleBottomLeftButton.setVisibility(View.VISIBLE);
                policeActionBinding.playerButtonsFrame.buttonsMiddleBottom.setVisibility(View.VISIBLE);
                break;
        }

        int playerNo = 0;
        ViewGroup outsideViews = ((ViewGroup)policeActionBinding.playerButtonsFrame.playerButtonsView);
        for (int i = 0; i < outsideViews.getChildCount(); i++) {
            for (int j = 0; j < ((ViewGroup)outsideViews.getChildAt(i)).getChildCount(); j++) {
                ViewGroup insideViews = ((ViewGroup)outsideViews.getChildAt(i));
                if (insideViews.getChildAt(j).getVisibility() == View.VISIBLE && insideViews.getChildAt(j) instanceof Button) {
                    String playerName = MainActivity.playersList.get(playerNo).getName();
                    Button playerButton = ((Button) insideViews.getChildAt(j));
                    playerButton.setTag(playerName);
                    Log.v(TAG, playerName);
                    playerButton.setText(playerName);
                    if(!MainActivity.playersList.get(playerNo).isAlive()){
                        playerButton.setEnabled(false);
                        playerButton.setBackground((ResourcesCompat.getDrawable(Objects.requireNonNull(getContext()).getResources(), R.drawable.dead_player_background, null)));
                    } else {
                        setPoliceButtonOnClickListener(playerButton);
                    }
                    playerNo++;
                }
            }
        }
    }

    private void rotateButtons(int rotateValue){
        policeActionBinding.playerButtonsFrame.topLeftButton.setRotation(policeActionBinding.playerButtonsFrame.topLeftButton.getRotation() - rotateValue);
        policeActionBinding.playerButtonsFrame.topRightButton.setRotation(policeActionBinding.playerButtonsFrame.topRightButton.getRotation() + rotateValue);
        policeActionBinding.playerButtonsFrame.bottomRightButton.setRotation(policeActionBinding.playerButtonsFrame.bottomRightButton.getRotation() - rotateValue);
        policeActionBinding.playerButtonsFrame.bottomLeftButton.setRotation(policeActionBinding.playerButtonsFrame.bottomLeftButton.getRotation() + rotateValue);
    }

    private void setPoliceButtonOnClickListener(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButton(v, button);
                Player temp = getPlayer(button.getTag().toString());
                checkedPlayer = checkPlayer(Objects.requireNonNull(getPlayer(button.getTag().toString())));
                button.setEnabled(false);
                //listener.onInputPoliceSent(checkedPlayer);
                ((MainActivity) Objects.requireNonNull(getActivity())).setViewPager(MainActivity.END_ROUND_ACTION_FRAGMENT);
            }
        });
    }

    private void selectButton(View view, Button button){
        ViewGroup outsideViews = ((ViewGroup)policeActionBinding.playerButtonsFrame.playerButtonsView);
        for (int i = 0; i < outsideViews.getChildCount(); i++) {
            for (int j = 0; j < ((ViewGroup)outsideViews.getChildAt(i)).getChildCount(); j++) {
                ViewGroup insideViews = ((ViewGroup)outsideViews.getChildAt(i));
                if (insideViews.getChildAt(j).getVisibility() == View.VISIBLE
                        && insideViews.getChildAt(j) instanceof Button
                        && insideViews.getChildAt(j).getBackground() == (ResourcesCompat.getDrawable(Objects.requireNonNull(getContext()).getResources(), R.drawable.police_selected_button_background, null))) {
                    insideViews.getChildAt(j).setBackground((ResourcesCompat.getDrawable(Objects.requireNonNull(getContext()).getResources(), R.drawable.police_buttons_background, null)));
                }
            }
        }
        button.setBackground(ResourcesCompat.getDrawable(Objects.requireNonNull(getContext()).getResources(), R.drawable.police_selected_button_background, null));
        button.setTextColor(getResources().getColor(R.color.darkBlue, null));
    }

    public Player getCheckedPlayer(){
        return checkedPlayer;
    }

    public void lockFragmentButtons(){
        policeActionBinding.playerButtons.setVisibility(View.GONE);
    }

    public void unlockFragmentButtons(){
        policeActionBinding.playerButtons.setVisibility(View.VISIBLE);
    }

}
