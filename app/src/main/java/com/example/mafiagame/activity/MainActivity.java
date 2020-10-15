package com.example.mafiagame.activity;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.mafiagame.CustomCountDownTimer;
import com.example.mafiagame.components.Citizen;
import com.example.mafiagame.components.Player;
import com.example.mafiagame.R;
import com.example.mafiagame.adapters.SectionStatePagerAdapter;
import com.example.mafiagame.components.Mafia;
import com.example.mafiagame.components.Police;
import com.example.mafiagame.databinding.ActivityMainBinding;
import com.example.mafiagame.ui.fragment.EndRoundFragment;
import com.example.mafiagame.ui.fragment.GameMenuFragment;
import com.example.mafiagame.ui.fragment.GameOverFragment;
import com.example.mafiagame.ui.fragment.HowToPlayFragment;
import com.example.mafiagame.ui.fragment.MafiaActionFragment;
import com.example.mafiagame.ui.fragment.PlayersAssignmentFragment;
import com.example.mafiagame.ui.fragment.PoliceActionFragment;
import com.example.mafiagame.ui.fragment.TalkActionFragment;
import com.example.mafiagame.ui.fragment.VoteActionFragment;
import com.example.mafiagame.ui.fragment.VoteResultsActionFragment;
import com.muddzdev.styleabletoast.StyleableToast;

import java.util.ArrayList;

public class MainActivity extends NoSensorExtensionActivity {

    private static final String TAG = "GameActivity";

    //TODO: for testing purposes move all -1 and comment PLAYER_ASSIGNMENT_FRAGMENT
    public static final int GAME_MENU_FRAGMENT = 0;
    public static final int HOW_TO_PLAY_FRAGMENT = 1;
    public static final int PLAYER_ASSIGNMENT_FRAGMENT = 2;
    public static final int MAFIA_ACTION_FRAGMENT = 3;
    public static final int POLICE_ACTION_FRAGMENT = 4;
    public static final int END_ROUND_ACTION_FRAGMENT = 5;
    public static final int TALK_ACTION_FRAGMENT = 6;
    public static final int VOTE_ACTION_FRAGMENT = 7;
    public static final int VOTE_RESULTS_ACTION_FRAGMENT = 8;
    public static final int GAME_OVER_FRAGMENT = 9;


    public static ArrayList<Player> playersList;
    private GameMenuFragment gameMenuFragment;
    private HowToPlayFragment howToPlayFragment;
    private PlayersAssignmentFragment playersAssignmentFragment;
    private MafiaActionFragment mafiaActionFragment;
    private PoliceActionFragment policeActionFragment;
    private EndRoundFragment endRoundFragment;
    private TalkActionFragment talkActionFragment;
    private VoteActionFragment voteActionFragment;
    private VoteResultsActionFragment voteResultsActionFragment;
    private GameOverFragment gameOverFragment;

    public CustomCountDownTimer endRoundFragmentTimer;
    private CustomCountDownTimer talkActionFragmentTimer;
    private boolean endRoundFragmentTimerRunning;
    private boolean talkActionFragmentTimerRunning;
    private boolean allInfoShown;

    private boolean doubleBackToMainMenuPressedOnce;

    private AnimatorSet mSetFadeOut;
    private AnimatorSet mSetFadeOutSecond;
    private MediaPlayer mediaPlayer;

    public boolean testRun = false;

    private ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = activityMainBinding.getRoot();
        setContentView(view);

        //TODO: for testing purposes
        testRun = false;
        if(testRun){
            createTest();
        }

        setupFragments();

        setupViewPager(activityMainBinding.container);

        setupTimers();
        setOnPageChangeListeners(activityMainBinding.container);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
            playersAssignmentFragment.getPlayersAssignmentFragmentEditText().clearFocus();
        }
        return super.dispatchTouchEvent(ev);
    }

    public int getNumberOfPlayers(){
        int message = gameMenuFragment.getNumberOfPlayers();
        Log.v("Message", String.valueOf(message));
        return message;
    }

    public void setPlayersList(ArrayList<Player> playersList){
        this.playersList = playersList;
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        if(doubleBackToMainMenuPressedOnce) {
            resetData();
            doubleBackToMainMenuPressedOnce = false;
            setViewPager(GAME_MENU_FRAGMENT);
        }
        StyleableToast.makeText(MainActivity.this,"Press again to leave",Toast.LENGTH_SHORT, R.style.mytoast).show();
        doubleBackToMainMenuPressedOnce = true;
        return;
    }

    public void resetData() {
        resetTimers();
        stopSound();
        cancelAnimations();
        setupViewPager(activityMainBinding.container);
    }

    public void quit() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(startMain);
    }

    private void setupFragments(){
        gameMenuFragment = new GameMenuFragment();
        if(!testRun) {
            playersAssignmentFragment = new PlayersAssignmentFragment();
        }
        howToPlayFragment = new HowToPlayFragment();
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
        //TODO: for test purposes
        adapter.addFragment(gameMenuFragment, "gameMenu");
        adapter.addFragment(howToPlayFragment, "howToPlay");
        if(!testRun) {
            adapter.addFragment(playersAssignmentFragment, "PlayerAssignment");
        }
        adapter.addFragment(mafiaActionFragment, "MafiaAction");
        adapter.addFragment(policeActionFragment, "PoliceAction");
        adapter.addFragment(endRoundFragment, "EndRoundFragment");
        adapter.addFragment(talkActionFragment, "TalkAction");
        adapter.addFragment(voteActionFragment, "VoteAction");
        adapter.addFragment(voteResultsActionFragment, "VoteResultsAction");
        adapter.addFragment(gameOverFragment, "GameOver");
        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View view, float position) {
                if (position < 0) {
                    view.setScrollX((int)((float)(view.getWidth()) * position));
                } else if (position > 0) {
                    view.setScrollX(-(int) ((float) (view.getWidth()) * -position));
                } else {
                    view.setScrollX(0);
                }
            }
        });
        viewPager.setAdapter(adapter);

        Log.v(TAG, adapter.getTitle(6));
    }

    public void setViewPager(int fragmentNumber){
        activityMainBinding.container.setCurrentItem(fragmentNumber);
    }

    public void transformPage(View view, float position) {
        view.setTranslationX(view.getWidth() * -position);

        if(position <= -1.0F || position >= 1.0F) {
            view.setAlpha(0.0F);
        } else if( position == 0.0F ) {
            view.setAlpha(1.0F);
        } else {
            // position is between -1.0F & 0.0F OR 0.0F & 1.0F
            view.setAlpha(1.0F - Math.abs(position));
        }
    }

    private void setupTimers(){
        endRoundFragmentTimer = new CustomCountDownTimer(getResources().getInteger(R.integer.end_round_info_timer), 1000) {

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

        talkActionFragmentTimer = new CustomCountDownTimer(getResources().getInteger(R.integer.talk_action_timer), 1000) {

            public void onTick(long millisecondsUntilDone) {
                talkActionFragmentTimerRunning = true;
                talkActionFragment.timeTextChange((int)millisecondsUntilDone);
            }

            public void onFinish(){
                talkActionFragmentTimerRunning = false;
                setViewPager(VOTE_ACTION_FRAGMENT);
                Log.v("Done", "Countdown timer finished");
            }
        };
    }

    private void resetTimers(){
        if(endRoundFragmentTimer.isRunning()){
            endRoundFragmentTimer.cancel();
        }
        if(talkActionFragmentTimer.isRunning()){
            talkActionFragmentTimer.cancel();
        }
    }

    public AnimatorSet turnFadeOutAnimations(View firstView, View secondView){
        mSetFadeOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.fade_out_animation);
        firstView.setAlpha(1);
        secondView.setAlpha(1);
        mSetFadeOut.setTarget(firstView);
        mSetFadeOut.start();
        mSetFadeOutSecond = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_out_animation);
        mSetFadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mSetFadeOutSecond.setTarget(secondView);
                mSetFadeOutSecond.start();
            }
        });
        return mSetFadeOutSecond;
    }

    private void cancelAnimations(){
        if(mSetFadeOut != null && mSetFadeOutSecond != null) {
            if (mSetFadeOut.isRunning()) {
                mSetFadeOut.cancel();
            }
            if (mSetFadeOutSecond.isRunning()) {
                mSetFadeOutSecond.cancel();
            }
            for (int i = 0; i < activityMainBinding.mainActivityParent.getChildCount(); i++) {
                if (activityMainBinding.mainActivityParent.getChildAt(i) instanceof FrameLayout) {
                    activityMainBinding.mainActivityParent.getChildAt(i).setAlpha(0);
                }
            }
        }
    }

    private void playSound(int resId){
        mediaPlayer = MediaPlayer.create(this, resId);
        mediaPlayer.start();
    }

    private void stopSound(){
        if(mediaPlayer!=null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
        }
    }

    private void setOnPageChangeListeners(ViewPager viewPager){
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                switch(position){
                    case HOW_TO_PLAY_FRAGMENT:
                        howToPlayFragment.setViewVisibility(View.VISIBLE);
                    case MAFIA_ACTION_FRAGMENT:
                        mafiaActionFragment.lockFragmentButtons();
                        mafiaActionFragment.setButtonsLayout();
                        playSound(R.raw.city_goes_to_sleep);
                        turnFadeOutAnimations(activityMainBinding.cityGoesToSleepFrame, activityMainBinding.mafiaWakesUpFrame).addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                playSound(R.raw.mafia_wakes_up);
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                mafiaActionFragment.unlockFragmentButtons();
                            }
                        });

                        break;
                    case POLICE_ACTION_FRAGMENT:
                        if(!checkIfGameIsOver()) {
                            policeActionFragment.lockFragmentButtons();
                            policeActionFragment.setButtonsLayout();
                            playSound(R.raw.mafia_goes_to_sleep);
                            turnFadeOutAnimations(activityMainBinding.mafiaGoesToSleepFrame, activityMainBinding.policeWakesUpFrame).addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                    playSound(R.raw.police_wakes_up);
                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    policeActionFragment.unlockFragmentButtons();
                                }
                            });
                        }
                        break;
                    case END_ROUND_ACTION_FRAGMENT:
                        endRoundFragment.updateKilledRole(mafiaActionFragment.getPlayerToKill());
                        endRoundFragment.updateCheckedPlayerText(policeActionFragment.getCheckedPlayer());
                        endRoundFragment.setEndFragmentDrawables();
                        if(isPoliceAlive()) {
                            playSound(R.raw.police_goes_to_sleep);
                            turnFadeOutAnimations(activityMainBinding.policeGoesToSleepFrame, activityMainBinding.cityWakesUpFrame).addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                    playSound(R.raw.city_wakes_up);
                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    endRoundFunction();
                                }
                            });
                        } else {
                            playSound(R.raw.mafia_goes_to_sleep);
                            turnFadeOutAnimations(activityMainBinding.mafiaGoesToSleepFrame, activityMainBinding.cityWakesUpFrame).addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                    playSound(R.raw.city_wakes_up);
                                }
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    endRoundFunction();
                                }
                            });
                        }
                        break;
                    case TALK_ACTION_FRAGMENT:
                        if(endRoundFragmentTimerRunning) endRoundFragmentTimer.cancel();
                        talkActionFragmentTimer.start();
                        talkActionFragment.setKilledPlayerNameText(mafiaActionFragment.getPlayerToKill());
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
            setViewPager(MainActivity.GAME_OVER_FRAGMENT);
            return true;
        }
        return false;
    }

    private void createTest() {

        if (playersList != null) {
            playersList.removeAll(playersList);
        }

        playersList = new ArrayList<>();

        playersList.add(new Player("Dawid", new Citizen()));
        playersList.add(new Player("Radek", new Mafia()));
        playersList.add(new Player("Maks", new Citizen()));
        playersList.add(new Player("Maciek", new Citizen()));
        playersList.add(new Player("Martyna", new Police()));
        playersList.add(new Player("Wladyslaw", new Citizen()));
    }
}
