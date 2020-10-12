package com.example.mafiagame.activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import listeners.OnSwipeTouchListener;
import com.example.mafiagame.components.Player;
import com.example.mafiagame.R;
import com.example.mafiagame.components.Citizen;
import com.example.mafiagame.components.Mafia;
import com.example.mafiagame.components.Police;
import com.example.mafiagame.components.Role;
import com.muddzdev.styleabletoast.StyleableToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends NoSensorExtensionActivity {

    private final static String TAG = "MainActivity";

    private ViewSwitcher switcher;
    private int numberOfPlayers;
    private int numberOfInitializedPlayers;
    private AnimatorSet mSetRightOut;
    private AnimatorSet mSetLeftIn;
    private View mCardFrontLayout;
    private View mCardBackLayout;
    private boolean cardTurned;
    public static ArrayList<Player> playersList;
    private EditText playerNameEditText;
    private TextView roleText;
    private TextView questionMarkFront;
    private ImageView gangsterFrontImage, policeFrontImage, citizenFrontImage;
    private ImageView gangsterPickImage, policePickImage, citizenPickImage;
    private AnimatorSet mSetOut, mSetIn;
    private int animationTarget;

    private boolean doubleBackToMainMenuPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playersList = new ArrayList<>();

        animationTarget = 0;

        findViews();
        createTest();

        loadFrontAnimations();
        loadFrontCardAnimations();
        swipeListeners();
        beginGame();
        changeCameraDistance();
        animateFrontCard();
    }

    @Override
    public void onBackPressed() {
        if(doubleBackToMainMenuPressedOnce) {
            doubleBackToMainMenuPressedOnce = false;
            final Intent intent = new Intent(MainActivity.this, StartActivity.class);
            startActivity(intent);
        }
        Toast.makeText(MainActivity.this,"Press again to leave",Toast.LENGTH_LONG).show();
        doubleBackToMainMenuPressedOnce = true;
        return;
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
            playerNameEditText.clearFocus();
        }
        return super.dispatchTouchEvent(ev);
    }

    private int getNumberOfPlayers(){
        Intent intent = getIntent();
        int message = intent.getIntExtra(StartActivity.EXTRA_MESSAGE, 0);
        Log.v("Message", String.valueOf(message));
        return message;
    }

    private void beginGame(){
        Button beginButton = findViewById(R.id.beginButton);
        beginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switcher != null) switcher.showNext();
            }
        });
        gameInfoInitialize();
    }

    private void loadFrontAnimations() {
        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.out_animation);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.in_animation);
    }

    private void loadBackAnimations() {
        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.out_animation_back);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.in_animation_back);
    }

    private void loadFrontCardAnimations() {
        mSetOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.front_card_object_fade_out);
        mSetIn = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.front_card_object_fade_in);
    }

    private void animateFrontCard() {
        Object animationTargetObject = questionMarkFront;
        mSetOut.setTarget(questionMarkFront);
        mSetIn.setTarget(gangsterFrontImage);
        mSetOut.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                if(animationTarget == 0){
                    mSetIn.setTarget(gangsterFrontImage);
                    mSetIn.start();
                    animationTarget = 1;
                } else if(animationTarget == 1){
                    mSetIn.setTarget(policeFrontImage);
                    mSetIn.start();
                    animationTarget = 2;
                } else if(animationTarget == 2){
                    mSetIn.setTarget(citizenFrontImage);
                    mSetIn.start();
                    animationTarget = 3;
                } else if(animationTarget == 3){
                    mSetIn.setTarget(questionMarkFront);
                    mSetIn.start();
                    animationTarget = 0;
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        mSetOut.start();

        mSetIn.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(animationTarget == 0){
                    mSetOut.setTarget(questionMarkFront);
                    mSetOut.start();
                } else if(animationTarget == 1){
                    mSetOut.setTarget(gangsterFrontImage);
                    mSetOut.start();
                } else if(animationTarget == 2){
                    mSetOut.setTarget(policeFrontImage);
                    mSetOut.start();
                } else if(animationTarget == 3){
                    mSetOut.setTarget(citizenFrontImage);
                    mSetOut.start();
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

//        gangsterFrontImage.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
 //       policeFrontImage.animate()
    }

    private void findViews() {
        switcher = findViewById(R.id.profileSwitcher);
        mCardBackLayout = findViewById(R.id.card_back);
        mCardFrontLayout = findViewById(R.id.card_front);
        playerNameEditText = findViewById(R.id.plyerNameEditText);
        roleText = findViewById(R.id.roleText);
        gangsterPickImage = findViewById(R.id.gangster_pick_image);
        policePickImage = findViewById(R.id.police_pick_image);
        citizenPickImage = findViewById(R.id.citizen_pick_image);
        gangsterFrontImage = findViewById(R.id.gangster_front_image);
        policeFrontImage = findViewById(R.id.police_front_image);
        citizenFrontImage = findViewById(R.id.citizen_front_image);
        questionMarkFront = findViewById(R.id.question_mark_front);
    }

    private void changeCameraDistance() {
        int distance = 8000;
        float scale = getResources().getDisplayMetrics().density * distance;
        mCardFrontLayout.setCameraDistance(scale);
        mCardBackLayout.setCameraDistance(scale);
    }

    private void gameInfoInitialize(){
        numberOfPlayers = getNumberOfPlayers();
        cardTurned = false;
        numberOfInitializedPlayers = 0;
        for(int i = 0; i < numberOfPlayers; i++){
            addEmptyPlayer();
        }
        setPlayersRoles(playersList);
    }

    private List<Integer> generateRandomNumbers(int numbersInterval, int numbersAmount){
        ArrayList<Integer> numbers = new ArrayList<>();
        Random randomGenerator = new Random();
        while (numbers.size() < numbersAmount) {
            int random = randomGenerator .nextInt(numbersInterval);
            if (!numbers.contains(random)) {
                numbers.add(random);
            }
        }
        for(int number : numbers){
            Log.v("randomNumbers", String.valueOf(number));
        }
        return numbers;
    }

    private void setPlayersRoles(List<Player> players){
        int numberOfMafia = 0;
        int numberOfPolice = 0;
        switch(numberOfPlayers){
            case 6: case 7:
                numberOfMafia = 1;
                numberOfPolice = 1;
                break;
            case 8: case 9: case 10:
                numberOfMafia = 2;
                numberOfPolice = 1;
                break;
            case 11: case 12: case 13:
                numberOfMafia = 2;
                numberOfPolice = 2;
                break;
//            case 14: case 15: case 16:
//                numberOfMafia = 3;
//                numberOfPolice = 2;
//                break;
        }

        ArrayList<Integer> rolePositions = new ArrayList<>(generateRandomNumbers(numberOfPlayers, (numberOfMafia + numberOfPolice)));

        while(numberOfMafia > 0){
            players.get(rolePositions.get(rolePositions.size()-1)).setRole(new Mafia());
            rolePositions.remove(rolePositions.size()-1);
            numberOfMafia--;
        }
        while(numberOfPolice > 0){
            players.get(rolePositions.get(rolePositions.size()-1)).setRole(new Police());
            rolePositions.remove(rolePositions.size()-1);
            numberOfPolice--;
        }
        for(Player player : players){
            Log.v("players", player.getName() + " " + player.getRole());
        }
    }

    private boolean allPlayersSet(){
        if(numberOfPlayers == numberOfInitializedPlayers) return true;
        return false;
    }

    private void cardAnimationToFinish(){
        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.out_right_animation);
        mSetRightOut.setTarget(mCardFrontLayout);
        mSetRightOut.start();
        mSetRightOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if(allPlayersSet()){
                    final Intent intent = new Intent(MainActivity.this, GameActivity.class);
                    intent.putParcelableArrayListExtra(StartActivity.EXTRA_MESSAGE, playersList);
                    startActivity(intent);
                }
            }
        });
    }

    private void setRoleScreen(Role role){
        String roleName = role.getClass().getSimpleName();
        gangsterPickImage.setVisibility(View.GONE);
        policePickImage.setVisibility(View.GONE);
        citizenPickImage.setVisibility(View.GONE);
        switch(roleName){
            case "Mafia":
                gangsterPickImage.setVisibility(View.VISIBLE);
                roleText.setText(roleName);
                break;
            case "Police":
                policePickImage.setVisibility(View.VISIBLE);
                roleText.setText("Police officer");
                break;
            case "Citizen":
                citizenPickImage.setVisibility(View.VISIBLE);
                roleText.setText(roleName);
                break;
        }
    }

    //TODO: modify animation values so that the card rotates in correct directions.
    // After half rotation colors of the card should be changed!
    private void rotateFrontToBack() {
        Log.v(TAG, "rotateFrontToBack");
        mCardBackLayout.setVisibility(View.VISIBLE);
        mSetRightOut.setTarget(mCardFrontLayout);
        mSetRightOut.start();
        mSetRightOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (cardTurned) {
                    mCardFrontLayout.setVisibility(View.GONE);
                    playerNameEditText.setText("");
                    loadBackAnimations();
                    if(allPlayersSet()){
                        View assignLayoutFrame = findViewById(R.id.assign_layout_frame);
                        View assignEndLayoutFrame = findViewById(R.id.assign_end_layout_frame);
                        assignLayoutFrame.setVisibility(View.GONE);
                        assignEndLayoutFrame.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        mSetLeftIn.setTarget(mCardBackLayout);
        mSetLeftIn.start();
    }
    private void rotateBackToFront() {
        Log.v(TAG, "rotateBackToFront");
        mCardFrontLayout.setVisibility(View.VISIBLE);
        mSetRightOut.setTarget(mCardBackLayout);
        mSetRightOut.start();
        mSetRightOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!cardTurned) {
                    mCardBackLayout.setVisibility(View.GONE);
                    roleText.setText("");
                    loadFrontAnimations();
                }
            }
        });
        mSetLeftIn.setTarget(mCardFrontLayout);
        mSetLeftIn.start();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void swipeListeners(){
        mCardFrontLayout.setOnTouchListener(new OnSwipeTouchListener(this){
            public void onSwipeRight() {
                String playerName = playerNameEditText.getText().toString();
                if(!mSetLeftIn.isRunning() || !mSetRightOut.isRunning()) {
                    if (!allPlayersSet()) {
                        if (!playerName.matches("")) {
                            if (!cardTurned) {
                                if (!setNewPlayerName(numberOfInitializedPlayers, playerName)) {
                                    Toast.makeText
                                            (MainActivity.this,
                                                    "Player " + playerName.toLowerCase().replaceFirst(Character.toString(playerName.charAt(0)), Character.toString(Character.toUpperCase(playerName.charAt(0)))) + " already exists",
                                                    Toast.LENGTH_LONG).show();
                                } else {
                                    setRoleScreen(playersList.get(numberOfInitializedPlayers).getRole());
                                    rotateFrontToBack();
                                    cardTurned = true;
                                    numberOfInitializedPlayers++;
                                    if (allPlayersSet()) {
                                        Toast.makeText(MainActivity.this, "all players know their roles", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        } else {
                            StyleableToast.makeText(MainActivity.this, "You did not enter name", Toast.LENGTH_LONG, R.style.mytoast).show();
                        }
                    } else {
                        cardAnimationToFinish();
                    }
                }
            }
        });
        mCardBackLayout.setOnTouchListener(new OnSwipeTouchListener(this){
            @Override
            public void onSwipeLeft() {
                if(!mSetLeftIn.isRunning() || !mSetRightOut.isRunning()) {
                    if (cardTurned) {
                        rotateBackToFront();
                        cardTurned = false;
                    }
                }
            }
        });
    }

    private void addEmptyPlayer(){
        Player player = new Player("");
        playersList.add(player);
    }

    private boolean setNewPlayerName(int index, String name) {
        name = name.toLowerCase();
        name = name.replaceFirst(Character.toString(name.charAt(0)), Character.toString(Character.toUpperCase(name.charAt(0))));
        if (!containsName(playersList, name)) {
            playersList.get(index).setName(name);
            return true;
        } else {
            return false;
        }
    }

    public boolean containsName(final List<Player> list, final String name){
        return list.stream().filter(o -> o.getName().equals(name)).findFirst().isPresent();
    }

    private void createTest(){

        if(playersList != null){
            playersList.removeAll(playersList);
        }

        playersList.add(new Player("Dawid", new Citizen()));
        playersList.add(new Player("Radek", new Mafia()));
        playersList.add(new Player("Maks", new Citizen()));
        playersList.add(new Player("Maciek", new Citizen()));
        playersList.add(new Player("Martyna", new Police()));
        playersList.add(new Player("Wladyslaw", new Citizen()));
//        playersList.add(new Player("Piotrek", new Citizen()));
//        playersList.add(new Player("Beata", new Mafia()));
//        playersList.add(new Player("Ela", new Citizen()));
//        playersList.add(new Player("Nina", new Citizen()));
//        playersList.add(new Player("Jasiu", new Citizen()));
//        playersList.add(new Player("Gosia", new Citizen()));

        numberOfInitializedPlayers = numberOfPlayers;

        final Intent intent = new Intent(MainActivity.this, GameActivity.class);
        intent.putParcelableArrayListExtra(StartActivity.EXTRA_MESSAGE, playersList);
        startActivity(intent);
    }
}