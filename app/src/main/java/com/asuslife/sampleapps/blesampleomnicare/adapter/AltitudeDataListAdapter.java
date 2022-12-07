package com.asuslife.sampleapps.blesampleomnicare.adapter;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asuslife.omnicaresdk.sync.OmniCareAltitudeData;
import com.asuslife.sampleapps.blesampleomnicare.R;

import java.util.ArrayList;
import java.util.List;

public class AltitudeDataListAdapter extends RecyclerView.Adapter {

    private List<OmniCareAltitudeData> altitudeList;

    public AltitudeDataListAdapter(List<OmniCareAltitudeData> altitudeList) {
        if (altitudeList != null)
            this.altitudeList = altitudeList;
        else
            this.altitudeList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_altitude_data_row, viewGroup, false);

        return new AltitudeDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        ((AltitudeDataViewHolder) viewHolder).tvTime.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss", altitudeList.get(i).getTimeInMillis()));
        ((AltitudeDataViewHolder) viewHolder).tvEndTime.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss", altitudeList.get(i).getEndTimeInMillis()));
        ((AltitudeDataViewHolder) viewHolder).tvDeviceId.setText(altitudeList.get(i).getDeviceId());
        ((AltitudeDataViewHolder) viewHolder).tvUnit.setText(altitudeList.get(i).getUnit());
        ((AltitudeDataViewHolder) viewHolder).tvValue.setText(String.valueOf(altitudeList.get(i).getValue()));
    }

    @Override
    public int getItemCount() {
        return altitudeList.size();
    }

    public void refresh(List<OmniCareAltitudeData> altitudeList) {
        this.altitudeList = altitudeList;
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
