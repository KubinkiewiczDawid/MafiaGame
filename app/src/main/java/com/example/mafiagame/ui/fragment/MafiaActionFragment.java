package com.example.mafiagame.ui.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.mafiagame.activity.GameActivity;
import com.example.mafiagame.components.Player;
import com.example.mafiagame.R;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.mafiagame.activity.GameActivity.getPlayer;
import static com.example.mafiagame.activity.GameActivity.isPoliceAlive;
import static com.example.mafiagame.components.Mafia.killPlayer;

public class MafiaActionFragment extends Fragment {
    public static final String TAG = "mafiaActionFragment";

    private Player killedPlayer;
    private FragmentMafiaActionListener listener;
    private View playerButtonsView;
    private Button bottomButton, bottomLeftButton, bottomRightButton;
    private Button middleBottomLeftButton, middleBottomRightButton;
    private Button middleLeftButton, middleRightButton;
    private Button middleTopLeftButton, middleTopRightButton;
    private Button topButton, topLeftButton, topRightButton;

    public interface FragmentMafiaActionListener {
        void onInputMafiaSent(Player input);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mafia_action, container, false);

        playerButtonsView = view.findViewById(R.id.players_buttons_layout);

        findPlayersButtonsViews(view);

        return view;
    }

    private void findPlayersButtonsViews(View view){
        bottomButton = view.findViewById(R.id.bottom_button);
        bottomRightButton = view.findViewById(R.id.bottom_right_button);
        bottomLeftButton = view.findViewById(R.id.bottom_left_button);
        middleBottomLeftButton = view.findViewById(R.id.middle_bottom_left_button);
        middleBottomRightButton = view.findViewById(R.id.middle_bottom_right_button);
        middleLeftButton = view.findViewById(R.id.middle_left_button);
        middleRightButton = view.findViewById(R.id.middle_right_button);
        middleTopLeftButton = view.findViewById(R.id.middle_top_left_button);
        middleTopRightButton = view.findViewById(R.id.middle_top_right_button);
        topButton = view.findViewById(R.id.top_button);
        topLeftButton = view.findViewById(R.id.top_left_button);
        topRightButton = view.findViewById(R.id.top_right_button);
    }

    public void setButtonsLayout(){
        int playersCount = (GameActivity.playersList.size());
        RelativeLayout buttonsTop = getView().findViewById(R.id.buttons_top);
        RelativeLayout buttonsBottom = getView().findViewById(R.id.buttons_bottom);
        buttonsTop.setVisibility(View.VISIBLE);
        buttonsBottom.setVisibility(View.VISIBLE);
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
                middleTopRightButton.setVisibility(View.VISIBLE);
            case 11:
                middleTopLeftButton.setVisibility(View.VISIBLE);
                View buttonsMiddleTop = getView().findViewById(R.id.buttons_middle_top);
                buttonsMiddleTop.setVisibility(View.VISIBLE);
            case 10:
                middleRightButton.setVisibility(View.VISIBLE);
            case 9:
                middleLeftButton.setVisibility(View.VISIBLE);
                View buttonsMiddle = getView().findViewById(R.id.buttons_middle);
                buttonsMiddle.setVisibility(View.VISIBLE);
            case 8:
                middleBottomRightButton.setVisibility(View.VISIBLE);
            case 7:
                middleBottomLeftButton.setVisibility(View.VISIBLE);
                View buttonsMiddleBottom = getView().findViewById(R.id.buttons_middle_bottom);
                buttonsMiddleBottom.setVisibility(View.VISIBLE);
                break;
        }
        Log.v(TAG, Integer.toString(((ViewGroup)playerButtonsView).getChildCount()));

        int visibleChildren = 0;
        int playerNo = 0;
        ViewGroup outsideViews = ((ViewGroup)playerButtonsView);
        for (int i = 0; i < outsideViews.getChildCount(); i++) {
            for (int j = 0; j < ((ViewGroup)outsideViews.getChildAt(i)).getChildCount(); j++) {
                Log.v(TAG, (((ViewGroup)outsideViews.getChildAt(i)).getChildAt(j)).toString());
                ViewGroup insideViews = ((ViewGroup)outsideViews.getChildAt(i));
                if (insideViews.getChildAt(j).getVisibility() == View.VISIBLE && insideViews.getChildAt(j) instanceof Button) {
                    visibleChildren++;
                    String playerName = GameActivity.playersList.get(playerNo).getName();
                    Button playerButton = ((Button) insideViews.getChildAt(j));
                    playerButton.setTag(playerName);
                    playerButton.setText(playerName);
                    if(!GameActivity.playersList.get(playerNo).isAlive()){
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
        topLeftButton.setRotation(topLeftButton.getRotation() - rotateValue);
        topRightButton.setRotation(topRightButton.getRotation() + rotateValue);
        bottomRightButton.setRotation(bottomRightButton.getRotation() - rotateValue);
        bottomLeftButton.setRotation(bottomLeftButton.getRotation() + rotateValue);
    }

    private void setMafiaButtonOnClickListener(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButton(button);

                Player playerToKill = getPlayer(button.getTag().toString());

                killedPlayer = killPlayer(GameActivity.playersList.get((GameActivity.playersList.indexOf(playerToKill))));
                button.setEnabled(false);

                listener.onInputMafiaSent(killedPlayer);

                if(isPoliceAlive()) {
                    ((GameActivity) getActivity()).setViewPager(GameActivity.POLICE_ACTION_FRAGMENT);
                } else {
                    ((GameActivity)getActivity()).setViewPager(GameActivity.END_ROUND_ACTION_FRAGMENT);
                }

//                TODO: OPTIONAL
//                if(isPoliceAlive()){
//                    ((GameActivity)getActivity()).setViewPager(GameActivity.POLICE_ACTION_FRAGMENT_NO);
//                }else {
//                    ((GameActivity)getActivity()).setViewPager(GameActivity.END_ROUND_ACTION_FRAGMENT);
//                }
            }
        });
    }

    private void selectButton(Button button){
        ViewGroup outsideViews = ((ViewGroup)playerButtonsView);
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

    public Player getKilledPlayer(){
        return killedPlayer;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof FragmentMafiaActionListener){
            listener = (FragmentMafiaActionListener)context;
        }else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentMafiaActionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
