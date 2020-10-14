package com.example.mafiagame.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mafiagame.databinding.FragmentGameOverBinding;

import static com.example.mafiagame.activity.MainActivity.getAlivePlayersCount;
import static com.example.mafiagame.activity.MainActivity.getNumberOfMafiaAlive;

public class GameOverFragment extends Fragment {

    private static final String TAG = "GameOverFragment";

    private FragmentGameOverBinding gameOverBinding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        gameOverBinding = FragmentGameOverBinding.inflate(inflater, container, false);
        Log.v(TAG, "onCreateView");

        // TODO: playAgain go to gameMenuFragment and remove all data
        gameOverBinding.playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                final Intent intent = new Intent(getContext(), StartActivity.class);
//                startActivity(intent);
            }
        });
        showWhoWon();
        return gameOverBinding.getRoot();
    }

    public void showWhoWon(){
        if(getNumberOfMafiaAlive() == 0){
            gameOverBinding.wonRoleText.setText("City");
        } else if(getNumberOfMafiaAlive() == getAlivePlayersCount() || getNumberOfMafiaAlive() == getAlivePlayersCount()-1) {
            gameOverBinding.wonRoleText.setText("Mafia");
        }
    }
}
