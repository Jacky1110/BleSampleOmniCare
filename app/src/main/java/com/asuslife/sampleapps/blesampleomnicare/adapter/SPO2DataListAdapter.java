package com.asuslife.sampleapps.blesampleomnicare.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asuslife.omnicaresdk.sync.OmniCareSPO2Data;
import com.asuslife.sampleapps.blesampleomnicare.R;

import java.util.ArrayList;
import java.util.List;

public class SPO2DataListAdapter extends RecyclerView.Adapter {

    private List<OmniCareSPO2Data> spo2List;

    public SPO2DataListAdapter(List<OmniCareSPO2Data> spo2List) {
        if (spo2List != null)
            this.spo2List = spo2List;
        else
            this.spo2List = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_spo2_data_row, viewGroup, false);

        return new SPO2DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        ((SPO2DataViewHolder) viewHolder).tvTime.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss",spo2List.get(i).getTimeInMillis()));
        ((SPO2DataViewHolder) viewHolder).tvEndTime.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss",spo2List.get(i).getEndTimeInMillis()));
        ((SPO2DataViewHolder) viewHolder).tvDeviceId.setText(spo2List.get(i).getDeviceId());
        ((SPO2DataViewHolder) viewHolder).tvSourceType.setText(spo2List.get(i).getSourceType());
        ((SPO2DataViewHolder) viewHolder).tvValue.setText(String.valueOf(spo2List.get(i).getValue()));
    }

    @Override
    public int getItemCount() {
        return spo2List.size();
    }

    public void refresh(List<OmniCareSPO2Data> spo2List) {
        this.spo2List = spo2List;
        notifyDataSetChanged();
    }

    private class SPO2DataViewHolder extends RecyclerView.ViewHolder {

        TextView tvTime, tvEndTime, tvDeviceId, tvSourceType, tvValue;

        public SPO2DataViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvEndTime = (TextView) itemView.findViewById(R.id.tv_end_time);
            tvDeviceId = (TextView) itemView.findViewById(R.id.tv_device_id);
            tvSourceType = (TextView) itemView.findViewById(R.id.tv_source_type);
            tvValue = (TextView) itemView.findViewById(R.id.tv_value);
        }
    }
}
