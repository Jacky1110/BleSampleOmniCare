package com.asuslife.sampleapps.blesampleomnicare.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asuslife.omnicaresdk.sync.OmniCareLocationData;
import com.asuslife.sampleapps.blesampleomnicare.R;

import java.util.ArrayList;
import java.util.List;

public class LocationDataListAdapter extends RecyclerView.Adapter {

    private List<OmniCareLocationData> locationList;

    public LocationDataListAdapter(List<OmniCareLocationData> locationList) {
        if (locationList != null)
            this.locationList = locationList;
        else
            this.locationList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_location_data_row, viewGroup, false);

        return new LocationDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((LocationDataViewHolder) viewHolder).tvTime.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss",locationList.get(i).getTimeInMillis()));
        ((LocationDataViewHolder) viewHolder).tvEndTime.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss",locationList.get(i).getEndTimeInMillis()));
        ((LocationDataViewHolder) viewHolder).tvDeviceId.setText(locationList.get(i).getDeviceId());
        ((LocationDataViewHolder) viewHolder).tvLat.setText(String.valueOf(locationList.get(i).getLat()));
        ((LocationDataViewHolder) viewHolder).tvLng.setText(String.valueOf(locationList.get(i).getLng()));
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public void refresh(List<OmniCareLocationData> locationList) {
        this.locationList = locationList;
        notifyDataSetChanged();
    }

    private class LocationDataViewHolder extends RecyclerView.ViewHolder {

        TextView tvTime, tvEndTime, tvDeviceId, tvLat, tvLng;

        public LocationDataViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvEndTime = (TextView) itemView.findViewById(R.id.tv_end_time);
            tvDeviceId = (TextView) itemView.findViewById(R.id.tv_device_id);
            tvLat = (TextView) itemView.findViewById(R.id.tv_lat);
            tvLng = (TextView) itemView.findViewById(R.id.tv_lng);
        }
    }
}
