package com.asuslife.sampleapps.blesampleomnicare.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asuslife.omnicaresdk.sync.OmniCarePPGData;
import com.asuslife.sampleapps.blesampleomnicare.R;

import java.util.ArrayList;
import java.util.List;

public class PPGDataListAdapter extends RecyclerView.Adapter {

    private List<OmniCarePPGData> ppgList;

    public PPGDataListAdapter(List<OmniCarePPGData> ppgList) {
        if (ppgList != null)
            this.ppgList = ppgList;
        else
            this.ppgList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_ppg_data_row, viewGroup, false);

        return new PPGDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((PPGDataViewHolder) viewHolder).tvTime.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss",ppgList.get(i).getTimeInMillis()));
        ((PPGDataViewHolder) viewHolder).tvEndTime.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss",ppgList.get(i).getEndTimeInMillis()));
        ((PPGDataViewHolder) viewHolder).tvDeviceId.setText(ppgList.get(i).getDeviceId());
        ((PPGDataViewHolder) viewHolder).tvSlotId.setText(String.valueOf(ppgList.get(i).getSlotId()));
        ((PPGDataViewHolder) viewHolder).tvData.setText(ppgList.get(i).getData());
        ((PPGDataViewHolder) viewHolder).tvOrigin.setText(String.valueOf(ppgList.get(i).getOrigin()));
        ((PPGDataViewHolder) viewHolder).tvPeriod.setText(String.valueOf(ppgList.get(i).getPeriod()));
        ((PPGDataViewHolder) viewHolder).tvFactor.setText(String.valueOf(ppgList.get(i).getFactor()));
        ((PPGDataViewHolder) viewHolder).tvLowerLimit.setText(String.valueOf(ppgList.get(i).getLowerLimit()));
        ((PPGDataViewHolder) viewHolder).tvUpperLimit.setText(String.valueOf(ppgList.get(i).getUpperLimit()));
        ((PPGDataViewHolder) viewHolder).tvDimensions.setText(String.valueOf(ppgList.get(i).getDimensions()));
        ((PPGDataViewHolder) viewHolder).tvDescription.setText(ppgList.get(i).getDescription());
    }

    @Override
    public int getItemCount() {
        return ppgList.size();
    }

    public void refresh(List<OmniCarePPGData> ppgList) {
        this.ppgList = ppgList;
        notifyDataSetChanged();
    }

    private class PPGDataViewHolder extends RecyclerView.ViewHolder {

        TextView tvTime, tvEndTime, tvDeviceId, tvSlotId, tvData, tvOrigin, tvPeriod, tvFactor, tvLowerLimit, tvUpperLimit, tvDimensions, tvDescription;

        public PPGDataViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvEndTime = (TextView) itemView.findViewById(R.id.tv_end_time);
            tvDeviceId = (TextView) itemView.findViewById(R.id.tv_device_id);
            tvSlotId = (TextView) itemView.findViewById(R.id.tv_slot_id);
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
