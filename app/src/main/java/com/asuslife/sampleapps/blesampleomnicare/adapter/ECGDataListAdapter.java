package com.asuslife.sampleapps.blesampleomnicare.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asuslife.omnicaresdk.sync.OmniCareECGData;
import com.asuslife.sampleapps.blesampleomnicare.R;

import java.util.ArrayList;
import java.util.List;

public class ECGDataListAdapter extends RecyclerView.Adapter {

    private List<OmniCareECGData> ecgList;

    public ECGDataListAdapter(List<OmniCareECGData> ecgList) {
        if (ecgList != null)
            this.ecgList = ecgList;
        else
            this.ecgList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_ecg_data_row, viewGroup, false);

        return new ECGDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((ECGDataViewHolder) viewHolder).tvTime.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss",ecgList.get(i).getTimeInMillis()));
        ((ECGDataViewHolder) viewHolder).tvEndTime.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss",ecgList.get(i).getEndTimeInMillis()));
        ((ECGDataViewHolder) viewHolder).tvDeviceId.setText(ecgList.get(i).getDeviceId());
        ((ECGDataViewHolder) viewHolder).tvSlotId.setText(String.valueOf(ecgList.get(i).getSlotId()));
        ((ECGDataViewHolder) viewHolder).tvQrs.setText(String.valueOf(ecgList.get(i).getQrs()));
        ((ECGDataViewHolder) viewHolder).tvSt.setText(String.valueOf(ecgList.get(i).getSt()));
        ((ECGDataViewHolder) viewHolder).tvData.setText(ecgList.get(i).getData());
        ((ECGDataViewHolder) viewHolder).tvOrigin.setText(String.valueOf(ecgList.get(i).getOrigin()));
        ((ECGDataViewHolder) viewHolder).tvPeriod.setText(String.valueOf(ecgList.get(i).getPeriod()));
        ((ECGDataViewHolder) viewHolder).tvFactor.setText(String.valueOf(ecgList.get(i).getFactor()));
        ((ECGDataViewHolder) viewHolder).tvLowerLimit.setText(String.valueOf(ecgList.get(i).getLowerLimit()));
        ((ECGDataViewHolder) viewHolder).tvUpperLimit.setText(String.valueOf(ecgList.get(i).getUpperLimit()));
        ((ECGDataViewHolder) viewHolder).tvDimensions.setText(String.valueOf(ecgList.get(i).getDimensions()));
        ((ECGDataViewHolder) viewHolder).tvDescription.setText(ecgList.get(i).getDescription());
    }

    @Override
    public int getItemCount() {
        return ecgList.size();
    }

    public void refresh(List<OmniCareECGData> ecgList) {
        this.ecgList = ecgList;
        notifyDataSetChanged();
    }

    private class ECGDataViewHolder extends RecyclerView.ViewHolder {

        TextView tvTime, tvEndTime, tvDeviceId, tvSlotId, tvQrs, tvSt, tvData, tvOrigin, tvPeriod, tvFactor, tvLowerLimit, tvUpperLimit, tvDimensions, tvDescription;

        public ECGDataViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvEndTime = (TextView) itemView.findViewById(R.id.tv_end_time);
            tvDeviceId = (TextView) itemView.findViewById(R.id.tv_device_id);
            tvSlotId = (TextView) itemView.findViewById(R.id.tv_slot_id);
            tvQrs = (TextView) itemView.findViewById(R.id.tv_qrs);
            tvSt = (TextView) itemView.findViewById(R.id.tv_st);
            tvData = (TextView) itemView.findViewById(R.id.tv_data);
            tvOrigin = (TextView) itemView.findViewById(R.id.tv_origin);
            tvPeriod = (TextView) itemView.findViewById(R.id.tv_period);
            tvFactor = (TextView) itemView.findViewById(R.id.tv_factor);
            tvLowerLimit = (TextView) itemView.findViewById(R.id.tv_lower_limit);
            tvUpperLimit = (TextView) itemView.findViewById(R.id.tv_upper_limit);
            tvDimensions = (TextView) itemView.findViewById(R.id.tv_dimensions);
            tvDescription = (TextView) itemView.findViewById(R.id.tv_description);
        }
    }
}
