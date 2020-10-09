package com.example.mafiagame;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.view.View;

import java.util.ArrayList;

public class fadeAnimator {
    private View view;
    private int numberOfItems;
    private ArrayList<Object> targets;
    private AnimatorSet mSetOut, mSetIn;
    private static int animationTarget = 0;

    public fadeAnimator(View view, int numberOfItems, ArrayList<Object> targets) {
        this.view = view;
        this.numberOfItems = numberOfItems;
        this.targets = targets;
        loadFadeAnimations(view);
    }

    private void loadFadeAnimations(View view) {
        mSetOut = (AnimatorSet) AnimatorInflater.loadAnimator(view.getContext(), R.animator.front_card_object_fade_out);
        mSetIn = (AnimatorSet) AnimatorInflater.loadAnimator(view.getContext(), R.animator.front_card_object_fade_in);
    }

//    private void animateFrontCard() {
//        Object animationTargetObject = questionMarkFront;
//        mSetOut.setTarget(questionMarkFront);
//        mSetIn.setTarget(gangsterFrontImage);
//        mSetOut.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animator) {
//                if(animationTarget == 0){
//                    mSetIn.setTarget(gangsterFrontImage);
//                    mSetIn.start();
//                    animationTarget = 1;
//                } else if(animationTarget == 1){
//                    mSetIn.setTarget(policeFrontImage);
//                    mSetIn.start();
//                    animationTarget = 2;
//                } else if(animationTarget == 2){
//                    mSetIn.setTarget(citizenFrontImage);
//                    mSetIn.start();
//                    animationTarget = 3;
//                } else if(animationTarget == 3){
//                    mSetIn.setTarget(questionMarkFront);
//                    mSetIn.start();
//                    animationTarget = 0;
//                }
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animator) {
//
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animator) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animator) {
//
//            }
//        });
//        mSetOut.start();
//
//        mSetIn.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animator) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animator) {
//                if(animationTarget == 0){
//                    mSetOut.setTarget(questionMarkFront);
//                    mSetOut.start();
//                } else if(animationTarget == 1){
//                    mSetOut.setTarget(gangsterFrontImage);
//                    mSetOut.start();
//                } else if(animationTarget == 2){
//                    mSetOut.setTarget(policeFrontImage);
//                    mSetOut.start();
//                } else if(animationTarget == 3){
//                    mSetOut.setTarget(citizenFrontImage);
//                    mSetOut.start();
//                }
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animator) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animator) {
//
//            }
//        });
}
