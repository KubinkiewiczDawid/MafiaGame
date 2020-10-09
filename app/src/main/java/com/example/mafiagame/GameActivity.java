package com.example.mafiagame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity implements MafiaActionFragment.FragmentMafiaActionListener, PoliceActionFragment.FragmentPoliceActionListener {

    private static final String TAG = "GameActivity";

    public static final int MAFIA_ACTION_FRAGMENT_NO = 0;
    public static final int POLICE_ACTION_FRAGMENT_NO = 1;
    public static final int END_ROUND_ACTION_FRAGMENT = 2;
    public static final int TALK_ACTION_FRAGMENT = 3;
    public static final int VOTE_ACTION_FRAGMENT = 4;
    public static final int VOTE_RESULTS_ACTION_FRAGMENT = 5;
    public static final int GAME_OVER_FRAGMENT = 6;


    public static ArrayList<Player> playersList;
    private SectionStatePagerAdapter mSectionStatePagerAdapter;
    private ViewPager mViewPager;
    private MafiaActionFragment mafiaActionFragment;
    private PoliceActionFragment policeActionFragment;
    private EndRoundFragment endRoundFragment;
    private TalkActionFragment talkActionFragment;
    private VoteActionFragment voteActionFragment;
    private VoteResultsActionFragment voteResultsActionFragment;
    private GameOverFragment gameOverFragment;

    public CountDownTimer endRoundFragmentTimer;
    private CountDownTimer talkActionFragmentTimer;
    private boolean endRoundFragmentTimerRunning;
    private boolean talkActionFragmentTimerRunning;
    private static Role wonRole;
    private TextView whosTurn;
    private TextView mafiaWakesUp;
    boolean allInfoShown;

    private boolean doubleBackToMainMenuPressedOnce = false;

    private static View playerButtonsView;
    private static Button bottomButton, bottomLeftButton, bottomRightButton;
    private static Button middleBottomLeftButton, middleBottomRightButton;
    private static Button middleLeftButton, middleRightButton;
    private static Button middleTopLeftButton, middleTopRightButton;
    private static Button topButton, topLeftButton, topRightButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        setupFragments();
        mafiaWakesUp = findViewById(R.id.mafiaWakesUpText);

        playersList = getPlayersList();
        for(Player player: playersList){
            Log.v("playersGameActitivy", player.getName() + " " + player.getRole());
        }

        mViewPager = findViewById(R.id.container);
        //whosTurn = findViewById(R.id.whosTurn);

        setupViewPager(mViewPager);

        setupTimers();
        setOnPageChangeListeners(mViewPager);
        citySleepFadeOut();
        //whosTurn.setText("Mafia Turn");


//        AnimationDrawable progressAnimation = (AnimationDrawable) whosTurn.getBackground();
//        progressAnimation.start();
    }

    private ArrayList<Player> getPlayersList(){
        Intent intent = getIntent();
        ArrayList<Player> message = intent.getParcelableArrayListExtra(StartActivity.EXTRA_MESSAGE);
        Log.v(TAG, String.valueOf(message));
        return message;
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        if(doubleBackToMainMenuPressedOnce) {
            doubleBackToMainMenuPressedOnce = false;
            final Intent intent = new Intent(GameActivity.this, StartActivity.class);
            startActivity(intent);
        }
        Toast.makeText(GameActivity.this,"Press again to leave",Toast.LENGTH_LONG).show();
        doubleBackToMainMenuPressedOnce = true;
        return;
    }

    private void setupFragments(){
        mafiaActionFragment = new MafiaActionFragment();
        policeActionFragment = new PoliceActionFragment();
        endRoundFragment = new EndRoundFragment();
        talkActionFragment = new TalkActionFragment();
        voteActionFragment = new VoteActionFragment();
        voteResultsActionFragment = new VoteResultsActionFragment();
        gameOverFragment = new GameOverFragment();
    }

    private void setupViewPager(ViewPager viewPager){
        SectionStatePagerAdapter adapter = new SectionStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(mafiaActionFragment, "MafiaAction");
        adapter.addFragment(policeActionFragment, "PoliceAction");
        adapter.addFragment(endRoundFragment, "EndRoundFragment");
        adapter.addFragment(talkActionFragment, "TalkAction");
        adapter.addFragment(voteActionFragment, "VoteAction");
        adapter.addFragment(voteResultsActionFragment, "VoteResultsAction");
        adapter.addFragment(gameOverFragment, "GameOver");
        viewPager.setAdapter(adapter);



        Log.v(TAG, adapter.getTitle(6));
    }

    public void setViewPager(int fragmentNumber){
        mViewPager.setCurrentItem(fragmentNumber);
    }

    private void setupTimers(){
        endRoundFragmentTimer = new CountDownTimer(getResources().getInteger(R.integer.end_round_info_timer), 1000) {

            public void onTick(long millisecondsUntilDone) {
                endRoundFragmentTimerRunning = true;
            }

            public void onFinish(){

                if(allInfoShown || !isPoliceAlive()) {
                    endRoundFragmentTimerRunning = false;
                    setViewPager(TALK_ACTION_FRAGMENT);
                    Log.v("Done", "Countdown timer finished");
                } else {
                    endRoundFragment.throwEndRoundKilledCardAnim();
//                    endRoundFragment.policeHitInfoAnim();

                    allInfoShown = true;
                }
            }
        };

        talkActionFragmentTimer = new CountDownTimer(getResources().getInteger(R.integer.talk_action_timer), 1000) {

            public void onTick(long millisecondsUntilDone) {
                talkActionFragmentTimerRunning = true;
                timeTextChange(talkActionFragment.beginVoteTimeText ,(int)millisecondsUntilDone);
            }

            public void onFinish(){
                talkActionFragmentTimerRunning = false;
                setViewPager(VOTE_ACTION_FRAGMENT);
                Log.v("Done", "Countdown timer finished");
            }
        };
    }

    private void citySleepFadeOut(){
        mafiaWakesUp.setAlpha(1);
        AnimatorSet mSetFadeOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.city_fade_out_animation);
        TextView citySleepsText = findViewById(R.id.citySleepsText);
        mSetFadeOut.setTarget(citySleepsText);
        mSetFadeOut.start();
        mSetFadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mafiaWakesUpFadeOut();
            }
        });
    }

    private void mafiaWakesUpFadeOut(){
        AnimatorSet mSetFadeOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.mafia_fade_out_animation);
        mSetFadeOut.setTarget(mafiaWakesUp);
        mSetFadeOut.start();
    }

    private void setOnPageChangeListeners(ViewPager viewPager){
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                switch(position){
                    case MAFIA_ACTION_FRAGMENT_NO:
                        citySleepFadeOut();
                        //whosTurn.setText("Mafia Turn");
                        break;
                    case POLICE_ACTION_FRAGMENT_NO:
                        checkIfGameIsOver();
                        //whosTurn.setText("Police Turn");
                        break;
                    case END_ROUND_ACTION_FRAGMENT:
                        allInfoShown = false;
                        endRoundFragmentTimer.start();
                        endRoundFragment.killedCardPunchAnim();
                        endRoundFragment.setCheckedPlayerResult(policeActionFragment.getCheckedPlayer());
                        break;
                    case TALK_ACTION_FRAGMENT:
                        if(endRoundFragmentTimerRunning) endRoundFragmentTimer.cancel();
                        talkActionFragmentTimer.start();
                        talkActionFragment.setKilledPlayerNameText(mafiaActionFragment.getKilledPlayer());
                        talkActionFragment.setCheckedPlayerResult(policeActionFragment.getCheckedPlayer());
                        break;
                    case VOTE_ACTION_FRAGMENT:
                        if(talkActionFragmentTimerRunning) talkActionFragmentTimer.cancel();
                        break;
                    case VOTE_RESULTS_ACTION_FRAGMENT:
                        checkIfGameIsOver();
                        break;
                    case GAME_OVER_FRAGMENT:
                        endRoundFragmentTimer.cancel();
                        break;
                }
                Toast.makeText(GameActivity.this,
                        "Selected page position: " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    public static Player getPlayer(String name){
        for (Player player : playersList) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        return null;
    }

    public static boolean isPlayerMafia(Player player){
        if(player.getRole().getClass() == Mafia.class){
            return true;
        }
        return false;
    }

    public static int getNumberOfMafiaAlive(){
        int numberOfMafiaAlive = 0;
        for (Player player : playersList) {
            if (player.getRole().getClass() == Mafia.class && player.isAlive()) {
                numberOfMafiaAlive++;
            }
        }
        return numberOfMafiaAlive;
    }

    public static ArrayList<Player> getAlivePlayersList(){
        ArrayList<Player> alivePlayers = new ArrayList<>();
        for(Player player : playersList){
            if(player.isAlive()) alivePlayers.add(player);
        }
        return alivePlayers;
    }

    public static int getAlivePlayersCount(){
        int alivePlayers = 0;
        for(Player player : playersList){
            if(player.isAlive()) alivePlayers++;
        }
        return alivePlayers;
    }

    public static boolean isPoliceAlive(){
        for (Player player : playersList) {
            if (player.getRole().getClass() == Police.class && player.isAlive()) {
                return true;
            }
        }
        return false;
    }

    public boolean checkIfGameIsOver(){
        if(getNumberOfMafiaAlive() == 0 || getNumberOfMafiaAlive() == getAlivePlayersCount()-1 || getNumberOfMafiaAlive() == getAlivePlayersCount()){
            setViewPager(GameActivity.GAME_OVER_FRAGMENT);
            return true;
        }
        return false;
    }


    public Role getWonRole(){
        return wonRole;
    }
    private static void timeTextChange(TextView timeText, int value){
        int seconds = (int) value / 1000;
        String timeString = String.format("%02d:%02d", (seconds / 60), (seconds % 60));
        Log.v("seekBar progress changed", timeString);
        timeText.setText(timeString);
        Log.v("Seconds left", String.valueOf((value / 1000)));
    }

//    private void findPlayersButtonsViews(View view){
//        bottomButton = view.findViewById(R.id.bottom_button);
//        bottomRightButton = view.findViewById(R.id.bottom_right_button);
//        bottomLeftButton = view.findViewById(R.id.bottom_left_button);
//        middleBottomLeftButton = view.findViewById(R.id.middle_bottom_left_button);
//        middleBottomRightButton = view.findViewById(R.id.middle_bottom_right_button);
//        middleLeftButton = view.findViewById(R.id.middle_left_button);
//        middleRightButton = view.findViewById(R.id.middle_right_button);
//        middleTopLeftButton = view.findViewById(R.id.middle_top_left_button);
//        middleTopRightButton = view.findViewById(R.id.middle_top_right_button);
//        topButton = view.findViewById(R.id.top_button);
//        topLeftButton = view.findViewById(R.id.top_left_button);
//        topRightButton = view.findViewById(R.id.top_right_button);
//    }
//
//    public static void setButtonsLayout(View view){
//        int playersCount = playersList.size();
//        RelativeLayout buttonsTop = view.findViewById(R.id.buttons_top);
//        RelativeLayout buttonsBottom = view.findViewById(R.id.buttons_bottom);
//        buttonsTop.setVisibility(View.VISIBLE);
//        buttonsBottom.setVisibility(View.VISIBLE);
//        switch (playersCount){
//            case 6:
//                rotateButtons(45);
//                break;
//            case 7: case 8:
//                rotateButtons(30);
//                break;
//            case 9: case 10:
//                rotateButtons(20);
//                break;
//        }
//        switch (playersCount){
//            case 12:
//                middleTopRightButton.setVisibility(View.VISIBLE);
//            case 11:
//                middleTopLeftButton.setVisibility(View.VISIBLE);
//                View buttonsMiddleTop = view.findViewById(R.id.buttons_middle_top);
//                buttonsMiddleTop.setVisibility(View.VISIBLE);
//            case 10:
//                middleRightButton.setVisibility(View.VISIBLE);
//            case 9:
//                middleLeftButton.setVisibility(View.VISIBLE);
//                View buttonsMiddle = view.findViewById(R.id.buttons_middle);
//                buttonsMiddle.setVisibility(View.VISIBLE);
//            case 8:
//                middleBottomRightButton.setVisibility(View.VISIBLE);
//            case 7:
//                middleBottomLeftButton.setVisibility(View.VISIBLE);
//                View buttonsMiddleBottom = view.findViewById(R.id.buttons_middle_bottom);
//                buttonsMiddleBottom.setVisibility(View.VISIBLE);
//                break;
//        }
//        Log.v(TAG, Integer.toString(((ViewGroup)playerButtonsView).getChildCount()));
//
//        int visibleChildren = 0;
//        int playerNo = 0;
//        ViewGroup outsideViews = ((ViewGroup)playerButtonsView);
//        ArrayList<Player> alivePlayers = new ArrayList<>(getAlivePlayersList());
//        for (int i = 0; i < outsideViews.getChildCount(); i++) {
//            for (int j = 0; j < ((ViewGroup)outsideViews.getChildAt(i)).getChildCount(); j++) {
//                Log.v(TAG, (((ViewGroup)outsideViews.getChildAt(i)).getChildAt(j)).toString());
//                ViewGroup insideViews = ((ViewGroup)outsideViews.getChildAt(i));
//                if (insideViews.getChildAt(j).getVisibility() == View.VISIBLE && insideViews.getChildAt(j) instanceof Button) {
//                    visibleChildren++;
//                    ((Button) insideViews.getChildAt(j)).setText(alivePlayers.get(playerNo++).getName());
//                }
//            }
//        }
//        Log.v(TAG, "Visible children: " + visibleChildren);
//        for(int i = 0; i < playersList.size(); i++){
//
//        }
//    }
//
//    private static void rotateButtons(int rotateValue){
//        topLeftButton.setRotation(topLeftButton.getRotation() - rotateValue);
//        topRightButton.setRotation(topRightButton.getRotation() + rotateValue);
//        bottomRightButton.setRotation(bottomRightButton.getRotation() - rotateValue);
//        bottomLeftButton.setRotation(bottomLeftButton.getRotation() + rotateValue);
//    }

    @Override
    public void onInputMafiaSent(Player input) {
        endRoundFragment.updateKilledRole(input);
    }

    @Override
    public void onInputPoliceSent(Player input) {
        endRoundFragment.updateCheckedPlayerText(input);
    }
}