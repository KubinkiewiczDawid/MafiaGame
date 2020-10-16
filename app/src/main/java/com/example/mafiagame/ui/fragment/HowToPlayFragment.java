package com.example.mafiagame.ui.fragment;

import android.os.Bundle;

import androidx.annotation.IntDef;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mafiagame.HowToPlayItem;
import com.example.mafiagame.R;
import com.example.mafiagame.ZoomOutPageTransformer;
import com.example.mafiagame.activity.MainActivity;
import com.example.mafiagame.adapters.HowToPlayPagerAdapter;
import com.example.mafiagame.databinding.FragmentHowToPlayBinding;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


public class HowToPlayFragment extends Fragment {

    @IntDef({VISIBLE, INVISIBLE, GONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Visibility{}

    private HowToPlayPagerAdapter howToPlayPagerAdapter;

    private FragmentHowToPlayBinding howToPlayBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        howToPlayBinding = FragmentHowToPlayBinding.inflate(inflater, container, false);

        setViewVisibility(INVISIBLE);

        setupHowToPlayItems();

        howToPlayBinding.viewpager.setAdapter(howToPlayPagerAdapter);
        howToPlayBinding.viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if((howToPlayBinding.viewpager.getCurrentItem()+1 == howToPlayPagerAdapter.getItemCount())){
                    howToPlayBinding.viewpager.setCurrentItem(0);
                }
            }
        });

        howToPlayBinding.goBackButtonFrame.goBackButton.setOnClickListener(v -> ((MainActivity) getActivity()).setViewPager(MainActivity.GAME_MENU_FRAGMENT));

        return howToPlayBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        howToPlayBinding.viewpager.setCurrentItem(0);
    }

    private void setupHowToPlayItems() {

        List<HowToPlayItem> howToPlayItems = new ArrayList<>();

        HowToPlayItem gameIntroductionItem = new HowToPlayItem(getString(R.string.game_introduction_title), getString(R.string.game_introduction_description), R.drawable.game_title);
        HowToPlayItem gamePrepareInstructionItem = new HowToPlayItem(getString(R.string.game_prepare_instruction_title), getString(R.string.game_prepare_instruction_description), R.drawable.phone_image);
        HowToPlayItem rolesInstructionItem = new HowToPlayItem(getString(R.string.roles_instruction_title), getString(R.string.roles_instruction_description), R.drawable.question_mark);
        HowToPlayItem mafiosiRoleInstructionItem = new HowToPlayItem(getString(R.string.mafiosi_role_instruction_title), getString(R.string.mafiosi_role_instruction_description), R.drawable.gangster_aiming);
        HowToPlayItem policeRoleInstructionItem = new HowToPlayItem(getString(R.string.police_role_instruction_title), getString(R.string.police_role_instruction_description), R.drawable.police_officer);
        HowToPlayItem citizensRoleInstructionItem = new HowToPlayItem(getString(R.string.citizens_role_instruction_title), getString(R.string.citizens_role_instruction_description), R.drawable.citizen);
        HowToPlayItem gameBeginningInstructionItem = new HowToPlayItem(getString(R.string.game_beginning_instruction_title), getString(R.string.game_beginning_instruction_description), R.drawable.menu_screen);
        HowToPlayItem gameDrawingRolesInstructionItem = new HowToPlayItem(getString(R.string.game_drawing_roles_instruction_title), getString(R.string.game_drawing_roles_instruction_description), R.drawable.assign_card_front);
        HowToPlayItem gameHidingRolesInstructionItem = new HowToPlayItem(getString(R.string.game_hiding_roles_instruction_title), getString(R.string.game_hiding_roles_instruction_description), R.drawable.assign_card_back);
        HowToPlayItem gameFlowFirstInstructionItem = new HowToPlayItem(getString(R.string.game_flow_instruction_title), getString(R.string.game_flow_1_instruction_description), R.drawable.mafia_sleeps);
        HowToPlayItem gameFlowSecondInstructionItem = new HowToPlayItem(getString(R.string.game_flow_instruction_title), getString(R.string.game_flow_2_instruction_description), R.drawable.mafia_sleeps);
        HowToPlayItem nightPhaseInstructionItem = new HowToPlayItem(getString(R.string.night_phase_instruction_title), getString(R.string.night_phase_instruction_description), R.drawable.city_sleeps);
        HowToPlayItem gameMafiaTurnInstructionItem = new HowToPlayItem(getString(R.string.game_mafia_turn_instruction_title), getString(R.string.game_mafia_turn_instruction_description), R.drawable.mafia_screen);
        HowToPlayItem gamePoliceTurnInstructionItem = new HowToPlayItem(getString(R.string.game_police_turn_instruction_title), getString(R.string.game_police_turn_instruction_description), R.drawable.police_screen);
        HowToPlayItem dayPhaseInstructionItem = new HowToPlayItem(getString(R.string.day_phase_instruction_title), getString(R.string.day_phase_instruction_description), R.drawable.killed_screen);
        HowToPlayItem talkTimeInstructionItem = new HowToPlayItem(getString(R.string.talk_time_instruction_title), getString(R.string.talk_time_instruction_description), R.drawable.talk_screen);
        HowToPlayItem voteFrontInstructionItem = new HowToPlayItem(getString(R.string.vote_instruction_title), getString(R.string.vote_instruction_1_description), R.drawable.vote_card_front);
        HowToPlayItem voteBackInstructionItem = new HowToPlayItem(getString(R.string.vote_instruction_title), getString(R.string.vote_instruction_2_description), R.drawable.vote_card_back);
        HowToPlayItem voteSummaryInstructionItem = new HowToPlayItem(getString(R.string.vote_summary_instruction_title), getString(R.string.vote_summary_instruction_description), R.drawable.vote_summary);
        HowToPlayItem instructionSummaryInstructionItem = new HowToPlayItem(getString(R.string.instruction_summary_title), getString(R.string.instruction_summary_description), R.drawable.menu_screen);
        HowToPlayItem emptyItem = new HowToPlayItem("", "");

        howToPlayItems.add(gameIntroductionItem);
        howToPlayItems.add(gamePrepareInstructionItem);
        howToPlayItems.add(rolesInstructionItem);
        howToPlayItems.add(mafiosiRoleInstructionItem);
        howToPlayItems.add(policeRoleInstructionItem);
        howToPlayItems.add(citizensRoleInstructionItem);
        howToPlayItems.add(gameBeginningInstructionItem);
        howToPlayItems.add(gameDrawingRolesInstructionItem);
        howToPlayItems.add(gameHidingRolesInstructionItem);
        howToPlayItems.add(gameFlowFirstInstructionItem);
        howToPlayItems.add(gameFlowSecondInstructionItem);
        howToPlayItems.add(nightPhaseInstructionItem);
        howToPlayItems.add(gameMafiaTurnInstructionItem);
        howToPlayItems.add(gamePoliceTurnInstructionItem);
        howToPlayItems.add(dayPhaseInstructionItem);
        howToPlayItems.add(talkTimeInstructionItem);
        howToPlayItems.add(voteFrontInstructionItem);
        howToPlayItems.add(voteBackInstructionItem);
        howToPlayItems.add(voteSummaryInstructionItem);
        howToPlayItems.add(instructionSummaryInstructionItem);
        howToPlayItems.add(emptyItem);

        howToPlayPagerAdapter = new HowToPlayPagerAdapter(howToPlayItems);
    }

    public void setViewVisibility(@Visibility int visibility){
        howToPlayBinding.howToPlayParentView.setVisibility(visibility);
    }
}