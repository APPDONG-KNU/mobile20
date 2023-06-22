package com.example.senior.community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.senior.R;

import java.util.List;

import kr.co.prnd.readmore.ReadMoreTextView;

public class CommunityFeedAdapter extends RecyclerView.Adapter<CommunityFeedAdapter.CommunityFeedViewHolder> {
    private List<CommunityFeedData> items;
    private Context context;

    public CommunityFeedAdapter(List<CommunityFeedData> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public CommunityFeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new CommunityFeedViewHolder(LayoutInflater.from(context).inflate(R.layout.item_community, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityFeedViewHolder holder, int position) {
        CommunityFeedData item = items.get(position);
        holder.setAttributes(item.getName(), item.getTime(), item.getMainText());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class CommunityFeedViewHolder extends RecyclerView.ViewHolder {
        TextView name, time;
        ReadMoreTextView mainText;

        public CommunityFeedViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.community_item_name);
            time = itemView.findViewById(R.id.community_item_date);
            mainText = itemView.findViewById(R.id.community_item_text);
        }

        public void setAttributes(String name, String time, String mainText) {
            this.name.setText(name);
            this.time.setText(time);
            this.mainText.setText(mainText);
        }
    }
}