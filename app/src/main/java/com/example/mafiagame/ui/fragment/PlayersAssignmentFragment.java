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
import com.example.mafiagame.databinding.FragmentPlayersAssignmentBinding;
import com.muddzdev.styleabletoast.StyleableToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import listeners.OnSwipeTouchListener;

public class PlayersAssignmentFragment extends Fragment {
    private final static String TAG = "playersAsignmentFragment";

    private int numberOfPlayers;
    private int numberOfInitializedPlayers;
    private AnimatorSet mSetRightOut;
    private AnimatorSet mSetLeftIn;
    private boolean cardTurned;
    public static ArrayList<Player> playersList;
    private AnimatorSet mSetOut, mSetIn;
    private int animationTarget;

    private FragmentPlayersAssignmentBinding playersAssignmentBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        playersAssignmentBinding = FragmentPlayersAssignmentBinding.inflate(inflater, container, false);

        playersList = new ArrayList<>();

        animationTarget = 0;

        //createTest();

        loadFrontAnimations();
        loadFrontCardAnimations();
        swipeListeners();
        beginGame();
        changeCameraDistance();
        animateFrontCard();
        return playersAssignmentBinding.getRoot();
    }

    private void beginGame(){
        playersAssignmentBinding.beginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playersAssignmentBinding.profileSwitcher != null) playersAssignmentBinding.profileSwitcher.showNext();
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
        Object animationTargetObject = playersAssignmentBinding.cardFrontAssignmentFrame.assignLayoutFrame.questionMarkFront;
        mSetOut.setTarget(playersAssignmentBinding.cardFrontAssignmentFrame.assignLayoutFrame.questionMarkFront);
        mSetIn.setTarget(playersAssignmentBinding.cardFrontAssignmentFrame.assignLayoutFrame.gangsterFrontImage);
        mSetOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                if(animationTarget == 0){
                    mSetIn.setTarget(playersAssignmentBinding.cardFrontAssignmentFrame.assignLayoutFrame.gangsterFrontImage);
                    mSetIn.start();
                    animationTarget = 1;
                } else if(animationTarget == 1){
                    mSetIn.setTarget(playersAssignmentBinding.cardFrontAssignmentFrame.assignLayoutFrame.policeFrontImage);
                    mSetIn.start();
                    animationTarget = 2;
                } else if(animationTarget == 2){
                    mSetIn.setTarget(playersAssignmentBinding.cardFrontAssignmentFrame.assignLayoutFrame.citizenFrontImage);
                    mSetIn.start();
                    animationTarget = 3;
                } else if(animationTarget == 3){
                    mSetIn.setTarget(playersAssignmentBinding.cardFrontAssignmentFrame.assignLayoutFrame.questionMarkFront);
                    mSetIn.start();
                    animationTarget = 0;
                }
            }
        });

        mSetOut.start();
        mSetIn.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if(animationTarget == 0){
                    mSetOut.setTarget(playersAssignmentBinding.cardFrontAssignmentFrame.assignLayoutFrame.questionMarkFront);
                    mSetOut.start();
                } else if(animationTarget == 1){
                    mSetOut.setTarget(playersAssignmentBinding.cardFrontAssignmentFrame.assignLayoutFrame.gangsterFrontImage);
                    mSetOut.start();
                } else if(animationTarget == 2){
                    mSetOut.setTarget(playersAssignmentBinding.cardFrontAssignmentFrame.assignLayoutFrame.policeFrontImage);
                    mSetOut.start();
                } else if(animationTarget == 3){
                    mSetOut.setTarget(playersAssignmentBinding.cardFrontAssignmentFrame.assignLayoutFrame.citizenFrontImage);
                    mSetOut.start();
                }
            }
        });
    }

    private void changeCameraDistance() {
        int distance = 8000;
        float scale = getResources().getDisplayMetrics().density * distance;
        playersAssignmentBinding.cardFrontAssignment.setCameraDistance(scale);
        playersAssignmentBinding.cardBackAssignment.setCameraDistance(scale);
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
        mSetRightOut.setTarget(playersAssignmentBinding.cardFrontAssignment);
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
        playersAssignmentBinding.cardBackAssignmentFrame.gangsterPickImage.setVisibility(View.GONE);
        playersAssignmentBinding.cardBackAssignmentFrame.policePickImage.setVisibility(View.GONE);
        playersAssignmentBinding.cardBackAssignmentFrame.citizenPickImage.setVisibility(View.GONE);
        switch(roleName){
            case "Mafia":
                playersAssignmentBinding.cardBackAssignmentFrame.gangsterPickImage.setVisibility(View.VISIBLE);
                playersAssignmentBinding.cardBackAssignmentFrame.roleText.setText(roleName);
                break;
            case "Police":
                playersAssignmentBinding.cardBackAssignmentFrame.policePickImage.setVisibility(View.VISIBLE);
                playersAssignmentBinding.cardBackAssignmentFrame.roleText.setText("Police officer");
                break;
            case "Citizen":
                playersAssignmentBinding.cardBackAssignmentFrame.citizenPickImage.setVisibility(View.VISIBLE);
                playersAssignmentBinding.cardBackAssignmentFrame.roleText.setText(roleName);
                break;
        }
    }

    //TODO: modify animation values so that the card rotates in correct directions.
    // After half rotation colors of the card should be changed!
    private void rotateFrontToBack() {
        Log.v(TAG, "rotateFrontToBack");
        playersAssignmentBinding.cardBackAssignment.setVisibility(View.VISIBLE);
        mSetRightOut.setTarget(playersAssignmentBinding.cardFrontAssignment);
        mSetRightOut.start();
        mSetRightOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (cardTurned) {
                    playersAssignmentBinding.cardFrontAssignment.setVisibility(View.GONE);
                    playersAssignmentBinding.cardFrontAssignmentFrame.assignLayoutFrame.playerNameEditText.setText("");
                    loadBackAnimations();
                    if(allPlayersSet()){
                        playersAssignmentBinding.cardFrontAssignmentFrame.assignLayout.setVisibility(View.GONE);
                        playersAssignmentBinding.cardFrontAssignmentFrame.assignEndLayout.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        mSetLeftIn.setTarget(playersAssignmentBinding.cardBackAssignment);
        mSetLeftIn.start();
    }
    private void rotateBackToFront() {
        Log.v(TAG, "rotateBackToFront");
        playersAssignmentBinding.cardFrontAssignment.setVisibility(View.VISIBLE);
        mSetRightOut.setTarget(playersAssignmentBinding.cardBackAssignment);
        mSetRightOut.start();
        mSetRightOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!cardTurned) {
                    playersAssignmentBinding.cardBackAssignment.setVisibility(View.GONE);
                    playersAssignmentBinding.cardBackAssignmentFrame.roleText.setText("");
                    loadFrontAnimations();
                }
            }
        });
        mSetLeftIn.setTarget(playersAssignmentBinding.cardFrontAssignment);
        mSetLeftIn.start();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void swipeListeners(){
        playersAssignmentBinding.cardFrontAssignment.setOnTouchListener(new OnSwipeTouchListener(getContext()){
            public void onSwipeRight() {
                String playerName = playersAssignmentBinding.cardFrontAssignmentFrame.assignLayoutFrame.playerNameEditText.getText().toString();
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
        playersAssignmentBinding.cardBackAssignment.setOnTouchListener(new OnSwipeTouchListener(getContext()){
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

        numberOfInitializedPlayers = 5;

        ((MainActivity)getActivity()).setPlayersList(playersList);
//        ((MainActivity)getActivity()).setViewPager(MainActivity.MAFIA_ACTION_FRAGMENT);
    }
}