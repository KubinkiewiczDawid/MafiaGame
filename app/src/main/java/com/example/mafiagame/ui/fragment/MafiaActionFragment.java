package com.example.mafiagame.ui.fragment;

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

import com.example.mafiagame.activity.MainActivity;
import com.example.mafiagame.components.Player;
import com.example.mafiagame.R;
import com.example.mafiagame.databinding.FragmentMafiaActionBinding;
import com.example.mafiagame.databinding.FramePlayersButtonsBinding;

import java.util.Objects;

import static com.example.mafiagame.activity.MainActivity.getPlayer;
import static com.example.mafiagame.activity.MainActivity.isPoliceAlive;

public class MafiaActionFragment extends Fragment {
    public static final String TAG = "mafiaActionFragment";

    private Player playerToKill;
    private FragmentMafiaActionListener listener;
    private View playerButtonsView;

    public interface FragmentMafiaActionListener {
        void onInputMafiaSent(Player input);
    }

    private FragmentMafiaActionBinding mafiaActionBinding;
    private FramePlayersButtonsBinding playersButtonsBinding;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mafiaActionBinding = FragmentMafiaActionBinding.inflate(inflater, container, false);

        return mafiaActionBinding.getRoot();
    }

    public void setButtonsLayout(){
        int playersCount = (MainActivity.playersList.size());
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
                mafiaActionBinding.playerButtons.middleTopRightButton.setVisibility(View.VISIBLE);
            case 11:
                mafiaActionBinding.playerButtons.middleTopLeftButton.setVisibility(View.VISIBLE);
                View buttonsMiddleTop = getView().findViewById(R.id.buttons_middle_top);
                buttonsMiddleTop.setVisibility(View.VISIBLE);
            case 10:
                mafiaActionBinding.playerButtons.middleRightButton.setVisibility(View.VISIBLE);
            case 9:
                mafiaActionBinding.playerButtons.middleLeftButton.setVisibility(View.VISIBLE);
                View buttonsMiddle = getView().findViewById(R.id.buttons_middle);
                buttonsMiddle.setVisibility(View.VISIBLE);
            case 8:
                mafiaActionBinding.playerButtons.middleBottomRightButton.setVisibility(View.VISIBLE);
            case 7:
                mafiaActionBinding.playerButtons.middleBottomLeftButton.setVisibility(View.VISIBLE);
                View buttonsMiddleBottom = getView().findViewById(R.id.buttons_middle_bottom);
                buttonsMiddleBottom.setVisibility(View.VISIBLE);
                break;
        }
        //Log.v(TAG, Integer.toString(((ViewGroup)mafiaActionBinding.playerButtonsView).getChildCount()));

        int visibleChildren = 0;
        int playerNo = 0;
        ViewGroup outsideViews = ((ViewGroup)mafiaActionBinding.playerButtons.playerButtonsView);
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
        mafiaActionBinding.playerButtons.topLeftButton.setRotation(mafiaActionBinding.playerButtons.topLeftButton.getRotation() - rotateValue);
        mafiaActionBinding.playerButtons.topRightButton.setRotation(mafiaActionBinding.playerButtons.topRightButton.getRotation() + rotateValue);
        mafiaActionBinding.playerButtons.bottomRightButton.setRotation(mafiaActionBinding.playerButtons.bottomRightButton.getRotation() - rotateValue);
        mafiaActionBinding.playerButtons.bottomLeftButton.setRotation(mafiaActionBinding.playerButtons.bottomLeftButton.getRotation() + rotateValue);
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

                if(isPoliceAlive()) {
                    ((MainActivity) getActivity()).setViewPager(MainActivity.POLICE_ACTION_FRAGMENT);
                } else {
                    ((MainActivity)getActivity()).setViewPager(MainActivity.END_ROUND_ACTION_FRAGMENT);
                }
            }
        });
    }

    private void selectButton(Button button){
        ViewGroup outsideViews = ((ViewGroup)mafiaActionBinding.playerButtons.playerButtonsView);
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

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        if(context instanceof FragmentMafiaActionListener){
//            listener = (FragmentMafiaActionListener)context;
//        }else {
//            throw new RuntimeException(context.toString()
//                    + " must implement FragmentMafiaActionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

}
