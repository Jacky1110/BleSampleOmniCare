package com.asuslife.sampleapps.blesampleomnicare.devices;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.asuslife.omnicaresdk.DeviceModel;
import com.asuslife.omnicaresdk.DeviceMonitoringCallback;
import com.asuslife.omnicaresdk.SCMHGERDCapAdvancedFeature;
import com.asuslife.omnicaresdk.VivoWatchBPAdvancedFeature;
import com.asuslife.omnicaresdk.bluetooth.DeviceCommunicationException;
import com.asuslife.omnicaresdk.sync.OmniCarePHData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.asuslife.omnicaresdk.AsusLifeOmniCare;
import com.asuslife.omnicaresdk.Device;
import com.asuslife.omnicaresdk.DeviceConnectionStateListener;
import com.asuslife.omnicaresdk.DeviceManager;
import com.asuslife.omnicaresdk.SyncingCallback;
import com.asuslife.omnicaresdk.bluetooth.DeviceConnectionFailedException;
import com.asuslife.omnicaresdk.bluetooth.SyncingFailedException;
import com.asuslife.sampleapps.blesampleomnicare.R;
import com.asuslife.sampleapps.blesampleomnicare.adapter.PairedDevicesListAdapter;
import com.asuslife.sampleapps.blesampleomnicare.pairing.ScanningDialogFragment;
import com.asuslife.sampleapps.blesampleomnicare.util.ConfirmationDialog;
import com.asuslife.sampleapps.blesampleomnicare.util.MyProgressDialog;

import java.util.List;


public class PairedDevicesDialogFragment extends DialogFragment implements DeviceConnectionStateListener {

    private String TAG = PairedDevicesDialogFragment.class.getSimpleName();

    private View mRootView;

    private DeviceManager deviceManager;

    private PairedDevicesListAdapter pairedDevicesListAdapter;
    private RecyclerView pairedDevicesList;

    private List<Device> devices;

    private MyProgressDialog myProgressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_paired_devices, container, false);

        deviceManager = AsusLifeOmniCare.getDeviceManager();
        deviceManager.addConnectionStateListener(this);
        devices = deviceManager.getPairedDevices();

        FloatingActionButton mAddDeviceButton = mRootView.findViewById(R.id.add_device_button);
        mAddDeviceButton.setOnClickListener(mAddButtonListener);

        if (devices.isEmpty()) {
            ConfirmationDialog scanningConfirm = new ConfirmationDialog(getActivity(), "", getString(R.string.no_paired_devices), getString(R.string.response_yes),
                    getString(R.string.response_no), new ScanningBeginClickListener());
            scanningConfirm.show();
        }

        pairedDevicesListAdapter = new PairedDevicesListAdapter(devices);
        pairedDevicesListAdapter.setOnItemClickListener(new PairedDevicesListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Device device) {
                DevicesInfoDialogFragment fragment = new DevicesInfoDialogFragment();
                Bundle args = new Bundle();
                args.putSerializable(DevicesInfoDialogFragment.DEVICE, device);
                fragment.setArguments(args);

                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_root, fragment)
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void onItemLongClick(Device device) {
                ConfirmationDialog unpairDeviceConfirm = new ConfirmationDialog(getActivity(), "", getString(R.string.unpair), getString(R.string.response_yes),
                        getString(R.string.response_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == DialogInterface.BUTTON_POSITIVE) {
                            deviceManager.forget(device);
                            devices.remove(device);
                            pairedDevicesListAdapter.notifyDataSetChanged();
                            dialogInterface.dismiss();
                        } else {
                            dialogInterface.dismiss();
                        }
                    }
                });
                unpairDeviceConfirm.show();
            }

            @Override
            public void onPlayNowRequest(Device device) {
                if (device.deviceModel() == DeviceModel.GERD_CAP) {
                    ((SCMHGERDCapAdvancedFeature) device.advancedFeature()).startMonitoring(new DeviceMonitoringCallback(){

                        @Override
                        public void onMonitoringStart(Device device) {
                            Log.d(TAG, "onMonitoringStart");
                        }

                        @Override
                        public void onMonitoringStop(Device device, DeviceCommunicationException cause) {
                            Log.d(TAG, "onMonitoringStop");
                        }

                        @Override
                        public void onDataReceived(OmniCarePHData omniCarePHData) {
                            Log.d(TAG, "onDataReceived");
                            showToast(String.format("Time: %s\nDevice ID: %s\nPH Raw Data: %s\nPH Scale: %s", DateFormat.format("yyyy-MM-dd HH:mm:ss",omniCarePHData.getTimeInMillis()), omniCarePHData.getDeviceId(), omniCarePHData.getPhRawData(), omniCarePHData.getPhScale()));
                        }
                    });
                }
            }

            @Override
            public void onSyncNowRequested(Device device) {
                myProgressDialog = new MyProgressDialog(getActivity(), "", getActivity().getResources().getString(R.string.syncing));
                switch (device.deviceModel()){
                    case VIVOSMART_4:
                        myProgressDialog.show(true);
                        break;
                    default:
                        myProgressDialog.show(false);
                        break;
                }
                deviceManager.requestDeviceSync(device, new SyncingListener());
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        pairedDevicesList = (RecyclerView) mRootView.findViewById(R.id.rv_paired_list);
        pairedDevicesList.setHasFixedSize(true);
        pairedDevicesList.setLayoutManager(layoutManager);
        pairedDevicesList.setAdapter(pairedDevicesListAdapter);

        return mRootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        deviceManager.removeConnectionStateListener(this);
    }

    private View.OnClickListener mAddButtonListener = view ->
    {
        ScanningDialogFragment scanningDialogFragment = new ScanningDialogFragment();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        transaction.replace(R.id.main_root, scanningDialogFragment, scanningDialogFragment.getTag()).addToBackStack(getTag()).commit();
    };

    private void clearProgressDialog() {
        if (myProgressDialog != null && myProgressDialog.isShowing())
            myProgressDialog.dismiss();
    }

    @Override
    public void onDeviceConnected(Device device) {
        Log.d(TAG, String.format("onDeviceConnected: %s ( %s )", device.deviceName(), device.address()));
    }

    @Override
    public void onDeviceConnectionFailed(Device device, DeviceConnectionFailedException dcfe) {
        Log.d(TAG, "onDeviceConnectionFailed: " + dcfe.getMessage());
        clearProgressDialog();
        showToast(dcfe.getMessage());
    }

    @Override
    public void onDeviceDisconnected(Device device) {
        Log.d(TAG, "onDeviceDisconnected: ");
        clearProgressDialog();
    }

    private void showToast(String msg) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showToast(int res) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), res, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class SyncingListener implements SyncingCallback {

        @Override
        public void onSyncComplete(Device device) {
            Log.d(TAG, "onSyncComplete: ");
            clearProgressDialog();
            showToast(R.string.syncing_succeeded);
        }

        @Override
        public void onSyncFailed(Device device, SyncingFailedException sfe) {
            Log.d(TAG, "onSyncFailed: " + sfe.getMessage() + "\n" + sfe.getCause());
            clearProgressDialog();
            showToast(sfe.getMessage());
        }

        @Override
        public void onSyncStarted(Device device) {
            Log.d(TAG, "onSyncStarted: ");
        }

        @Override
        public void onSyncProgress(Device device, int progress) {
            //This callback only support Garmin devices currently
            if (myProgressDialog != null)
                myProgressDialog.setProgress(progress);
        }
    }

    private class ScanningBeginClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (i == DialogInterface.BUTTON_POSITIVE) {
                ScanningDialogFragment scanningDialogFragment = new ScanningDialogFragment();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

                transaction.replace(R.id.main_root, scanningDialogFragment, scanningDialogFragment.getTag()).addToBackStack(getTag()).commit();
            }
        }
    }
}
