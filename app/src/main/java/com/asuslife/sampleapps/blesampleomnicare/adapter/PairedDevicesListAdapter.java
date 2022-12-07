package com.asuslife.sampleapps.blesampleomnicare.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asuslife.omnicaresdk.Device;
import com.asuslife.omnicaresdk.DeviceModel;
import com.asuslife.sampleapps.blesampleomnicare.R;

import java.util.List;
import java.util.function.ToDoubleBiFunction;

public class PairedDevicesListAdapter extends RecyclerView.Adapter {

    private OnItemClickListener onItemClickListener;
    private List<Device> pairedDevicesList;

    public PairedDevicesListAdapter(List<Device> devices) {
        this.pairedDevicesList = devices;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_paired_device_row, viewGroup, false);

        return new PairedDeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(pairedDevicesList.get(viewHolder.getAdapterPosition()));
            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onItemClickListener.onItemLongClick(pairedDevicesList.get(viewHolder.getAdapterPosition()));
                return true;
            }
        });

        ((PairedDeviceViewHolder) viewHolder).ivUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onSyncNowRequested(pairedDevicesList.get(viewHolder.getAdapterPosition()));
            }
        });

        ((PairedDeviceViewHolder) viewHolder).ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onPlayNowRequest(pairedDevicesList.get(viewHolder.getAdapterPosition()));
            }
        });

        if (pairedDevicesList.get(viewHolder.getAdapterPosition()).deviceModel() == DeviceModel.GERD_CAP) {
            ((PairedDeviceViewHolder) viewHolder).ivPlay.setVisibility(View.VISIBLE);
        }

        ((PairedDeviceViewHolder) viewHolder).tvDeviceName.setText(pairedDevicesList.get(i).deviceName());
        ((PairedDeviceViewHolder) viewHolder).tvDeviceSn.setText(pairedDevicesList.get(i).deviceSerialNum());
    }

    @Override
    public int getItemCount() {
        return pairedDevicesList.size();
    }

    private class PairedDeviceViewHolder extends RecyclerView.ViewHolder {

        TextView tvDeviceName, tvDeviceSn;
        ImageView ivUpload, ivPlay;

        public PairedDeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDeviceName = (TextView) itemView.findViewById(R.id.tv_device_name);
            tvDeviceSn = (TextView) itemView.findViewById(R.id.tv_device_sn);
            ivUpload = (ImageView) itemView.findViewById(R.id.iv_upload);
            ivPlay = (ImageView) itemView.findViewById(R.id.iv_play);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(Device device);

        void onItemLongClick(Device device);

        void onSyncNowRequested(Device device);

        void onPlayNowRequest(Device device);
    }
}
