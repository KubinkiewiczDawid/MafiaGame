package com.example.mafiagame;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity {

    public static final String TAG = "StartActivity";
    public static final String EXTRA_MESSAGE = "com.example.mafiagame.MESSAGE";
    private static final int MIN_PLAYERS_NO = 6;
    private static final int MAX_PLAYERS_NO = 12;
    private int numberOfPlayers;
    private View gameMenuLayout;
    private View questionMarksView;
    private TextView gameTitle;
    private TextView numberOfPlayersText;
    private TextView increasePlayerButton;
    private TextView  decreasePlayerButton;
    private ImageView startButton;
    private FrameLayout questionMarksFrame, gameMenuFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        findViews();

        int numberOfQuestionMarks = ((ViewGroup)questionMarksView).getChildCount();
        Log.v(TAG, "Number of questionmarks: " + numberOfQuestionMarks);

        numberOfPlayers = MIN_PLAYERS_NO;

        numberOfPlayersText.setText(String.valueOf(numberOfPlayers));

        increasePlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numberOfPlayers < MAX_PLAYERS_NO) {
                    numberOfPlayers++;
                    ((ViewGroup) questionMarksView).getChildAt(numberOfPlayers-1).animate()
                            .alpha(0.6f)
                            .setDuration(300).start();
                    gameTitle.animate()
                            .alpha(gameTitle.getAlpha() + 0.02f)
                            .setDuration(300).start();
                    numberOfPlayersText.setText(String.valueOf(numberOfPlayers));

                }
            }
        });

        decreasePlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numberOfPlayers > MIN_PLAYERS_NO){
                    ((ViewGroup) questionMarksView).getChildAt(numberOfPlayers-1).animate()
                            .alpha(0.0f)
                            .setDuration(300)
                            .start();
                    gameTitle.animate()
                            .alpha(gameTitle.getAlpha() - 0.02f)
                            .setDuration(300).start();
                    numberOfPlayers--;
                    numberOfPlayersText.setText(String.valueOf(numberOfPlayers));
                }
            }
        });


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView gangsterImage = gameMenuLayout.findViewById(R.id.gangster_image);
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
                gangsterImage.startAnimation(anim);
            }
        });
    }

    private void findViews() {
        gameMenuLayout = findViewById(R.id.game_menu_layout);
        questionMarksView = findViewById(R.id.question_marks_view);
        gameTitle = gameMenuLayout.findViewById(R.id.game_title);
        numberOfPlayersText = gameMenuLayout.findViewById(R.id.number_of_players_text);
        increasePlayerButton = gameMenuLayout.findViewById(R.id.increase_player_button);
        decreasePlayerButton = findViewById(R.id.decrease_player_button);
        startButton = findViewById(R.id.play_button);
        questionMarksFrame = findViewById(R.id.question_marks_frame);
        gameMenuFrame = findViewById(R.id.game_menu_frame);
    }

    private void menuFadeOut(){
        gameMenuFrame.animate()
                .alpha(0.0f)
                .setDuration(1000)
                .start();
        questionMarksFrame.animate()
                .alpha(0.0f)
                .setDuration(1000)
                .start();
    }
}
