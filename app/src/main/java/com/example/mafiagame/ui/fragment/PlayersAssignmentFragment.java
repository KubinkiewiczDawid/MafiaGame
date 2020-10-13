package com.example.mafiagame.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.mafiagame.R;
import com.example.mafiagame.activity.MainActivity;
import com.example.mafiagame.components.Citizen;
import com.example.mafiagame.components.Mafia;
import com.example.mafiagame.components.Player;
import com.example.mafiagame.components.Police;
import com.example.mafiagame.components.Role;
import com.muddzdev.styleabletoast.StyleableToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import listeners.OnSwipeTouchListener;

public class PlayersAssignmentFragment extends Fragment {
    private final static String TAG = "playersAsignmentFragment";

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_players_asignment, container, false);

        findViews(view);

        playersList = new ArrayList<>();

        animationTarget = 0;

        //createTest();

        loadFrontAnimations();
        loadFrontCardAnimations();
        swipeListeners();
        beginGame(view);
        changeCameraDistance();
        animateFrontCard();
        return view;
    }

    private void beginGame(View view){
        Button beginButton = view.findViewById(R.id.beginButton);
        beginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switcher != null) switcher.showNext();
            }
        });
        gameInfoInitialize();
    }

    private void loadFrontAnimations() {
        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.out_animation);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.in_animation);
    }

    private void loadBackAnimations() {
        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.out_animation_back);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.in_animation_back);
    }

    private void loadFrontCardAnimations() {
        mSetOut = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.front_card_object_fade_out);
        mSetIn = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.front_card_object_fade_in);
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

    private void findViews(View view) {
        switcher = view.findViewById(R.id.profileSwitcher);
        mCardBackLayout = view.findViewById(R.id.card_back);
        mCardFrontLayout = view.findViewById(R.id.card_front);
        playerNameEditText = view.findViewById(R.id.plyerNameEditText);
        roleText = view.findViewById(R.id.roleText);
        gangsterPickImage = view.findViewById(R.id.gangster_pick_image);
        policePickImage = view.findViewById(R.id.police_pick_image);
        citizenPickImage = view.findViewById(R.id.citizen_pick_image);
        gangsterFrontImage = view.findViewById(R.id.gangster_front_image);
        policeFrontImage = view.findViewById(R.id.police_front_image);
        citizenFrontImage = view.findViewById(R.id.citizen_front_image);
        questionMarkFront = view.findViewById(R.id.question_mark_front);
    }

    private void changeCameraDistance() {
        int distance = 8000;
        float scale = getResources().getDisplayMetrics().density * distance;
        mCardFrontLayout.setCameraDistance(scale);
        mCardBackLayout.setCameraDistance(scale);
    }

    private void gameInfoInitialize(){
        numberOfPlayers = ((MainActivity)getActivity()).getNumberOfPlayers();
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
        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.out_right_animation);
        mSetRightOut.setTarget(mCardFrontLayout);
        mSetRightOut.start();
        mSetRightOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if(allPlayersSet()){
                    ((MainActivity)getActivity()).setPlayersList(playersList);
                    ((MainActivity)getActivity()).setViewPager(MainActivity.MAFIA_ACTION_FRAGMENT);
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
                        View assignLayoutFrame = getView().findViewById(R.id.assign_layout_frame);
                        View assignEndLayoutFrame = getView().findViewById(R.id.assign_end_layout_frame);
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
        mCardFrontLayout.setOnTouchListener(new OnSwipeTouchListener(getContext()){
            public void onSwipeRight() {
                String playerName = playerNameEditText.getText().toString();
                if(!mSetLeftIn.isRunning() || !mSetRightOut.isRunning()) {
                    if (!allPlayersSet()) {
                        if (!playerName.matches("")) {
                            if (!cardTurned) {
                                if (!setNewPlayerName(numberOfInitializedPlayers, playerName)) {
                                    StyleableToast.makeText
                                            (getContext(),
                                                    "Player " + playerName.toLowerCase().replaceFirst(Character.toString(playerName.charAt(0)), Character.toString(Character.toUpperCase(playerName.charAt(0)))) + " already exists",
                                                    Toast.LENGTH_LONG, R.style.mytoast).show();
                                } else {
                                    setRoleScreen(playersList.get(numberOfInitializedPlayers).getRole());
                                    rotateFrontToBack();
                                    cardTurned = true;
                                    numberOfInitializedPlayers++;
                                }
                            }
                        } else {
                            StyleableToast.makeText(getContext(), "You did not enter name", Toast.LENGTH_LONG, R.style.mytoast).show();
                        }
                    } else {
                        cardAnimationToFinish();
                    }
                }
            }
        });
        mCardBackLayout.setOnTouchListener(new OnSwipeTouchListener(getContext()){
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
//        playersList.add(new Player("Wladyslaw", new Citizen()));
//        playersList.add(new Player("Piotrek", new Citizen()));
//        playersList.add(new Player("Beata", new Mafia()));
//        playersList.add(new Player("Ela", new Citizen()));
//        playersList.add(new Player("Nina", new Citizen()));
//        playersList.add(new Player("Jasiu", new Citizen()));
//        playersList.add(new Player("Gosia", new Citizen()));

        numberOfInitializedPlayers = 5;

        ((MainActivity)getActivity()).setPlayersList(playersList);
//        ((MainActivity)getActivity()).setViewPager(MainActivity.MAFIA_ACTION_FRAGMENT);
    }
}