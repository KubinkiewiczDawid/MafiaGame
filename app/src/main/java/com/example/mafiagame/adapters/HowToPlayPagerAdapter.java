package com.example.mafiagame.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mafiagame.HowToPlayItem;
import com.example.mafiagame.R;

import java.util.List;

public class HowToPlayPagerAdapter extends  RecyclerView.Adapter<HowToPlayPagerAdapter.HowToPlayViewHolder> {

    public static int LOOPS_COUNT = 10;
    private List<HowToPlayItem> howToPlayItems;

    public HowToPlayPagerAdapter(List<HowToPlayItem> howToPlayItems) {
        this.howToPlayItems = howToPlayItems;
    }

    @NonNull
    @Override
    public HowToPlayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HowToPlayViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_how_to_play, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull HowToPlayViewHolder holder, int position) {
        holder.setHowToPlayData(howToPlayItems.get(position));
    }

    @Override
    public int getItemCount() {
        return howToPlayItems.size();
    }



    class HowToPlayViewHolder extends RecyclerView.ViewHolder {

        private TextView textTitle;
        private TextView textDescription;
        private ImageView imageInstruction;

        public HowToPlayViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDescription = itemView.findViewById(R.id.textDescription);
            imageInstruction = itemView.findViewById(R.id.imageInstruction);
        }

        void setHowToPlayData(HowToPlayItem howToPlayItem) {
            textTitle.setText(howToPlayItem.getTitle());
            textDescription.setText(howToPlayItem.getDescription());
            imageInstruction.setImageResource(howToPlayItem.getImage());
        }
    }
}
