package com.asuslife.sampleapps.blesampleomnicare.adapter;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asuslife.omnicaresdk.sync.OmniCareBodyTemperatureData;
import com.asuslife.sampleapps.blesampleomnicare.R;

import java.util.ArrayList;
import java.util.List;

public class BodyTemperatureDataListAdapter extends RecyclerView.Adapter {

    private List<OmniCareBodyTemperatureData> bodyTemperatureList;

    public BodyTemperatureDataListAdapter(List<OmniCareBodyTemperatureData> bodyTemperatureList) {
        if (bodyTemperatureList != null)
            this.bodyTemperatureList = bodyTemperatureList;
        else
            this.bodyTemperatureList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_body_temperature_data_row, viewGroup, false);

        return new BodyTemperatureDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        ((BodyTemperatureDataViewHolder) viewHolder).tvTime.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss", bodyTemperatureList.get(i).getTimeInMillis()));
        ((BodyTemperatureDataViewHolder) viewHolder).tvEndTime.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss", bodyTemperatureList.get(i).getEndTimeInMillis()));
        ((BodyTemperatureDataViewHolder) viewHolder).tvDeviceId.setText(bodyTemperatureList.get(i).getDeviceId());
        ((BodyTemperatureDataViewHolder) viewHolder).tvType.setText(bodyTemperatureList.get(i).getType());
        ((BodyTemperatureDataViewHolder) viewHolder).tvUnit.setText(bodyTemperatureList.get(i).getUnit());
        ((BodyTemperatureDataViewHolder) viewHolder).tvValue.setText(String.valueOf(bodyTemperatureList.get(i).getValue()));
    }

    @Override
    public int getItemCount() {
        return bodyTemperatureList.size();
    }

    public void refresh(List<OmniCareBodyTemperatureData> bodyTemperatureList) {
        this.bodyTemperatureList = bodyTemperatureList;
        notifyDataSetChanged();
    }

    private class BodyTemperatureDataViewHolder extends RecyclerView.ViewHolder {

        TextView tvTime, tvEndTime, tvDeviceId, tvType, tvUnit, tvValue;

        public BodyTemperatureDataViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvEndTime = (TextView) itemView.findViewById(R.id.tv_end_time);
            tvDeviceId = (TextView) itemView.findViewById(R.id.tv_device_id);
            tvType = (TextView) itemView.findViewById(R.id.tv_type);
            tvUnit = (TextView) itemView.findViewById(R.id.tv_unit);
            tvValue = (TextView) itemView.findViewById(R.id.tv_value);
        }
    }
}
