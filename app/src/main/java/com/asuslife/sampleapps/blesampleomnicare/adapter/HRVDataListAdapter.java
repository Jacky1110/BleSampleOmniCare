package com.asuslife.sampleapps.blesampleomnicare.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asuslife.omnicaresdk.sync.OmniCareHRVData;
import com.asuslife.sampleapps.blesampleomnicare.R;

import java.util.ArrayList;
import java.util.List;

public class HRVDataListAdapter extends RecyclerView.Adapter {

    private List<OmniCareHRVData> hrvList;

    public HRVDataListAdapter(List<OmniCareHRVData> hrvList) {
        if (hrvList != null)
            this.hrvList = hrvList;
        else
            this.hrvList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_hrv_data_row, viewGroup, false);

        return new HRVDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((HRVDataViewHolder) viewHolder).tvTime.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss",hrvList.get(i).getTimeInMillis()));
        ((HRVDataViewHolder) viewHolder).tvEndTime.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss",hrvList.get(i).getEndTimeInMillis()));
        ((HRVDataViewHolder) viewHolder).tvDeviceId.setText(hrvList.get(i).getDeviceId());
        ((HRVDataViewHolder) viewHolder).tvSourceType.setText(hrvList.get(i).getSourceType());
        ((HRVDataViewHolder) viewHolder).tvTp.setText(String.valueOf(hrvList.get(i).getTp()));
        ((HRVDataViewHolder) viewHolder).tvVlfp.setText(String.valueOf(hrvList.get(i).getVlfp()));
        ((HRVDataViewHolder) viewHolder).tvLfp.setText(String.valueOf(hrvList.get(i).getLfp()));
        ((HRVDataViewHolder) viewHolder).tvHfp.setText(String.valueOf(hrvList.get(i).getHfp()));
        ((HRVDataViewHolder) viewHolder).tvNlfp.setText(String.valueOf(hrvList.get(i).getNlfp()));
        ((HRVDataViewHolder) viewHolder).tvNhfp.setText(String.valueOf(hrvList.get(i).getNhfp()));
        ((HRVDataViewHolder) viewHolder).tvLfHf.setText(String.valueOf(hrvList.get(i).getLfHf()));
        ((HRVDataViewHolder) viewHolder).tvSdnn.setText(String.valueOf(hrvList.get(i).getSdnn()));
        ((HRVDataViewHolder) viewHolder).tvSdann.setText(String.valueOf(hrvList.get(i).getSdann()));
        ((HRVDataViewHolder) viewHolder).tvNn50.setText(String.valueOf(hrvList.get(i).getNn50()));
        ((HRVDataViewHolder) viewHolder).tvPnn50.setText(String.valueOf(hrvList.get(i).getPnn50()));
        ((HRVDataViewHolder) viewHolder).tvRmssd.setText(String.valueOf(hrvList.get(i).getRmssd()));
        ((HRVDataViewHolder) viewHolder).tvAf.setText(String.valueOf(hrvList.get(i).getAf()));
        ((HRVDataViewHolder) viewHolder).tvPac.setText(String.valueOf(hrvList.get(i).getPac()));
        ((HRVDataViewHolder) viewHolder).tvAnb.setText(String.valueOf(hrvList.get(i).getAnb()));
        ((HRVDataViewHolder) viewHolder).tvStress.setText(String.valueOf(hrvList.get(i).getStress()));
        ((HRVDataViewHolder) viewHolder).tvVo2.setText(String.valueOf(hrvList.get(i).getVo2()));
        ((HRVDataViewHolder) viewHolder).tvHrr.setText(String.valueOf(hrvList.get(i).getHrr()));
        ((HRVDataViewHolder) viewHolder).tvRri.setText(String.valueOf(hrvList.get(i).getRri()));
        ((HRVDataViewHolder) viewHolder).tvPpi.setText(String.valueOf(hrvList.get(i).getPpi()));
        ((HRVDataViewHolder) viewHolder).tvBbi.setText(hrvList.get(i).getBbi());
    }

    @Override
    public int getItemCount() {
        return hrvList.size();
    }

    public void refresh(List<OmniCareHRVData> hrvList) {
        this.hrvList = hrvList;
        notifyDataSetChanged();
    }

    private class HRVDataViewHolder extends RecyclerView.ViewHolder {

        TextView tvTime, tvEndTime, tvDeviceId, tvSourceType, tvTp, tvVlfp, tvLfp, tvHfp, tvNlfp, tvNhfp, tvLfHf, tvSdnn, tvSdann, tvNn50, tvPnn50, tvRmssd, tvAf, tvPac, tvAnb, tvStress, tvVo2, tvHrr, tvRri, tvPpi, tvBbi;

        public HRVDataViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvEndTime = (TextView) itemView.findViewById(R.id.tv_end_time);
            tvDeviceId = (TextView) itemView.findViewById(R.id.tv_device_id);
            tvSourceType = (TextView) itemView.findViewById(R.id.tv_source_type);
            tvTp = (TextView) itemView.findViewById(R.id.tv_tp);
            tvVlfp = (TextView) itemView.findViewById(R.id.tv_vlfp);
            tvLfp = (TextView) itemView.findViewById(R.id.tv_lfp);
            tvHfp = (TextView) itemView.findViewById(R.id.tv_hfp);
            tvNlfp = (TextView) itemView.findViewById(R.id.tv_nlfp);
            tvNhfp = (TextView) itemView.findViewById(R.id.tv_nhfp);
            tvLfHf = (TextView) itemView.findViewById(R.id.tv_lf_hf);
            tvSdnn = (TextView) itemView.findViewById(R.id.tv_sdnn);
            tvSdann = (TextView) itemView.findViewById(R.id.tv_sdann);
            tvNn50 = (TextView) itemView.findViewById(R.id.tv_nn50);
            tvPnn50 = (TextView) itemView.findViewById(R.id.tv_pnn50);
            tvRmssd = (TextView) itemView.findViewById(R.id.tv_rmssd);
            tvAf = (TextView) itemView.findViewById(R.id.tv_af);
            tvPac = (TextView) itemView.findViewById(R.id.tv_pac);
            tvAnb = (TextView) itemView.findViewById(R.id.tv_anb);
            tvStress = (TextView) itemView.findViewById(R.id.tv_stress);
            tvVo2 = (TextView) itemView.findViewById(R.id.tv_vo2);
            tvHrr = (TextView) itemView.findViewById(R.id.tv_hrr);
            tvRri = (TextView) itemView.findViewById(R.id.tv_rri);
            tvPpi = (TextView) itemView.findViewById(R.id.tv_ppi);
            tvBbi = (TextView) itemView.findViewById(R.id.tv_bbi);
        }
    }
}
