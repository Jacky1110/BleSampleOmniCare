package com.asuslife.sampleapps.blesampleomnicare.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asuslife.omnicaresdk.settings.PillReminder;
import com.asuslife.sampleapps.blesampleomnicare.R;

import java.util.ArrayList;
import java.util.List;

public class PillReminderListAdapter extends RecyclerView.Adapter {

    private List<PillReminder> pillReminderList;

    public PillReminderListAdapter(List<PillReminder> pillReminderList) {
        if (pillReminderList != null)
            this.pillReminderList = pillReminderList;
        else
            this.pillReminderList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_pill_reminder_row, viewGroup, false);

        return new PillReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((PillReminderViewHolder) viewHolder).tvTime.setText(DateFormat.format("HH:mm",pillReminderList.get(i).getTime()));
        ((PillReminderViewHolder) viewHolder).ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pillReminderList.remove(viewHolder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return pillReminderList.size();
    }

    public void refresh(List<PillReminder> pillReminderList) {
        this.pillReminderList = pillReminderList;
        notifyDataSetChanged();
    }

    private class PillReminderViewHolder extends RecyclerView.ViewHolder {

        TextView tvTime;
        ImageView ivDelete;

        public PillReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            ivDelete = (ImageView) itemView.findViewById(R.id.iv_delete);
        }
    }
}
