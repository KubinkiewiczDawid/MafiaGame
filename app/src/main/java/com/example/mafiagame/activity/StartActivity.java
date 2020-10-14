package com.example.mafiagame.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mafiagame.R;
import com.example.mafiagame.databinding.ActivityStartBinding;

public class StartActivity extends NoSensorExtensionActivity implements NavController.OnDestinationChangedListener {

    public static final String TAG = "StartActivity";
    public static final String EXTRA_MESSAGE = "com.example.mafiagame.MESSAGE";
    private static final int MIN_PLAYERS_NO = 6;
    private static final int MAX_PLAYERS_NO = 12;
    private int numberOfPlayers;

    private ActivityStartBinding startBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startBinding = ActivityStartBinding.inflate(getLayoutInflater());
        setContentView(startBinding.getRoot());

        int numberOfQuestionMarks = ((ViewGroup)startBinding.questionMarksFrame.questionMarksView).getChildCount();
        Log.v(TAG, "Number of questionmarks: " + numberOfQuestionMarks);

        numberOfPlayers = MIN_PLAYERS_NO;

        startBinding.gameMenuFrame.uiPlayersNumberButton.numberOfPlayersText.setText(String.valueOf(numberOfPlayers));

        startBinding.gameMenuFrame.uiPlayersNumberButton.increasePlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numberOfPlayers < MAX_PLAYERS_NO) {
                    numberOfPlayers++;
                    ((ViewGroup) startBinding.questionMarksFrame.questionMarksView).getChildAt(numberOfPlayers-1).animate()
                            .alpha(0.6f)
                            .setDuration(300).start();
                    startBinding.gameMenuFrame.gameTitle.animate()
                            .alpha(startBinding.gameMenuFrame.gameTitle.getAlpha() + 0.02f)
                            .setDuration(300).start();
                    startBinding.gameMenuFrame.uiPlayersNumberButton.numberOfPlayersText.setText(String.valueOf(numberOfPlayers));

                }
            }
        });

        startBinding.gameMenuFrame.uiPlayersNumberButton.decreasePlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numberOfPlayers > MIN_PLAYERS_NO){
                    ((ViewGroup) startBinding.questionMarksFrame.questionMarksView).getChildAt(numberOfPlayers-1).animate()
                            .alpha(0.0f)
                            .setDuration(300)
                            .start();
                    startBinding.gameMenuFrame.gameTitle.animate()
                            .alpha(startBinding.gameMenuFrame.gameTitle.getAlpha() - 0.02f)
                            .setDuration(300).start();
                    numberOfPlayers--;
                    startBinding.gameMenuFrame.uiPlayersNumberButton.numberOfPlayersText.setText(String.valueOf(numberOfPlayers));
                }
            }
        });

        startBinding.gameMenuFrame.uiPlayButton.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuFadeOut();
                Animation anim = new ScaleAnimation(
                        1f, 1f, // Start and end values for the X axis scaling
                        1f, 2f, // Start and end values for the Y axis scaling
                        Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                        Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
                anim.setFillAfter(true); // Needed to keep the result of the animation
                anim.setDuration(1000);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        final Intent intent = new Intent(v.getContext(), MainActivity.class);
                        intent.putExtra(EXTRA_MESSAGE, numberOfPlayers);
                        startActivity(intent);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                startBinding.gameMenuFrame.gangsterImage.startAnimation(anim);
            }
        });

        startBinding.gameMenuFrame.uiHowToPlayButton.howToPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setupNavigation() {
       // NavHostFragment navHostFragment
    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {

    }

    @Override
    public void onBackPressed() {
        quit();
    }

    private void menuFadeOut(){
        startBinding.gameMenu.animate()
                .alpha(0.0f)
                .setDuration(1000)
                .start();
        startBinding.questionMarks.animate()
                .alpha(0.0f)
                .setDuration(1000)
                .start();
    }

    public void quit() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(startMain);
    }


}
