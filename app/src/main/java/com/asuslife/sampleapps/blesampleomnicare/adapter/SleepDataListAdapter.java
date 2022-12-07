package com.asuslife.sampleapps.blesampleomnicare.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asuslife.omnicaresdk.sync.OmniCareSleepData;
import com.asuslife.sampleapps.blesampleomnicare.R;

import java.util.ArrayList;
import java.util.List;

public class SleepDataListAdapter extends RecyclerView.Adapter {

    private List<OmniCareSleepData> sleepList;

    public SleepDataListAdapter(List<OmniCareSleepData> sleepList) {
        if (sleepList != null)
            this.sleepList = sleepList;
        else
            this.sleepList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_sleep_data_row, viewGroup, false);

        return new SleepDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        ((SleepDataViewHolder) viewHolder).tvTime.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss",sleepList.get(i).getTimeInMillis()));
        ((SleepDataViewHolder) viewHolder).tvEndTime.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss",sleepList.get(i).getEndTimeInMillis()));
        ((SleepDataViewHolder) viewHolder).tvDeviceId.setText(sleepList.get(i).getDeviceId());
        ((SleepDataViewHolder) viewHolder).tvState.setText(sleepList.get(i).getState());
        ((SleepDataViewHolder) viewHolder).tvDuration.setText(String.valueOf(sleepList.get(i).getDuration()));
    }

    @Override
    public int getItemCount() {
        return sleepList.size();
    }

    public void refresh(List<OmniCareSleepData> sleepList) {
        this.sleepList = sleepList;
        notifyDataSetChanged();
    }

    private class SleepDataViewHolder extends RecyclerView.ViewHolder {

        TextView tvTime, tvEndTime, tvDeviceId, tvState, tvDuration;

        public SleepDataViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvEndTime = (TextView) itemView.findViewById(R.id.tv_end_time);
            tvDeviceId = (TextView) itemView.findViewById(R.id.tv_device_id);
            tvState = (TextView) itemView.findViewById(R.id.tv_state);
            tvDuration = (TextView) itemView.findViewById(R.id.tv_duration);
        }
    }
}
