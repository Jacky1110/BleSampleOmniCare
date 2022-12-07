package com.asuslife.sampleapps.blesampleomnicare.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asuslife.omnicaresdk.sync.OmniCareActivityData;
import com.asuslife.sampleapps.blesampleomnicare.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityDataListAdapter extends RecyclerView.Adapter {

    private List<OmniCareActivityData> activityList;

    public ActivityDataListAdapter(List<OmniCareActivityData> activityList) {
        if (activityList != null)
            this.activityList = activityList;
        else
            this.activityList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_activity_data_row, viewGroup, false);

        return new ActivityDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((ActivityDataViewHolder) viewHolder).tvTime.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss",activityList.get(i).getTimeInMillis()));
        ((ActivityDataViewHolder) viewHolder).tvEndTime.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss",activityList.get(i).getEndTimeInMillis()));
        ((ActivityDataViewHolder) viewHolder).tvDeviceId.setText(activityList.get(i).getDeviceId());
        ((ActivityDataViewHolder) viewHolder).tvType.setText(activityList.get(i).getType());
        ((ActivityDataViewHolder) viewHolder).tvValue.setText(String.valueOf(activityList.get(i).getValue()));
        ((ActivityDataViewHolder) viewHolder).tvUnit.setText(activityList.get(i).getUnit());
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    public void refresh(List<OmniCareActivityData> activityList) {
        this.activityList = activityList;
        notifyDataSetChanged();
    }

    private class ActivityDataViewHolder extends RecyclerView.ViewHolder {

        TextView tvTime, tvEndTime, tvDeviceId, tvType, tvValue, tvUnit;

        public ActivityDataViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvEndTime = (TextView) itemView.findViewById(R.id.tv_end_time);
            tvDeviceId = (TextView) itemView.findViewById(R.id.tv_device_id);
            tvType = (TextView) itemView.findViewById(R.id.tv_type);
            tvValue = (TextView) itemView.findViewById(R.id.tv_value);
            tvUnit = (TextView) itemView.findViewById(R.id.tv_unit);
        }
    }
}
