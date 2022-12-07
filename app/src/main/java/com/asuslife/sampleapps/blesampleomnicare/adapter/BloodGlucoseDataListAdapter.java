package com.asuslife.sampleapps.blesampleomnicare.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asuslife.omnicaresdk.sync.OmniCareBloodGlucoseData;
import com.asuslife.sampleapps.blesampleomnicare.R;

import java.util.ArrayList;
import java.util.List;

public class BloodGlucoseDataListAdapter extends RecyclerView.Adapter {

    private List<OmniCareBloodGlucoseData> bloodGlucoseList;

    public BloodGlucoseDataListAdapter(List<OmniCareBloodGlucoseData> bloodGlucoseList) {
        if (bloodGlucoseList != null)
            this.bloodGlucoseList = bloodGlucoseList;
        else
            this.bloodGlucoseList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_blood_glucose_data_row, viewGroup, false);

        return new BloodGlucoseDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((BloodGlucoseDataViewHolder) viewHolder).tvTime.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss",bloodGlucoseList.get(i).getTimeInMillis()));
        ((BloodGlucoseDataViewHolder) viewHolder).tvEndTime.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss",bloodGlucoseList.get(i).getEndTimeInMillis()));
        ((BloodGlucoseDataViewHolder) viewHolder).tvDeviceId.setText(bloodGlucoseList.get(i).getDeviceId());
        ((BloodGlucoseDataViewHolder) viewHolder).tvGlu.setText(String.valueOf(bloodGlucoseList.get(i).getGlu()));
        ((BloodGlucoseDataViewHolder) viewHolder).tvHbA1c.setText(String.valueOf(bloodGlucoseList.get(i).getHbA1c()));
    }

    @Override
    public int getItemCount() {
        return bloodGlucoseList.size();
    }

    public void refresh(List<OmniCareBloodGlucoseData> bloodGlucoseList) {
        this.bloodGlucoseList = bloodGlucoseList;
        notifyDataSetChanged();
    }

    private class BloodGlucoseDataViewHolder extends RecyclerView.ViewHolder {

        TextView tvTime, tvEndTime, tvDeviceId, tvGlu, tvHbA1c;

        public BloodGlucoseDataViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvEndTime = (TextView) itemView.findViewById(R.id.tv_end_time);
            tvDeviceId = (TextView) itemView.findViewById(R.id.tv_device_id);
            tvGlu = (TextView) itemView.findViewById(R.id.tv_glu);
            tvHbA1c = (TextView) itemView.findViewById(R.id.tv_hba1c);
        }
    }
}
