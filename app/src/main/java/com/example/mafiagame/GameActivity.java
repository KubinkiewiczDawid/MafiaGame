package com.example.mafiagame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class GameActivity extends AppCompatActivity implements MafiaActionFragment.FragmentMafiaActionListener, PoliceActionFragment.FragmentPoliceActionListener {

    private static final String TAG = "GameActivity";

    public static final int MAFIA_ACTION_FRAGMENT = 0;
    public static final int POLICE_ACTION_FRAGMENT = 1;
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
    public View mafiaWakesUpView;
    public View mafiaSleepsView;
    public View cityWakesUpView;
    public View citySleepsView;
    public View policeWakesUpView;
    public View policeSleepsView;
    private boolean allInfoShown;

    private MediaPlayer mediaPlayer;

    private boolean doubleBackToMainMenuPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        findViews();
        setupFragments();

        playersList = getPlayersList();
        for(Player player: playersList){
            Log.v("playersGameActitivy", player.getName() + " " + player.getRole());
        }

        setupViewPager(mViewPager);

        setupTimers();
        setOnPageChangeListeners(mViewPager);
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

    private void findViews(){
        mViewPager = findViewById(R.id.container);

        mafiaSleepsView = findViewById(R.id.mafia_goes_to_sleep);
        mafiaWakesUpView = findViewById(R.id.mafia_wakes_up);
        citySleepsView = findViewById(R.id.city_goes_to_sleep);
        cityWakesUpView = findViewById(R.id.city_wakes_up);
        policeSleepsView = findViewById(R.id.police_goes_to_sleep);
        policeWakesUpView = findViewById(R.id.police_wakes_up);
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

    public AnimatorSet turnFadeOutAnimations(View firstView, View secondView){
        AnimatorSet mSetFadeOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.fade_out_animation);
        firstView.setAlpha(1);
        secondView.setAlpha(1);
        mSetFadeOut.setTarget(firstView);
        mSetFadeOut.start();
        AnimatorSet mSetFadeOutSecond = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_out_animation);
        mSetFadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mSetFadeOutSecond.setTarget(secondView);
                mSetFadeOutSecond.start();
            }
        });
        return mSetFadeOutSecond;
    }

    public void playSound(int resid){
        mediaPlayer = MediaPlayer.create(this, resid);
        mediaPlayer.start();
    }

    private void setOnPageChangeListeners(ViewPager viewPager){
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                switch(position){
                    case MAFIA_ACTION_FRAGMENT:
                        playSound(R.raw.city_goes_to_sleep);
                        turnFadeOutAnimations(citySleepsView, mafiaWakesUpView).addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                playSound(R.raw.mafia_wakes_up);
                            }
                        });

                        break;
                    case POLICE_ACTION_FRAGMENT:
                        if(!checkIfGameIsOver()) {
                            playSound(R.raw.mafia_goes_to_sleep);
                            turnFadeOutAnimations(mafiaSleepsView, policeWakesUpView).addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                    playSound(R.raw.police_wakes_up);
                                }
                            });
                        }
                        break;
                    case END_ROUND_ACTION_FRAGMENT:
                        playSound(R.raw.police_goes_to_sleep);
                        turnFadeOutAnimations(policeSleepsView, cityWakesUpView).addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                playSound(R.raw.city_wakes_up);
                            }
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                endRoundFunction();
                            }
                        });
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
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    public void endRoundFunction(){
        allInfoShown = false;
        endRoundFragmentTimer.start();

        endRoundFragment.killedCardPunchAnim();
        //endRoundFragment.setCheckedPlayerResult(policeActionFragment.getCheckedPlayer());
    }


    public static Player getPlayer(String name){
        for (Player player : playersList) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        return null;
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

    private static void timeTextChange(TextView timeText, int value){
        int seconds = (int) value / 1000;
        String timeString = String.format("%02d:%02d", (seconds / 60), (seconds % 60));
        Log.v("seekBar progress changed", timeString);
        timeText.setText(timeString);
        Log.v("Seconds left", String.valueOf((value / 1000)));
    }

    @Override
    public void onInputMafiaSent(Player input) {
        endRoundFragment.updateKilledRole(input);
    }

    @Override
    public void onInputPoliceSent(Player input) {
        endRoundFragment.updateCheckedPlayerText(input);
    }
}
