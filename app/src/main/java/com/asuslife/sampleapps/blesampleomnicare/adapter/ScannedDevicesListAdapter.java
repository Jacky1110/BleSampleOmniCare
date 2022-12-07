package com.asuslife.sampleapps.blesampleomnicare.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asuslife.omnicaresdk.ScannedDevice;
import com.asuslife.sampleapps.blesampleomnicare.R;

import java.util.ArrayList;
import java.util.List;

public class ScannedDevicesListAdapter extends RecyclerView.Adapter {

    private ClickListener clickListener;
    private List<ScannedDevice> scannedDeviceList;

    public ScannedDevicesListAdapter() {
        this.scannedDeviceList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_scanned_device_row, viewGroup, false);

        return new ScannedDeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((ScannedDeviceViewHolder) viewHolder).tvDeviceName.setText(scannedDeviceList.get(i).deviceName());
        ((ScannedDeviceViewHolder) viewHolder).tvDeviceMacAddress.setText(scannedDeviceList.get(i).address());
    }

    @Override
    public int getItemCount() {
        return scannedDeviceList.size();
    }

    public void addDevice(ScannedDevice scannedDevice){
        for (ScannedDevice sd : scannedDeviceList) {
            if (scannedDevice.address().equals(sd.address())) {
                return;
            }
        }

        scannedDeviceList.add(scannedDevice);
        notifyDataSetChanged();
    }

    public void close(){
        scannedDeviceList.clear();
        notifyDataSetChanged();
    }

    private class ScannedDeviceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvDeviceName, tvDeviceMacAddress;

        public ScannedDeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvDeviceName = (TextView) itemView.findViewById(R.id.tv_device_name);
            tvDeviceMacAddress = (TextView) itemView.findViewById(R.id.tv_device_mac);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClickListener(scannedDeviceList.get(getAdapterPosition()));
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClickListener(ScannedDevice scannedDevice);
    }
}
