package com.example.mafiagame;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;

import java.util.ArrayList;

import static com.example.mafiagame.GameActivity.getAlivePlayersList;
import static com.example.mafiagame.GameActivity.getPlayer;
import static com.example.mafiagame.GameActivity.isPoliceAlive;
import static com.example.mafiagame.Mafia.killPlayer;

public class MafiaActionFragment extends Fragment {
    public static final String TAG = "mafiaActionFragment";
    ArrayList<Player> playersList;

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
        View view = inflater.inflate(R.layout.mafia_action_layout, container, false);

        playerButtonsView = view.findViewById(R.id.players_buttons_layout);

        findPlayersButtonsViews(view);

        playersList = ((GameActivity)getActivity()).playersList;

 //       citySleepFadeOut(view);

        setButtonsLayout(view);
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

//    private void citySleepFadeOut(View view){
//        AnimatorSet mSetFadeOut = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.mafia_fade_out_animation);
//        TextView citySleepsText = view.findViewById(R.id.citySleepsText);
//        mSetFadeOut.setTarget(citySleepsText);
//        mSetFadeOut.start();
//        mSetFadeOut.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                mafiaWakesUpFadeOut(view);
//            }
//        });
//    }
//
//    private void mafiaWakesUpFadeOut(View view){
//        AnimatorSet mSetFadeOut = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.mafia_fade_out_animation);
//        TextView mafiaWakesUp = view.findViewById(R.id.mafiaWakesUpText);
//        mSetFadeOut.setTarget(mafiaWakesUp);
//        mSetFadeOut.start();
//    }

    public void setButtonsLayout(View view){
        int playersCount = playersList.size();
        RelativeLayout buttonsTop = view.findViewById(R.id.buttons_top);
        RelativeLayout buttonsBottom = view.findViewById(R.id.buttons_bottom);
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
                View buttonsMiddleTop = view.findViewById(R.id.buttons_middle_top);
                buttonsMiddleTop.setVisibility(View.VISIBLE);
            case 10:
                middleRightButton.setVisibility(View.VISIBLE);
            case 9:
                middleLeftButton.setVisibility(View.VISIBLE);
                View buttonsMiddle = view.findViewById(R.id.buttons_middle);
                buttonsMiddle.setVisibility(View.VISIBLE);
            case 8:
                middleBottomRightButton.setVisibility(View.VISIBLE);
            case 7:
                middleBottomLeftButton.setVisibility(View.VISIBLE);
                View buttonsMiddleBottom = view.findViewById(R.id.buttons_middle_bottom);
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
                    String playerName = playersList.get(playerNo).getName();
                    Button playerButton = ((Button) insideViews.getChildAt(j));
                    playerButton.setTag(playerName);
                    playerButton.setText(playerName);
              //      playerButton.setBackground(view.getResources().getDrawable(R.drawable.mafia_buttons_background, null));
                    if(!playersList.get(playerNo).isAlive()){
                        playerButton.setEnabled(false);
                        playerButton.setBackground(getResources().getDrawable(R.drawable.dead_player_background, null));
                    } else {
                        setMafiaButtonOnClickListener(playerButton);
                    }
                    playerNo++;
                }
            }
        }
        Log.v(TAG, "Visible children: " + visibleChildren);
        for(int i = 0; i < playersList.size(); i++){

        }
    }

    private void rotateButtons(int rotateValue){
        topLeftButton.setRotation(topLeftButton.getRotation() - rotateValue);
        topRightButton.setRotation(topRightButton.getRotation() + rotateValue);
        bottomRightButton.setRotation(bottomRightButton.getRotation() - rotateValue);
        bottomLeftButton.setRotation(bottomLeftButton.getRotation() + rotateValue);
    }
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public void setButtonsLayout(View view){
//        GridLayout gridLayout = view.findViewById(R.id.mafiaGridButtonsLayout);
//
//        gridLayout.setRowCount(playersList.size()%2==0? playersList.size()/2 : (playersList.size()/2)+1);
//        gridLayout.setColumnCount(2);
//
//        int columnCount = playersList.size()%2==0? playersList.size()/2 : (playersList.size()/2)+1;
//
//        for(int i = 0, c = 0; i < playersList.size(); i++, c++){
//
//            if(i == columnCount)
//            {
//                c = 0;
//            }
//
//            String playerName = playersList.get(i).getName();
//
//            Button btnTag = new Button(getActivity());
//            btnTag.setText(playerName);
//            btnTag.setTextColor(getResources().getColor(R.color.white, null));
//            btnTag.setShadowLayer(5, 0, 0, Color.parseColor("#A8A8A8"));
//            btnTag.setAutoSizeTextTypeUniformWithConfiguration(12, 20, 2, TypedValue.COMPLEX_UNIT_DIP);
//            btnTag.setAllCaps(false);
//            btnTag.setTypeface(null, Typeface.ITALIC);
//            btnTag.setBackground(getResources().getDrawable(R.drawable.mafia_buttons_background, null));
//            btnTag.setId(i+1);
//            btnTag.setTag(playerName);
//
//            if(!playersList.get(i).isAlive()){
//                btnTag.setEnabled(false);
//            } else {
//                setMafiaButtonOnClickListener(btnTag);
//            }
//
//            GridLayout.LayoutParams param = new GridLayout.LayoutParams();
//            param.rowSpec = GridLayout.spec(i%2==0? 0:1);
//            param.columnSpec = GridLayout.spec(c);
//
////            param.leftMargin = 10;
////            param.rightMargin = 10;
////            param.topMargin = 10;
////            param.bottomMargin = 10;
//            view.setLayoutParams(param);
//            //add button to the layout
//            gridLayout.addView(btnTag);
//        }
//        Log.v(TAG, "Layout created");
//    }

    private void setMafiaButtonOnClickListener(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButton(v, button);

                String buttonTag = button.getTag().toString();
                Player playerToKill = getPlayer(button.getTag().toString());

                killedPlayer = killPlayer(playersList.get(playersList.indexOf(playerToKill)));
                button.setEnabled(false);

                listener.onInputMafiaSent(killedPlayer);

                if(isPoliceAlive()) {
                    ((GameActivity) getActivity()).setViewPager(GameActivity.POLICE_ACTION_FRAGMENT_NO);
                } else {
                    ((GameActivity)getActivity()).setViewPager(GameActivity.END_ROUND_ACTION_FRAGMENT);
                }

//                OPTIONAL
//                if(isPoliceAlive()){
//                    ((GameActivity)getActivity()).setViewPager(GameActivity.POLICE_ACTION_FRAGMENT_NO);
//                }else {
//                    ((GameActivity)getActivity()).setViewPager(GameActivity.END_ROUND_ACTION_FRAGMENT);
//                }
            }
        });
    }

    private void selectButton(View view, Button button){
        ViewGroup outsideViews = ((ViewGroup)playerButtonsView);
        for (int i = 0; i < outsideViews.getChildCount(); i++) {
            for (int j = 0; j < ((ViewGroup)outsideViews.getChildAt(i)).getChildCount(); j++) {
                Log.v(TAG, (((ViewGroup)outsideViews.getChildAt(i)).getChildAt(j)).toString());
                ViewGroup insideViews = ((ViewGroup)outsideViews.getChildAt(i));
                if (insideViews.getChildAt(j).getVisibility() == View.VISIBLE
                        && insideViews.getChildAt(j) instanceof Button
                        && insideViews.getChildAt(j).getBackground() == view.getResources().getDrawable(R.drawable.mafia_selected_button_background, null)) {
                    insideViews.getChildAt(j).setBackground(view.getResources().getDrawable(R.drawable.mafia_buttons_background, null));
                }
            }
        }
        button.setBackground(view.getResources().getDrawable(R.drawable.mafia_selected_button_background, null));
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
