package com.asuslife.sampleapps.blesampleomnicare.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asuslife.omnicaresdk.sync.OmniCareBloodPressureData;
import com.asuslife.sampleapps.blesampleomnicare.R;

import java.util.ArrayList;
import java.util.List;

public class BloodPressureDataListAdapter extends RecyclerView.Adapter {

    private List<OmniCareBloodPressureData> bloodPressureList;

    public BloodPressureDataListAdapter(List<OmniCareBloodPressureData> bloodPressureList) {
        if (bloodPressureList != null)
            this.bloodPressureList = bloodPressureList;
        else
            this.bloodPressureList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_blood_pressure_data_row, viewGroup, false);

        return new ActivityDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((ActivityDataViewHolder) viewHolder).tvTime.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss",bloodPressureList.get(i).getTimeInMillis()));
        ((ActivityDataViewHolder) viewHolder).tvEndTime.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss",bloodPressureList.get(i).getEndTimeInMillis()));
        ((ActivityDataViewHolder) viewHolder).tvDeviceId.setText(bloodPressureList.get(i).getDeviceId());
        ((ActivityDataViewHolder) viewHolder).tvSourceType.setText(bloodPressureList.get(i).getSourceType());
        ((ActivityDataViewHolder) viewHolder).tvSys.setText(String.valueOf(bloodPressureList.get(i).getSys()));
        ((ActivityDataViewHolder) viewHolder).tvDia.setText(String.valueOf(bloodPressureList.get(i).getDia()));
        ((ActivityDataViewHolder) viewHolder).tvHr.setText(String.valueOf(bloodPressureList.get(i).getHr()));
        ((ActivityDataViewHolder) viewHolder).tvMean.setText(String.valueOf(bloodPressureList.get(i).getMean()));
        ((ActivityDataViewHolder) viewHolder).tvPtt.setText(String.valueOf(bloodPressureList.get(i).getPtt()));
    }

    @Override
    public int getItemCount() {
        return bloodPressureList.size();
    }

    public void refresh(List<OmniCareBloodPressureData> bloodPressureList) {
        this.bloodPressureList = bloodPressureList;
        notifyDataSetChanged();
    }

    private class ActivityDataViewHolder extends RecyclerView.ViewHolder {

        TextView tvTime, tvEndTime, tvDeviceId, tvSourceType, tvSys, tvDia, tvHr, tvMean, tvPtt;

        public ActivityDataViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvEndTime = (TextView) itemView.findViewById(R.id.tv_end_time);
            tvDeviceId = (TextView) itemView.findViewById(R.id.tv_device_id);
            tvSourceType = (TextView) itemView.findViewById(R.id.tv_source_type);
            tvSys = (TextView) itemView.findViewById(R.id.tv_sys);
            tvDia = (TextView) itemView.findViewById(R.id.tv_dia);
            tvHr = (TextView) itemView.findViewById(R.id.tv_hr);
            tvMean = (TextView) itemView.findViewById(R.id.tv_mean);
            tvPtt = (TextView) itemView.findViewById(R.id.tv_ppt);
        }
    }
}
