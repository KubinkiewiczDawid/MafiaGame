package com.example.mafiagame;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.example.mafiagame.GameActivity.getAlivePlayersCount;
import static com.example.mafiagame.GameActivity.getNumberOfMafiaAlive;

public class GameOverFragment extends Fragment {

    private static final String TAG = "GameOverFragment";

    TextView wonRoleText;
    Button playAgainButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.game_over_layout, container, false);
        Log.v(TAG, "onCreateView");
        wonRoleText = view.findViewById(R.id.wonRoleText);
        playAgainButton = view.findViewById(R.id.playAgainButton);
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(getContext(), StartActivity.class);
                startActivity(intent);
            }
        });
        showWhoWon();
        return view;
    }

    public void showWhoWon(){
        if(getNumberOfMafiaAlive() == 0){
            wonRoleText.setText("City");
        } else if(getNumberOfMafiaAlive() == getAlivePlayersCount() || getNumberOfMafiaAlive() == getAlivePlayersCount()-1) {
            wonRoleText.setText("Mafia");
        }
    }
}
