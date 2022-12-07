package com.asuslife.sampleapps.blesampleomnicare.adapter;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asuslife.omnicaresdk.sync.OmniCareGSensorData;
import com.asuslife.sampleapps.blesampleomnicare.R;

import java.util.ArrayList;
import java.util.List;

public class GSensorDataListAdapter extends RecyclerView.Adapter {

    private List<OmniCareGSensorData> gSensorList;

    public GSensorDataListAdapter(List<OmniCareGSensorData> gSensorList) {
        if (gSensorList != null)
            this.gSensorList = gSensorList;
        else
            this.gSensorList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_gsensor_data_row, viewGroup, false);

        return new AltitudeDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        ((AltitudeDataViewHolder) viewHolder).tvTime.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss", gSensorList.get(i).getTimeInMillis()));
        ((AltitudeDataViewHolder) viewHolder).tvEndTime.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss", gSensorList.get(i).getEndTimeInMillis()));
        ((AltitudeDataViewHolder) viewHolder).tvDeviceId.setText(gSensorList.get(i).getDeviceId());
        ((AltitudeDataViewHolder) viewHolder).tvUnit.setText(gSensorList.get(i).getUnit());
        ((AltitudeDataViewHolder) viewHolder).tvValue.setText(String.valueOf(gSensorList.get(i).getValue()));
    }

    @Override
    public int getItemCount() {
        return gSensorList.size();
    }

    public void refresh(List<OmniCareGSensorData> gSensorList) {
        this.gSensorList = gSensorList;
        notifyDataSetChanged();
    }

    private class AltitudeDataViewHolder extends RecyclerView.ViewHolder {

        TextView tvTime, tvEndTime, tvDeviceId, tvUnit, tvValue;

        public AltitudeDataViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvEndTime = (TextView) itemView.findViewById(R.id.tv_end_time);
            tvDeviceId = (TextView) itemView.findViewById(R.id.tv_device_id);
            tvUnit = (TextView) itemView.findViewById(R.id.tv_unit);
            tvValue = (TextView) itemView.findViewById(R.id.tv_value);
        }
    }
}
