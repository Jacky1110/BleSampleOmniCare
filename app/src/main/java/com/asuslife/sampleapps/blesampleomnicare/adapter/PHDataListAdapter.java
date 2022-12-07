package com.asuslife.sampleapps.blesampleomnicare.adapter;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asuslife.omnicaresdk.sync.OmniCareLocationData;
import com.asuslife.omnicaresdk.sync.OmniCarePHData;
import com.asuslife.sampleapps.blesampleomnicare.R;

import java.util.ArrayList;
import java.util.List;

public class PHDataListAdapter extends RecyclerView.Adapter {

    private List<OmniCarePHData> phList;

    public PHDataListAdapter(List<OmniCarePHData> phList) {
        if (phList != null)
            this.phList = phList;
        else
            this.phList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_ph_data_row, viewGroup, false);

        return new PHDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((PHDataViewHolder) viewHolder).tvTime.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss",phList.get(i).getTimeInMillis()));
        ((PHDataViewHolder) viewHolder).tvEndTime.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss",phList.get(i).getEndTimeInMillis()));
        ((PHDataViewHolder) viewHolder).tvDeviceId.setText(phList.get(i).getDeviceId());
        ((PHDataViewHolder) viewHolder).tvPHRawData.setText(String.valueOf(phList.get(i).getPhRawData()));
        ((PHDataViewHolder) viewHolder).tvPHScale.setText(String.valueOf(phList.get(i).getPhScale()));
    }

    @Override
    public int getItemCount() {
        return phList.size();
    }

    public void refresh(List<OmniCarePHData> phList) {
        this.phList = phList;
        notifyDataSetChanged();
    }

    private class PHDataViewHolder extends RecyclerView.ViewHolder {

        TextView tvTime, tvEndTime, tvDeviceId, tvPHRawData, tvPHScale;

        public PHDataViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvEndTime = (TextView) itemView.findViewById(R.id.tv_end_time);
            tvDeviceId = (TextView) itemView.findViewById(R.id.tv_device_id);
            tvPHRawData = (TextView) itemView.findViewById(R.id.tv_ph_raw_data);
            tvPHScale = (TextView) itemView.findViewById(R.id.tv_ph_scale);
        }
    }
}
